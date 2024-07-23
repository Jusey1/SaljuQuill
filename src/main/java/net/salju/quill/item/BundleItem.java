package net.salju.quill.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraft.stats.Stats;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.ChatFormatting;
import java.util.stream.Stream;
import java.util.Optional;
import java.util.List;

public class BundleItem extends Item implements Vanishable {
	public BundleItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, world, list, flag);
		list.add(Component.translatable("item.minecraft.bundle.fullness", getContentWeight(stack), 256).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		NonNullList<ItemStack> list = NonNullList.create();
		getContents(stack).forEach(list::add);
		return Optional.of(new BundleTooltip(list, getContentWeight(stack)));
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return getContentWeight(stack) > 0;
	}

	@Override
	public boolean canFitInsideContainerItems() {
		return false;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return Math.min(1 + 12 * getContentWeight(stack) / 256, 13);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return Mth.color(0.4F, 0.4F, 1.0F);
	}

	@Override
	public void onDestroyed(ItemEntity target) {
		ItemUtils.onContainerDestroyed(target, getContents(target.getItem()));
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity target, int e, boolean check) {
		super.inventoryTick(stack, world, target, e, check);
		CompoundTag tag = stack.getOrCreateTag();
		if (EnchantmentHelper.hasVanishingCurse(stack)) {
			if (tag.contains("Items")) {
				tag.remove("Items");
			}
		}
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
		if (action != ClickAction.SECONDARY) {
			return false;
		} else {
			ItemStack target = slot.getItem();
			if (!target.isEmpty() && EnchantmentHelper.hasVanishingCurse(stack)) {
				this.playSound(player, SoundEvents.BUNDLE_INSERT);
				target.shrink(target.getCount());
			} else if (target.isEmpty() && getContentWeight(stack) > 0) {
				this.playSound(player, SoundEvents.BUNDLE_REMOVE_ONE);
				removeOne(stack).ifPresent((item) -> {
					add(stack, slot.safeInsert(item));
				});
			} else if (target.getItem().canFitInsideContainerItems()) {
				int i = (256 - getContentWeight(stack));
				if (add(stack, slot.safeTake(target.getCount(), i, player))) {
					this.playSound(player, SoundEvents.BUNDLE_INSERT);
				}
			}
			return true;
		}
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack target, Slot slot, ClickAction action, Player player, SlotAccess acc) {
		if (action != ClickAction.SECONDARY) {
			return false;
		} else if (slot.allowModification(player)) {
			if (!target.isEmpty() && EnchantmentHelper.hasVanishingCurse(stack)) {
				this.playSound(player, SoundEvents.BUNDLE_INSERT);
				target.shrink(target.getCount());
			} else if (target.isEmpty()) {
				removeOne(stack).ifPresent((i) -> {
					this.playSound(player, SoundEvents.BUNDLE_REMOVE_ONE);
					acc.set(i);
				});
			} else if (target.getItem().canFitInsideContainerItems()) {
				int i = (256 - getContentWeight(stack));
				if (add(stack, target)) {
					this.playSound(player, SoundEvents.BUNDLE_INSERT);
					target.shrink(i);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (dropContents(stack, player)) {
			this.playSound(player, SoundEvents.BUNDLE_DROP_CONTENTS);
			player.awardStat(Stats.ITEM_USED.get(this));
			return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
		} else {
			return InteractionResultHolder.fail(stack);
		}
	}

	private Optional<CompoundTag> getMatchingItem(ItemStack stack, ListTag list) {
		return stack.is(Items.BUNDLE) ? Optional.empty() : list.stream().filter(CompoundTag.class::isInstance).map(CompoundTag.class::cast).filter((item) -> {
			return ItemStack.isSameItemSameTags(ItemStack.of(item), stack);
		}).findFirst();
	}

	private boolean add(ItemStack stack, ItemStack item) {
		if (!item.isEmpty() && item.getItem().canFitInsideContainerItems()) {
			CompoundTag tag = stack.getOrCreateTag();
			if (!tag.contains("Items")) {
				tag.put("Items", new ListTag());
			}
			if (getContentWeight(stack) >= 256) {
				return false;
			} else {
				ListTag list = tag.getList("Items", 10);
				Optional<CompoundTag> optional = getMatchingItem(item, list);
				if (optional.isPresent()) {
					CompoundTag opttag = optional.get();
					ItemStack itemstack = ItemStack.of(opttag);
					if (itemstack.getCount() >= itemstack.getMaxStackSize()) {
						ItemStack newstack = item.copyWithCount(item.getCount());
						CompoundTag itemtag = new CompoundTag();
						newstack.save(itemtag);
						list.add(0, (Tag) itemtag);
					} else {
						int i = (itemstack.getCount() + item.getCount());
						if (i > itemstack.getMaxStackSize()) {
							itemstack.setCount(itemstack.getMaxStackSize());
							itemstack.save(opttag);
							list.remove(opttag);
							list.add(0, (Tag) opttag);
							ItemStack newstack = item.copyWithCount(i - itemstack.getMaxStackSize());
							CompoundTag itemtag = new CompoundTag();
							newstack.save(itemtag);
							list.add(0, (Tag) itemtag);
						} else {
							itemstack.grow(item.getCount());
							itemstack.save(opttag);
							list.remove(opttag);
							list.add(0, (Tag) opttag);
						}
					}
				} else {
					ItemStack itemstack = item.copyWithCount(item.getCount());
					CompoundTag itemtag = new CompoundTag();
					itemstack.save(itemtag);
					list.add(0, (Tag) itemtag);
				}
				return true;
			}
		} else {
			return false;
		}
	}

	public boolean dropContents(ItemStack stack, Entity target) {
		CompoundTag tag = stack.getOrCreateTag();
		if (!tag.contains("Items")) {
			return false;
		} else {
			ListTag list = tag.getList("Items", 10);
			for (int i = 0; i < list.size(); ++i) {
				CompoundTag newtag = list.getCompound(i);
				ItemStack newstack = ItemStack.of(newtag);
				if (target instanceof ServerPlayer ply) {
					ply.drop(newstack, true);
				} else {
					target.spawnAtLocation(newstack);
				}
			}
			stack.removeTagKey("Items");
			return true;
		}
	}

	private Optional<ItemStack> removeOne(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (!tag.contains("Items")) {
			return Optional.empty();
		} else {
			ListTag list = tag.getList("Items", 10);
			if (list.isEmpty()) {
				return Optional.empty();
			} else {
				CompoundTag itemtag = list.getCompound(0);
				list.remove(0);
				if (list.isEmpty()) {
					stack.removeTagKey("Items");
				}
				return Optional.of(ItemStack.of(itemtag));
			}
		}
	}

	private Stream<ItemStack> getContents(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag == null) {
			return Stream.empty();
		} else {
			ListTag list = tag.getList("Items", 10);
			return list.stream().map(CompoundTag.class::cast).map(ItemStack::of);
		}
	}

	private int getContentWeight(ItemStack stack) {
		return getContents(stack).mapToInt((item) -> {
			return 1 * item.getCount();
		}).sum();
	}

	private void playSound(Entity target, SoundEvent sound) {
		if (target.level() instanceof ServerLevel lvl) {
			lvl.playSound(null, target.blockPosition(), sound, SoundSource.AMBIENT, 1.0F, 0.8F + lvl.getRandom().nextFloat() * 0.4F);
		} else {
			target.playSound(sound, 0.8F, 0.8F + target.level().getRandom().nextFloat() * 0.4F);
		}
	}
}