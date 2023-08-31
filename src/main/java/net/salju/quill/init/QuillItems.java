package net.salju.quill.init;

import net.salju.quill.QuillMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.BlockItem;

public class QuillItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, QuillMod.MODID);
	public static final RegistryObject<Item> TRADER_BLOCK = block(QuillBlocks.TRADER_BLOCK);
	public static final RegistryObject<Item> NETHERITE_HORSE_ARMOR = REGISTRY.register("netherite_horse_armor", () -> new HorseArmorItem(18, "netherite", (new Item.Properties()).stacksTo(1).fireResistant()));

	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}