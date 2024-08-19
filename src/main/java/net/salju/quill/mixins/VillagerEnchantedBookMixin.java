package net.salju.quill.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.quill.init.QuillVillagers;
import net.salju.quill.init.QuillEnchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.core.registries.BuiltInRegistries;
import java.util.stream.Collectors;
import java.util.List;
import com.google.common.collect.Lists;

@Mixin(targets = {"net.minecraft.world.entity.npc.VillagerTrades$EnchantBookForEmeralds"})
public class VillagerEnchantedBookMixin {
	@Inject(method = "getOffer", at = @At(value = "INVOKE"), cancellable = true)
	public void newOffer(Entity entity, RandomSource rng, CallbackInfoReturnable<MerchantOffer> ci) {
		List<Enchantment> van = BuiltInRegistries.ENCHANTMENT.stream().filter(Enchantment::isTradeable).collect(Collectors.toList());
		Enchantment ench = van.get(rng.nextInt(van.size()));
		int e = Mth.nextInt(rng, 0, 2);
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
		ci.setReturnValue(new MerchantOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), book, 12, 5 * i, 0.2F));
	}

	private List<Enchantment> getPlains() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.FISHING_LUCK);
		magic.add(Enchantments.FISHING_SPEED);
		magic.add(Enchantments.PUNCH_ARROWS);
		return magic;
	}

	private List<Enchantment> getDesert() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.THORNS);
		magic.add(QuillEnchantments.AUTO_SMELT.get());
		magic.add(QuillEnchantments.SPIKES.get());
		return magic;
	}

	private List<Enchantment> getSavanna() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.KNOCKBACK);
		magic.add(Enchantments.SWEEPING_EDGE);
		magic.add(Enchantments.MOB_LOOTING);
		return magic;
	}

	private List<Enchantment> getTaiga() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.FIRE_ASPECT);
		magic.add(Enchantments.FLAMING_ARROWS);
		magic.add(Enchantments.BLAST_PROTECTION);
		return magic;
	}

	private List<Enchantment> getSnow() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.SILK_TOUCH);
		magic.add(Enchantments.FROST_WALKER);
		magic.add(Enchantments.BLOCK_FORTUNE);
		return magic;
	}

	private List<Enchantment> getJungle() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.PROJECTILE_PROTECTION);
		magic.add(Enchantments.INFINITY_ARROWS);
		magic.add(Enchantments.FALL_PROTECTION);
		return magic;
	}

	private List<Enchantment> getSwamp() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.DEPTH_STRIDER);
		magic.add(Enchantments.RESPIRATION);
		magic.add(Enchantments.AQUA_AFFINITY);
		return magic;
	}

	private List<Enchantment> getOcean() {
		List<Enchantment> magic = Lists.newArrayList();
		magic.add(Enchantments.CHANNELING);
		magic.add(Enchantments.RIPTIDE);
		magic.add(Enchantments.LOYALTY);
		return magic;
	}
}