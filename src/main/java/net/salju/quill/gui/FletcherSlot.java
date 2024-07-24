package net.salju.quill.gui;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.Container;

public class FletcherSlot extends Slot {
	private final Item locked;

	public FletcherSlot(Container con, int a, int i, int e, Item item) {
		super(con, a, i, e);
		this.locked = item;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return (stack.getItem() == this.locked);
	}
}