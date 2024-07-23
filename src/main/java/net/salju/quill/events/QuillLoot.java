package net.salju.quill.events;

import net.salju.quill.init.QuillItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class QuillLoot {
	@SubscribeEvent
	public void onLoot(LootTableLoadEvent event) {
		if (event.getName().equals(BuiltInLootTables.ABANDONED_MINESHAFT) || event.getName().equals(BuiltInLootTables.SIMPLE_DUNGEON)) {
			LootTable table = event.getTable();
			LootPool unique = QuillManager.setLoot("cannon_loot", -2.0F, 1.0F, QuillManager.getLoot(QuillItems.CANNON.get()));
			table.addPool(unique);
			event.setTable(table);
		} else if (event.getName().equals(BuiltInLootTables.STRONGHOLD_LIBRARY)) {
			LootTable table = event.getTable();
			LootPool unique = QuillManager.setLoot("special_loot", 0.25F, 1.0F, QuillManager.getLoot(QuillItems.MAGIC_MIRROR.get()), QuillManager.getLoot(QuillItems.BUNDLE.get()));
			table.addPool(unique);
			event.setTable(table);
		}
	}
}