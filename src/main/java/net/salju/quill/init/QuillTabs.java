package net.salju.quill.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.resources.ResourceKey;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class QuillTabs {
	@SubscribeEvent
	public static void buildTabs(BuildCreativeModeTabContentsEvent event) {
		MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> targets = event.getEntries();
		ResourceKey<CreativeModeTab> tab = event.getTabKey();
		if (tab.equals(CreativeModeTabs.COMBAT)) {
			targets.putAfter(new ItemStack(Items.DIAMOND_HORSE_ARMOR), new ItemStack(QuillItems.NETHERITE_HORSE_ARMOR.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		}
		if (tab.equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
			targets.putAfter(new ItemStack(Items.BELL), new ItemStack(QuillItems.TRADER_BLOCK.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		}
	}
}