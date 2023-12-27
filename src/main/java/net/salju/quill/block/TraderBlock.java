package net.salju.quill.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;

public class TraderBlock extends Block {
	public TraderBlock(BlockBehaviour.Properties props) {
		super(props);
	}

	@Override
	public void tick(BlockState block, ServerLevel lvl, BlockPos pos, RandomSource random) {
		super.tick(block, lvl, pos, random);
		if (lvl.getServer().getWorldData().overworldData().getWanderingTraderSpawnDelay() <= 2400) {
			Player player = lvl.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 64, false);
			if (player != null) {
				BlockPos spawnpos = this.findSpawnPositionNear(lvl, pos, 12, random);
				if (spawnpos != null) {
					WanderingTrader trader = EntityType.WANDERING_TRADER.spawn(lvl, spawnpos, MobSpawnType.EVENT);
					if (trader != null) {
						lvl.getServer().getWorldData().overworldData().setWanderingTraderSpawnDelay(24000);
						for (int i = 0; i < 2; ++i) {
							TraderLlama pet = EntityType.TRADER_LLAMA.spawn(lvl, spawnpos, MobSpawnType.EVENT);
							if (pet != null) {
								pet.setLeashedTo(trader, true);
							}
						}
						trader.setDespawnDelay(18000);
						trader.setWanderTarget(pos);
						trader.restrictTo(pos, 16);
						lvl.playSound(null, pos, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 1.0F, 1.0F);
					}
				}
			}
		}
	}

	@Nullable
	private BlockPos findSpawnPositionNear(LevelReader world, BlockPos old, int range, RandomSource random) {
		BlockPos pos = null;
		for (int i = 0; i < 10; ++i) {
			int x = old.getX() + random.nextInt(range * 2) - range;
			int y = old.getY() + random.nextInt(range * 2) - range;
			int z = old.getZ() + random.nextInt(range * 2) - range;
			BlockPos test = new BlockPos(x, y, z);
			if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, world, test, EntityType.WANDERING_TRADER)) {
				pos = test;
				break;
			}
		}
		return pos;
	}
}