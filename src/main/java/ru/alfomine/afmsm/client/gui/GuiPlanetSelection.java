package ru.alfomine.afmsm.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import ru.alfomine.afmsm.client.gui.api.CustomButton;
import ru.alfomine.afmsm.client.gui.api.CustomGui;
import ru.alfomine.afmsm.planet.Planet;

import java.io.IOException;
import java.util.List;

public class GuiPlanetSelection extends CustomGui {

	public static boolean active = false;

	static ResourceLocation mainGuiLoc = new ResourceLocation("afmsm", "textures/gui_planetselection.png");
	public GuiSlotPlanetSelection list;
	List<Planet> planets;
	private int lWidth = 262;
	private int lHeight = 512;
	public int selected = -1;

	public GuiPlanetSelection(List<Planet> planets) {
		this.planets = planets;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		
		int lX = res.getScaledWidth() - lWidth;
		
		mc.renderEngine.bindTexture(mainGuiLoc);
		drawTexturedModalRect512(lX, 0, lWidth, lHeight, 0, 0, lWidth, lHeight);
		
		int titleX = lX + 15;
		drawString(fontRenderer, I18n.format("afmsm.text.gui_planetselection.title"), titleX, 5, 0xFFFFFF);
		
		list.drawScreen(mouseX, mouseY, partialTicks);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		
		if (button.id == 0) {
			Minecraft.getMinecraft().displayGuiScreen(null);
			Minecraft.getMinecraft().displayGuiScreen(new GuiPlanetSelectionConfirmation());
		}
	}
	
	/*
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		
		int lX = res.getScaledWidth() - lWidth;
		
		mc.renderEngine.bindTexture(mainGuiLoc);
		drawTexturedModalRect512(lX, 0, lWidth, lHeight, 0, 0, lWidth, lHeight);
		
		int titleX = lX + 15;
		drawString(fontRenderer, I18n.format("afmsm.text.gui_planetselection.title"), titleX, 5, 0xFFFFFF);
		
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
			
			GlStateManager.color(1f, 1f, 1f);
			
			int iconRes = 34;
			
			mc.renderEngine.bindTexture(element.planet.iconResource);
			drawModalRectWithCustomSizedTexture(element.x + 10, element.y + 10, 0, 0, iconRes, iconRes, iconRes, iconRes);
			
			drawString(fontRenderer, element.planet.name, element.x + 12 + center(fontRenderer.getStringWidth(element.planet.name), 188), element.y + 8, 0xFFFFFF);
			drawString(fontRenderer, String.format("Сложность: %s", element.planet.difficulty.name), element.x + 52, element.y + 20, 0xFFFFFF);
			drawString(fontRenderer, String.format("Размер: %sx%s", element.planet.sizeX, element.planet.sizeZ), element.x + 52, element.y + 20 + fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	 */
	
	@Override
	public void initGui() {
		super.initGui();

		active = true;

		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

		GuiButton butt = new CustomButton(0, res.getScaledWidth() - lWidth + 7, 187, 50, 20, I18n.format("afmsm.text.gui_planetselection.button_select"));
		butt.enabled = false;

		buttonList.add(butt);

		int lX = res.getScaledWidth() - lWidth + 70;

		list = new GuiSlotPlanetSelection(planets, this, lX, 0, lWidth, res.getScaledHeight(), 53, width, height);
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();

		int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

		this.list.handleMouseInput(mouseX, mouseY);
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		
		active = false;
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
}
