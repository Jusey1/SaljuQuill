package net.salju.quill.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class QuillConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec CONFIG;

	public static final ForgeConfigSpec.BooleanValue USER;
	public static final ForgeConfigSpec.BooleanValue SWORD;
	public static final ForgeConfigSpec.BooleanValue AXER;
	public static final ForgeConfigSpec.BooleanValue SHIELD;
	public static final ForgeConfigSpec.BooleanValue FARMER;
	public static final ForgeConfigSpec.IntValue ARROWS;

	public static final ForgeConfigSpec.BooleanValue DEATH;
	public static final ForgeConfigSpec.BooleanValue CREEPER;
	public static final ForgeConfigSpec.BooleanValue TRADES;
	public static final ForgeConfigSpec.BooleanValue OCEAN;
	public static final ForgeConfigSpec.BooleanValue TAXI;
	public static final ForgeConfigSpec.BooleanValue KICK;
	public static final ForgeConfigSpec.BooleanValue CAMPFIRE;

	public static final ForgeConfigSpec.BooleanValue ENCHS;
	public static final ForgeConfigSpec.IntValue MAXENCH;
	public static final ForgeConfigSpec.IntValue CURSES;
	public static final ForgeConfigSpec.BooleanValue UNBREAKING;
	public static final ForgeConfigSpec.BooleanValue REPAIR;
	public static final ForgeConfigSpec.BooleanValue ANBOOK;
	public static final ForgeConfigSpec.IntValue MAXANBOOKCOST;
	public static final ForgeConfigSpec.BooleanValue RENAME;
	public static final ForgeConfigSpec.BooleanValue GRIND;
	
	static {
		BUILDER.push("Block & Item Configuration");
		USER = BUILDER.comment("Should basic items get a cooldown during use after being hit by an enemy?").define("Basic Cooldown", true);
		SWORD = BUILDER.comment("Should swords have the parry ability?").define("Sword Parrying", true);
		AXER = BUILDER.comment("Should vanilla axes be rebalanced?").define("Rebalanced Axes", true);
		SHIELD = BUILDER.comment("Should the shield be prioritized when using it with an axe while right-clicking on copper & logs?").define("I Wanna Block", true);
		FARMER = BUILDER.comment("Should hoes have the ability to harvest crops?").define("Hoe Harvesting", true);
		ARROWS = BUILDER.comment("How many arrows can be crafted with the Fletching Table?").defineInRange("Arrow Count", 8, 4, 64);
		BUILDER.pop();
		BUILDER.push("Player & Entity Configuration");
		DEATH = BUILDER.comment("Should hotbar & equipment be kept on death?").define("Death Protection", true);
		CREEPER = BUILDER.comment("Should creepers be prideful?").define("Prideful Creepers", true);
		TRADES = BUILDER.comment("Should updated villager trades be enabled?").define("Villager Trade Rebalance", true);
		OCEAN = BUILDER.comment("Should ocean villagers be enabled?").define("Pirate Villagers", true);
		TAXI = BUILDER.comment("Should villagers join a player's camel or boat by simply being nearby?").define("Taxi Camel", true);
		KICK = BUILDER.comment("Should the player be able to crouch right-click entities off of what they are riding?").define("Kick Them Off", true);
		CAMPFIRE = BUILDER.comment("Should campfires disable enemy spawning if lit?").define("Campfire Protection", true);
		BUILDER.pop();
		BUILDER.push("Enchantment Configuration");
		ENCHS = BUILDER.comment("Should this mod's enchantment rework be in effect?").define("Enchantments Rework", true);
		MAXENCH = BUILDER.comment("How many enchantments should be applicable to an item?").defineInRange("Max Enchantments", 3, 0, 25);
		CURSES = BUILDER.comment("What is the chance of a curse being applied while randomly enchanting?").defineInRange("Cursed Enchanting", 15, 0, 100);
		UNBREAKING = BUILDER.comment("Should enchanted equipment be unbreakable?").define("Unbreaking Protection", true);
		REPAIR = BUILDER.comment("Should repairing equipment be better & only costing 1 level?").define("Better Anvil Repair", true);
		ANBOOK = BUILDER.comment("Should this mod adjust combining enchanted items on an anvil?").define("Better Anvil Enchanting", true);
		MAXANBOOKCOST = BUILDER.comment("Maximum cost on an anvil when combining?").defineInRange("Max Anvil Cost", 15, 0, 45);
		RENAME = BUILDER.comment("Should renaming always only cost 1 level?").define("Better Anvil Renaming", true);
		GRIND = BUILDER.comment("Should the grindstone be usable to disenchant items onto an enchanted book?").define("Grindstone Disenchant", true);
		BUILDER.pop();
		CONFIG = BUILDER.build();
	}
}