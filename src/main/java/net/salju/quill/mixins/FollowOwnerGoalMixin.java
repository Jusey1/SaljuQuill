package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillTags;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.TamableAnimal;

@Mixin(FollowOwnerGoal.class)
public abstract class FollowOwnerGoalMixin {
	@Inject(method = "canUse", at = @At("RETURN"), cancellable = true)
	public void canFollow(CallbackInfoReturnable<Boolean> ci) {
		FollowOwnerGoal thys = (FollowOwnerGoal) (Object) this;
		if (this.tamable.getType().is(QuillTags.PETS) && QuillConfig.PETS.get() && tamable.getPersistentData().getBoolean("isWandering")) {
			ci.setReturnValue(false);
		}
	}

	@Shadow
	private TamableAnimal tamable;
}