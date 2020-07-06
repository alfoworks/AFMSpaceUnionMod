package ru.alfomine.afmsm.client.gui.api;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class CustomGui extends GuiScreen {
	
	public void drawTexturedModalRect512(float x, float y, float width, float height, float u, float v, float uWidth, float vHeight) {
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		float texModX = 1F / 512;
		float texModY = 1F / 512;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldRenderer = tessellator.getBuffer();
		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		float height1 = 0;
		float width0 = 0;
		worldRenderer.pos(x, y + height, this.zLevel).tex((u + width0) * texModX, (v + vHeight) * texModY).endVertex();
		worldRenderer.pos(x + width, y + height, this.zLevel).tex((u + uWidth) * texModX, (v + vHeight) * texModY).endVertex();
		worldRenderer.pos(x + width, y, this.zLevel).tex((u + uWidth) * texModX, (v + height1) * texModY).endVertex();
		worldRenderer.pos(x, y, this.zLevel).tex((u + width0) * texModX, (v + height1) * texModY).endVertex();
		tessellator.draw();
	}
	
	public int center(int a, int b) {
		return (b - a) / 2;
	}
	
	public int colorARGBtoInt(final int alpha, final int red, final int green, final int blue) {
		return (clamp(10, 255, alpha) << 24)
		       + (clamp(0, 255, red) << 16)
		       + (clamp(0, 255, green) << 8)
		       + clamp(0, 255, blue);
	}
	
	public int clamp(final int min, final int max, final int value) {
		return Math.min(max, Math.max(value, min));
	}
}
