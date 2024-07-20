package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillBlockEntities;
import net.salju.quill.block.FletcherEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.FletchingTableBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;

@Mixin(FletchingTableBlock.class)
public class FletcherBlockMixin extends CraftingTableBlock implements EntityBlock {
	public FletcherBlockMixin(BlockBehaviour.Properties props) {
		super(props);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rez) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			if (world.getBlockEntity(pos) instanceof FletcherEntity target) {
				player.openMenu(target);
			}
			return InteractionResult.CONSUME;
		}
	}

	@Override
	public void onRemove(BlockState newState, Level world, BlockPos pos, BlockState oldState, boolean check) {
		if (!newState.is(oldState.getBlock())) {
			if (world.getBlockEntity(pos) instanceof FletcherEntity target) {
				target.clearContent();
			}
			super.onRemove(newState, world, pos, oldState, check);
		}
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int i, int e) {
		return world.getBlockEntity(pos) == null ? super.triggerEvent(state, world, pos, i, e) : world.getBlockEntity(pos).triggerEvent(i, e);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FletcherEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, QuillBlockEntities.FLETCHER.get(), FletcherEntity::tick);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Nullable
	public <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> a, BlockEntityType<E> e, BlockEntityTicker<? super E> se) {
		return e == a ? (BlockEntityTicker<A>) se : null;
	}
}