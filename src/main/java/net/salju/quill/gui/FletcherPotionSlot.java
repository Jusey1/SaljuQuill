package net.salju.quill.gui;

import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.Container;

public class FletcherPotionSlot extends Slot {
	public FletcherPotionSlot(Container con, int a, int i, int e) {
		super(con, a, i, e);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() instanceof PotionItem;
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		if (stack.getItem() instanceof PotionItem) {
			return 1;
		}
		return super.getMaxStackSize(stack);
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (this.container.getItem(0).is(Items.FLINT) && this.container.getItem(1).is(Items.STICK) && this.container.getItem(2).is(Items.FEATHER)) {
			ItemStack stack = new ItemStack(Items.ARROW);
			if (this.container.getItem(3).getItem() instanceof PotionItem) {
				stack = PotionUtils.setPotion(new ItemStack(Items.TIPPED_ARROW), PotionUtils.getPotion(this.container.getItem(3)));
			}
			stack.setCount(QuillConfig.ARROWS.get());
			this.container.setItem(4, stack);
		} else {
			this.container.setItem(4, ItemStack.EMPTY);
		}
	}
}