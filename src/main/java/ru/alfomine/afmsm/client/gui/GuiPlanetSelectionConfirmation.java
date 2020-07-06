package ru.alfomine.afmsm.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import ru.alfomine.afmsm.client.gui.api.CustomButton;
import ru.alfomine.afmsm.client.gui.api.CustomGui;

import java.util.List;

public class GuiPlanetSelectionConfirmation extends CustomGui {
	
	private int lWidth = 212;
	private int lHeight = 130;
	
	private int centerX;
	private int centerY;
	
	private String title = I18n.format("afmsm.text.gui_planetselection_confirm.title");
	
	private long timeStarted;
	
	private List<String> warnList;
	
	public GuiPlanetSelectionConfirmation() {
		this.timeStarted = System.currentTimeMillis();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawDefaultBackground();
		
		mc.renderEngine.bindTexture(GuiPlanetSelection.mainGuiLoc);
		drawTexturedModalRect512(centerX, centerY, lWidth, lHeight, 262, 68, lWidth, lHeight);
		
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		
		int labelX = center(fontRenderer.getStringWidth(title), res.getScaledWidth());
		int labelY = center(lHeight, res.getScaledHeight()) + 10;
		
		long time = System.currentTimeMillis() - timeStarted;
		int alpha = (int) Math.round(128 + 127 * Math.sin(((double) time % 2001) / (double) 2000 * 2 * Math.PI));
		
		drawString(fontRenderer, title, labelX, labelY, colorARGBtoInt(alpha, 255, 255, 255));
		
		for (int i = 0; i < warnList.size(); i++) {
			String toDraw = warnList.get(i);
			
			drawString(fontRenderer, toDraw, center(fontRenderer.getStringWidth(toDraw), res.getScaledWidth()), i * fontRenderer.FONT_HEIGHT + labelY + 15, 0xFFFFFF);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		
		centerX = center(lWidth, res.getScaledWidth());
		centerY = center(lHeight, res.getScaledHeight());
		
		int btnWidth = 85;
		int btnY = center(20, res.getScaledHeight()) + 43;
		
		buttonList.add(new CustomButton(0, center(btnWidth, res.getScaledWidth()) - btnWidth / 2 - 5, btnY, btnWidth, 20, I18n.format("afmsm.text.gui_planetselection_confirm.button.no")));
		buttonList.add(new CustomButton(1, center(btnWidth, res.getScaledWidth()) + btnWidth / 2 + 5, btnY, btnWidth, 20, I18n.format("afmsm.text.gui_planetselection_confirm.button.yes")));
		
		warnList = this.fontRenderer.listFormattedStringToWidth(I18n.format("afmsm.text.gui_planetselection_confirm.warn", "\n"), lWidth - 15);
	}
}
