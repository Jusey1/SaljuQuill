package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.salju.quill.init.QuillModSounds;
import net.salju.quill.QuillMod;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Minecraft;

import java.util.List;
import com.google.common.collect.Lists;

@Mixin(Creeper.class)
public class CreeperMixin {
	private boolean check;

	@Inject(method = "explodeCreeper", at = @At(value = "INVOKE"), cancellable = true)
	private void boom(CallbackInfo ci) {
		if (check != true) {
			check = true;
			Creeper creeper = (Creeper) (Object) this;
			double x = creeper.getX();
			double y = creeper.getY();
			double z = creeper.getZ();
			boolean powered = creeper.isPowered();
			Level world = creeper.level();
			world.playLocalSound(x, y, z, SoundEvents.FIREWORK_ROCKET_TWINKLE, SoundSource.HOSTILE, 1.0F, 1.0F, false);
			if (powered || (Math.random() <= 0.12)) {
				world.playLocalSound(x, y, z, QuillModSounds.CHEERS.get(), SoundSource.HOSTILE, 2.0F, 1.0F, false);
			}
			if (world.isClientSide) {
				ParticleEngine eng = Minecraft.getInstance().particleEngine;
				eng.add(new FireworkParticles.Starter((ClientLevel) world, x, y + 0.5F, z, 0, 0, 0, eng, getPride(creeper)));
			}
			QuillMod.queueServerWork(2, () -> {
				float f = powered ? 2.0F : 1.0F;
				Explosion demoman = new Explosion(world, creeper, null, null, x, y, z, 3.0F * f, false, Explosion.BlockInteraction.KEEP);
				demoman.explode();
				creeper.discard();
			});
		}
		ci.cancel();
	}

	private CompoundTag getPride(Creeper creeper) {
		int i = Mth.nextInt(RandomSource.create(), 0, 10);
		CompoundTag mainTag = new CompoundTag();
		CompoundTag finalTag = new CompoundTag();
		ListTag nbt = new ListTag();
		List<Integer> list = Lists.<Integer>newArrayList();
		if (i <= 0) {
			list.add(0x1D1D21);
			list.add(0x474F52);
			list.add(0xF9FFFE);
			list.add(0x80C71F);
		} else if (i == 1) {
			list.add(0x1D1D21);
			list.add(0x474F52);
			list.add(0xF9FFFE);
			list.add(0x8932B8);
		} else if (i == 2) {
			list.add(0xB02E26);
			list.add(0xF9801D);
			list.add(0xFED83D);
			list.add(0x009933);
			list.add(0x3C44AA);
			list.add(0x8932B8);
		} else if (i == 3) {
			list.add(0xF9801D);
			list.add(0xF9FFFE);
			list.add(0xFF00FF);
		} else if (i == 4) {
			list.add(0xEE2B7A);
			list.add(0x8932B8);
			list.add(0x3C44AA);
		} else if (i == 5) {
			list.add(0x00FFFF);
			list.add(0xF38BAA);
			list.add(0xF9FFFE);
		} else if (i == 6) {
			list.add(0xEE2B7A);
			list.add(0xFED83D);
			list.add(0x3AB3DA);
		} else if (i == 7) {
			list.add(0xFED83D);
			list.add(0xF9FFFE);
			list.add(0x8932B8);
			list.add(0x1D1D21);
		} else if (i == 8) {
			list.add(0xF38BAA);
			list.add(0xF9FFFE);
			list.add(0xDF83FF);
			list.add(0x1D1D21);
			list.add(0x3C44AA);
		} else if (i == 9) {
			list.add(0xDF83FF);
			list.add(0xF9FFFE);
			list.add(0x009933);
		} else if (i >= 10) {
			list.add(0xFED83D);
			list.add(0x8932B8);
		}
		int[] colors = new int[list.size()];
		for (int c = 0; c < colors.length; c++) {
			colors[c] = list.get(c).intValue();
		}
		mainTag.putIntArray("Colors", colors);
		mainTag.putBoolean("Flicker", true);
		mainTag.putByte("Type", (byte) 4);
		nbt.add(mainTag);
		finalTag.put("Explosions", nbt);
		return finalTag;
	}
}