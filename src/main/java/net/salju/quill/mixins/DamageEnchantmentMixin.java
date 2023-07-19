package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.DiggingEnchantment;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.entity.EquipmentSlot;

@Mixin(DamageEnchantment.class)
public class DamageEnchantmentMixin extends Enchantment {
	public DamageEnchantmentMixin(Enchantment.Rarity rare, EquipmentSlot... slots) {
		super(rare, EnchantmentCategory.WEAPON, slots);
	}

	@Override
	public boolean checkCompatibility(Enchantment ench) {
		return !(ench instanceof DamageEnchantment || ench instanceof DiggingEnchantment);
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return stack.getItem() instanceof DiggerItem ? true : super.canEnchant(stack);
	}
}