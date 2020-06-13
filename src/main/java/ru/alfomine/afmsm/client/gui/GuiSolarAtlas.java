package ru.alfomine.afmsm.client.gui;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import ru.alfomine.afmsm.planet.Planet;

import java.util.List;

public class GuiSolarAtlas extends CustomGui {
	
	static int lWidth = 366;
	static int lHeight = 275;
	List<Planet> planets;
	int spaceSize;
	int prevScale = -1;
	
	public GuiSolarAtlas(List<Planet> planets, int spaceSize) {
		this.planets = planets;
		this.spaceSize = spaceSize;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		ScaledResolution res = new ScaledResolution(mc);
		
		int lX = center(lWidth, res.getScaledWidth());
		int lY = center(lHeight, res.getScaledHeight());
		
		// Отрисовка контейнера
		
		mc.renderEngine.bindTexture(new ResourceLocation("afmsm", "textures/gui_solaratlas.png"));
		drawTexturedModalRect512(lX, lY, lWidth, lHeight, 0, 0, lWidth, lHeight);
		
		// Отрисовка заголовка
		
		drawString(fontRenderer, "SolarAtlas", lX + 4, lY + 8, 0xFFFFFF);
		
		// Отрисовка имён планет в списке (еще не закончено)
		
		for (int i = 0; i < planets.size(); i++) {
			drawString(fontRenderer, planets.get(i).name, lX + 259, (i * fontRenderer.FONT_HEIGHT + 3) + lY + 24, 0xFFFFFF);
		}
		
		/*
		Пример отрисовки самих планет:
		drawModalRectWithCustomSizedTexture(0, 0, 0, 0, width, height, width, height);
		 */
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		ScaledResolution res = new ScaledResolution(mc);
		
		/*
		Грязный хак, уменьшает размер интерфейса если контейнер не помещается в окне.
		 */
		
		if (lHeight > res.getScaledHeight()) {
			prevScale = mc.gameSettings.guiScale;
			mc.gameSettings.guiScale = 1;
		} else if (prevScale != -1) {
			mc.gameSettings.guiScale = prevScale;
		}
		
		/*
		Т.к. после изменения интерфейса меняется разрешение, то и тут нужно его поменять.
		 */
		
		res = new ScaledResolution(mc);
		height = res.getScaledHeight();
		width = res.getScaledWidth();
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		
		if (prevScale != -1) {
			mc.gameSettings.guiScale = prevScale;
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
