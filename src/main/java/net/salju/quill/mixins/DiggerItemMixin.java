package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillTags;
import net.salju.quill.init.QuillConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.tags.TagKey;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap;

@Mixin(DiggerItem.class)
public abstract class DiggerItemMixin extends TieredItem implements Vanishable {
	public DiggerItemMixin(float f1, float f2, Tier t, TagKey<Block> tag, Item.Properties p) {
		super(t, p);
	}

	@Inject(method = "getDefaultAttributeModifiers", at = @At("RETURN"), cancellable = true)
	public void isEnchantable(EquipmentSlot slot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> ci) {
		if (QuillConfig.AXEMAN.get() && slot == EquipmentSlot.MAINHAND) {
			DiggerItem thys = (DiggerItem) (Object) this;
			if (new ItemStack(thys).is(QuillTags.AXES)) {
				ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
				builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (thys.asItem() == Items.IRON_AXE ? 7.0 : thys.asItem() == Items.STONE_AXE ? 6.0 : 5.0), AttributeModifier.Operation.ADDITION));
				builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.0, AttributeModifier.Operation.ADDITION));
				ci.setReturnValue(builder.build());
			}
		}
	}
}