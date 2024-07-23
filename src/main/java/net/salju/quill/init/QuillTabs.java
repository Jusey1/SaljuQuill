package net.salju.quill.init;

import net.salju.quill.QuillMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

public class QuillTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, QuillMod.MODID);
	public static final RegistryObject<CreativeModeTab> QUILL = REGISTRY.register("quill",
			() -> CreativeModeTab.builder().title(Component.translatable("itemGroup.quill")).icon(() -> new ItemStack(QuillItems.SALJU.get())).displayItems((parameters, tabData) -> {
				tabData.accept(QuillItems.CANNON.get());
				tabData.accept(QuillItems.CANNONBALL.get());
				tabData.accept(QuillItems.COPPER_CANNONBALL.get());
				tabData.accept(QuillItems.IRON_HELMET.get());
				tabData.accept(QuillItems.GOLDEN_HELMET.get());
				tabData.accept(QuillItems.DIAMOND_HELMET.get());
				tabData.accept(QuillItems.NETHERITE_HELMET.get());
				tabData.accept(QuillItems.MAGIC_MIRROR.get());
				tabData.accept(QuillItems.BUNDLE.get());
				tabData.accept(QuillItems.TRADER_BLOCK.get());
				tabData.accept(QuillItems.NETHERITE_HORSE_ARMOR.get());
			}).build());
}