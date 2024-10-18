package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.HorseArmorItem;

@Mixin(targets = {"net.minecraft.world.item.enchantment.EnchantmentCategory$1", "net.minecraft.world.item.enchantment.EnchantmentCategory$2"})
public class EnchantmentCategoryMixin {
	@Inject(method = "canEnchant(Lnet/minecraft/world/item/Item;)Z", at = @At("RETURN"), cancellable = true)
	public void enchant(Item item, CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			if (item instanceof HorseArmorItem) {
				ci.setReturnValue(true);
			}
		}
	}
}