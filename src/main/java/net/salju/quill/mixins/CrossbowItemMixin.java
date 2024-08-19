package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillEnchantments;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin extends ProjectileWeaponItem implements Vanishable {
	public CrossbowItemMixin(Item.Properties props) {
		super(props);
	}

	@Inject(method = "getArrow", at = @At("RETURN"), cancellable = true)
	private static void onGetArrow(Level level, LivingEntity shooter, ItemStack crossbow, ItemStack ammo, CallbackInfoReturnable<AbstractArrow> ci) {
		AbstractArrow arrow = ci.getReturnValue();
		int sharpshooter = crossbow.getEnchantmentLevel(QuillEnchantments.SHARPSHOOTER.get());
		arrow.getPersistentData().putDouble("Sharpshooter", sharpshooter);
		if (crossbow.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0 && ammo.is(Items.ARROW)) {
			arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment ench) {
		if (QuillConfig.ENCHS.get() && ench == Enchantments.INFINITY_ARROWS) {
			return true;
		}
		return super.canApplyAtEnchantingTable(stack, ench);
	}
}