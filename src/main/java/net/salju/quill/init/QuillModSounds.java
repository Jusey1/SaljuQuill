
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.salju.quill.init;

import net.salju.quill.QuillMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

public class QuillModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, QuillMod.MODID);
	public static final RegistryObject<SoundEvent> CHEERS = REGISTRY.register("cheers", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("quill", "cheers")));
}
