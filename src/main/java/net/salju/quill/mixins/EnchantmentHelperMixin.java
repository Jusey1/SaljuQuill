package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.ListTag;
import java.util.Map;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
	@Inject(method = "getEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/entity/LivingEntity;)I", at = @At("HEAD"), cancellable = true)
	private static void getEnchantmentLvl(Enchantment ench, LivingEntity target, CallbackInfoReturnable<Integer> ci) {
		if (target instanceof Horse pony && !pony.getArmor().isEmpty()) {
			int i = EnchantmentHelper.getItemEnchantmentLevel(ench, pony.getArmor());
			ci.setReturnValue(i);
		}
	}

	@Inject(method = "setEnchantments", at = @At("HEAD"), cancellable = true)
	private static void setEnchantments(Map<Enchantment, Integer> map, ItemStack stack, CallbackInfo ci) {
		if (!stack.is(Items.ENCHANTED_BOOK)) {
			ci.cancel();
			int i = 0;
			ListTag list = new ListTag();
			for (Map.Entry<Enchantment, Integer> e : map.entrySet()) {
				Enchantment ench = e.getKey();
				if (ench != null && (i < 3 || ench.isCurse())) {
					list.add(EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(ench), e.getValue()));
					if (!ench.isCurse()) {
						i++;
					}
				}
			}
			if (list.isEmpty()) {
				stack.removeTagKey("Enchantments");
			} else {
				stack.addTagElement("Enchantments", list);
			}
		}
	}
}