package ru.alfomine.afmsm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import ru.alfomine.afmsm.MenuPlanet;

import java.io.IOException;
import java.util.List;

public class GuiPlanetSelection extends CustomGui {
	public static boolean active = false;
	
	List<MenuPlanet> planets;
	
	public GuiPlanetSelection(List<MenuPlanet> planets) {
		this.planets = planets;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		active = true;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for (int i = 0; i < planets.size(); i++) {
			/*
			MenuPlanet planet = planets.get(i);
			
			int y = (i + 1) * 15;
			
			drawString(fontRenderer, String.valueOf(a), 1, 1, 0xFF0000);
			drawString(fontRenderer, planet.name, 5, y, 0xFFFFFF);
			mc.renderEngine.bindTexture(planet.iconResource);
			drawModalRectWithCustomSizedTexture(60, y, 0, 0, 50, 50, 50, 50);
			*/
			
			ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
			
			int lWidth = 260;
			int lHeight = 512;
			
			int lX = res.getScaledWidth() - lWidth;

			mc.renderEngine.bindTexture(new ResourceLocation("afmsm", "textures/gui_planetselection.png"));
			drawTexturedModalRect512(lX, 0, lWidth, lHeight, 0, 0, lWidth, lHeight);
			
			int titleX = lX + 15;
			
			drawString(fontRenderer, "Выберите планету", titleX, 5, 0xFFFFFF);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		
		active = false;
	}
}
