package net.salju.quill.gui;

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
		return (stack.getItem() instanceof PotionItem || stack.is(Items.GLOWSTONE_DUST));
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		if (stack.getItem() instanceof PotionItem) {
			return 1;
		}
		return super.getMaxStackSize(stack);
	}
}