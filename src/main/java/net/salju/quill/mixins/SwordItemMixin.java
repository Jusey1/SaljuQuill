package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillConfig;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;

@Mixin(SwordItem.class)
public abstract class SwordItemMixin extends TieredItem {
	public SwordItemMixin(Tier t, Item.Properties props) {
		super(t, props);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return QuillConfig.SWORD.get() ? 24 : super.getUseDuration(stack);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return QuillConfig.SWORD.get() ? UseAnim.BLOCK : super.getUseAnimation(stack);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (QuillConfig.SWORD.get()) {
			ItemStack off = player.getItemInHand(InteractionHand.OFF_HAND);
			if (off.isEmpty() || hand == InteractionHand.OFF_HAND) {
				player.startUsingItem(hand);
				return InteractionResultHolder.consume(player.getItemInHand(hand));
			}
		}
		return super.use(world, player, hand);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity target, int i) {
		super.releaseUsing(stack, world, target, i);
		if (target instanceof Player player) {
			player.getCooldowns().addCooldown(stack.getItem(), 80 - (i * 2));
		}
	}

	@Override
	public void onUseTick(Level world, LivingEntity target, ItemStack stack, int i) {
		super.onUseTick(world, target, stack, i);
		if (i <= 2 && target instanceof Player player) {
			player.getCooldowns().addCooldown(stack.getItem(), 80);
		}
	}
}