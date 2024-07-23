package net.salju.quill.init;

import net.salju.quill.client.renderer.*;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class QuillRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(QuillMobs.CANNON.get(), CannonRenderer::new);
		event.registerEntityRenderer(QuillMobs.CANNONBALL.get(), ThrownItemRenderer::new);
	}
}