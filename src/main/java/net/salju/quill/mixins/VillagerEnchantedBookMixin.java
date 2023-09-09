package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillVillagers;

import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.stream.Collectors;
import java.util.List;
import com.google.common.collect.Lists;

@Mixin(targets = {"net.minecraft.world.entity.npc.VillagerTrades$EnchantBookForEmeralds"})
public class VillagerEnchantedBookMixin implements VillagerTrades.ItemListing {
	@Override
	public MerchantOffer getOffer(Entity entity, RandomSource rng) {
		List<Enchantment> van = BuiltInRegistries.ENCHANTMENT.stream().filter(Enchantment::isTradeable).collect(Collectors.toList());
		Enchantment ench = van.get(rng.nextInt(van.size()));
		int e = Mth.nextInt(rng, 0, 3);
		if (entity instanceof Villager target) {
			VillagerType type = target.getVillagerData().getType();
			if (type == VillagerType.PLAINS) {
				List<Enchantment> list = getPlains();
				ench = list.get(e);
			} else if (type == VillagerType.DESERT) {
				List<Enchantment> list = getDesert();
				ench = list.get(e);
			} else if (type == VillagerType.SAVANNA) {
				List<Enchantment> list = getSavanna();
				ench = list.get(e);
			} else if (type == VillagerType.TAIGA) {
				List<Enchantment> list = getTaiga();
				ench = list.get(e);
			} else if (type == VillagerType.SNOW) {
				List<Enchantment> list = getSnow();
				ench = list.get(e);
			} else if (type == VillagerType.JUNGLE) {
				List<Enchantment> list = getJungle();
				ench = list.get(e);
			} else if (type == VillagerType.SWAMP) {
				List<Enchantment> list = getSwamp();
				ench = list.get(e);
			} else if (type == QuillVillagers.OCEAN.get()) {
				List<Enchantment> list = getOcean();
				ench = list.get(e);
			}
		}
		int i = Mth.nextInt(rng, ench.getMinLevel(), ench.getMaxLevel());
		ItemStack book = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(ench, i));
		int j = 2 + (i * Mth.nextInt(rng, 4, 8));
		if (j > 64) {
			j = 64;
		}
		return new MerchantOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), book, 12, (5 * i), 0.2F);
	}

	private List<Enchantment> getPlains() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.UNBREAKING);
		magic.add(Enchantments.BLOCK_FORTUNE);
		magic.add(Enchantments.FISHING_SPEED);
		magic.add(Enchantments.FISHING_LUCK);
		return magic;
	}

	private List<Enchantment> getDesert() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.THORNS);
		magic.add(Enchantments.FIRE_ASPECT);
		magic.add(Enchantments.FIRE_PROTECTION);
		magic.add(Enchantments.FLAMING_ARROWS);
		return magic;
	}

	private List<Enchantment> getSavanna() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.MULTISHOT);
		magic.add(Enchantments.QUICK_CHARGE);
		magic.add(Enchantments.PROJECTILE_PROTECTION);
		magic.add(Enchantments.PIERCING);
		return magic;
	}

	private List<Enchantment> getTaiga() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.KNOCKBACK);
		magic.add(Enchantments.SMITE);
		magic.add(Enchantments.BLAST_PROTECTION);
		magic.add(Enchantments.MOB_LOOTING);
		return magic;
	}

	private List<Enchantment> getSnow() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.FALL_PROTECTION);
		magic.add(Enchantments.FROST_WALKER);
		magic.add(Enchantments.PUNCH_ARROWS);
		magic.add(Enchantments.BANE_OF_ARTHROPODS);
		return magic;
	}

	private List<Enchantment> getJungle() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.VANISHING_CURSE);
		magic.add(Enchantments.INFINITY_ARROWS);
		magic.add(Enchantments.POWER_ARROWS);
		magic.add(Enchantments.SWEEPING_EDGE);
		return magic;
	}

	private List<Enchantment> getSwamp() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.DEPTH_STRIDER);
		magic.add(Enchantments.RESPIRATION);
		magic.add(Enchantments.AQUA_AFFINITY);
		magic.add(Enchantments.BINDING_CURSE);
		return magic;
	}

	private List<Enchantment> getOcean() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.CHANNELING);
		magic.add(Enchantments.RIPTIDE);
		magic.add(Enchantments.LOYALTY);
		magic.add(Enchantments.IMPALING);
		return magic;
	}
}