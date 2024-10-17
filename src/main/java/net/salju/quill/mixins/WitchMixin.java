package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillPotions;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.sounds.SoundEvents;

@Mixin(Witch.class)
public abstract class WitchMixin {
	@Inject(method = "aiStep", at = @At(value = "INVOKE"), cancellable = true)
	private void step(CallbackInfo ci) {
		if (QuillConfig.FROGGO.get()) {
			Witch hag = (Witch) (Object) this;
			if (!hag.level().isClientSide && hag.isAlive()) {
				Villager trader = hag.level().getNearestEntity(Villager.class, TargetingConditions.DEFAULT, hag, hag.getX(), hag.getY(), hag.getZ(), hag.getBoundingBox().inflate(12.85D));
				if (trader != null && trader.getVillagerData().getProfession() == VillagerProfession.NITWIT) {
					hag.setTarget(trader);
				}
			}
		}
	}

	@Inject(method = "performRangedAttack", at = @At(value = "INVOKE"), cancellable = true)
	private void potion(LivingEntity target, float f1, CallbackInfo ci) {
		if (QuillConfig.FROGGO.get()) {
			Witch hag = (Witch) (Object) this;
			if (target instanceof Villager trader && trader.getVillagerData().getProfession() == VillagerProfession.NITWIT && !hag.isDrinkingPotion()) {
				ThrownPotion pot = new ThrownPotion(hag.level(), hag);
				double d0 = trader.getX() + trader.getDeltaMovement().x - hag.getX();
				double d1 = trader.getEyeY() - (double) 1.1F - hag.getY();
				double d2 = trader.getZ() + trader.getDeltaMovement().z - hag.getZ();
				double d3 = Math.sqrt(d0 * d0 + d2 * d2);
				pot.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), QuillPotions.FROGGO.get()));
				pot.setXRot(pot.getXRot() + 20.0F);
				pot.shoot(d0, d1 + d3 * 0.2D, d2, 0.75F, 8.0F);
				if (!hag.isSilent()) {
					hag.level().playSound(null, hag.getX(), hag.getY(), hag.getZ(), SoundEvents.WITCH_THROW, hag.getSoundSource(), 1.0F, 0.8F + hag.getRandom().nextFloat() * 0.4F);
				}
				hag.level().addFreshEntity(pot);
				hag.setTarget(null);
				ci.cancel();
			}
		}
	}
}
