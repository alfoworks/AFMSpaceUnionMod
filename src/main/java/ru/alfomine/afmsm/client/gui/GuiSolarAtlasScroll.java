package ru.alfomine.afmsm.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiSolarAtlasScroll extends GuiButton {

    public float sliderValue = 1.0f;
    public float minimumValue = 1.0f;
    public float maximumValue = 1.0f;
    public boolean dragging = false;
    public String text;
    private float sliderPosition;

    public GuiSolarAtlasScroll(int id, int x, int y, int width, int height,
                               float minIn, float maxIn,
                               float defaultValue, String sliderText) {

        super(id, x, y, width, height, sliderText);

        this.sliderValue = defaultValue;
        this.sliderPosition = (int) 0.004f;
        this.minimumValue = minIn;
        this.maximumValue = maxIn;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            FontRenderer fontRenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(new ResourceLocation("afmsm", "textures/gui_solaratlas.png"));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            drawTexturedModalRect512(this.x, this.y,
                    this.width, this.height,
                    366, 0,
                    20, 200);
            GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F);
            this.drawTexturedModalRect(this.x, this.y + (this.sliderPosition) * this.height,
                    this.x + this.width, this.y + this.height,
                    this.width, this.height / 20);


            this.mouseDragged(mc, mouseX, mouseY);

            int l = 14737632; // Light Gray

            if (packedFGColour != 0) {
                l = packedFGColour;
            } else if (!this.enabled){
                l = 10526880; // Dark Gray
            } else if (this.hovered) {
                l = 16777120; // Lighter Gray
            }

            this.drawCenteredString(fontRenderer, this.displayString, this.x + this.width / 2, this.y + this.height/2, 0xFFFFFF);
        }
    }

    protected int getHoverState(boolean hovering) {
        return 0;
    }

    protected void mouseDragged(Minecraft mc, int dx, int dy) {
        if (this.enabled && this.visible && this.packedFGColour == 0) {
            if (this.dragging) {
                this.sliderPosition = (float) (dy-this.y)/height;
                this.sliderValue = (this.maximumValue-this.minimumValue)*(sliderPosition)+this.minimumValue;

                if (sliderPosition < 0.004) sliderPosition = 0.004f;
                if (sliderPosition > 0.95) sliderPosition = 0.95f;

                if (this.sliderValue < this.minimumValue) this.sliderValue = this.minimumValue;
                if (this.sliderValue > this.maximumValue) this.sliderValue = this.maximumValue;

                this.displayString = String.format(": %.3f", (sliderValue));
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                //this.drawTexturedModalRect(this.x, (int) (sliderValue * (this.height - 8f)), 66, 0, 4, 20);
                //this.drawTexturedModalRect(this.x, (int) (sliderValue * (this.height - 8f)) + 4, 66, 196, 4, 20);
            }
        }
    }

    public boolean mousePressed(Minecraft mc, int x, int y) {
        if (super.mousePressed(mc, x, y)) {
            this.sliderPosition = (float) (y-this.y)/height;
            this.sliderValue = (this.maximumValue-this.minimumValue)*(sliderPosition)+this.minimumValue;

            if (sliderPosition < 0.004) sliderPosition = 0.004f;
            if (sliderPosition > 0.95) sliderPosition = 0.95f;

            if (this.sliderValue < this.minimumValue) this.sliderValue = this.minimumValue;
            if (this.sliderValue > this.maximumValue) this.sliderValue = this.maximumValue;

            this.dragging = true;
            this.displayString = String.format(": %.3f", (sliderValue));
            return true;

        } else {
            return false;
        }

    }

    public void mouseReleased(int x, int y) {
        this.dragging = false;
    }

    private void drawTexturedModalRect512(float x, float y, float width, float height, float u, float v, float uWidth, float vHeight) {
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
}
