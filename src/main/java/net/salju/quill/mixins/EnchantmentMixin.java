package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.DiggingEnchantment;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DiggerItem;
import java.util.Map;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
	public void enchant(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			Enchantment thys = (Enchantment) (Object) this;
			Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
			boolean check = false;
			int i = 0;
			for (Map.Entry<Enchantment, Integer> e : map.entrySet()) {
				Enchantment ench = e.getKey();
				if (ench != null && !ench.isCurse()) {
					i++;
					if (ench == thys && EnchantmentHelper.getItemEnchantmentLevel(ench, stack) < ench.getMaxLevel()){
						check = true;
					}
				}
			}
			if (i >= 3 && !check && !thys.isCurse()) {
				ci.setReturnValue(false);
			} else if (thys instanceof DamageEnchantment && stack.getItem() instanceof DiggerItem) {
				ci.setReturnValue(true);
			}
		}
	}

	@Inject(method = "checkCompatibility", at = @At("HEAD"), cancellable = true)
	public void check(Enchantment ench, CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			Enchantment thys = (Enchantment) (Object) this;
			if (thys instanceof DamageEnchantment && (ench instanceof DiggingEnchantment || ench instanceof DamageEnchantment)) {
				ci.setReturnValue(false);
			}
		}
	}
}