package net.salju.quill;

import net.salju.quill.init.*;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.MinecraftForge;

@Mod("quill")
public class QuillMod {
	public static final String MODID = "quill";

	public QuillMod() {
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		QuillBlocks.REGISTRY.register(bus);
		QuillItems.REGISTRY.register(bus);
		QuillEnchantments.REGISTRY.register(bus);
		AxeModItems.REGISTRY.register(bus);
	}
}