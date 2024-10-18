package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
	public void getMax(CallbackInfoReturnable<Integer> ci) {
		if (QuillConfig.POTS.get()) {
			ItemStack stack = (ItemStack) (Object) this;
			if (stack.getItem() instanceof PotionItem) {
				ci.setReturnValue(16);
			}
		}
	}

	@Inject(method = "isDamageableItem", at = @At("RETURN"), cancellable = true)
	public void isUnbreakable(CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get() && QuillConfig.UNBREAKING.get()) {
			ItemStack stack = (ItemStack) (Object) this;
			if (stack.isEnchanted()) {
				ci.setReturnValue(false);
			}
		}
	}
}