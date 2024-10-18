package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillTags;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;

@Mixin(Item.class)
public abstract class ItemMixin {
	@Inject(method = "canAttackBlock", at = @At("HEAD"), cancellable = true)
	public void canBreakBlock(BlockState state, Level world, BlockPos pos, Player player, CallbackInfoReturnable<Boolean> ci) {
		Item thys = (Item) (Object) this;
		if (player.isCreative() && thys instanceof DiggerItem) {
			ci.setReturnValue(false);
		}
	}

	@Inject(method = "isEnchantable", at = @At("HEAD"), cancellable = true)
	public void isEnchantable(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
		if (QuillConfig.ENCHS.get()) {
			Item thys = (Item) (Object) this;
			if (stack.is(QuillTags.ENCHS) || thys instanceof HorseArmorItem) {
				ci.setReturnValue(true);
			}
		}
	}

	@Inject(method = "getEnchantmentValue", at = @At("HEAD"), cancellable = true)
	public void getValue(CallbackInfoReturnable<Integer> ci) {
		if (QuillConfig.ENCHS.get()) {
			Item thys = (Item) (Object) this;
			if (new ItemStack(thys).is(QuillTags.ENCHS) || thys instanceof HorseArmorItem) {
				ci.setReturnValue(thys instanceof HorseArmorItem ? 15 : 1);
			}
		}
	}
}