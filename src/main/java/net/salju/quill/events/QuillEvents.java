package net.salju.quill.events;

import net.salju.quill.init.QuillVillagers;
import net.salju.quill.init.QuillEnchantments;
import net.salju.quill.init.QuillConfig;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.common.Tags;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CropBlock;
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
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.BiomeTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
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
				if (weapon.getItem() instanceof PickaxeItem && QuillConfig.PICKMAN.get()) {
					pickaxe = event.getAmount();
					if (weapon.getEnchantmentLevel(QuillEnchantments.AUTO_SMELT.get()) > 0) {
						target.setSecondsOnFire(3);
					}
				}
				if (target.isUsingItem()) {
					ItemStack stack = target.getUseItem();
					if (stack.getItem() instanceof SwordItem sword && isSwordBlocked(event.getSource(), target)) {
						float d = ((sword.getDamage() - 2.0F) + (EnchantmentHelper.getDamageBonus(stack, attacker.getMobType()) * 0.5F) + stack.getEnchantmentLevel(Enchantments.SWEEPING_EDGE));
						event.setAmount(damage * 0.75F);
						target.swing(target.getUsedItemHand());
						if (target instanceof Player player) {
							attacker.hurt(player.damageSources().playerAttack(player), d);
							if (player.level() instanceof ServerLevel lvl && d > 2.0F) {
								int i = (int) (d * 0.5F);
								lvl.sendParticles(ParticleTypes.DAMAGE_INDICATOR, attacker.getX(), attacker.getY(0.5), attacker.getZ(), i, 0.1, 0.0, 0.1, 0.2);
							}
						} else {
							attacker.hurt(target.damageSources().mobAttack(target), d);
						}
						sword.hurtEnemy(stack, attacker, target);
					}
					if (stack.getUseDuration() <= 64 && QuillConfig.USER.get()) {
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
		if (event.getEntity() != null && event.getSource().getDirectEntity() != null) {
			Entity direct = event.getSource().getDirectEntity();
			LivingEntity target = event.getEntity();
			DamageSource source = event.getSource();
			if (direct instanceof LivingEntity attacker) {
				ItemStack weapon = attacker.getMainHandItem();
				if (weapon.getItem() instanceof PickaxeItem && QuillConfig.PICKMAN.get()) {
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
	public static void onBlock(ShieldBlockEvent event) {
		if (QuillConfig.SHIELD.get()) {
			LivingEntity target = event.getEntity();
			DamageSource source = event.getDamageSource();
			if (source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)) {
				target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0));
				target.stopUsingItem();
				if (target instanceof Player player) {
					player.getCooldowns().addCooldown(target.getUseItem().getItem(), 200);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onCritical(CriticalHitEvent event) {
		Player player = event.getEntity();
		ItemStack weapon = player.getMainHandItem();
		if (event.isVanillaCritical()) {
			if (weapon.getItem() instanceof PickaxeItem && QuillConfig.PICKMAN.get()) {
				event.setDamageModifier(event.getDamageModifier() + 0.5F);
				critical = true;
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
	public static void onTrades(VillagerTradesEvent event) {
		final var basic = event.getTrades().get(1);
		final var second = event.getTrades().get(2);
		final var middle = event.getTrades().get(3);
		final var expert = event.getTrades().get(4);
		final var master = event.getTrades().get(5);
		if (QuillConfig.TRADES.get()) {
			if (event.getType() == VillagerProfession.LIBRARIAN) {
				master.add(new QuillVillagerManager.EnchantBookMaster(25));
				event.getTrades().put(5, master);
			} else if (event.getType() == VillagerProfession.WEAPONSMITH || event.getType() == VillagerProfession.TOOLSMITH || event.getType() == VillagerProfession.ARMORER) {
				basic.removeAll(basic);
				second.removeAll(second);
				middle.removeAll(middle);
				expert.removeAll(expert);
				master.removeAll(master);
				basic.add(new QuillVillagerManager.EmeraldForItems(Items.COAL, 15, 16, 2, 1));
				basic.add(new QuillVillagerManager.EmeraldForItems(Items.IRON_INGOT, 5, 16, 2, 1));
				if (event.getType() == VillagerProfession.ARMORER) {
					second.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_HELMET, 4, 12, 5, false));
					second.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_CHESTPLATE, 9, 12, 5, false));
					second.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_LEGGINGS, 7, 12, 5, false));
					second.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_BOOTS, 5, 12, 5, false));
					middle.add(new QuillVillagerManager.EmeraldForItems(Items.LAVA_BUCKET, 1, 12, 15, 1));
					middle.add(new QuillVillagerManager.ItemsForEmeralds(Items.SHIELD, 5, 12, 15, 1));
					expert.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_HELMET, 4, 12, 25, true));
					expert.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_CHESTPLATE, 9, 12, 25, true));
					expert.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_LEGGINGS, 7, 12, 25, true));
					expert.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_BOOTS, 5, 12, 25, true));
					master.add(new QuillVillagerManager.DiamondForEmeralds(Items.DIAMOND_HELMET, 12, 2, 12, 25, true));
					master.add(new QuillVillagerManager.DiamondForEmeralds(Items.DIAMOND_CHESTPLATE, 16, 4, 12, 25, true));
					master.add(new QuillVillagerManager.DiamondForEmeralds(Items.DIAMOND_LEGGINGS, 16, 3, 12, 25, true));
					master.add(new QuillVillagerManager.DiamondForEmeralds(Items.DIAMOND_BOOTS, 12, 2, 12, 25, true));
				} else {
					second.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_AXE, 3, 12, 5, false));
					middle.add(new QuillVillagerManager.EmeraldForItems(Items.DIAMOND, 1, 12, 15, 1));
					middle.add(new QuillVillagerManager.EmeraldForItems(Items.FLINT, 24, 16, 15, 1));
					expert.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_AXE, 3, 12, 25, true));
					master.add(new QuillVillagerManager.DiamondForEmeralds(Items.DIAMOND_AXE, 12, 2, 12, 25, true));
				}
				if (event.getType() == VillagerProfession.WEAPONSMITH) {
					second.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_SWORD, 2, 12, 5, false));
					expert.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_SWORD, 2, 12, 25, true));
					master.add(new QuillVillagerManager.DiamondForEmeralds(Items.DIAMOND_SWORD, 12, 2, 12, 25, true));
				} else if (event.getType() == VillagerProfession.TOOLSMITH) {
					second.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_HOE, 2, 12, 5, false));
					second.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_SHOVEL, 2, 12, 5, false));
					second.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_PICKAXE, 2, 12, 5, false));
					expert.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_HOE, 2, 12, 25, true));
					expert.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_SHOVEL, 2, 12, 25, true));
					expert.add(new QuillVillagerManager.IronForEmeralds(Items.IRON_PICKAXE, 2, 12, 25, true));
					master.add(new QuillVillagerManager.DiamondForEmeralds(Items.DIAMOND_HOE, 12, 1, 12, 25, true));
					master.add(new QuillVillagerManager.DiamondForEmeralds(Items.DIAMOND_SHOVEL, 12, 1, 12, 25, true));
					master.add(new QuillVillagerManager.DiamondForEmeralds(Items.DIAMOND_PICKAXE, 12, 2, 12, 25, true));
				}
				middle.add(new QuillVillagerManager.ItemsForEmeralds(Items.BELL, 36, 12, 15, 1));
				master.add(new QuillVillagerManager.EmeraldForItems(Items.IRON_BLOCK, 1, 16, 25, 4));
				master.add(new QuillVillagerManager.EmeraldForItems(Items.DIAMOND_BLOCK, 1, 16, 25, 42));
				event.getTrades().put(1, basic);
				event.getTrades().put(2, second);
				event.getTrades().put(3, middle);
				event.getTrades().put(4, expert);
				event.getTrades().put(5, master);
			}
		}
		if (event.getType() == VillagerProfession.FISHERMAN && QuillConfig.OCEAN.get()) {
			master.remove(1);
			master.add(new QuillVillagerManager.FishermanMaster(25));
			event.getTrades().put(5, master);
		}
	}

	@SubscribeEvent
	public static void onTick(LivingEvent.LivingTickEvent event) {
		if (event.getEntity() instanceof Villager target && QuillConfig.RIDER.get()) {
			Player player = target.level().getNearestPlayer(target, 2);
			if (player != null && player.isPassenger()) {
				Entity mount = player.getVehicle();
				if ((mount instanceof Camel || mount instanceof Boat) && mount.getPassengers().size() < 2) {
					target.startRiding(mount);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		if (QuillConfig.OCEAN.get()) {
			Entity entity = event.getEntity();
			LevelAccessor world = entity.level();
			double x = entity.getX();
			double y = entity.getY();
			double z = entity.getZ();
			BlockPos pos = BlockPos.containing(x, y, z);
			if (entity instanceof Villager target && world.getBiome(pos).is(BiomeTags.IS_OCEAN) && target.getVillagerData().getProfession() == VillagerProfession.NONE && target.getVillagerData().getType() != QuillVillagers.OCEAN.get()) {
				VillagerData data = new VillagerData(QuillVillagers.OCEAN.get(), VillagerProfession.NONE, 1);
				target.setVillagerData(data);
			}
		}
	}

	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		Player player = event.getEntity();
		if (event.getTarget() instanceof Villager target && QuillConfig.RIDER.get()) {
			if (target.isPassenger() && player.isCrouching()) {
				target.stopRiding();
				player.swing(InteractionHand.MAIN_HAND);
			}
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();
		LevelAccessor world = event.getLevel();
		ItemStack weapon = event.getItemStack();
		BlockPos pos = event.getPos();
		BlockState state = world.getBlockState(pos);
		double x = (pos.getX() + 0.5);
		double y = (pos.getY() + 0.5);
		double z = (pos.getZ() + 0.5);
		int f = weapon.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
		if (weapon.getItem() instanceof AxeItem && QuillConfig.AXER.get()) {
			ItemStack off = player.getOffhandItem();
			if (state.is(BlockTags.LOGS) && !(off.isEmpty() || off == weapon)) {
				event.setCanceled(true);
			}
		} else if (weapon.getItem() instanceof HoeItem && QuillConfig.FARMER.get()) {
			Block target = state.getBlock();
			if (target instanceof CropBlock crops && crops.isMaxAge(state)) {
				player.swing(event.getHand());
				if (world instanceof ServerLevel lvl) {
					lvl.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
					weapon.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
					List<ItemStack> drops = target.getDrops(state, lvl, pos, null, player, weapon);
					ItemStack base = crops.getCloneItemStack(lvl, pos, state);
					lvl.setBlock(pos, crops.getStateForAge(0), 2);
					for (ItemStack stack : drops) {
						if (stack.is(Tags.Items.CROPS)) {
							lvl.addFreshEntity(new ItemEntity(lvl, x, y, z, stack));
							if (!base.isEdible() && f > 0) {
								if (Math.random() <= 0.56) {
									int i = Mth.nextInt(RandomSource.create(), 0, f);
									if (i > 0) {
										stack.setCount(i);
										lvl.addFreshEntity(new ItemEntity(lvl, x, y, z, stack));
									}
								}
							}
						} else if (stack.is(Tags.Items.SEEDS)) {
							stack.setCount(1);
							if (Math.random() <= 0.45) {
								lvl.addFreshEntity(new ItemEntity(lvl, x, y, z, stack));
							}
						}
					}
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
		if (state.is(Tags.Blocks.ORES) && world instanceof ServerLevel lvl && weapon.getEnchantmentLevel(QuillEnchantments.AUTO_SMELT.get()) > 0) {
			Block target = state.getBlock();
			List<ItemStack> drops = target.getDrops(state, lvl, pos, null, player, weapon);
			for (ItemStack stack : drops) {
				Optional<SmeltingRecipe> recipe = lvl.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), lvl);
				if (recipe.isPresent()) {
					ItemStack smelt = recipe.get().getResultItem(lvl.registryAccess()).copy();
					smelt.setCount(stack.getCount());
					lvl.addFreshEntity(new ItemEntity(lvl, x, y, z, smelt));
					if (!event.isCanceled()) {
						world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
						weapon.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
						lvl.sendParticles(ParticleTypes.FLAME, x, y, z, 4, 0.35, 0.35, 0.35, 0);
						lvl.addFreshEntity(new ExperienceOrb(lvl, x, y, z, 2));
						event.setCanceled(true);
					}
				}
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