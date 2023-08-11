package net.salju.quill.events;

import net.salju.quill.init.QuillEnchantments;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.SimpleContainer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import java.util.Optional;
import java.util.List;

@Mod.EventBusSubscriber
public class QuillEvents {
	private static float pickaxe;
	private static boolean critical;

	@SubscribeEvent
	public static void onHurt(LivingHurtEvent event) {
		if (event != null && event.getEntity() != null && event.getSource().getDirectEntity() != null) {
			Entity direct = event.getSource().getDirectEntity();
			LivingEntity target = event.getEntity();
			float damage = event.getAmount();
			if (direct instanceof AbstractArrow && direct.getPersistentData().getDouble("Sharpshooter") > 0) {
				double x = direct.getPersistentData().getDouble("Sharpshooter");
				event.setAmount((float) (Math.round(Mth.nextInt(RandomSource.create(), 7, 11)) + (2.5 * x)));
			} else if (direct instanceof LivingEntity attacker) {
				ItemStack weapon = attacker.getMainHandItem();
				if (weapon.getItem() instanceof PickaxeItem) {
					pickaxe = event.getAmount();
					if (weapon.getEnchantmentLevel(QuillEnchantments.AUTO_SMELT.get()) > 0) {
						target.setSecondsOnFire(3);
					}
				}
				if (target.isUsingItem()) {
					ItemStack stack = target.getUseItem();
					if (stack.getItem() instanceof SwordItem sword && isSwordBlocked(event.getSource(), target)) {
						float d = (sword.getDamage() + EnchantmentHelper.getDamageBonus(stack, attacker.getMobType()) + stack.getEnchantmentLevel(Enchantments.SWEEPING_EDGE));
						event.setAmount(damage * 0.65F);
						target.swing(target.getUsedItemHand());
						if (target instanceof Player player) {
							attacker.hurt(player.damageSources().playerAttack(player), d);
							if (player.level() instanceof ServerLevel lvl && d > 2.0F) {
								int i = (int)(d * 0.5F);
								lvl.sendParticles(ParticleTypes.DAMAGE_INDICATOR, attacker.getX(), attacker.getY(0.5), attacker.getZ(), i, 0.1, 0.0, 0.1, 0.2);
							}
						} else {
							attacker.hurt(target.damageSources().mobAttack(target), d);
						}
						sword.hurtEnemy(stack, attacker, target);
					}
					if (stack.getUseDuration() <= 64) {
						target.stopUsingItem();
						if (target instanceof Player player) {
							int i = (stack.getItem() instanceof SwordItem ? 30 : 12);
							player.getCooldowns().addCooldown(stack.getItem(), i);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onDamage(LivingDamageEvent event) {
		if (event != null && event.getEntity() != null && event.getSource().getDirectEntity() != null) {
			Entity direct = event.getSource().getDirectEntity();
			LivingEntity target = event.getEntity();
			DamageSource source = event.getSource();
			if (direct instanceof LivingEntity attacker) {
				ItemStack weapon = attacker.getMainHandItem();
				if (weapon.getItem() instanceof PickaxeItem) {
					if (pickaxe != 0 && critical == true) {
						float armor = ((float) target.getArmorValue() * 0.7F);
						float at = ((float) target.getAttributeValue(Attributes.ARMOR_TOUGHNESS) * 0.7F);
						float damage = CombatRules.getDamageAfterAbsorb(pickaxe, armor, at);
						int k = EnchantmentHelper.getDamageProtection(target.getArmorSlots(), source);
						if (k > 0) {
							damage = CombatRules.getDamageAfterMagicAbsorb(damage, (float) k);
						}
						event.setAmount(damage);
					}
					pickaxe = 0;
					critical = false;
				}
			}
		}
	}

	@SubscribeEvent
	public static void onProjectile(LivingGetProjectileEvent event) {
		ItemStack weapon = event.getProjectileWeaponItemStack();
		if ((weapon.getItem() instanceof CrossbowItem || weapon.getItem() instanceof BowItem) && weapon.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0) {
			ItemStack arrow = event.getProjectileItemStack();
			if (arrow.isEmpty()) {
				event.setProjectileItemStack(new ItemStack(Items.ARROW));
			} else if (arrow.getItem() == Items.ARROW) {
				event.setProjectileItemStack(arrow.copy());
			}
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();
		ItemStack stack = player.getOffhandItem();
		BlockState state = event.getLevel().getBlockState(event.getPos());
		if (state.is(BlockTags.LOGS) && !(stack.isEmpty() || stack.getItem() instanceof AxeItem)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onCritical(CriticalHitEvent event) {
		if (event != null && event.getEntity() != null) {
			Player player = event.getEntity();
			ItemStack weapon = player.getMainHandItem();
			if (event.isVanillaCritical()) {
				if (weapon.getItem() instanceof PickaxeItem) {
					event.setDamageModifier(event.getDamageModifier() + 0.5F);
					critical = true;
				}
			}
		}
	}

	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		Player player = event.getPlayer();
		ItemStack weapon = player.getMainHandItem();
		BlockPos pos = event.getPos();
		BlockState state = event.getState();
		LevelAccessor world = event.getLevel();
		double x = (pos.getX() + 0.5);
		double y = (pos.getY() + 0.5);
		double z = (pos.getZ() + 0.5);
		if (state.is(BlockTags.create(new ResourceLocation("forge:ores"))) && world instanceof ServerLevel lvl && weapon.getEnchantmentLevel(QuillEnchantments.AUTO_SMELT.get()) > 0) {
			Block target = state.getBlock();
			List<ItemStack> drops = target.getDrops(state, lvl, pos, null, player, weapon);
			boolean smelted = false;
			for (ItemStack stack : drops) {
				Optional<SmeltingRecipe> recipe = lvl.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), lvl);
				if (recipe.isPresent()) {
					smelted = true;
					ItemStack smelt = recipe.get().getResultItem(lvl.registryAccess()).copy();
					smelt.setCount(stack.getCount());
					ItemEntity item = new ItemEntity(lvl, x, y, z, smelt);
					item.setPickUpDelay(10);
					lvl.addFreshEntity(item);
				}
			}
			if (smelted == true) {
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				weapon.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
				lvl.sendParticles(ParticleTypes.FLAME, x, y, z, 4, 0.35, 0.35, 0.35, 0);
				lvl.addFreshEntity(new ExperienceOrb(lvl, x, y, z, 2));
				event.setCanceled(true);
			}
		}
	}

	private static boolean isSwordBlocked(DamageSource source, LivingEntity target) {
		if (!source.is(DamageTypeTags.BYPASSES_SHIELD)) {
			Vec3 vec32 = source.getSourcePosition();
			if (vec32 != null) {
				Vec3 vec3 = target.getViewVector(1.0F);
				Vec3 vec31 = vec32.vectorTo(target.position()).normalize();
				vec31 = new Vec3(vec31.x, 0.0D, vec31.z);
				if (vec31.dot(vec3) < 0.0D) {
					return true;
				}
			}
		}
		return false;
	}
}