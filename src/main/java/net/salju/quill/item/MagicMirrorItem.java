package net.salju.quill.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.ChatFormatting;
import java.util.List;

public class MagicMirrorItem extends Item implements Vanishable {
	public MagicMirrorItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, world, list, flag);
		CompoundTag tag = stack.getOrCreateTag();
		list.add(Component.literal(Integer.toString(tag.getInt("X")) + ", " + Integer.toString(tag.getInt("Y")) + ", " + Integer.toString(tag.getInt("Z"))).withStyle(ChatFormatting.DARK_PURPLE));
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 64;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BLOCK;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity target, int e, boolean check) {
		super.inventoryTick(stack, world, target, e, check);
		CompoundTag tag = stack.getOrCreateTag();
		if (target instanceof ServerPlayer ply && world instanceof ServerLevel lvl) {
			if (ply.getRespawnPosition() == null) {
				if (tag.getInt("X") != (int) world.getLevelData().getXSpawn()) {
					tag.putInt("X", (int) world.getLevelData().getXSpawn());
				}
				if (tag.getInt("Y") != (int) world.getLevelData().getYSpawn()) {
					tag.putInt("Y", (int) world.getLevelData().getYSpawn());
				}
				if (tag.getInt("Z") != (int) world.getLevelData().getZSpawn()) {
					tag.putInt("Z", (int) world.getLevelData().getZSpawn());
				}
			} else {
				if (tag.getInt("X") != (int) ply.getRespawnPosition().getX()) {
					tag.putInt("X", (int) ply.getRespawnPosition().getX());
				}
				if (tag.getInt("Y") != (int) ply.getRespawnPosition().getY()) {
					tag.putInt("Y", (int) ply.getRespawnPosition().getY());
				}
				if (tag.getInt("Z") != (int) ply.getRespawnPosition().getZ()) {
					tag.putInt("Z", (int) ply.getRespawnPosition().getZ());
				}
			}
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(player.getItemInHand(hand));
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity target, int i) {
		super.releaseUsing(stack, world, target, i);
		if (i <= 10 && target instanceof ServerPlayer ply && world instanceof ServerLevel lvl) {
			ResourceKey<Level> dim = ply.getRespawnDimension();
			if (dim == null) {
				dim = Level.OVERWORLD;
			}
			double x;
			double y;
			double z;
			ServerLevel loc = ply.server.getLevel(dim);
			if (ply.getRespawnPosition() == null) {
				x = world.getLevelData().getXSpawn() + 0.5;
				y = world.getLevelData().getYSpawn();
				z = world.getLevelData().getZSpawn() + 0.5;
			} else {
				x = ply.getRespawnPosition().getX() + 0.5;
				y = ply.getRespawnPosition().getY() + 0.7;
				z = ply.getRespawnPosition().getZ() + 0.5;
			}
			if (loc != null) {
				lvl.playSound(null, ply.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
				lvl.sendParticles(ParticleTypes.PORTAL, ply.getX(), ply.getY(), ply.getZ(), 12, 0.5, 0.5, 0.5, 0.65);
				ply.teleportTo(loc, x, y, z, ply.getYRot(), ply.getXRot());
				loc.playSound(null, BlockPos.containing(x, y, z), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
				loc.sendParticles(ParticleTypes.PORTAL, x, y, z, 12, 0.5, 0.5, 0.5, 0.65);
			}
			ply.getCooldowns().addCooldown(stack.getItem(), 200);
		}
	}

	@Override
	public void onUseTick(Level world, LivingEntity target, ItemStack stack, int i) {
		super.onUseTick(world, target, stack, i);
		if (world instanceof ServerLevel lvl) {
			if (i <= 1) {
				this.releaseUsing(stack, world, target, i);
			} else {
				lvl.sendParticles(ParticleTypes.PORTAL, target.getX(), target.getY(), target.getZ(), 1, 0.5, 0.5, 0.5, 0.65);
			}
		}
	}
}