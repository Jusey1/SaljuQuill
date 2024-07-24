package net.salju.quill.enchantment;

import net.salju.quill.init.QuillEnchantments;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.EquipmentSlot;
import java.util.List;

public class Sharpshooter extends Enchantment {
	public Sharpshooter(EquipmentSlot... slots) {
		super(Enchantment.Rarity.COMMON, EnchantmentCategory.CROSSBOW, slots);
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		return !List.of(QuillEnchantments.SHARPSHOOTER.get(), Enchantments.MULTISHOT, Enchantments.PIERCING).contains(ench);
	}
}