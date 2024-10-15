package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.HorseArmorItem;

@Mixin(HorseArmorItem.class)
public abstract class HorseArmorItemMixin extends Item {
	public HorseArmorItemMixin(Item.Properties p) {
		super(p);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment ench) {
		if (QuillConfig.ENCHS.get() && (ench.category == EnchantmentCategory.ARMOR_FEET || ench instanceof ProtectionEnchantment)) {
			return true;
		}
		return super.canApplyAtEnchantingTable(stack, ench);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return (QuillConfig.ENCHS.get() ? true : super.isEnchantable(stack));
	}

	@Override
	public int getEnchantmentValue() {
		return (QuillConfig.ENCHS.get() ? 15 : super.getEnchantmentValue());
	}
}