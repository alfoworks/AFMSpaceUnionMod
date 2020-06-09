package ru.alfomine.afmsm.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import ru.alfomine.afmsm.client.PlanetData;

import java.util.ArrayList;
import java.util.List;

public class GuiListPlanetSelection extends GuiListExtended {
	
	int x;
	
	List<GuiListPlanetSelectionEntry> entries = new ArrayList<>();
	int selectedIndex;
	
	public GuiListPlanetSelection(Minecraft mcIn, int x, int y, int widthIn, int heightIn, int slotHeightIn, List<PlanetData> planets, GuiPlanetSelection gui) {
		super(mcIn, widthIn, heightIn, y, y + heightIn, slotHeightIn);
		
		this.x = x;
		
		planets.forEach(planet -> entries.add(new GuiListPlanetSelectionEntry(planet, gui)));
		
		this.hasListHeader = false;
		this.showSelectionBox = false;
		this.centerListVertically = false;
	}
	
	@Override
	protected void drawBackground() {
		super.drawBackground();
	}
	
	@SuppressWarnings("NullableProblems")
	@Override
	public GuiListPlanetSelectionEntry getListEntry(int index) {
		return entries.get(index);
	}
	
	@Override
	public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
		if (this.visible) {
			this.mouseX = mouseXIn;
			this.mouseY = mouseYIn;
			this.drawBackground();
			int i = this.getScrollBarX();
			int j = i + 6;
			this.bindAmountScrolled();
			GlStateManager.disableLighting();
			GlStateManager.disableFog();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			
			int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
			int l = this.top + 4 - (int) this.amountScrolled;
			
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			glScissor(0, width, 0, height);
			this.drawSelectionBox(k, l, mouseXIn, mouseYIn, partialTicks);
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
			
			GlStateManager.disableDepth();
			
			/*
			this.overlayBackground(0, this.top, 255, 255);
			this.overlayBackground(this.bottom, this.height, 255, 255);
			 */
			
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
			GlStateManager.disableAlpha();
			GlStateManager.shadeModel(7425);
			GlStateManager.disableTexture2D();
			
			int j1 = this.getMaxScroll();
			
			if (j1 > 0) {
				int k1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
				k1 = MathHelper.clamp(k1, 32, this.bottom - this.top - 8);
				int l1 = (int) this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;
				
				if (l1 < this.top) {
					l1 = this.top;
				}
				
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				bufferbuilder.pos(i, this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(j, this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(j, this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(i, this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
				tessellator.draw();
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				bufferbuilder.pos(i, l1 + k1, 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
				bufferbuilder.pos(j, l1 + k1, 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
				bufferbuilder.pos(j, l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
				bufferbuilder.pos(i, l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
				tessellator.draw();
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				bufferbuilder.pos(i, l1 + k1 - 1, 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
				bufferbuilder.pos(j - 1, l1 + k1 - 1, 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
				bufferbuilder.pos(j - 1, l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
				bufferbuilder.pos(i, l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
				tessellator.draw();
			}
			
			GlStateManager.enableTexture2D();
			GlStateManager.shadeModel(7424);
			GlStateManager.enableAlpha();
			GlStateManager.disableBlend();
		}
	}
	
	public void selectPlanet(int slutIndex) {
		selectedIndex = slutIndex;
	}
	
	@Override
	protected boolean isSelected(int slotIndex) {
		return slotIndex == selectedIndex;
	}
	
	@Override
	protected int getSize() {
		return entries.size();
	}
	
	private void glScissor(float x1, float x2, float y1, float y2) {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		x1 *= sr.getScaleFactor();
		y1 *= sr.getScaleFactor();
		x2 *= sr.getScaleFactor();
		y2 *= sr.getScaleFactor();
		float temp;
		if (y1 > y2) {
			temp = y2;
			y2 = y1;
			y1 = temp;
		}
		GL11.glScissor((int) x1, (int) (Display.getHeight() - y2), (int) (x2 - x1), (int) (y2 - y1));
	}
}
