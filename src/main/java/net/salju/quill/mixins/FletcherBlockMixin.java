package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.gui.FletcherGuiMenu;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FletchingTableBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

@Mixin(FletchingTableBlock.class)
public class FletcherBlockMixin {
	@Inject(method = "use", at = @At("RETURN"), cancellable = true)
	public void onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rez, CallbackInfoReturnable<InteractionResult> ci) {
		if (world.isClientSide) {
			ci.setReturnValue(InteractionResult.SUCCESS);
		} else {
			player.openMenu(new MenuProvider() {
				@Override
				public Component getDisplayName() {
					return Component.translatable("block.minecraft.fletching_table");
				}
				
				@Override
				public AbstractContainerMenu createMenu(int i, Inventory inv, Player player) {
					return new FletcherGuiMenu(i, inv, new SimpleContainer(5));
				}
			});
			ci.setReturnValue(InteractionResult.CONSUME);
		}
	}
}