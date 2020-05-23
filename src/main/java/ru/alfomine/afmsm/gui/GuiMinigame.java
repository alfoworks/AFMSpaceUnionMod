package ru.alfomine.afmsm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector2d;
import java.io.IOException;

public class GuiMinigame extends CustomGui {
	
	boolean isActive = false;
	
	// game
	
	Vector2d dotPos = new Vector2d(0, 0);
	Direction direction = Direction.RIGHT;
	
	@Override
	public void initGui() {
		super.initGui();
		
		this.buttonList.add(new GuiButton(0, 0, 0, 80, 20, "Start minigame"));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if (isActive) {
			ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
			
			int backgroundX = center(512, resolution.getScaledWidth());
			int backgroundY = center(256, resolution.getScaledHeight());
			
			mc.renderEngine.bindTexture(new ResourceLocation("afmsm", "textures/gui_minigame.png"));
			drawTexturedModalRect512(backgroundX, backgroundY, 512, 256, 0, 0, 512, 256);
			
			drawTexturedModalRect512((float) dotPos.x + backgroundX, (float) dotPos.y + backgroundY, 19, 19, 0, 256, 19, 19);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			isActive = true;
			this.selectedButton.enabled = false;
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (!isActive) {
			return;
		}
		
		switch (direction) {
			case UP:
				dotPos.add(new Vector2d(0, 1));
			case DOWN:
				dotPos.negate(new Vector2d(0, 1));
			case LEFT:
				dotPos.add(new Vector2d(1, 0));
			case RIGHT:
				dotPos.negate(new Vector2d(1, 0));
		}
	}
	
	// =========== //
	
	private int center(int a, int b) {
		return (b - a) / 2;
	}
	
	enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
}
