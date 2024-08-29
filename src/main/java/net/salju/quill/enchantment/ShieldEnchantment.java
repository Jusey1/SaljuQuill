package net.salju.quill.enchantment;

import net.salju.quill.init.QuillTags;
import net.salju.quill.init.QuillEnchantments;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;

public class ShieldEnchantment extends QuillEnchantment {
	public ShieldEnchantment(Enchantment.Rarity rare, EnchantmentCategory cate, int i, boolean check, EquipmentSlot... slots) {
		super(rare, cate, i, check, slots);
	}

	@Override
	public int getMinCost(int i) {
		return 5 + (i - 1) * 5;
	}

	@Override
	public int getMaxCost(int i) {
		return this.getMinCost(i) + 76;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return (stack.is(QuillTags.SHIELDS));
	}
}