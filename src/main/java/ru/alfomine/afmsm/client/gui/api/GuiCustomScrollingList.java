package ru.alfomine.afmsm.client.gui.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public abstract class GuiCustomScrollingList {
    protected final int listWidth;
    protected final int listHeight;
    protected final int screenWidth;
    protected final int screenHeight;
    protected final int top;
    protected final int bottom;
    protected final int right;
    protected final int left;
    protected final int slotHeight;
    private final Minecraft client;
    protected int mouseX;
    protected int mouseY;
    protected int selectedIndex = -1;
    protected boolean captureMouse = true;
    private int scrollUpActionId;
    private int scrollDownActionId;
    public float initialMouseClickY = -2.0F;
    private float scrollFactor;
    private float scrollDistance;
    public long lastClickTime = 0L;
    private boolean highlightSelected = true;
    private boolean hasHeader;
    private int headerHeight;
    public boolean animActive = false;
    private long animTime = 0;

    // Легкий конструктор без хуйни. А то накодят какую-то парашу непонятную, а ты потом разбирайся.
    public GuiCustomScrollingList(int x, int y, int width, int height, int entryHeight, int screenWidth, int screenHeight) {
        this(Minecraft.getMinecraft(), width, screenHeight, y, y + height, x, entryHeight, screenWidth, screenHeight);
    }

    public GuiCustomScrollingList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight) {
        this.client = client;
        this.listWidth = width;
        this.listHeight = height;
        this.top = top;
        this.bottom = bottom;
        this.slotHeight = entryHeight;
        this.left = left;
        this.right = width + this.left;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @Deprecated // Unused, remove in 1.9.3?
    public void func_27258_a(boolean p_27258_1_) {
        this.highlightSelected = p_27258_1_;
    }

    @Deprecated
    protected void func_27259_a(boolean hasFooter, int footerHeight) {
        setHeaderInfo(hasFooter, footerHeight);
    }

    protected void setHeaderInfo(boolean hasHeader, int headerHeight) {
        this.hasHeader = hasHeader;
        this.headerHeight = headerHeight;
        if (!hasHeader) this.headerHeight = 0;
    }

    protected abstract int getSize();

    protected abstract void elementClicked(int index, boolean doubleClick);

    protected abstract boolean isSelected(int index);

    protected int getContentHeight() {
        return this.getSize() * this.slotHeight + this.headerHeight;
    }

    protected abstract void drawBackground();

    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside of the view box. Do not mess with SCISSOR unless you support this.
     */
    protected abstract void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, int mouseX, int mouseY, Tessellator tess);

    @Deprecated
    protected void func_27260_a(int entryRight, int relativeY, Tessellator tess) {
    }

    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside of the view box. Do not mess with SCISSOR unless you support this.
     */
    protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {
        func_27260_a(entryRight, relativeY, tess);
    }

    @Deprecated
    protected void func_27255_a(int x, int y) {
    }

    protected void clickHeader(int x, int y) {
        func_27255_a(x, y);
    }

    @Deprecated
    protected void func_27257_b(int mouseX, int mouseY) {
    }

    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside of the view box. Do not mess with SCISSOR unless you support this.
     */
    protected void drawScreen(int mouseX, int mouseY) {
        func_27257_b(mouseX, mouseY);
    }

    @Deprecated // Unused, Remove in 1.9.3?
    public int func_27256_c(int x, int y) {
        int left = this.left + 1;
        int right = this.left + this.listWidth - 7;
        int relativeY = y - this.top - this.headerHeight + (int) this.scrollDistance - 4;
        int entryIndex = relativeY / this.slotHeight;
        return x >= left && x <= right && entryIndex >= 0 && relativeY >= 0 && entryIndex < this.getSize() ? entryIndex : -1;
    }

    public static float easeOut(float t, float b, float c, float d) {
        return c * ((t = t / d - 1) * t * t + 1) + b;
    }

    public void handleMouseInput(int mouseX, int mouseY) throws IOException {
        boolean isHovering = mouseX >= this.left && mouseX <= this.left + this.listWidth &&
                mouseY >= this.top && mouseY <= this.bottom;
        if (!isHovering)
            return;

        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            this.scrollDistance += (float) ((-1 * scroll / 120.0F) * this.slotHeight / 2);
        }
    }

    private void applyScrollLimits() {
        int listHeight = this.getContentHeight() - (this.bottom - this.top - 4);

        if (listHeight < 0) {
            //listHeight /= 2;
            this.scrollDistance = 0;
        } else if (this.scrollDistance < 0.0F) {
            this.scrollDistance = 0.0F;
        } else if (this.scrollDistance > (float) listHeight) {
            this.scrollDistance = (float) listHeight;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.drawBackground();

        boolean isHovering = mouseX >= this.left && mouseX <= this.left + this.listWidth &&
                mouseY >= this.top && mouseY <= this.bottom;
        int listLength = this.getSize();
        int scrollBarWidth = 6;
        int scrollBarRight = this.left + this.listWidth;
        int scrollBarLeft = scrollBarRight - scrollBarWidth;
        int entryRight = scrollBarLeft - 1;
        int viewHeight = this.bottom - this.top;
        int border = 0;

        if (Mouse.isButtonDown(0)) {
            if (this.initialMouseClickY == -1.0F) {
                if (isHovering) {
                    this.lastClickTime = System.currentTimeMillis();

                    if (mouseX >= scrollBarLeft && mouseX <= scrollBarRight) {
                        this.scrollFactor = -1.0F;
                        int scrollHeight = this.getContentHeight() - viewHeight - border;
                        if (scrollHeight < 1) scrollHeight = 1;

                        int var13 = (int) ((float) (viewHeight * viewHeight) / (float) this.getContentHeight());

                        if (var13 < 32) var13 = 32;
                        if (var13 > viewHeight - border * 2)
                            var13 = viewHeight - border * 2;

                        this.scrollFactor /= (float) (viewHeight - var13) / (float) scrollHeight;
                    } else {
                        this.scrollFactor = 1.0F;
                    }

                    this.initialMouseClickY = mouseY;
                } else {
                    this.initialMouseClickY = -2.0F;
                }
            } else if (this.initialMouseClickY >= 0.0F) {
                this.scrollDistance -= ((float) mouseY - this.initialMouseClickY) * this.scrollFactor;
                this.initialMouseClickY = (float) mouseY;
            }
        } else {
            this.initialMouseClickY = -1.0F;
        }

        //Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(String.valueOf(initialMouseClickY), 0, 0, 0xFFFFFF);

        this.applyScrollLimits();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldr = tess.getBuffer();

        ScaledResolution res = new ScaledResolution(client);
        double scaleW = client.displayWidth / res.getScaledWidth_double();
        double scaleH = client.displayHeight / res.getScaledHeight_double();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (left * scaleW), (int) (client.displayHeight - (bottom * scaleH)),
                (int) (listWidth * scaleW), (int) (viewHeight * scaleH));

        int baseY = this.top + border - (int) this.scrollDistance;

        if (this.hasHeader) {
            this.drawHeader(entryRight, baseY, tess);
        }

        if (animTime == 0) {
            animActive = true;
            animTime = System.currentTimeMillis();
        }

        int actualSlotHeight;

        if (animActive) {
            actualSlotHeight = (int) easeOut((float) ((System.currentTimeMillis() - animTime) / 0.9), 0, slotHeight, 1000);

            if (actualSlotHeight >= slotHeight) {
                animTime = -1;
                animActive = false;
            }
        } else {
            actualSlotHeight = slotHeight;
        }

        for (int slotIdx = 0; slotIdx < listLength; ++slotIdx) {
            int slotTop = baseY + (slotIdx * actualSlotHeight) + this.headerHeight;
            int slotBuffer = actualSlotHeight - border;

            if (slotTop <= this.bottom && slotTop + slotBuffer >= this.top) {
                this.drawSlot(slotIdx, entryRight, slotTop, slotBuffer, mouseX, mouseY, tess);
            }
        }

        GlStateManager.disableDepth();

        int extraHeight = (this.getContentHeight() + border) - viewHeight;
        if (extraHeight > 0) {
            int height = (viewHeight * viewHeight) / this.getContentHeight();

            if (height < 32) height = 32;

            if (height > viewHeight - border * 2)
                height = viewHeight - border * 2;

            int barTop = (int) this.scrollDistance * (viewHeight - height) / extraHeight + this.top;
            if (barTop < this.top) {
                barTop = this.top;
            }

            GlStateManager.disableTexture2D();
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft, this.bottom, 0.0D).tex(0.0D, 1.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarRight, this.bottom, 0.0D).tex(1.0D, 1.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarRight, this.top, 0.0D).tex(1.0D, 0.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarLeft, this.top, 0.0D).tex(0.0D, 0.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            tess.draw();
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft, barTop + height, 0.0D).tex(0.0D, 1.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarRight, barTop + height, 0.0D).tex(1.0D, 1.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarRight, barTop, 0.0D).tex(1.0D, 0.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarLeft, barTop, 0.0D).tex(0.0D, 0.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            tess.draw();
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft, barTop + height - 1, 0.0D).tex(0.0D, 1.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarRight - 1, barTop + height - 1, 0.0D).tex(1.0D, 1.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarRight - 1, barTop, 0.0D).tex(1.0D, 0.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarLeft, barTop, 0.0D).tex(0.0D, 0.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            tess.draw();
        }

        this.drawScreen(mouseX, mouseY);
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    protected void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
        GuiUtils.drawGradientRect(0, left, top, right, bottom, color1, color2);
    }
}