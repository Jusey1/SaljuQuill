package net.salju.quill.block;

import net.salju.quill.init.QuillConfig;
import net.salju.quill.init.QuillBlockEntities;
import net.salju.quill.gui.FletcherGuiMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Containers;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.network.Connection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;

public class FletcherEntity extends BaseContainerBlockEntity {
	public NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(5, ItemStack.EMPTY);

	public FletcherEntity(BlockPos pos, BlockState state) {
		super(QuillBlockEntities.FLETCHER.get(), pos, state);
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		ContainerHelper.saveAllItems(tag, this.stacks);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, this.stacks);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection queen, ClientboundBlockEntityDataPacket packet) {
		if (packet != null && packet.getTag() != null) {
			this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
			ContainerHelper.loadAllItems(packet.getTag(), this.stacks);
		}
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
		ContainerHelper.saveAllItems(tag, this.stacks);
		return tag;
	}

	@Override
	public Component getDefaultName() {
		return Component.translatable("block.minecraft.fletching_table");
	}

	@Override
	public AbstractContainerMenu createMenu(int i, Inventory bag) {
		return new FletcherGuiMenu(i, bag, this);
	}

	@Override
	public int getContainerSize() {
		return stacks.size();
	}

	@Override
	public boolean isEmpty() {
		return this.getResultItem().isEmpty();
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public ItemStack getItem(int i) {
		if (this.isResultItem(this.stacks.get(i))) {
			return ItemStack.EMPTY;
		}
		return this.stacks.get(i);
	}

	@Override
	public boolean canPlaceItem(int i, ItemStack stack) {
		if (i == 0 && stack.is(Items.FLINT)) {
			return true;
		} else if (i == 1 && stack.is(Items.STICK)) {
			return true;
		} else if (i == 2 && stack.is(Items.FEATHER)) {
			return true;
		} else if (i == 3 && stack.getItem() instanceof PotionItem) {
			return true;
		}
		return false;
	}

	@Override
	public ItemStack removeItemNoUpdate(int i) {
		return ContainerHelper.takeItem(this.stacks, i);
	}

	@Override
	public ItemStack removeItem(int i, int e) {
		this.updateBlock();
		return ContainerHelper.removeItem(this.stacks, i, e);
	}

	@Override
	public void setItem(int i, ItemStack stack) {
		this.stacks.set(i, stack.copy());
		this.updateBlock();
	}

	@Override
	public void clearContent() {
		double x = ((double) this.getBlockPos().getX() + 0.5);
		double y = ((double) this.getBlockPos().getY() + 0.5);
		double z = ((double) this.getBlockPos().getZ() + 0.5);
		for (ItemStack stack : this.stacks) {
			if (!stack.isEmpty() && !this.isResultItem(stack)) {
				Containers.dropItemStack(this.getLevel(), x, y, z, stack);
			}
		}
		this.updateBlock();
	}

	public ItemStack getResultItem() {
		return this.stacks.get(4);
	}

	public boolean isResultItem(ItemStack stack) {
		return (stack == this.getResultItem());
	}

	public void updateBlock() {
		this.setChanged();
		this.getLevel().updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
	}

	public static void tick(Level world, BlockPos pos, BlockState state, FletcherEntity target) {
		if (world instanceof ServerLevel lvl) {
			if (target.getItem(0).is(Items.FLINT) && target.getItem(1).is(Items.STICK) && target.getItem(2).is(Items.FEATHER)) {
				ItemStack stack = new ItemStack(Items.ARROW);
				if (target.getItem(3).getItem() instanceof PotionItem) {
					stack = PotionUtils.setPotion(new ItemStack(Items.TIPPED_ARROW), PotionUtils.getPotion(target.getItem(3)));
				}
				stack.setCount(QuillConfig.ARROWS.get());
				target.setItem(4, stack);
			} else {
				target.setItem(4, ItemStack.EMPTY);
			}
		}
	}
}