package net.salju.quill.init;

import net.salju.quill.block.FletcherEntity;
import net.salju.quill.QuillMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Blocks;

public class QuillBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, QuillMod.MODID);
	public static final RegistryObject<BlockEntityType<FletcherEntity>> FLETCHER = REGISTRY.register("fletcher", () -> BlockEntityType.Builder.of(FletcherEntity::new, Blocks.FLETCHING_TABLE).build(null));
}