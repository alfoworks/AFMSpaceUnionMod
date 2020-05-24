package ru.alfomine.afmsm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.alfomine.afmsm.MenuPlanet;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GuiPlanetSelection extends CustomGui {
	
	public static boolean active = false;
	
	private static ResourceLocation mainGuiLoc = new ResourceLocation("afmsm", "textures/gui_planetselection.png");
	List<MenuPlanet> planets;
	GuiListElement[] elements;
	GuiListElement selected;
	private int lWidth = 262;
	private int lHeight = 512;
	
	public GuiPlanetSelection(List<MenuPlanet> planets) {
		this.planets = planets;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		
		int lX = res.getScaledWidth() - lWidth;
		
		mc.renderEngine.bindTexture(mainGuiLoc);
		drawTexturedModalRect512(lX, 0, lWidth, lHeight, 0, 0, lWidth, lHeight);
		
		int titleX = lX + 15;
		drawString(fontRenderer, "Выберите планету", titleX, 5, 0xFFFFFF);
		
		for (GuiListElement element : elements) {
			mc.renderEngine.bindTexture(mainGuiLoc);
			
			Color color = element.planet.difficulty.color;
			
			if (selected != null && selected == element) { // selected
				color = element.planet.difficulty.colorPressed;
			} else if (element.isHovered(mouseX, mouseY)) { // hover
				color = element.planet.difficulty.colorHovered;
			}
			
			GL11.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
			drawTexturedModalRect512(element.x, element.y, 188, 53, 262, 0, 188, 53);
			
			if (selected != null && selected == element) {
				drawTexturedModalRect512(element.x + 166, element.y + 10, 16, 15, 262, 53, 16, 15);
			}
			
			GlStateManager.color(255, 255, 255);
			
			int iconRes = 34;
			
			mc.renderEngine.bindTexture(element.planet.iconResource);
			drawModalRectWithCustomSizedTexture(element.x + 10, element.y + 10, 0, 0, iconRes, iconRes, iconRes, iconRes);
			
			drawString(fontRenderer, element.planet.name, element.x + 12 + center(fontRenderer.getStringWidth(element.planet.name), 188), element.y + 8, 0xFFFFFF);
			drawString(fontRenderer, String.format("Сложность: %s", element.planet.difficulty.name), element.x + 52, element.y + 20, 0xFFFFFF);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		for (GuiListElement element : elements) {
			if (element.isHovered(mouseX, mouseY)) {
				if (mouseButton == 1 && selected == element) {
					selected = null;
				} else if (mouseButton == 0) {
					selected = element;
				}
				
				break;
			}
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		active = true;
		
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		
		elements = planets.stream().map(planet -> new GuiListElement(res.getScaledWidth() - lWidth + 70, planets.indexOf(planet) * 70 + 30, planet)).toArray(GuiListElement[]::new);
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		
		active = false;
	}
}
