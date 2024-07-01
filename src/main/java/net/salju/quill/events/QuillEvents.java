package net.salju.quill.events;

import net.salju.quill.init.QuillVillagers;
import net.salju.quill.init.QuillTags;
import net.salju.quill.init.QuillEnchantments;
import net.salju.quill.init.QuillConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.common.Tags;
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
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
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
	@SubscribeEvent
	public static void onHurt(LivingHurtEvent event) {
		if (event.getEntity() != null && event.getSource().getDirectEntity() != null) {
			Entity direct = event.getSource().getDirectEntity();
			LivingEntity target = event.getEntity();
			if (direct instanceof Projectile proj) {
				if (proj.getPersistentData().getDouble("Sharpshooter") > 0) {
					double x = direct.getPersistentData().getDouble("Sharpshooter");
					event.setAmount((float) (Math.round(Mth.nextInt(RandomSource.create(), 7, 11)) + (2.5 * x)));
				}
			} else if (direct instanceof LivingEntity attacker) {
				if (attacker.getMainHandItem().getEnchantmentLevel(QuillEnchantments.AUTO_SMELT.get()) > 0) {
					target.setSecondsOnFire(3);
				}
				if (target.isUsingItem()) {
					ItemStack stack = target.getUseItem();
					if (stack.getItem() instanceof SwordItem && QuillManager.isBlocked(event.getSource(), target)) {
						float d = (((float) target.getAttributeValue(Attributes.ATTACK_DAMAGE) - 2.0F) + EnchantmentHelper.getDamageBonus(stack, attacker.getMobType()));
						event.setAmount(event.getAmount() * 0.75F);
						target.swing(target.getUsedItemHand(), true);
						if (target instanceof Player player) {
							attacker.hurt(player.damageSources().playerAttack(player), d);
							if (player.level() instanceof ServerLevel lvl && d > 2.0F) {
								lvl.sendParticles(ParticleTypes.DAMAGE_INDICATOR, attacker.getX(), attacker.getY(0.5), attacker.getZ(), (int) (d * 0.5F), 0.15, 0.0, 0.15, 0.25);
							}
						} else {
							attacker.hurt(target.damageSources().mobAttack(target), d);
						}
						stack.getItem().hurtEnemy(stack, attacker, target);
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
	public static void OnDamage(LivingDamageEvent event) {
		if (event.getEntity() != null && event.getSource().getDirectEntity() != null) {
			Entity direct = event.getSource().getDirectEntity();
			LivingEntity target = event.getEntity();
			if (direct instanceof Projectile proj && target.attackable()) {
				if (proj.getOwner() instanceof Player player) {
					float d = event.getAmount();
					if (target.getHealth() < event.getAmount()) {
						d = target.getHealth();
					}
					if (player.level() instanceof ServerLevel lvl && d > 2.0F && target.attackable() && !event.isCanceled()) {
						lvl.sendParticles(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getY(0.5), target.getZ(), (int) (d * 0.5F), 0.15, 0.0, 0.15, 0.25);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onCritical(CriticalHitEvent event) {
		if (event.isVanillaCritical()) {
			if (event.getEntity().getMainHandItem().getItem() instanceof PickaxeItem && QuillConfig.PICKMAN.get()) {
				event.setDamageModifier(event.getDamageModifier() + 0.5F);
			}
		}
	}

	@SubscribeEvent
	public static void onProjectile(LivingGetProjectileEvent event) {
		if ((event.getProjectileWeaponItemStack().getItem() instanceof CrossbowItem || event.getProjectileWeaponItemStack().getItem() instanceof BowItem) && event.getProjectileWeaponItemStack().getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0) {
			if (event.getProjectileItemStack().isEmpty()) {
				event.setProjectileItemStack(new ItemStack(Items.ARROW));
			} else if (event.getProjectileItemStack().getItem() == Items.ARROW) {
				event.setProjectileItemStack(event.getProjectileItemStack().copy());
			}
		}
	}

	@SubscribeEvent
	public static void onBlockAttack(ShieldBlockEvent event) {
		if (event.getEntity() != null && event.getDamageSource().getDirectEntity() != null) {
			ItemStack stack = event.getEntity().getUseItem();
			if (event.getDamageSource().getDirectEntity() instanceof LivingEntity target) {
				int i = stack.getEnchantmentLevel(QuillEnchantments.SPIKES.get());
				int e = stack.getEnchantmentLevel(QuillEnchantments.BLAZE.get());
				if (i > 0) {
					if (event.getEntity() instanceof Player player) {
						float d = (target instanceof Phantom ? (float) (i * 2.5F) : (float) i);
						target.hurt(player.damageSources().playerAttack(player), d);
						if (player.level() instanceof ServerLevel lvl && d > 2.0F) {
							lvl.sendParticles(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getY(0.5), target.getZ(), (int) (d * 0.5F), 0.15, 0.0, 0.15, 0.25);
						}
					} else {
						float d = (target instanceof Phantom ? (float) (i * 2.5F) : (float) i);
						target.hurt(event.getEntity().damageSources().mobAttack(event.getEntity()), d);
					}
				}
				if (e > 0 && !target.fireImmune()) {
					target.setSecondsOnFire(e * 3);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onMobSpawned(MobSpawnEvent.PositionCheck event) {
		if (QuillConfig.CAMPFIRE.get() && event.getEntity() instanceof Enemy && event.getSpawnType() == MobSpawnType.NATURAL && QuillManager.getCampfire(event.getLevel(), event.getEntity().blockPosition(), 64)) {
			event.setResult(Result.DENY);
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
				if ((player.getVehicle() instanceof Camel || player.getVehicle() instanceof Boat) && player.getVehicle().getPassengers().size() < 2) {
					target.startRiding(player.getVehicle());
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		if (QuillConfig.OCEAN.get()) {
			if (event.getEntity() instanceof Villager target && target.level().getBiome(BlockPos.containing(target.getX(), target.getY(), target.getZ())).is(BiomeTags.IS_OCEAN) && !event.loadedFromDisk()) {
				target.setVillagerData(new VillagerData(QuillVillagers.OCEAN.get(), VillagerProfession.NONE, 1));
			}
		}
	}

	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		Player player = event.getEntity();
		if (event.getTarget() instanceof LivingEntity target && QuillConfig.RIDER.get()) {
			if (target.isPassenger() && player.isCrouching()) {
				target.stopRiding();
				player.swing(InteractionHand.MAIN_HAND, true);
			}
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();
		LevelAccessor world = event.getLevel();
		ItemStack stack = event.getItemStack();
		BlockPos pos = event.getPos();
		BlockState state = world.getBlockState(pos);
		if (state.is(QuillTags.LB) && stack.is(QuillTags.LI) && state.getBlock().asItem() == stack.getItem()) {
			if (world.isEmptyBlock(pos.below())) {
				world.setBlock(pos.below(), state, 3);
				world.playSound(player, pos.below(), state.getSoundType(world, pos.below(), player).getPlaceSound(), SoundSource.BLOCKS);
				player.swing(event.getHand());
				if (!player.isCreative()) {
					stack.shrink(1);
				}
			} else if (world.isEmptyBlock(pos.above())) {
				world.setBlock(pos.above(), state, 3);
				world.playSound(player, pos.above(), state.getSoundType(world, pos.above(), player).getPlaceSound(), SoundSource.BLOCKS);
				player.swing(event.getHand());
				if (!player.isCreative()) {
					stack.shrink(1);
				}
			}
		} else if (stack.getItem() instanceof HoeItem && QuillConfig.FARMER.get()) {
			Block target = state.getBlock();
			if (target instanceof CropBlock crops && crops.isMaxAge(state)) {
				player.swing(event.getHand());
				if (world instanceof ServerLevel lvl) {
					lvl.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
					stack.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
					List<ItemStack> drops = target.getDrops(state, lvl, pos, null, player, stack);
					ItemStack base = crops.getCloneItemStack(lvl, pos, state);
					lvl.setBlock(pos, crops.getStateForAge(0), 2);
					double x = (pos.getX() + 0.5);
					double y = (pos.getY() + 0.5);
					double z = (pos.getZ() + 0.5);
					int f = stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
					for (ItemStack item : drops) {
						if (item.is(Tags.Items.CROPS)) {
							lvl.addFreshEntity(new ItemEntity(lvl, x, y, z, item));
							if (!base.isEdible() && f > 0) {
								if (Math.random() <= 0.56) {
									int i = Mth.nextInt(RandomSource.create(), 0, f);
									if (i > 0) {
										item.setCount(i);
										lvl.addFreshEntity(new ItemEntity(lvl, x, y, z, item));
									}
								}
							}
						} else if (item.is(Tags.Items.SEEDS)) {
							item.setCount(1);
							if (Math.random() <= 0.45) {
								lvl.addFreshEntity(new ItemEntity(lvl, x, y, z, item));
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		BlockState state = event.getEntity().level().getBlockState(event.getPos());
		if (!event.getEntity().isCreative() && !state.is(QuillTags.DND) && QuillConfig.DND.get()) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		Player player = event.getPlayer();
		ItemStack stack = player.getMainHandItem();
		BlockPos pos = event.getPos();
		BlockState state = event.getState();
		LevelAccessor world = event.getLevel();
		double x = (pos.getX() + 0.5);
		double y = (pos.getY() + 0.5);
		double z = (pos.getZ() + 0.5);
		if (state.is(Tags.Blocks.ORES) && world instanceof ServerLevel lvl && stack.getEnchantmentLevel(QuillEnchantments.AUTO_SMELT.get()) > 0) {
			Block target = state.getBlock();
			List<ItemStack> drops = target.getDrops(state, lvl, pos, null, player, stack);
			for (ItemStack item : drops) {
				Optional<SmeltingRecipe> recipe = lvl.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(item), lvl);
				if (recipe.isPresent()) {
					ItemStack smelt = recipe.get().getResultItem(lvl.registryAccess()).copy();
					smelt.setCount(item.getCount());
					lvl.addFreshEntity(new ItemEntity(lvl, x, y, z, smelt));
					if (!event.isCanceled()) {
						world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
						stack.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
						lvl.sendParticles(ParticleTypes.FLAME, x, y, z, 4, 0.35, 0.35, 0.35, 0);
						lvl.addFreshEntity(new ExperienceOrb(lvl, x, y, z, 2));
						event.setCanceled(true);
					}
				}
			}
		}
	}
}
