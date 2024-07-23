package net.salju.quill.client.renderer;

import net.salju.quill.init.QuillModels;
import net.salju.quill.entity.Cannon;
import net.salju.quill.client.model.CannonModel;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

public class CannonRenderer extends LivingEntityRenderer<Cannon, CannonModel<Cannon>> {
	public CannonRenderer(EntityRendererProvider.Context context) {
		super(context, new CannonModel(context.bakeLayer(QuillModels.CANNON)), 0.85f);
	}

	@Override
	public ResourceLocation getTextureLocation(Cannon target) {
		return new ResourceLocation("quill:textures/entity/cannon_" + Integer.toString(target.getCannonType()) + ".png");
	}

	@Override
	protected boolean shouldShowName(Cannon target) {
		return (!target.isEmpty());
	}

	@Override
	protected void setupRotations(Cannon target, PoseStack pose, float f1, float f2, float f3) {
		pose.mulPose(Axis.YP.rotationDegrees(180.0F - f2));
		float f = (float) (target.level().getGameTime() - target.lastHit) + f3;
		if (f < 5.0F) {
			pose.mulPose(Axis.YP.rotationDegrees(Mth.sin(f / 1.5F * (float) Math.PI) * 3.0F));
		}
	}
}