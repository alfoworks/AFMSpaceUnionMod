package ru.alfomine.afmsm.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import org.lwjgl.opengl.GL11;
import ru.alfomine.afmsm.planet.PlanetData;

import java.awt.*;

public class GuiListPlanetSelectionEntry implements IGuiListEntry {
	
	PlanetData planet;
	GuiPlanetSelection gui;
	
	public GuiListPlanetSelectionEntry(PlanetData planet, GuiPlanetSelection gui) {
		this.planet = planet;
		this.gui = gui;
	}
	
	@Override
	public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
	
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
		// isSelected значит hovered, не selected
		
		Minecraft mc = Minecraft.getMinecraft();
		
		mc.renderEngine.bindTexture(GuiPlanetSelection.mainGuiLoc);
		
		Color color = planet.difficulty.color;
		
		if (gui.list.selectedIndex == slotIndex) { // select
			color = planet.difficulty.colorPressed;
		} else if (isSelected) { // hover
			color = planet.difficulty.colorHovered;
		}
		
		GL11.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
		gui.drawTexturedModalRect512(x, y, 188, 53, 262, 0, 188, 53);
		
		if (gui.list.selectedIndex == slotIndex) {
			gui.drawTexturedModalRect512(x + 166, y + 10, 16, 15, 262, 53, 16, 15);
		}
		
		GL11.glColor3f(1f, 1f, 1f);
		
		int iconRes = 34;
		
		mc.renderEngine.bindTexture(planet.iconResource);
		Gui.drawModalRectWithCustomSizedTexture(x + 10, y + 10, 0, 0, iconRes, iconRes, iconRes, iconRes);
		
		gui.drawString(mc.fontRenderer, planet.name, x + 12 + gui.center(mc.fontRenderer.getStringWidth(planet.name), 188), y + 8, 0xFFFFFF);
		gui.drawString(mc.fontRenderer, String.format("Сложность: %s", planet.difficulty.name), x + 52, y + 20, 0xFFFFFF);
		gui.drawString(mc.fontRenderer, String.format("Размер: %sx%s", planet.size, planet.size), x + 52, y + 20 + mc.fontRenderer.FONT_HEIGHT, 0xFFFFFF);
	}
	
	@Override
	public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
		this.gui.list.selectPlanet(slotIndex);
		
		return true;
	}
	
	@Override
	public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
	
	}
}
