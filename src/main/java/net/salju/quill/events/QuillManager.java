package net.salju.quill.events;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Minecraft;

import java.util.List;
import com.google.common.collect.Lists;

public class QuillManager {
	public static boolean isSwordBlocked(DamageSource source, LivingEntity target) {
		if (!source.is(DamageTypeTags.BYPASSES_SHIELD)) {
			Vec3 vec32 = source.getSourcePosition();
			if (vec32 != null) {
				Vec3 vec3 = target.getViewVector(1.0F);
				Vec3 vec31 = vec32.vectorTo(target.position()).normalize();
				vec31 = new Vec3(vec31.x, 0.0D, vec31.z);
				if (vec31.dot(vec3) < 0.0D) {
					return true;
				}
			}
		}
		return false;
	}

	public static void creeperFireworks(Level world, double x, double y, double z) {
		if (world instanceof ClientLevel lvl) {
			ParticleEngine eng = Minecraft.getInstance().particleEngine;
			eng.add(new FireworkParticles.Starter(lvl, x, y + 0.5F, z, 0, 0, 0, eng, getPride()));
		}
	}

	private static CompoundTag getPride() {
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