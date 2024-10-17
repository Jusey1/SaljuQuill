package net.salju.quill.init;

import net.salju.quill.QuillMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

public class QuillFrogs {
	public static final DeferredRegister<FrogVariant> REGISTRY = DeferredRegister.create(Registries.FROG_VARIANT, QuillMod.MODID);
	public static final RegistryObject<FrogVariant> WITCH = REGISTRY.register("witch", () -> new FrogVariant(new ResourceLocation(QuillMod.MODID, "textures/entity/witch_frog.png")));
}