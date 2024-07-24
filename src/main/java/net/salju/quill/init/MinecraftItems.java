package net.salju.quill.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.AxeItem;

public class MinecraftItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");
	public static final RegistryObject<Item> GOLDEN_AXE = REGISTRY.register("golden_axe", () -> new AxeItem(Tiers.GOLD, 5.0F, -3.0F, (new Item.Properties())));
	public static final RegistryObject<Item> WOODEN_AXE = REGISTRY.register("wooden_axe", () -> new AxeItem(Tiers.WOOD, 5.0F, -3.0F, (new Item.Properties())));
	public static final RegistryObject<Item> STONE_AXE = REGISTRY.register("stone_axe", () -> new AxeItem(Tiers.STONE, 5.0F, -3.0F, (new Item.Properties())));
	public static final RegistryObject<Item> IRON_AXE = REGISTRY.register("iron_axe", () -> new AxeItem(Tiers.IRON, 5.0F, -3.0F, (new Item.Properties())));
}