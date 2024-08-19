package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.item.MagicMirrorItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.player.AbstractClientPlayer;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
	@Inject(method = "renderArmWithItem", at = @At(value = "INVOKE_ASSIGN"))
	private void renderSwordBlock(AbstractClientPlayer player, float ticks, float p, InteractionHand hand, float sP, ItemStack stack, float f, PoseStack pose, MultiBufferSource buffer, int i, CallbackInfo ci) {
		if (player.isUsingItem() && stack == player.getUseItem()) {
			if (stack.getItem() instanceof SwordItem) {
				float r = (hand == InteractionHand.MAIN_HAND ? 6.0F : -6.0F);
				float t = (hand == InteractionHand.MAIN_HAND ? 0.045F : -0.045F);
				pose.translate(t, 0.015F, 0.02F);
				pose.mulPose(Axis.XP.rotationDegrees(-6.0F));
				pose.mulPose(Axis.YP.rotationDegrees(r));
				pose.mulPose(Axis.ZP.rotationDegrees(r));
			} else if (stack.getItem() instanceof MagicMirrorItem) {
				float r = (hand == InteractionHand.MAIN_HAND ? 5.0F : -5.0F);
				float t = (hand == InteractionHand.MAIN_HAND ? 0.045F : -0.045F);
				pose.translate(t, 0.015F, 0.02F);
				pose.mulPose(Axis.XP.rotationDegrees(-1.0F));
				pose.mulPose(Axis.YP.rotationDegrees(r));
			}
		}
	}
}