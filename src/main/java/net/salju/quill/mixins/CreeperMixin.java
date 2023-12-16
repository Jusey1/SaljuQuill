package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;

import net.salju.quill.network.Fireworks;
import net.salju.quill.init.QuillModSounds;
import net.salju.quill.init.QuillConfig;
import net.salju.quill.QuillMod;

import net.minecraft.world.level.Explosion;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;

@Mixin(Creeper.class)
public abstract class CreeperMixin {
	@Inject(method = "explodeCreeper", at = @At(value = "INVOKE"), cancellable = true)
	private void boom(CallbackInfo ci) {
		if (QuillConfig.CREEPER.get()) {
			Creeper creeper = (Creeper) (Object) this;
			float f = creeper.isPowered() ? 2.0F : 1.0F;
			if (creeper.level() instanceof ServerLevel lvl) {
				lvl.playSound(null, creeper.blockPosition(), SoundEvents.FIREWORK_ROCKET_TWINKLE, SoundSource.HOSTILE, 1.0F, 1.0F);
				if (creeper.isPowered() || (Math.random() <= 0.12)) {
					lvl.playSound(null, creeper.blockPosition(), QuillModSounds.CHEERS.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
				}
				for (ServerPlayer ply : lvl.getPlayers(LivingEntity::isAlive)) {
					if (ply.isCloseEnough(creeper, 32)) {
						QuillMod.sendToClientPlayer(new Fireworks(creeper.getX(), creeper.getY(), creeper.getZ()), ply);
					}
				}
				new Explosion(creeper.level(), creeper, creeper.getX(), creeper.getY(), creeper.getZ(), 3.0F * f, false, Explosion.BlockInteraction.KEEP).explode();
				spawnLingeringCloud();
				creeper.discard();
			}
			ci.cancel();
		}
	}

	@Shadow
	abstract void spawnLingeringCloud();
}