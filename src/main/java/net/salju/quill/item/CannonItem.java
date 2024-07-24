package net.salju.quill.item;

import net.salju.quill.init.QuillTags;
import net.salju.quill.init.QuillMobs;
import net.salju.quill.entity.Cannon;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.ChatFormatting;
import java.util.function.Consumer;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

public class CannonItem extends Item {
	public CannonItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, world, list, flag);
		CompoundTag tag = stack.getOrCreateTag();
		list.add(Component.translatable(new ItemStack(getType(tag.getInt("CannonType"))).getDescriptionId()).withStyle(ChatFormatting.DARK_PURPLE));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		CompoundTag tag = stack.getOrCreateTag();
		if (player.getOffhandItem().is(QuillTags.CANNON)) {
			tag.putInt("CannonType", getType(player.getOffhandItem()));
			return InteractionResultHolder.consume(stack);
		}
		return super.use(world, player, hand);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getClickedFace() == Direction.DOWN) {
			return InteractionResult.FAIL;
		} else {
			if (context.getLevel() instanceof ServerLevel lvl) {
				BlockPos pos = new BlockPlaceContext(context).getClickedPos();
				AABB nums = QuillMobs.CANNON.get().getDimensions().makeBoundingBox(Vec3.atBottomCenterOf(pos).x(), Vec3.atBottomCenterOf(pos).y(), Vec3.atBottomCenterOf(pos).z());
				Consumer<Cannon> consumer = EntityType.createDefaultStackConfig(lvl, context.getItemInHand(), context.getPlayer());
				Cannon target = QuillMobs.CANNON.get().create(lvl, context.getItemInHand().getTag(), consumer, pos, MobSpawnType.SPAWN_EGG, true, true);
				CompoundTag tag = context.getItemInHand().getOrCreateTag();
				if (target != null && lvl.noCollision(target, nums) && lvl.getEntities(target, nums).isEmpty()) {
					target.moveTo(target.getX(), target.getY(), target.getZ());
					float rot = (float) Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
					target.setYRot(rot);
					target.setYBodyRot(rot);
					target.setYHeadRot(rot);
					target.yRotO = rot;
					target.yBodyRotO = rot;
					target.yHeadRotO = rot;
					target.setOwner(context.getPlayer().getUUID());
					target.setCannonStack(context.getItemInHand().copy());
					target.getEntityData().set(Cannon.TYPE, tag.getInt("CannonType"));
					lvl.addFreshEntityWithPassengers(target);
					lvl.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
					target.gameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
					context.getItemInHand().shrink(1);
					context.getPlayer().swing(context.getHand(), true);
					return InteractionResult.SUCCESS;
				}
			}
			return InteractionResult.PASS;
		}
	}

	private int getType(ItemStack stack) {
		Map<Item, Integer> map = new HashMap<>();
		map.put(Items.STICK, 0);
		map.put(Items.IRON_INGOT, 1);
		map.put(Items.COPPER_INGOT, 2);
		map.put(Items.EMERALD, 3);
		return map.getOrDefault(stack.getItem(), 0);
	}

	private Item getType(int i) {
		Map<Integer, Item> map = new HashMap<>();
		map.put(0, Items.STICK);
		map.put(1, Items.IRON_INGOT);
		map.put(2, Items.COPPER_INGOT);
		map.put(3, Items.EMERALD);
		return map.getOrDefault(i, Items.STICK);
	}
}