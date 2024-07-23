package net.salju.quill.init;

import net.salju.quill.item.*;
import net.salju.quill.QuillMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ArmorItem;

public class QuillItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, QuillMod.MODID);
	public static final RegistryObject<Item> TRADER_BLOCK = block(QuillBlocks.TRADER_BLOCK);
	public static final RegistryObject<Item> SALJU = REGISTRY.register("salju", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> NETHERITE_HORSE_ARMOR = REGISTRY.register("netherite_horse_armor", () -> new HorseArmorItem(21, "netherite", (new Item.Properties()).stacksTo(1).fireResistant()));
	public static final RegistryObject<Item> IRON_HELMET = REGISTRY.register("iron_helmet", () -> new AltHelmetItem(ArmorMaterials.IRON, ArmorItem.Type.HELMET, new Item.Properties(), "quill:textures/models/armor/gothic_iron.png"));
	public static final RegistryObject<Item> DIAMOND_HELMET = REGISTRY.register("diamond_helmet", () -> new AltHelmetItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new Item.Properties(), "quill:textures/models/armor/gothic_diamond.png"));
	public static final RegistryObject<Item> GOLDEN_HELMET = REGISTRY.register("golden_helmet", () -> new AltHelmetItem(ArmorMaterials.GOLD, ArmorItem.Type.HELMET, new Item.Properties(), "quill:textures/models/armor/gothic_golden.png"));
	public static final RegistryObject<Item> NETHERITE_HELMET = REGISTRY.register("netherite_helmet", () -> new AltHelmetItem(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new Item.Properties(), "quill:textures/models/armor/gothic_netherite.png"));
	public static final RegistryObject<Item> MAGIC_MIRROR = REGISTRY.register("magic_mirror", () -> new MagicMirrorItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BUNDLE = REGISTRY.register("bundle", () -> new BundleItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CANNON = REGISTRY.register("cannon", () -> new CannonItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CANNONBALL = REGISTRY.register("cannonball", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> COPPER_CANNONBALL = REGISTRY.register("copper_cannonball", () -> new Item(new Item.Properties()));

	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}