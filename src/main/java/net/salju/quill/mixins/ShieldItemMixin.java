package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Equipable;

@Mixin(ShieldItem.class)
public abstract class ShieldItemMixin extends Item implements Equipable {
	public ShieldItemMixin(Item.Properties props) {
		super(props);
	}

	@Override
	public int getEnchantmentValue() {
		return (QuillConfig.ENCHS.get() ? 5 : super.getEnchantmentValue());
	}
}