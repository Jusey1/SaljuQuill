package net.salju.quill.init;

import net.salju.quill.client.model.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.ModelLayerLocation;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class QuillModels {
	public static final ModelLayerLocation ALT_ARMOR = new ModelLayerLocation(new ResourceLocation("quill", "alt_armor"), "main");
	public static final ModelLayerLocation KOBOLD_ALT_ARMOR = new ModelLayerLocation(new ResourceLocation("quill", "alt_kobold_armor"), "main");
	public static final ModelLayerLocation CANNON = new ModelLayerLocation(new ResourceLocation("quill", "cannon"), "main");

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(CANNON, CannonModel::createBodyLayer);
		event.registerLayerDefinition(ALT_ARMOR, AltArmorModel::createBodyLayer);
		event.registerLayerDefinition(KOBOLD_ALT_ARMOR, AltKoboldArmorModel::createBodyLayer);
	}
}