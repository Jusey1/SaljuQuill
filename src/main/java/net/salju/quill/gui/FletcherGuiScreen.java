package net.salju.quill.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;

public class FletcherGuiScreen extends AbstractContainerScreen<FletcherGuiMenu> {
	public FletcherGuiScreen(FletcherGuiMenu menu, Inventory inv, Component text) {
		super(menu, inv, text);
		this.imageWidth = 176;
		this.imageHeight = 170;
		this.inventoryLabelY = 76;
	}

	@Override
	public void render(GuiGraphics ms, int x, int y, float ticks) {
		super.render(ms, x, y, ticks);
	}

	@Override
	protected void renderBg(GuiGraphics ms, float ticks, int x, int y) {
		ms.blit(new ResourceLocation("quill:textures/gui/fletcher.png"), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
	}
}