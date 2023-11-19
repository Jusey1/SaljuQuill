package net.salju.quill;

import org.slf4j.Logger;
import net.salju.quill.init.*;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.Map;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import com.mojang.logging.LogUtils;

@Mod("quill")
public class QuillMod {
	public static final String MODID = "quill";

	public QuillMod() {
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		QuillBlocks.REGISTRY.register(bus);
		QuillItems.REGISTRY.register(bus);
		QuillEnchantments.REGISTRY.register(bus);
		QuillModSounds.REGISTRY.register(bus);
		QuillMod.debugVillagerLog();
		QuillVillagers.REGISTRY.register(bus);
		AxeModItems.REGISTRY.register(bus);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, QuillConfig.CONFIG, "quill-common.toml");
	}

	public static void debugVillagerLog() {
		Logger log = LogUtils.getLogger();
		Map<VillagerProfession, Int2ObjectMap<VillagerTrades.ItemListing[]>> map = VillagerTrades.TRADES;
		log.info(Integer.toString(map.size()));
	}
}