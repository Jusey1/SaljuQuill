package net.salju.quill;

import org.slf4j.Logger;
import net.salju.quill.init.*;

import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;
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
		QuillBlockEntities.REGISTRY.register(bus);
		MinecraftItems.REGISTRY.register(bus);
		QuillItems.REGISTRY.register(bus);
		QuillEnchantments.REGISTRY.register(bus);
		QuillModSounds.REGISTRY.register(bus);
		QuillMenus.REGISTRY.register(bus);
		QuillMod.debugVillagerLog();
		QuillVillagers.REGISTRY.register(bus);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, QuillConfig.CONFIG, "quill-common.toml");
	}

	public static void debugVillagerLog() {
		Logger log = LogUtils.getLogger();
		Map<VillagerProfession, Int2ObjectMap<VillagerTrades.ItemListing[]>> map = VillagerTrades.TRADES;
		log.info(Integer.toString(map.size()));
	}

	private static final String V = "1";
	public static final SimpleChannel PACKET = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> V, V::equals, V::equals);
	private static int id = 0;

	public static <T> void addNetworkMessage(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> supply) {
		PACKET.registerMessage(id, type, encoder, decoder, supply);
		id++;
	}

	public static <MSG> void sendToClientPlayer(MSG msg, ServerPlayer ply) {
		PACKET.sendTo(msg, ply.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}
}