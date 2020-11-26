package ru.alfomine.afmsm.client.gui;

import cr0s.warpdrive.render.RenderSpaceSky;
import micdoodle8.mods.galacticraft.api.galaxies.IChildBody;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import ru.alfomine.afmsm.client.gui.api.CustomButton;
import ru.alfomine.afmsm.client.gui.api.CustomGui;
import ru.alfomine.afmsm.space.Planet;
import ru.alfomine.afmsm.space.PlanetRenderData;
import ru.alfomine.afmsm.space.Space;
import java.io.IOException;
import java.nio.FloatBuffer;

import static ru.alfomine.afmsm.client.gui.GuiPlanetTeleportation.*;


public class GuiPlanetSelection extends CustomGui {

    public static boolean active = false;

    private ResourceLocation mainGuiLoc = new ResourceLocation("afmsm", "textures/gui_selection.png");
    public GuiSlotPlanetSelection list;
    public int selected = -1;
    Space space;
    private int lWidth = 262;
    private int lHeight = 512;

    Framebuffer GUI_FBO;

    public GuiPlanetSelection(Space space) {
        this.space = space;
    }

    /*
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//drawRect(0, 0, width, height, 0xFF000000);
		//GL11.glColor3f(1.0F, 1.0F, 1.0F);

		// =======3d stuff====== //

		// =========GUI========= //

		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		
		int lX = res.getScaledWidth() - lWidth;
		
		mc.renderEngine.bindTexture(mainGuiLoc);
		drawTexturedModalRect512(lX, 0, lWidth, lHeight, 0, 0, lWidth, lHeight);
		
		int titleX = lX + 15;
		drawString(fontRenderer, I18n.format("afmsm.text.gui_planetselection.title"), titleX, 5, 0xFFFFFF);
		
		list.drawScreen(mouseX, mouseY, partialTicks);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	*/

    @Override
    public void drawScreen(int mousePosX, int mousePosY, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);

        render3d(partialTicks);

        // GUI render

        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        int lX = res.getScaledWidth() - lWidth;

        mc.renderEngine.bindTexture(mainGuiLoc);
        drawTexturedModalRect512(lX, 0, lWidth, lHeight, 0, 0, lWidth, lHeight);

        int titleX = lX + 15;
        drawString(fontRenderer, I18n.format("afmsm.text.gui_planetselection.title"), titleX, 5, 0xFFFFFF);

        list.drawScreen(mousePosX, mousePosY, partialTicks);

        super.drawScreen(mousePosX, mousePosY, partialTicks);

        //

        GL11.glPopMatrix();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }
    
    private void render3d(float partialTicks) {
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
        //Matrix4f.translate(new Vector3f(width / 2.0F, height / 2, 0), mat0, mat0);
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

        RenderSpaceSky.getInstance().currentRenderer.render(tessellator, mc, mc.world, 255, 1.0F);

        float scale = 10.0F;

        GL11.glScalef(scale, scale, scale);

        if (selected != -1) {
            // render the planet

            renderPlanet(space.mainPlanet);
        }

        /*
        mc.renderEngine.bindTexture(selected != -1 ? space.planets.get(selected).iconResource : new ResourceLocation("anal"));

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(1.0f, -1.0f, -1.0f).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(-1.0f, -1.0f, -1.0f).tex(0.0D, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(-1.0f, 1.0f, -1.0f).tex(1, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(1.0f, 1.0f, -1.0f).tex(1, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
         */

        GL11.glPopMatrix();
    }

    private void renderPlanet(Planet planet) {
        final double time = Minecraft.getSystemTime() / 1000.0D;

        Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder vertexBuffer = tessellator.getBuffer();

        for (final PlanetRenderData renderData : planet.renderDatas) {
            // compute texture offsets for clouds animation
            final float offsetU = (float) ( Math.signum(renderData.periodU) * ((time / Math.abs(renderData.periodU)) % 1.0D) );
            final float offsetV = (float) ( Math.signum(renderData.periodV) * ((time / Math.abs(renderData.periodV)) % 1.0D) );

            // apply rendering parameters
            if (renderData.texture != null) {
                GlStateManager.enableTexture2D();
                Minecraft.getMinecraft().getTextureManager().bindTexture(renderData.resourceLocation);
                vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            } else {
                GlStateManager.disableTexture2D();
                vertexBuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            }
            if (renderData.isAdditive) {
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            } else {
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            }
            int renderSize = 1;

            int renderRange = 2;

            int offsetX = 1;
            int offsetZ = 1;
            // draw current layer
            for (int indexVertex = 0; indexVertex < 4; indexVertex++) {
                final double offset1 = ((indexVertex & 2) - 1) * renderSize;
                final double offset2 = ((indexVertex + 1 & 2) - 1) * renderSize;
                final double valV = offset1 - offset2;
                final double valH = offset2 + offset1;
                final double y = valV + renderRange;
                final double valD = renderRange - valV;
                final double x = valD - valH + renderSize * offsetX;
                final double z = valH + valD + renderSize * offsetZ;
                vertexBuffer.pos(x, y, z);
                if (renderData.texture != null) {
                    vertexBuffer.tex((indexVertex & 2) / 2 + offsetU, (indexVertex + 1 & 2) / 2 + offsetV);
                }
                vertexBuffer.color(renderData.red, renderData.green, renderData.blue, renderData.alpha * 1.0F).endVertex();
            }
            tessellator.draw();
        }
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
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 0) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            Minecraft.getMinecraft().displayGuiScreen(new GuiPlanetSelectionConfirmation());
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        active = true;

        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        GuiButton butt = new CustomButton(0, res.getScaledWidth() - lWidth + 7, 187, 50, 20, I18n.format("afmsm.text.gui_planetselection.button_select"));
        butt.enabled = false;

        buttonList.add(butt);

        int listY = 37;
        int listHeight = Math.min(lHeight, res.getScaledHeight() - listY);

        list = new GuiSlotPlanetSelection(space.planets, this, res.getScaledWidth() - 200, listY, 200, listHeight, 63, width, height);
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
        return false;
    }

    public void setSelected(int slotIdx) {
        selected = slotIdx;

        buttonList.get(0).enabled = slotIdx != -1;
    }
}
