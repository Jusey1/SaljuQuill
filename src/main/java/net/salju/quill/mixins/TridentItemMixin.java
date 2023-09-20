package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.salju.quill.init.QuillEnchantments;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;

@Mixin(TridentItem.class)
public class TridentItemMixin {
	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void getAirstrike(Level world, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder> ci) {
		ItemStack stack = player.getItemInHand(hand);
		int i = EnchantmentHelper.getItemEnchantmentLevel(QuillEnchantments.AIRSTRIKE.get(), stack);
		if (i > 0) {
			ci.cancel();
			if (player.isFallFlying()) {
				player.startUsingItem(hand);
				ci.setReturnValue(InteractionResultHolder.consume(stack));
			} else {
				ci.setReturnValue(InteractionResultHolder.fail(stack));
			}
		}
	}

	@Inject(method = "releaseUsing", at = @At("HEAD"), cancellable = true)
	private void getFlight(ItemStack stack, Level world, LivingEntity target, int i, CallbackInfo ci) {
		int j = EnchantmentHelper.getItemEnchantmentLevel(QuillEnchantments.AIRSTRIKE.get(), stack);
		if (j > 0 && target instanceof Player player && player.isFallFlying()) {
			ci.cancel();
			if (!world.isClientSide) {
				stack.hurtAndBreak(1, player, (user) -> {
					user.broadcastBreakEvent(target.getUsedItemHand());
				});
			}
			float f7 = player.getYRot();
			float f = player.getXRot();
			float f1 = -Mth.sin(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
			float f2 = -Mth.sin(f * ((float) Math.PI / 180F));
			float f3 = Mth.cos(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
			float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
			float f5 = 2.0F * ((1.0F + (float) j) / 4.0F);
			f1 *= f5 / f4;
			f2 *= f5 / f4;
			f3 *= f5 / f4;
			player.push((double) f1, (double) f2, (double) f3);
			player.startAutoSpinAttack(20);
			player.getCooldowns().addCooldown(stack.getItem(), (30 * j));
			world.playSound(null, player, SoundEvents.TRIDENT_RIPTIDE_1, SoundSource.PLAYERS, 1.0F, 1.0F);
		}
	}
}