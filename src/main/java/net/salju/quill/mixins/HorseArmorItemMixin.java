package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillItems;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.HorseArmorItem;
import java.util.Map;
import java.util.HashMap;

@Mixin(HorseArmorItem.class)
public abstract class HorseArmorItemMixin extends Item {
	public HorseArmorItemMixin(Item.Properties p) {
		super(p);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment ench) {
		if (QuillConfig.ENCHS.get() && (ench.category == EnchantmentCategory.ARMOR_FEET || ench instanceof ProtectionEnchantment)) {
			return true;
		}
		return super.canApplyAtEnchantingTable(stack, ench);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return (QuillConfig.ENCHS.get() ? true : super.isEnchantable(stack));
	}

	@Override
	public int getEnchantmentValue() {
		return (QuillConfig.ENCHS.get() ? this.getMap().getOrDefault(this, 1) : super.getEnchantmentValue());
	}

	public Map<Item, Integer> getMap() {
		Map<Item, Integer> map = new HashMap<>();
		map.put(Items.LEATHER_HORSE_ARMOR, 5);
		map.put(Items.IRON_HORSE_ARMOR, 14);
		map.put(Items.GOLDEN_HORSE_ARMOR, 22);
		map.put(Items.DIAMOND_HORSE_ARMOR, 10);
		map.put(QuillItems.NETHERITE_HORSE_ARMOR.get(), 15);
		return map;
	}
}