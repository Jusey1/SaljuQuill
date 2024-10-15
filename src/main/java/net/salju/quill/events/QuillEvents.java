package net.salju.quill.events;

import net.salju.quill.init.QuillTags;
import net.salju.quill.init.QuillEnchantments;
import net.salju.quill.init.QuillConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.common.Tags;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.BlockTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import java.util.Optional;
import java.util.Map;
import java.util.List;
import com.google.common.collect.Maps;

@Mod.EventBusSubscriber
public class QuillEvents {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			Player player = event.player;
			if (!player.getItemBySlot(EquipmentSlot.CHEST).isEmpty() && player.getItemBySlot(EquipmentSlot.CHEST).isEnchanted()) {
				int i = player.getItemBySlot(EquipmentSlot.CHEST).getEnchantmentLevel(QuillEnchantments.MAGNETIC.get());
				if (i > 0) {
					for (ItemEntity item : player.level().getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(8.0 * i))) {
						if (item.isAlive() && player.isCrouching() && QuillManager.isValidMagneticItem(player, item.getItem())) {
							item.setNoGravity(true);
							Vec3 v = player.getEyePosition().subtract(item.position());
							if (item.level().isClientSide) {
								item.yOld = item.getY();
							}
							item.setDeltaMovement(item.getDeltaMovement().scale(0.95D).add(v.normalize().scale(0.05D)));
						} else {
							item.setNoGravity(false);
						}
					}
				}
			}
		}
	}

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
			} else if (direct instanceof LivingEntity && target.isUsingItem() && QuillConfig.USER.get()) {
				if (target.getUseItem().getUseDuration() <= 64) {
					target.stopUsingItem();
					if (target instanceof Player player) {
						player.getCooldowns().addCooldown(target.getUseItem().getItem(), 12);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onProjectile(LivingGetProjectileEvent event) {
		if ((event.getProjectileWeaponItemStack().getItem() instanceof CrossbowItem || event.getProjectileWeaponItemStack().getItem() instanceof BowItem) && event.getProjectileWeaponItemStack().getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0) {
			if (event.getProjectileItemStack().isEmpty() || event.getProjectileItemStack().getItem() == Items.ARROW) {
				event.setProjectileItemStack(new ItemStack(Items.ARROW));
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
				int u = stack.getEnchantmentLevel(QuillEnchantments.ZOMBIE.get());
				if (i > 0) {
					if (event.getEntity() instanceof Player player) {
						float fd = (target instanceof Phantom ? (float) (i * 2.5F) : (float) i);
						target.hurt(player.damageSources().thorns(player), fd);
						if (player.level() instanceof ServerLevel lvl && fd > 2.0F) {
							lvl.sendParticles(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getY(0.5), target.getZ(), (int) (fd * 0.5F), 0.15, 0.0, 0.15, 0.25);
						}
					} else {
						float fd = (target instanceof Phantom ? (float) (i * 2.5F) : (float) i);
						target.hurt(event.getEntity().damageSources().thorns(event.getEntity()), fd);
					}
				}
				if (u > 0) {
					if (target.level() instanceof ServerLevel lvl) {
						lvl.playSound(null, target.blockPosition(), SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 1.25F, Mth.nextFloat(lvl.getRandom(), 0.12F, 0.21F));
						lvl.playSound(null, target.blockPosition(), SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 1.25F, Mth.nextFloat(lvl.getRandom(), 0.12F, 0.21F));
					}
					event.getEntity().stopUsingItem();
					if (event.getEntity() instanceof Player player) {
						player.getCooldowns().addCooldown(stack.getItem(), 600);
					}
					for (Zombie billy : event.getEntity().level().getEntitiesOfClass(Zombie.class, event.getEntity().getBoundingBox().inflate(64.0D))) {
						if (billy.level() instanceof ServerLevel lvl) {
							lvl.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, billy.getX(), billy.getY() + 1.05, billy.getZ(), 12, 0.45, 0.25, 0.45, 0);
						}
						if (target.isAlive() && !(target instanceof Zombie) && Math.random() >= 0.7) {
							billy.setTarget(target);
						} else {
							billy.setTarget(event.getEntity());
						}
					}
				}
				if (e > 0 && !target.fireImmune()) {
					target.setSecondsOnFire(e * 3);
				}
			} else if (event.getDamageSource().getDirectEntity() instanceof Projectile target) {
				int d = stack.getEnchantmentLevel(QuillEnchantments.DEFLECT.get());
				if (d > 0 && target instanceof Arrow && target.getOwner() != null) {
					double x = Mth.nextDouble(event.getEntity().getRandom(), -0.15, 0.15);
					double y = Mth.nextDouble(event.getEntity().getRandom(), -0.07, 0.07);
					Arrow newbie = new Arrow(event.getEntity().level(), event.getEntity());
					newbie.setCritArrow(true);
					newbie.setBaseDamage(0.75 * d);
					newbie.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
					newbie.shoot(event.getEntity().getViewVector(1.0F).x + x, event.getEntity().getViewVector(1.0F).y + y, event.getEntity().getViewVector(1.0F).z, 0.65F * 3.0F, 1.0F);
					event.getEntity().level().addFreshEntity(newbie);
					target.discard();
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
	}

	@SubscribeEvent
	public static void onTick(LivingEvent.LivingTickEvent event) {
		if (event.getEntity() instanceof Villager target && QuillConfig.TAXI.get()) {
			Player player = target.level().getNearestPlayer(target, 2);
			if (player != null && player.isPassenger()) {
				if ((player.getVehicle() instanceof Camel || player.getVehicle() instanceof Boat) && player.getVehicle().getPassengers().size() < 2) {
					target.startRiding(player.getVehicle());
				}
			}
		}
	}

	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		Player player = event.getEntity();
		if (event.getTarget() instanceof LivingEntity target && QuillConfig.KICK.get()) {
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
		if (state.is(BlockTags.CLIMBABLE) && state.getBlock().asItem() == stack.getItem()) {
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
		} else if (player.getOffhandItem().getItem() instanceof ShieldItem && stack.getItem() instanceof AxeItem && state.is(QuillTags.AXER) && QuillConfig.SHIELD.get()) {
			event.setCanceled(true);
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

	@SubscribeEvent
	public static void onGrindstone(GrindstoneEvent.OnPlaceItem event) {
		if (QuillConfig.ENCHS.get() && QuillConfig.GRIND.get() && event.getTopItem().isEnchanted() && event.getBottomItem().is(Items.ENCHANTED_BOOK)) {
			ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
			EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(event.getTopItem()), stack);
			Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(event.getBottomItem());
			int i = 0;
			for (Enchantment ench : map.keySet()) {
				if (ench.isCurse()) {
					EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(ench, map.get(ench)));
				} else {
					i += ench.getMinCost(map.get(ench));
				}
			}
			event.setOutput(stack);
			event.setXp(i);
		}
	}

	@SubscribeEvent
	public static void onAnvil(AnvilUpdateEvent event) {
		if (QuillConfig.ENCHS.get()) {
			if (QuillConfig.REPAIR.get() && QuillManager.isValidRepairItem(event.getLeft(), event.getRight()) && event.getLeft().isDamaged()) {
				ItemStack stack = event.getLeft().copy();
				int i = (stack.getDamageValue() > (stack.getMaxDamage() / 2) && event.getRight().getCount() > 1) ? 2 : 1;
				stack.setDamageValue(Math.max(stack.getDamageValue() - ((stack.getMaxDamage() / 2) * i), 0));
				event.setCost(i);
				event.setMaterialCost(i);
				event.setOutput(stack);
			} else if (QuillConfig.ANBOOK.get() && (event.getLeft().isEnchantable() || event.getLeft().is(Items.ENCHANTED_BOOK) || event.getLeft().isEnchanted())
					&& (event.getRight().is(Items.ENCHANTED_BOOK) || (event.getRight().is(event.getLeft().getItem()) && event.getRight().isEnchanted()))) {
				Map<Enchantment, Integer> map = event.getLeft().isEnchanted() || event.getLeft().is(Items.ENCHANTED_BOOK) ? EnchantmentHelper.getEnchantments(event.getLeft().copy()) : Maps.newLinkedHashMap();
				Map<Enchantment, Integer> book = EnchantmentHelper.getEnchantments(event.getRight());
				int i = 0;
				int m = event.getLeft().is(QuillTags.DOUBENCHS) ? QuillConfig.MAXENCH.get() * 2 : QuillConfig.MAXENCH.get();
				for (Enchantment ench : book.keySet()) {
					int e = Math.min(map.getOrDefault(ench, 0) == book.get(ench) ? book.get(ench) + 1 : Math.max(book.get(ench), map.getOrDefault(ench, 0)), ench.getMaxLevel());
					int t = 0;
					boolean check = event.getLeft().is(Items.ENCHANTED_BOOK) ? true : ench.canEnchant(event.getLeft());
					for (Enchantment target : map.keySet()) {
						if (!target.isCurse()) {
							t++;
						}
						if ((ench != target && !ench.isCompatibleWith(target)) || (ench == target && book.get(ench) == e)) {
							check = false;
						}
					}
					boolean always = (ench.isCurse() || e > book.get(ench));
					if (check && (always || t < m)) {
						map.put(ench, e);
						int c = 1;
						switch (ench.getRarity()) {
							case UNCOMMON :
								c = 2;
								break;
							case RARE :
								c = 4;
								break;
							case VERY_RARE :
								c = 6;
						}
						i = (i + ((e * c) / 2));
					}
				}
				if (i > 0) {
					ItemStack stack = event.getLeft().copy();
					if (stack.isEnchanted()) {
						stack.removeTagKey("Enchantments");
					}
					if (event.getName() != null && event.getName() != "") {
						stack.setHoverName(Component.literal(event.getName()));
						i++;
					}
					EnchantmentHelper.setEnchantments(map, stack);
					event.setCost(Math.min(i, QuillConfig.MAXANBOOKCOST.get()));
					event.setMaterialCost(1);
					event.setOutput(stack);
				} else {
					event.setCanceled(true);
				}
			} else if (QuillConfig.RENAME.get() && !event.getLeft().isEmpty() && event.getRight().isEmpty() && event.getName() != null && event.getName() != "") {
				ItemStack stack = event.getLeft().copy();
				stack.setHoverName(Component.literal(event.getName()));
				event.setCost(1);
				event.setMaterialCost(1);
				event.setOutput(stack);
			}
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