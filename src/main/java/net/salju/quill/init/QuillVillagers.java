package net.salju.quill.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.core.registries.Registries;

public class QuillVillagers {
	public static final DeferredRegister<VillagerType> REGISTRY = DeferredRegister.create(Registries.VILLAGER_TYPE, "minecraft");
	public static final RegistryObject<VillagerType> OCEAN = REGISTRY.register("ocean", () -> new VillagerType("ocean"));
}