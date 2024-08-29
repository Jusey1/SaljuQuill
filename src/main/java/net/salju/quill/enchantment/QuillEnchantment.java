package net.salju.quill.enchantment;

import net.salju.quill.init.QuillEnchantments;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import java.util.List;

public class QuillEnchantment extends Enchantment {
	private final int max;
	private final boolean isCursed;

	public QuillEnchantment(Enchantment.Rarity rare, EnchantmentCategory cate, int i, boolean check, EquipmentSlot... slots) {
		super(rare, cate, slots);
		this.max = i;
		this.isCursed = check;
	}

	@Override
	public int getMaxLevel() {
		return this.max;
	}

	@Override
	public boolean isCurse() {
		return this.isCursed;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (this == QuillEnchantments.AUTO_SMELT.get()) {
			return (stack.getItem() instanceof PickaxeItem);
		}
		return super.canApplyAtEnchantingTable(stack);
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		if (this == QuillEnchantments.AUTO_SMELT.get()) {
			return List.of(Enchantments.SILK_TOUCH).contains(ench) ? false : super.checkCompatibility(ench);
		} else if (this == QuillEnchantments.SHARPSHOOTER.get()) {
			return List.of(Enchantments.MULTISHOT, Enchantments.PIERCING).contains(ench) ? false : super.checkCompatibility(ench);
		}
		return super.checkCompatibility(ench);
	}
}