package net.salju.quill.enchantment;

import net.salju.quill.init.QuillEnchantments;

import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.List;

public class Airstrike extends Enchantment {
	public Airstrike(EquipmentSlot... slots) {
		super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.TRIDENT, slots);
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		return !List.of(QuillEnchantments.AIRSTRIKE.get(), Enchantments.RIPTIDE, Enchantments.LOYALTY, Enchantments.CHANNELING).contains(ench);
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}
}