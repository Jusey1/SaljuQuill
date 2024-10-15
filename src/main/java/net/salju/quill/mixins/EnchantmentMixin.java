package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.DiggingEnchantment;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.ArrowInfiniteEnchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.CrossbowItem;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Inject(method = "canEnchant", at = @At("RETURN"), cancellable = true)
	public void enchant(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			Enchantment thys = (Enchantment) (Object) this;
			if (thys instanceof DamageEnchantment && stack.getItem() instanceof DiggerItem) {
				ci.setReturnValue(true);
			} else if (thys instanceof ArrowInfiniteEnchantment && stack.getItem() instanceof CrossbowItem) {
				ci.setReturnValue(true);
			} else if ((thys instanceof DigDurabilityEnchantment || thys instanceof MendingEnchantment) && QuillConfig.UNBREAKING.get()) {
				ci.setReturnValue(false);
			}
		}
	}

	@Inject(method = "checkCompatibility", at = @At("RETURN"), cancellable = true)
	public void check(Enchantment ench, CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			Enchantment thys = (Enchantment) (Object) this;
			if ((thys instanceof DiggingEnchantment || thys instanceof DamageEnchantment) && (ench instanceof DiggingEnchantment || ench instanceof DamageEnchantment)) {
				ci.setReturnValue(false);
			} else if ((thys instanceof DigDurabilityEnchantment || thys instanceof MendingEnchantment) && QuillConfig.UNBREAKING.get()) {
				ci.setReturnValue(false);
			}
		}
	}

	@Inject(method = "isTreasureOnly", at = @At("RETURN"), cancellable = true)
	public void treasure(CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			Enchantment thys = (Enchantment) (Object) this;
			if ((thys instanceof DigDurabilityEnchantment || thys instanceof MendingEnchantment) && QuillConfig.UNBREAKING.get()) {
				ci.setReturnValue(true);
			}
		}
	}

	@Inject(method = "isTradeable", at = @At("RETURN"), cancellable = true)
	public void trade(CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			Enchantment thys = (Enchantment) (Object) this;
			if ((thys instanceof DigDurabilityEnchantment || thys instanceof MendingEnchantment) && QuillConfig.UNBREAKING.get()) {
				ci.setReturnValue(false);
			}
		}
	}

	@Inject(method = "isDiscoverable", at = @At("RETURN"), cancellable = true)
	public void discover(CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			Enchantment thys = (Enchantment) (Object) this;
			if ((thys instanceof DigDurabilityEnchantment || thys instanceof MendingEnchantment) && QuillConfig.UNBREAKING.get()) {
				ci.setReturnValue(false);
			}
		}
	}
}