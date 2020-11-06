package ru.alfomine.afmsm.client.gui;

import cr0s.warpdrive.data.CelestialObject;
import cr0s.warpdrive.data.CelestialObjectManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.input.Mouse;
import ru.alfomine.afmsm.client.gui.api.CustomGui;
import ru.alfomine.afmsm.planet.Planet;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

public class GuiSolarAtlas extends CustomGui {
	
	static int lWidth = 366;
	static int lHeight = 275;
	List<Planet> planets;
	List<GuiSolarAtlasPlanet> guiPlanets = new ArrayList<>();
	int spaceSize;
	int prevScale = -1;

	static float zoom;
	static int offsetX;
	static int offsetY;
	private GuiSolarAtlasScroll scroll;

	public GuiSolarAtlas(List<Planet> planets, int spaceSize) {
		this.planets = planets;
		this.spaceSize = spaceSize;

		zoom = 1.0f;
		offsetX = 0;
		offsetY = 0;
	}

	String hoveringOver = null;
	boolean grab = false;
	boolean pressed = false;
	static final int scale = 10000;
	static int lX;
	static int lY;


	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		ScaledResolution res = new ScaledResolution(mc);

		lX = center(lWidth, res.getScaledWidth());
		lY = center(lHeight, res.getScaledHeight());

		// Отрисовка фона
		mc.renderEngine.bindTexture(new ResourceLocation("afmsm", "textures/gui_solaratlas_bg.png"));
		drawTexturedModalRect512(lX, lY+22, lWidth-114, lHeight-25, -offsetX/zoom/5f, -offsetY/zoom/5f, lWidth/zoom, lHeight/zoom);

		// Отрисовка планет
		for (GuiSolarAtlasPlanet planet : guiPlanets) {
			mc.renderEngine.bindTexture(planet.planet.iconResource);
			planet.calculate();
			//Debug string
			//drawString(fontRenderer, "" + planet.sizeX + " " + planet.sizeY + " " + planet.realSize, lX+40, lY + 40, 0xFFFFFF);
			drawModalRectWithCustomSizedTexture(planet.x, planet.y, planet.overthrowXNegative, planet.overthrowYNegative, planet.sizeX, planet.sizeY, planet.realSize, planet.realSize);
		}

		// Отрисовка контейнера
		mc.renderEngine.bindTexture(new ResourceLocation("afmsm", "textures/gui_solaratlas.png"));
		drawTexturedModalRect512(lX, lY, lWidth, lHeight, 0, 0, lWidth, lHeight);

		// Отрисовка заголовка
		assert mc.currentScreen != null;
		// 109/1920 = 0.05677 - Distance on the fullscreen by width to the center of SolarAtlas display area
		drawString(fontRenderer, String.format("SolarAtlas Zoom x%.1f", zoom), lX + 4, lY + 8, 0xFFFFFF);
		int uniOffset = Math.round(14*scroll.sliderValue-14);
		// Отрисовка имён планет в списке
		for (float i = uniOffset; i < planets.size(); i++) {
			if (i > Math.round(14*scroll.sliderValue)) {
				continue;
			}
			drawString(fontRenderer, planets.get(Math.round(i)).name, lX + 259, Math.round((i * fontRenderer.FONT_HEIGHT + 3) + lY + 24 - uniOffset * fontRenderer.FONT_HEIGHT), 0xFFFFFF);
		}

		// Mouse over planet actions
		for (GuiSolarAtlasPlanet planet : guiPlanets) {
			if (planet.isPointOverlapping(mouseX, mouseY)) {
				hoveringOver = planet.celestial.id;
				drawHoveringText((pressed) ? "Coordinates copied to clipboard!" : planet.planet.name.toUpperCase(), mouseX, mouseY);

			} else if (planet.celestial.id.equalsIgnoreCase(hoveringOver)) {
				hoveringOver = null;
				pressed = false;
			}
		}

		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void updateScreen() {
		// Zoom modification
		int temp = Mouse.getDWheel();
		if (temp != 0 && Mouse.isInsideWindow()) {
			ScaledResolution res = new ScaledResolution(mc);

			// Zoom offset
			if (temp > 0) {
				if (zoom != 64f) {
					offsetX *= 2;
					offsetY *= 2;
				}

				assert mc.currentScreen != null;
				offsetX += getRelativeX()/2;
				offsetY -= getRelativeY()/2;
			}

			zoom = temp < 0 ?
					(zoom > 0.25) ? zoom / 2f : zoom
					:
					(zoom < 64) ? zoom * 2 : zoom;


			if (temp < 0) {
				offsetX /= 2;
				offsetY /= 2;
			}
		}

		// Grab check
		if (Mouse.isButtonDown(0)) {
			if (hoveringOver != null) {
				pressed = true;
				CelestialObject planet = CelestialObjectManager.get(false, hoveringOver);
				setClipboardString(String.format("%s %s",
						(int) (planet.getAreaInParent().minX+planet.getAreaInParent().maxX)/2,
						(int) (planet.getAreaInParent().minZ+planet.getAreaInParent().maxZ)/2));
			}

			if (!((GuiSolarAtlasScroll) (buttonList.stream().filter(b -> b.id == "AFMAtlasSlider".hashCode()).findFirst().get())).dragging) {
				if (!grab) {
					grab = true;
					Mouse.getDX();
					Mouse.getDY();
				}
				offsetX += Mouse.getDX()*1.5;
				offsetY -= Mouse.getDY()*1.5;
			}
		} else {
			grab = false;
		}
	}

	private double getRelativeX() {
		return mc.displayWidth/2f-mc.displayWidth*0.05677-Mouse.getX();
	}

	private double getRelativeY() {
		return mc.displayHeight/2f-Mouse.getY();
	}

	@Override
	public void initGui() {
		super.initGui();

		this.scroll = new GuiSolarAtlasScroll("AFMAtlasSlider".hashCode(), lX + 338, lY+24, 20, 130, 1.0f, 1+14f/planets.size(), 1.0f, "a");
		addButton(scroll);

		ScaledResolution res = new ScaledResolution(mc);

		// Initialization of planets
		for (Planet p : planets) {
			//Debug case
			//if (p.name.equalsIgnoreCase("jupiter"))
			GuiSolarAtlasPlanet planet = new GuiSolarAtlasPlanet(p);
			planet.calculate();
			guiPlanets.add(planet);
		}

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
		scroll.x = center(lWidth, res.getScaledWidth()) + 338;
		scroll.y = center(lHeight, res.getScaledHeight()) + 24;
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
