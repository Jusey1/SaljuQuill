package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.LivingEntity;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "getEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/entity/LivingEntity;)I", at = @At("HEAD"), cancellable = true)
	private static void getEnchantmentLvl(Enchantment ench, LivingEntity target, CallbackInfoReturnable<Integer> ci) {
		if (target instanceof Horse pony && !pony.getArmor().isEmpty()) {
			int i = EnchantmentHelper.getItemEnchantmentLevel(ench, pony.getArmor());
			ci.setReturnValue(i);
		}
	}
}