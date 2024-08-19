package net.salju.quill.enchantment;

import net.salju.quill.init.QuillTags;
import net.salju.quill.init.QuillEnchantments;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;

public class ShieldEnchantment extends Enchantment {
	private final int max;

	public ShieldEnchantment(Enchantment.Rarity rare, int i, EquipmentSlot... slots) {
		super(rare, EnchantmentCategory.BREAKABLE, slots);
		this.max = i;
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
	public int getMaxLevel() {
		return this.max;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return (stack.is(QuillTags.SHIELDS));
	}

	@Override
	public boolean isCurse() {
		return this != QuillEnchantments.ZOMBIE.get() ? false : true;
	}
}