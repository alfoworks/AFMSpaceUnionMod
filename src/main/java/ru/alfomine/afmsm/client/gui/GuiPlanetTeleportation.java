package ru.alfomine.afmsm.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

public class GuiPlanetTeleportation extends GuiScreen {
    public GuiPlanetTeleportation() {

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mousePosX, int mousePosY, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);

        Matrix4f camMatrix = new Matrix4f();
        Matrix4f.translate(new Vector3f(0.0F, 0.0F, -9000.0F), camMatrix, camMatrix); // See EntityRenderer.java:setupOverlayRendering
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.m00 = 2.0F / width;
        viewMatrix.m11 = 2.0F / -height;
        viewMatrix.m22 = -2.0F / 9000.0F;
        viewMatrix.m30 = -1.0F;
        viewMatrix.m31 = 1.0F;
        viewMatrix.m32 = -2.0F;

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        FloatBuffer fb = BufferUtils.createFloatBuffer(16 * Float.SIZE);
        fb.rewind();
        viewMatrix.store(fb);
        fb.flip();
        GL11.glMultMatrix(fb);
        fb.clear();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        fb.rewind();
        camMatrix.store(fb);
        fb.flip();
        fb.clear();
        GL11.glMultMatrix(fb);

        this.setBlackBackground();

        GL11.glPushMatrix();
        Matrix4f mat0 = new Matrix4f();
        Matrix4f.translate(new Vector3f(width / 2.0F, height / 2, 0), mat0, mat0);
        //Matrix4f.rotate(50, new Vector3f(1, 0, 0), mat0, mat0);

        Matrix4f.translate(new Vector3f(0,0, 0), mat0, mat0);
        FloatBuffer fb2 = BufferUtils.createFloatBuffer(16);
        fb2.rewind();
        mat0.store(fb2);
        fb2.flip();
        GL11.glMultMatrix(fb2);

        // test
        GL11.glScaled(8, 8, 8);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        float scale = 10.0F;

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glScalef(scale, scale, scale);

        GL11.glRotatef(-89f, 1.0f, 0.0f, 0.0f);

        mc.renderEngine.bindTexture(new ResourceLocation("warpdrive", "textures/celestial/surface_temperate2.png"));

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(1.0f, 1.0f, -1.0f).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(-1.0f, 1.0f, -1.0f).tex(0.0D, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(-1.0f, 1.0f, 1.0f).tex(1, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(1.0f, 1.0f, 1.0f).tex(1, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(1.0f, -1.0f, 1.0f).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(-1.0f, -1.0f, 1.0f).tex(0.0D, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(-1.0f, -1.0f, -1.0f).tex(1, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(1.0f, -1.0f, -1.0f).tex(1, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(1.0f, 1.0f, 1.0f).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(-1.0f, 1.0f, 1.0f).tex(0.0D, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(-1.0f, -1.0f, 1.0f).tex(1, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(1.0f, -1.0f, 1.0f).tex(1, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(1.0f, -1.0f, -1.0f).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(-1.0f, -1.0f, -1.0f).tex(0.0D, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(-1.0f, 1.0f, -1.0f).tex(1, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(1.0f, 1.0f, -1.0f).tex(1, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(-1.0f, 1.0f, 1.0f).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(-1.0f, 1.0f, -1.0f).tex(0.0D, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(-1.0f, -1.0f, -1.0f).tex(1, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(-1.0f, -1.0f, 1.0f).tex(1, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(1.0f, 1.0f, -1.0f).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(1.0f, 1.0f, 1.0f).tex(0.0D, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(1.0f, -1.0f, 1.0f).tex(1, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(1.0f, -1.0f, -1.0f).tex(1, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();

        //

        GL11.glPopMatrix();

        GL11.glPopMatrix();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }

    public void setBlackBackground() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(0.0D, height, -90.0D).endVertex();
        worldRenderer.pos(width, height, -90.0D).endVertex();
        worldRenderer.pos(width, 0.0D, -90.0D).endVertex();
        worldRenderer.pos(0.0D, 0.0D, -90.0D).endVertex();
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}