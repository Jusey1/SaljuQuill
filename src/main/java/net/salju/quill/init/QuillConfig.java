package net.salju.quill.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class QuillConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec CONFIG;

	public static final ForgeConfigSpec.BooleanValue ENCHS;
	public static final ForgeConfigSpec.BooleanValue CREEPER;
	public static final ForgeConfigSpec.BooleanValue TRADES;
	public static final ForgeConfigSpec.BooleanValue OCEAN;
	public static final ForgeConfigSpec.BooleanValue RIDER;
	public static final ForgeConfigSpec.BooleanValue FARMER;
	public static final ForgeConfigSpec.BooleanValue SWORD;
	public static final ForgeConfigSpec.BooleanValue USER;
	public static final ForgeConfigSpec.BooleanValue CAMPFIRE;
	public static final ForgeConfigSpec.BooleanValue DEATH;
	public static final ForgeConfigSpec.BooleanValue SHIELD;
	public static final ForgeConfigSpec.BooleanValue AXER;
	public static final ForgeConfigSpec.IntValue ARROWS;
	
	static {
		BUILDER.push("Features");
		ENCHS = BUILDER.comment("Should this mod's enchantment rework be in effect?").define("Enchantments Rework", true);
		CREEPER = BUILDER.comment("Should creepers be prideful?").define("Prideful Creepers", true);
		TRADES = BUILDER.comment("Should updated villager trades be enabled?").define("Villager Trades", true);
		OCEAN = BUILDER.comment("Should ocean villagers be enabled?").define("Pirate Villagers", true);
		RIDER = BUILDER.comment("Should this mod's entity rider changes be implemented?").define("Riders", true);
		FARMER = BUILDER.comment("Should hoes have the ability to harvest crops?").define("Crops Harvest", true);
		SWORD = BUILDER.comment("Should tools have the parry ability?").define("Parrying", true);
		USER = BUILDER.comment("Should basic items get a cooldown during use after being hit by an enemy?").define("User", true);
		CAMPFIRE = BUILDER.comment("Should campfires disable enemy spawning if lit?").define("Campfire Protection", true);
		DEATH = BUILDER.comment("Should hotbar & equipment be kept on death?").define("Death Protection", true);
		SHIELD = BUILDER.comment("Should the shield be prioritized when using it with an axe while right-clicking on copper & logs?").define("I-Wanna-Block", true);
		ARROWS = BUILDER.comment("How many arrows can be crafted with the Fletching Table?").defineInRange("Arrows", 8, 4, 64);
		AXER = BUILDER.comment("Should vanilla axes be rebalanced?").define("Axeman", true);
		BUILDER.pop();
		CONFIG = BUILDER.build();
	}
}