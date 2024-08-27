package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.DiggingEnchantment;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DiggerItem;

@Mixin(DamageEnchantment.class)
public class DamageEnchantmentMixin {
	@Inject(method = "canEnchant", at = @At("RETURN"), cancellable = true)
	public void enchant(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			if (stack.getItem() instanceof DiggerItem) {
				ci.setReturnValue(true);
			}
		}
	}

	@Inject(method = "checkCompatibility", at = @At("RETURN"), cancellable = true)
	public void check(Enchantment ench, CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			if (ench instanceof DiggingEnchantment || ench instanceof DamageEnchantment) {
				ci.setReturnValue(false);
			}
		}
	}
}