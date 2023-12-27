package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.HorseArmorItem;

@Mixin(HorseArmorItem.class)
public class HorseArmorItemMixin extends Item {
	public HorseArmorItemMixin(Item.Properties p) {
		super(p);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment ench) {
		return (ench.category == EnchantmentCategory.ARMOR_FEET || ench instanceof ProtectionEnchantment);
	}
}