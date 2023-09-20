package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.core.BlockPos;

@Mixin(DiggerItem.class)
public class DiggerItemMixin extends TieredItem implements Vanishable {
	public DiggerItemMixin(Tier t, Item.Properties p) {
		super(t, p);
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player player) {
		return !player.isCreative();
	}

	@Override
	public boolean hurtEnemy(ItemStack axe, LivingEntity target, LivingEntity attacker) {
		axe.hurtAndBreak(1, attacker, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}
}