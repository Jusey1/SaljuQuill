package net.salju.quill.effect;

import net.salju.quill.init.QuillFrogs;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;

public class FrogEffect extends MobEffect {
	private final String name;

	public FrogEffect(MobEffectCategory cate, int i, String s) {
		super(cate, i);
		this.name = s;
	}

	@Override
	public String getDescriptionId() {
		return this.name;
	}

	@Override
	public void applyInstantenousEffect(Entity direct, Entity owner, LivingEntity target, int i, double d) {
		if (target instanceof Villager && target.level() instanceof ServerLevel lvl) {
			lvl.playSound(null, target.blockPosition(), SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.HOSTILE, 1.0F, 1.0F);
			Frog froggo = EntityType.FROG.create(lvl);
			froggo.moveTo(Vec3.atBottomCenterOf(target.blockPosition()));
			froggo.addEffect(new MobEffectInstance(MobEffects.LUCK, 3600, 0, false, true));
			froggo.setVariant(QuillFrogs.WITCH.get());
			froggo.setPersistenceRequired();
			lvl.addFreshEntity(froggo);
			target.discard();
		}
	}

	@Override
	public boolean isInstantenous() {
		return true;
	}
}