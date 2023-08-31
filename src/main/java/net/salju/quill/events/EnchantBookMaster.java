package net.salju.quill.events;

import net.salju.quill.init.QuillEnchantments;

import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class EnchantBookMaster implements VillagerTrades.ItemListing {
	private final int xp;

	public EnchantBookMaster(int i) {
		this.xp = i;
	}

	@Override
	public MerchantOffer getOffer(Entity entity, RandomSource rng) {
		Enchantment ench = Enchantments.BINDING_CURSE;
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
			}
		}
		int i = (ench.getMaxLevel() > 1 ? (ench.getMaxLevel() - 1) : 1);
		ItemStack book = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(ench, i));
		int j = Mth.nextInt(rng, 28, 34);
		return new MerchantOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), book, 12, this.xp, 0.2F);
	}
}