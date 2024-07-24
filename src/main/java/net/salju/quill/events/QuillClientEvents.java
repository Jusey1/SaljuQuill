package net.salju.quill.events;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class QuillClientEvents {
	@SubscribeEvent
	public void onRenderFog(ViewportEvent.RenderFog event) {
		event.setNearPlaneDistance(35.0F);
		event.setFarPlaneDistance(35.0F);
		event.setCanceled(true);
	}
}