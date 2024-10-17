package net.salju.quill;

import org.lwjgl.system.windows.MSG;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;

@Mod("quill")
public class QuillMod {
	public static final String MODID = "quill";

	public QuillMod() {
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		QuillEffects.REGISTRY.register(bus);
		QuillPotions.REGISTRY.register(bus);
		QuillFrogs.REGISTRY.register(bus);
		QuillBlocks.REGISTRY.register(bus);
		QuillItems.REGISTRY.register(bus);
		QuillEnchantments.REGISTRY.register(bus);
		QuillModSounds.REGISTRY.register(bus);
		QuillMobs.REGISTRY.register(bus);
		QuillMenus.REGISTRY.register(bus);
		QuillTabs.REGISTRY.register(bus);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, QuillConfig.CONFIG, "quill-common.toml");
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