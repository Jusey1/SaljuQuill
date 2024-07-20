package net.salju.quill.gui;

import net.salju.quill.block.FletcherEntity;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.Container;

public class FletcherResultSlot extends Slot {
	public FletcherResultSlot(Container con, int a, int i, int e) {
		super(con, a, i, e);
	}

	@Override
	public ItemStack getItem() {
		if (this.container instanceof FletcherEntity target) {
			return target.getResultItem();
		}
		return super.getItem();
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack remove(int i) {
		if (this.container instanceof FletcherEntity target) {
			for (ItemStack stack : target.stacks) {
				if (!stack.isEmpty() && !target.isResultItem(stack)) {
					if (stack.getItem() instanceof PotionItem) {
						target.setItem(3, new ItemStack(Items.GLASS_BOTTLE));
					} else if (stack != target.getItem(3)) {
						stack.shrink(1);
					}
				}
			}
		}
		return this.getItem();
	}
}