package net.salju.quill.gui;

import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.sounds.SoundEvents;

public class FletcherResultSlot extends Slot {
	private final Player player;

	public FletcherResultSlot(Player ply, Container con, int a, int i, int e) {
		super(con, a, i, e);
		this.player = ply;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack remove(int e) {
		ItemStack copy = this.getItem().copy();
		for (int i = 0; i < 4; ++i) {
			ItemStack stack = this.container.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof PotionItem) {
					this.container.setItem(3, new ItemStack(Items.GLASS_BOTTLE));
				} else if (stack.getItem() != Items.GLASS_BOTTLE) {
					stack.shrink(1);
				}
			}
		}
		if (this.player != null) {
			this.player.playSound(SoundEvents.VILLAGER_WORK_FLETCHER, 0.25F, 1.0F);
		}
		this.setChanged();
		return copy;
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