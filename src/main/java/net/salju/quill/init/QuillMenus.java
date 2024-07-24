package net.salju.quill.init;

import net.salju.quill.gui.FletcherGuiMenu;
import net.salju.quill.QuillMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraft.world.inventory.MenuType;

public class QuillMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, QuillMod.MODID);
	public static final RegistryObject<MenuType<FletcherGuiMenu>> FLETCHER = REGISTRY.register("fletcher_gui", () -> IForgeMenuType.create(FletcherGuiMenu::new));
}