package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;

@Mixin(DigDurabilityEnchantment.class)
public class DigDurabilityEnchantmentMixin extends Enchantment {
	public DigDurabilityEnchantmentMixin(Enchantment.Rarity r, EquipmentSlot... s) {
      super(r, EnchantmentCategory.BREAKABLE, s);
   }

	@Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
	public void canEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get() && QuillConfig.UNBREAKING.get()) {
			ci.setReturnValue(false);
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (QuillConfig.ENCHS.get() && QuillConfig.UNBREAKING.get()) {
			return false;
		}
		return super.canApplyAtEnchantingTable(stack);
	}
}