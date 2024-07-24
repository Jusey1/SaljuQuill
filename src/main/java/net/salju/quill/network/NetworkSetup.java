package net.salju.quill.network;

import net.salju.quill.QuillMod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetworkSetup {
	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		QuillMod.addNetworkMessage(Fireworks.class, Fireworks::buffer, Fireworks::reader, Fireworks::handler);
	}
}