package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.HorseArmorItem;
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
			} else if (thys == Enchantments.INFINITY_ARROWS && stack.getItem() instanceof CrossbowItem) {
				ci.setReturnValue(true);
			} else if ((thys == Enchantments.UNBREAKING || thys == Enchantments.MENDING) && QuillConfig.UNBREAKING.get()) {
				ci.setReturnValue(false);
			} else if ((thys.category == EnchantmentCategory.ARMOR_FEET || thys.category == EnchantmentCategory.ARMOR) && stack.getItem() instanceof HorseArmorItem) {
				ci.setReturnValue(true);
			}
		}
	}

	@Inject(method = "checkCompatibility", at = @At("RETURN"), cancellable = true)
	public void check(Enchantment ench, CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			Enchantment thys = (Enchantment) (Object) this;
			if ((thys == Enchantments.BLOCK_EFFICIENCY || thys instanceof DamageEnchantment) && (ench == Enchantments.BLOCK_EFFICIENCY || ench instanceof DamageEnchantment)) {
				ci.setReturnValue(false);
			} else if ((thys == Enchantments.UNBREAKING || thys == Enchantments.MENDING) && QuillConfig.UNBREAKING.get()) {
				ci.setReturnValue(false);
			}
		}
	}

	@Inject(method = "isTreasureOnly", at = @At("RETURN"), cancellable = true)
	public void treasure(CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			Enchantment thys = (Enchantment) (Object) this;
			if ((thys == Enchantments.UNBREAKING || thys == Enchantments.MENDING) && QuillConfig.UNBREAKING.get()) {
				ci.setReturnValue(true);
			}
		}
	}

	@Inject(method = "isTradeable", at = @At("RETURN"), cancellable = true)
	public void trade(CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			Enchantment thys = (Enchantment) (Object) this;
			if ((thys == Enchantments.UNBREAKING || thys == Enchantments.MENDING) && QuillConfig.UNBREAKING.get()) {
				ci.setReturnValue(false);
			}
		}
	}

	@Inject(method = "isDiscoverable", at = @At("RETURN"), cancellable = true)
	public void discover(CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			Enchantment thys = (Enchantment) (Object) this;
			if ((thys == Enchantments.UNBREAKING || thys == Enchantments.MENDING) && QuillConfig.UNBREAKING.get()) {
				ci.setReturnValue(false);
			}
		}
	}
}