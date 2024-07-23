package net.salju.quill.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.DyeableArmorItem;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class QuillColors {
	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, layer) -> (layer > 0) ? -1 : ((DyeableArmorItem) stack.getItem()).getColor(stack), new ItemLike[]{
						(ItemLike) QuillItems.IRON_HELMET.get(), (ItemLike) QuillItems.DIAMOND_HELMET.get(),
						(ItemLike) QuillItems.NETHERITE_HELMET.get(), (ItemLike) QuillItems.GOLDEN_HELMET.get()});
	}
}