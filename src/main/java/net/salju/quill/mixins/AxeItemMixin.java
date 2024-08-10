package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.tags.BlockTags;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap;

@Mixin(AxeItem.class)
public class AxeItemMixin extends DiggerItem {
	public AxeItemMixin(Tier t, float f1, float f2, Item.Properties p) {
		super(f1, f2, t, BlockTags.MINEABLE_WITH_AXE, p);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		if (slot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			if (this.asItem() == Items.IRON_AXE) {
				builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 7.0, AttributeModifier.Operation.ADDITION));
				builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.0, AttributeModifier.Operation.ADDITION));
				return builder.build();
			} else if (this.asItem() == Items.STONE_AXE) {
				builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 6.0, AttributeModifier.Operation.ADDITION));
				builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.0, AttributeModifier.Operation.ADDITION));
				return builder.build();
			} else if (this.asItem() == Items.GOLDEN_AXE) {
				builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 5.0, AttributeModifier.Operation.ADDITION));
				builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.0, AttributeModifier.Operation.ADDITION));
				return builder.build();
			} else if (this.asItem() == Items.WOODEN_AXE) {
				builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 5.0, AttributeModifier.Operation.ADDITION));
				builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.0, AttributeModifier.Operation.ADDITION));
				return builder.build();
			}
		}
		return super.getDefaultAttributeModifiers(slot);
	}
}