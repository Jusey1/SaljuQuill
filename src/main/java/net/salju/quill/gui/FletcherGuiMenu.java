package net.salju.quill.gui;

import net.salju.quill.init.QuillMenus;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.Container;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;

public class FletcherGuiMenu extends AbstractContainerMenu {
	private final Container crafter;

	public FletcherGuiMenu(int id, Inventory inv, @Nullable FriendlyByteBuf extraData) {
		this(id, inv, new SimpleContainer(5));
	}

	public FletcherGuiMenu(int id, Inventory inv, Container con) {
		super(QuillMenus.FLETCHER.get(), id);
		checkContainerSize(con, 5);
		this.crafter = con;
		con.startOpen(inv.player);
		this.addSlot(new FletcherSlot(con, 0, 30, 17, Items.FLINT));
		this.addSlot(new FletcherSlot(con, 1, 30, 35, Items.STICK));
		this.addSlot(new FletcherSlot(con, 2, 30, 53, Items.FEATHER));
		this.addSlot(new FletcherPotionSlot(con, 3, 66, 35));
		this.addSlot(new FletcherResultSlot(con, 4, 124, 35));
		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(inv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}
		for (int l = 0; l < 9; ++l) {
			this.addSlot(new Slot(inv, l, 8 + l * 18, 142));
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return this.crafter.stillValid(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int i) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.slots.get(i);
		if (slot != null && slot.hasItem()) {
			ItemStack slotstack = slot.getItem();
			stack = slotstack.copy();
			if (slot instanceof FletcherResultSlot) {
				return ItemStack.EMPTY;
			} else if (i < 5) {
				if (!this.moveItemStackTo(slotstack, 5, 41, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(slotstack, 0, 5, false)) {
				return ItemStack.EMPTY;
			}
			if (slotstack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (slotstack.getCount() == stack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, slotstack);
		}
		return stack;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.crafter.stopOpen(player);
	}
}