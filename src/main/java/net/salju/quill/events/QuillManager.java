package net.salju.quill.events;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.core.SectionPos;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;

public class QuillManager {
	public static LootPoolSingletonContainer.Builder<?> getLoot(Item gem) {
		LootPoolSingletonContainer.Builder<?> stack = LootItem.lootTableItem(gem);
		stack.setWeight(7);
		return stack;
	}

	public static LootPool setLoot(String name, float low, float max, @Nullable LootPoolEntryContainer.Builder<?>... targets) {
		LootPool.Builder newbie = LootPool.lootPool();
		newbie.name(name);
		newbie.setRolls(UniformGenerator.between(low, max));
		for (LootPoolEntryContainer.Builder<?> target : targets) {
			if (target != null) {
				newbie.add(target);
			}
		}
		LootPool done = newbie.build();
		return done;
	}

	public static boolean getCampfire(ServerLevelAccessor lvl, BlockPos pos, int radius) {
		int minX = SectionPos.blockToSectionCoord(pos.getX() - radius);
		int minZ = SectionPos.blockToSectionCoord(pos.getZ() - radius);
		int maxX = SectionPos.blockToSectionCoord(pos.getX() + radius);
		int maxZ = SectionPos.blockToSectionCoord(pos.getZ() + radius);
		boolean check = false;
		for (int chunkX = minX; chunkX <= maxX; chunkX++) {
			for (int chunkZ = minZ; chunkZ <= maxZ; chunkZ++) {
				LevelChunk chunk = lvl.getChunkSource().getChunk(chunkX, chunkZ, false);
				if (chunk != null) {
					for (BlockPos target : chunk.getBlockEntitiesPos()) {
						BlockState state = chunk.getBlockState(target);
						if (state.getBlock() == Blocks.CAMPFIRE || state.getBlock() == Blocks.SOUL_CAMPFIRE) {
							if (state.getValue(CampfireBlock.LIT)) {
								if (pos.closerThan(target, radius)) {
									check = true;
									break;
								}
							}
						}
					}
				}
			}
		}
		return check;
	}

	public static boolean isBlocked(DamageSource source, LivingEntity target) {
		if (!source.is(DamageTypeTags.BYPASSES_SHIELD)) {
			Vec3 vec32 = source.getSourcePosition();
			if (vec32 != null) {
				Vec3 vec3 = target.getViewVector(1.0F);
				Vec3 vec31 = vec32.vectorTo(target.position()).normalize();
				vec31 = new Vec3(vec31.x, 0.0D, vec31.z);
				if (vec31.dot(vec3) < 0.0D) {
					return true;
				}
			}
		}
		return false;
	}
}