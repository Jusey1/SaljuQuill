package net.salju.quill.init;

import net.salju.quill.enchantment.*;
import net.salju.quill.QuillMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;

public class QuillEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, QuillMod.MODID);
	public static final RegistryObject<Enchantment> AUTO_SMELT = REGISTRY.register("auto_smelt", () -> new QuillEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, 1, false));
	public static final RegistryObject<Enchantment> SHARPSHOOTER = REGISTRY.register("sharpshooter", () -> new QuillEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.CROSSBOW, 5, false));
	public static final RegistryObject<Enchantment> MAGNETIC = REGISTRY.register("magnetic", () -> new QuillEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_CHEST, 2, false));
	public static final RegistryObject<Enchantment> SPIKES = REGISTRY.register("spikes", () -> new ShieldEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.WEARABLE, 3, false));
	public static final RegistryObject<Enchantment> BLAZE = REGISTRY.register("blazing", () -> new ShieldEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEARABLE, 2, false));
	public static final RegistryObject<Enchantment> DEFLECT = REGISTRY.register("deflecting", () -> new ShieldEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEARABLE, 5, false));
	public static final RegistryObject<Enchantment> ZOMBIE = REGISTRY.register("curse_of_zombie", () -> new ShieldEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEARABLE, 1, true));
}