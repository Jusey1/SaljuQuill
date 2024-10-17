package net.salju.quill.init;

import net.salju.quill.QuillMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.effect.MobEffectInstance;

//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class QuillPotions {
	public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, QuillMod.MODID);
	public static final RegistryObject<Potion> FROGGO = REGISTRY.register("froggo", () -> new Potion(new MobEffectInstance(QuillEffects.FROGGO.get(), 0, 0, false, true)));
	
	/*@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		//
	}*/
}