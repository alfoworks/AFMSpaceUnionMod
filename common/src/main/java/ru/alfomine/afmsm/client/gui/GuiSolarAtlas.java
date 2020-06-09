package ru.alfomine.afmsm.client.gui;

import cr0s.warpdrive.data.CelestialObject;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class GuiSolarAtlas extends CustomGui {
	
	CelestialObject[] objects;
	
	public GuiSolarAtlas(CelestialObject[] objects) {
		this.objects = objects;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		ScaledResolution res = new ScaledResolution(mc);
		
		int lWidth = 366;
		int lHeight = 275;
		
		int lX = center(lWidth, res.getScaledWidth());
		int lY = center(lHeight, res.getScaledHeight());
		
		mc.renderEngine.bindTexture(new ResourceLocation("afmsm", "textures/gui_solaratlas.png"));
		drawTexturedModalRect512(lX, lY, lWidth, lHeight, 0, 0, lWidth, lHeight);
		
		drawString(fontRenderer, "SolarAtlas", lX + 4, lY + 8, 0xFFFFFF);
		
		for (int i = 0; i < objects.length; i++) {
			drawString(fontRenderer, objects[i].getDisplayName(), lX + 259, (i * fontRenderer.FONT_HEIGHT + 3) + lY + 24, 0xFFFFFF);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
}
