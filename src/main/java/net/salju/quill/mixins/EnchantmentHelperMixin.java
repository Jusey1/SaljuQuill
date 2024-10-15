package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillTags;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.registries.BuiltInRegistries;
import java.util.Map;
import java.util.List;
import com.google.common.collect.Lists;

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
		if (QuillConfig.ENCHS.get()) {
			ci.cancel();
			int i = 0;
			int m = QuillConfig.MAXENCH.get() * (stack.is(QuillTags.DOUBENCHS) ? 2 : 1);
			ListTag list = new ListTag();
			for (Enchantment ench : map.keySet()) {
				if (i < m || ench.isCurse()) {
					list.add(EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(ench), map.get(ench)));
					if (stack.is(Items.ENCHANTED_BOOK)) {
						EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(ench, map.get(ench)));
					}
					if (!ench.isCurse()) {
						i++;
					}
				}
			}
			if (list.isEmpty()) {
				stack.removeTagKey("Enchantments");
			} else if (!stack.is(Items.ENCHANTED_BOOK)) {
				stack.addTagElement("Enchantments", list);
			}
		}
	}

	@Inject(method = "selectEnchantment", at = @At("RETURN"), cancellable = true)
	private static void getEnchantments(RandomSource rng, ItemStack stack, int i, boolean check, CallbackInfoReturnable<List<EnchantmentInstance>> ci) {
		if (rng.nextInt(100) < QuillConfig.CURSES.get() && !stack.is(Items.BOOK)) {
			List<EnchantmentInstance> list = ci.getReturnValue();
			List<Enchantment> curses = Lists.newArrayList();
			for (Enchantment ench : BuiltInRegistries.ENCHANTMENT) {
				if (ench.isCurse() && ench.canEnchant(stack)) {
					curses.add(ench);
				}
			}
			list.add(new EnchantmentInstance(curses.get(rng.nextInt(curses.size())), 1));
			ci.setReturnValue(list);
		}
	}
}