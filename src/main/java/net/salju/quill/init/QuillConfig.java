package net.salju.quill.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class QuillConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec CONFIG;

	public static final ForgeConfigSpec.BooleanValue CREEPER;
	public static final ForgeConfigSpec.BooleanValue TRADES;
	public static final ForgeConfigSpec.BooleanValue OCEAN;
	public static final ForgeConfigSpec.BooleanValue RIDER;
	public static final ForgeConfigSpec.BooleanValue FARMER;
	public static final ForgeConfigSpec.BooleanValue PICKMAN;
	public static final ForgeConfigSpec.BooleanValue SWORD;
	public static final ForgeConfigSpec.BooleanValue USER;
	public static final ForgeConfigSpec.BooleanValue CROSSBOW;
	public static final ForgeConfigSpec.BooleanValue CAMPFIRE;
	public static final ForgeConfigSpec.BooleanValue DEATH;
	public static final ForgeConfigSpec.BooleanValue DND;
	public static final ForgeConfigSpec.BooleanValue SHIELD;
	
	static {
		BUILDER.push("Features");
		CREEPER = BUILDER.comment("Should creepers be prideful?").define("Prideful Creepers", true);
		TRADES = BUILDER.comment("Should updated villager trades be enabled?").define("Villager Trades", true);
		OCEAN = BUILDER.comment("Should ocean villagers be enabled?").define("Pirate Villagers", true);
		RIDER = BUILDER.comment("Should this mod's entity rider changes be implemented?").define("Riders", true);
		FARMER = BUILDER.comment("Should hoes have the ability to harvest crops?").define("Crops Harvest", true);
		PICKMAN = BUILDER.comment("Should pickaxes have better critical hits?").define("Pickman", true);
		SWORD = BUILDER.comment("Should tools have the parry ability?").define("Parrying", true);
		USER = BUILDER.comment("Should basic items get a cooldown during use after being hit by an enemy?").define("User", true);
		CROSSBOW = BUILDER.comment("Should crossbows have infinity?").define("Ceaseless Crossbows", true);
		CAMPFIRE = BUILDER.comment("Should campfires disable enemy spawning if lit?").define("Campfire Protection", true);
		DEATH = BUILDER.comment("Should hotbar & equipment be kept on death?").define("Death Protection", true);
		DND = BUILDER.comment("Should players be unable to break most blocks in survival mode?").define("Caves & Kobolds", false);
		SHIELD = BUILDER.comment("Should the shield be prioritized when using it with an axe while right-clicking on copper & logs?").define("I-Wanna-Block", true);
		BUILDER.pop();
		CONFIG = BUILDER.build();
	}
}