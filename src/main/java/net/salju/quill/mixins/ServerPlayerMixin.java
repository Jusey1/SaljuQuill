package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.salju.quill.init.QuillConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.server.level.ServerPlayer;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
	@Inject(method = "restoreFrom", at = @At(value = "HEAD"))
	private void dontDropGear(ServerPlayer dead, boolean alive, CallbackInfo ci) {
		if (QuillConfig.DEATH.get()) {
			ServerPlayer ply = (ServerPlayer) (Object) this;
			for (int i = 0; i < dead.getInventory().items.size(); i++) {
				if (i < 9) {
					ply.getInventory().items.set(i, dead.getInventory().items.get(i));
				}
			}
			ply.setItemSlot(EquipmentSlot.HEAD, dead.getItemBySlot(EquipmentSlot.HEAD));
			ply.setItemSlot(EquipmentSlot.CHEST, dead.getItemBySlot(EquipmentSlot.CHEST));
			ply.setItemSlot(EquipmentSlot.LEGS, dead.getItemBySlot(EquipmentSlot.LEGS));
			ply.setItemSlot(EquipmentSlot.FEET, dead.getItemBySlot(EquipmentSlot.FEET));
			ply.setItemSlot(EquipmentSlot.OFFHAND, dead.getItemBySlot(EquipmentSlot.OFFHAND));
		}
	}
}