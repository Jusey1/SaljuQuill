package net.salju.quill.events;

import net.salju.quill.init.QuillVillagers;
import net.salju.quill.init.QuillEnchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import java.util.List;

public class QuillVillagerManager {
	static class EmeraldForItems implements VillagerTrades.ItemListing {
		private final Item target;
		private final int cost;
		private final int maxUses;
		private final int xp;
		private final int amount;

		public EmeraldForItems(ItemLike item, int c, int u, int x, int a) {
			this.target = item.asItem();
			this.cost = c;
			this.maxUses = u;
			this.xp = x;
			this.amount = a;
		}

		@Override
		public MerchantOffer getOffer(Entity entity, RandomSource rng) {
			return new MerchantOffer(new ItemStack(this.target, this.cost), new ItemStack(Items.EMERALD, this.amount), this.maxUses, this.xp, 0.05F);
		}
	}

	static class ItemsForEmeralds implements VillagerTrades.ItemListing {
		private final Item target;
		private final int cost;
		private final int maxUses;
		private final int xp;
		private final int amount;

		public ItemsForEmeralds(ItemLike item, int c, int u, int x, int a) {
			this.target = item.asItem();
			this.cost = c;
			this.maxUses = u;
			this.xp = x;
			this.amount = a;
		}

		@Override
		public MerchantOffer getOffer(Entity entity, RandomSource rng) {
			return new MerchantOffer(new ItemStack(Items.EMERALD, this.cost), new ItemStack(this.target, this.amount), this.maxUses, this.xp, 0.05F);
		}
	}

	static class IronForEmeralds implements VillagerTrades.ItemListing {
		private final Item target;
		private final int cost;
		private final int maxUses;
		private final int xp;
		private boolean enchanted;

		public IronForEmeralds(ItemLike item, int c, int u, int x, boolean b) {
			this.target = item.asItem();
			this.cost = c;
			this.maxUses = u;
			this.xp = x;
			this.enchanted = b;
		}

		@Override
		public MerchantOffer getOffer(Entity entity, RandomSource rng) {
			Item t = this.target;
			if (entity instanceof Villager target) {
				VillagerType type = target.getVillagerData().getType();
				if (type == QuillVillagers.OCEAN.get() || type == VillagerType.SWAMP || type == VillagerType.JUNGLE) {
					if (t == Items.IRON_HELMET) {
						t = Items.CHAINMAIL_HELMET;
					} else if (t == Items.IRON_CHESTPLATE) {
						t = Items.CHAINMAIL_CHESTPLATE;
					} else if (t == Items.IRON_LEGGINGS) {
						t = Items.CHAINMAIL_LEGGINGS;
					} else if (t == Items.IRON_BOOTS) {
						t = Items.CHAINMAIL_BOOTS;
					}
				}
			}
			ItemStack stack = new ItemStack(t);
			int j = this.cost;
			if (this.enchanted) {
				EnchantmentHelper.enchantItem(rng, stack, 9, true);
				j = (j * 2);
			}
			return new MerchantOffer(new ItemStack(Items.EMERALD, j), stack, this.maxUses, this.xp, 0.2F);
		}
	}

	static class DiamondForEmeralds implements VillagerTrades.ItemListing {
		private final Item target;
		private final int cost;
		private final int diamond;
		private final int maxUses;
		private final int xp;
		private boolean enchanted;

		public DiamondForEmeralds(ItemLike item, int c, int d, int u, int x, boolean b) {
			this.target = item.asItem();
			this.cost = c;
			this.diamond = d;
			this.maxUses = u;
			this.xp = x;
			this.enchanted = b;
		}

		@Override
		public MerchantOffer getOffer(Entity entity, RandomSource rng) {
			ItemStack stack = new ItemStack(this.target);
			if (this.enchanted) {
				EnchantmentHelper.enchantItem(rng, stack, 10, true);
			}
			return new MerchantOffer(new ItemStack(Items.EMERALD, this.cost), new ItemStack(Items.DIAMOND, this.diamond), stack, this.maxUses, this.xp, 0.2F);
		}
	}

	static class FishermanMaster implements VillagerTrades.ItemListing {
		private final int xp;

		public FishermanMaster(int i) {
			this.xp = i;
		}

		@Override
		public MerchantOffer getOffer(Entity entity, RandomSource rng) {
			ItemStack stack = new ItemStack(Items.MANGROVE_BOAT);
			int j = 1;
			if (entity instanceof Villager target) {
				VillagerType type = target.getVillagerData().getType();
				if (type == VillagerType.PLAINS) {
					stack = new ItemStack(Items.OAK_BOAT);
				} else if (type == VillagerType.DESERT || type == VillagerType.JUNGLE) {
					stack = new ItemStack(Items.JUNGLE_BOAT);
				} else if (type == VillagerType.SAVANNA) {
					stack = new ItemStack(Items.ACACIA_BOAT);
				} else if (type == VillagerType.TAIGA || type == VillagerType.SNOW) {
					stack = new ItemStack(Items.SPRUCE_BOAT);
				} else if (type == VillagerType.SWAMP) {
					stack = new ItemStack(Items.DARK_OAK_BOAT);
				} else if (type == QuillVillagers.OCEAN.get()) {
					stack = new ItemStack(Items.TRIDENT);
					j = Mth.nextInt(rng, 32, 42);
				}
			}
			return new MerchantOffer(new ItemStack(Items.EMERALD, j), stack, 4, this.xp, 0.2F);
		}
	}

	static class EnchantBookMaster implements VillagerTrades.ItemListing {
		private final int xp;

		public EnchantBookMaster(int i) {
			this.xp = i;
		}

		@Override
		public MerchantOffer getOffer(Entity entity, RandomSource rng) {
			Enchantment ench = Enchantments.MENDING;
			if (entity instanceof Villager target) {
				VillagerType type = target.getVillagerData().getType();
				if (type == VillagerType.PLAINS) {
					ench = Enchantments.ALL_DAMAGE_PROTECTION;
				} else if (type == VillagerType.DESERT) {
					ench = QuillEnchantments.AUTO_SMELT.get();
				} else if (type == VillagerType.SAVANNA) {
					ench = QuillEnchantments.SHARPSHOOTER.get();
				} else if (type == VillagerType.TAIGA) {
					ench = Enchantments.SHARPNESS;
				} else if (type == VillagerType.SNOW) {
					ench = Enchantments.SILK_TOUCH;
				} else if (type == VillagerType.JUNGLE) {
					ench = Enchantments.BLOCK_EFFICIENCY;
				} else if (type == VillagerType.SWAMP) {
					ench = Enchantments.MENDING;
				} else if (type == QuillVillagers.OCEAN.get()) {
					ench = Enchantments.CHANNELING;
				}
			}
			int i = (ench.getMaxLevel() > 1 ? (ench.getMaxLevel() - 1) : 1);
			ItemStack book = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(ench, i));
			int j = Mth.nextInt(rng, 28, 34);
			return new MerchantOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), book, 12, this.xp, 0.2F);
		}
	}
}