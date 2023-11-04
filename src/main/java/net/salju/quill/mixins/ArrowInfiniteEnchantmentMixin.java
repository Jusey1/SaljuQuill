package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillConfig;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ArrowInfiniteEnchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.entity.EquipmentSlot;

@Mixin(ArrowInfiniteEnchantment.class)
public class ArrowInfiniteEnchantmentMixin extends Enchantment {
	public ArrowInfiniteEnchantmentMixin(Enchantment.Rarity rare, EquipmentSlot... slots) {
		super(rare, EnchantmentCategory.BOW, slots);
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return (stack.getItem() instanceof CrossbowItem && QuillConfig.CROSSBOW.get()) ? true : super.canEnchant(stack);
	}
}