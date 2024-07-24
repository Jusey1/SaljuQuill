package net.salju.quill.init;

import net.salju.quill.enchantment.*;
import net.salju.quill.QuillMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.item.enchantment.Enchantment;

public class QuillEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, QuillMod.MODID);
	public static final RegistryObject<Enchantment> AUTO_SMELT = REGISTRY.register("auto_smelt", () -> new AutoSmelt());
	public static final RegistryObject<Enchantment> SHARPSHOOTER = REGISTRY.register("sharpshooter", () -> new Sharpshooter());
	public static final RegistryObject<Enchantment> SPIKES = REGISTRY.register("spikes", () -> new Spikes());
	public static final RegistryObject<Enchantment> BLAZE = REGISTRY.register("blazing", () -> new Blazing());
}