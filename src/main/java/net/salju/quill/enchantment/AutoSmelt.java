

import net.salju.quill.init.QuillEnchantments;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;


public class AutoSmelt extends Enchantment {
	public AutoSmelt(EquipmentSlot... slots) {
		super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.DIGGER, slots);
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		return !List.of(QuillEnchantments.AUTO_SMELT.get(), Enchantments.SILK_TOUCH).contains(ench);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return (stack.getItem() instanceof PickaxeItem);
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}
}