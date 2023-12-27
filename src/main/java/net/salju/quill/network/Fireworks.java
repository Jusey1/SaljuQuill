package net.salju.quill.network;

import net.salju.quill.events.QuillClientManager;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import java.util.function.Supplier;

public class Fireworks {
	private static double getX;
	private static double getY;
	private static double getZ;

	public Fireworks(double x, double y, double z) {
		this.getX = x;
		this.getY = y;
		this.getZ = z;
	}

	public static Fireworks reader(FriendlyByteBuf buffer) {
		return new Fireworks(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
	}

	public static void buffer(Fireworks message, FriendlyByteBuf buffer) {
		buffer.writeDouble(getX);
		buffer.writeDouble(getY);
		buffer.writeDouble(getZ);
	}

	public static void handler(Fireworks message, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			Player player = QuillClientManager.getPlayer(context.get().getDirection().getReceptionSide());
			if (player != null) {
				QuillClientManager.creeperFireworks(player.level(), getX, getY, getZ);
			}
		});
		context.get().setPacketHandled(true);
	}
}