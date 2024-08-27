package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;

@Mixin(MendingEnchantment.class)
public class MendingEnchantmentMixin extends Enchantment {
	public MendingEnchantmentMixin(Enchantment.Rarity r, EquipmentSlot... s) {
		super(r, EnchantmentCategory.BREAKABLE, s);
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		if (QuillConfig.ENCHS.get() && QuillConfig.UNBREAKING.get()) {
			return false;
		}
		return super.canEnchant(stack);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (QuillConfig.ENCHS.get() && QuillConfig.UNBREAKING.get()) {
			return false;
		}
		return super.canApplyAtEnchantingTable(stack);
	}
}