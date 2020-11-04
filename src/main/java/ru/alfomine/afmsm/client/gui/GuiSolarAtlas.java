package ru.alfomine.afmsm.client.gui;

import cr0s.warpdrive.data.CelestialObject;
import cr0s.warpdrive.data.CelestialObjectManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.input.Mouse;
import ru.alfomine.afmsm.client.gui.api.CustomGui;
import ru.alfomine.afmsm.planet.Planet;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
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

	float zoom = 1.0f;
	int offsetX = 0;
	int offsetY = 0;
	String hoveringOver = null;
	boolean grab = false;
	boolean pressed = false;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		ScaledResolution res = new ScaledResolution(mc);
		
		int lX = center(lWidth, res.getScaledWidth());
		int lY = center(lHeight, res.getScaledHeight());

		// Отрисовка фона

		mc.renderEngine.bindTexture(new ResourceLocation("afmsm", "textures/gui_solaratlas_bg.png"));
		drawTexturedModalRect512(lX, lY+22, lWidth-114, lHeight-25, -offsetX/zoom/5f, -offsetY/zoom/5f, lWidth/zoom, lHeight/zoom);

		// Отрисовка планет

		for (Planet planet : planets) {
			mc.renderEngine.bindTexture(planet.iconResource);
			CelestialObject c = CelestialObjectManager.get(false, planet.warpId);
			AxisAlignedBB area = c.getAreaInParent();

			// Overthrow over borders of the display area
			int overthrowX = 0;
			int overthrowXNegative = 0;
			int overthrowY = 0;
			int overthrowYNegative = 0;

			final int x = lX+259/2+((int) (area.minX/10000*zoom))+Math.round(offsetX);
			final int y = lY+lHeight/2+((int) (area.minZ/10000*zoom))+Math.round(offsetY);
			final int size = (int) Math.round(Math.abs((area.maxX - area.minX))/10000*zoom);


			// Overthrow X calculation
			if (x > lX+252-size) {
				overthrowX = x-(lX+252-size);
				// If we're overthrowing over size of the planet we're getting into negative numbers we don't want
				if (overthrowXNegative > size)
					overthrowXNegative = size;
			}

			if (x < lX) {
				overthrowXNegative = lX-x;
				if (overthrowX > size)
					overthrowXNegative = size;
			}

			// Overthrow Y calculation
			if (y > lY+269-size) {
				overthrowY = y-(lY+269-size);
				if (overthrowY > size)
					overthrowY = size;
			}

			if (y < lY + 22) {
				overthrowYNegative = lY+22-y;
				if (overthrowYNegative > size)
					overthrowYNegative = size;
			}

			int realX = (overthrowXNegative != 0) ? x+overthrowXNegative : x;
			int realY = (overthrowYNegative != 0) ? y+overthrowYNegative : y;

			drawModalRectWithCustomSizedTexture(realX, realY, overthrowXNegative, overthrowYNegative, size-overthrowX-overthrowXNegative, size-overthrowY-overthrowYNegative, size, size);

			// Debug coordinates on planets
			// drawString(fontRenderer, String.format("%s %s", realX+overthrowXNegative, mouseX), realX, realY, 0xFFFFFF);
			// drawString(fontRenderer, String.format("%s %s", realY-overthrowYNegative, mouseY), realX, realY+fontRenderer.FONT_HEIGHT+3, 0xFFFFFF);

			// Mouse over planet check
			if (	(mouseX > 303 && mouseX < 548) && (mouseY > 144 && mouseY < 387) &&
					(mouseX > realX-overthrowXNegative && mouseX < realX-overthrowXNegative+size) &&
					(mouseY > realY-overthrowYNegative && mouseY < realY-overthrowYNegative+size)) {
				hoveringOver = planet.warpId;
				drawHoveringText((pressed) ? "Coordinates copied to clipboard!" : planet.name.toUpperCase(), mouseX, mouseY);
			} else if (planet.warpId.equalsIgnoreCase(hoveringOver)) {
				hoveringOver = null;
				pressed = false;
			}
		}

		// Отрисовка контейнера

		mc.renderEngine.bindTexture(new ResourceLocation("afmsm", "textures/gui_solaratlas.png"));
		drawTexturedModalRect512(lX, lY, lWidth, lHeight, 0, 0, lWidth, lHeight);

		// Отрисовка заголовка

		assert mc.currentScreen != null;
		// 109/1920 = 0.05677 - Distance on the fullscreen by width to the center of SolarAtlas display area
		drawString(fontRenderer, String.format("SolarAtlas Zoom x%.1f", zoom), lX + 4, lY + 8, 0xFFFFFF);

		// Отрисовка имён планет в списке

		for (int i = 0; i < planets.size(); i++) {
			drawString(fontRenderer, planets.get(i).name, lX + 259, (i * fontRenderer.FONT_HEIGHT + 3) + lY + 24, 0xFFFFFF);
		}

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
				StringSelection coordinates = new StringSelection(String.format("%s %s", (int) planet.getAreaInParent().minX, (int) planet.getAreaInParent().minZ));
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(coordinates, null);
			}

			if (!grab) {
				grab = true;
				Mouse.getDX();
				Mouse.getDY();
			}
			offsetX += Mouse.getDX()*1.5;
			offsetY -= Mouse.getDY()*1.5;
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
