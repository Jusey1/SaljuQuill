package net.salju.quill.init;

import net.salju.quill.effect.*;
import net.salju.quill.QuillMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class QuillEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, QuillMod.MODID);
	public static final RegistryObject<MobEffect> FROGGO = REGISTRY.register("froggo", () -> new FrogEffect(MobEffectCategory.HARMFUL, -6750055, "effect.quill.froggo"));
}