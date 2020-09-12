package ru.alfomine.afmsm.client.gui;

import cr0s.warpdrive.render.skybox.ISkyBoxRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.alfomine.afmsm.client.gui.api.GuiCustomScrollingList;

import java.awt.*;
import java.util.List;

public class GuiSlotSkyboxSelection extends GuiCustomScrollingList {
    List<ISkyBoxRenderer> list;
    GuiSkyboxSelection parent;

    private ResourceLocation mainGuiLoc = new ResourceLocation("afmsm", "textures/gui_selection.png");

    public GuiSlotSkyboxSelection(List<ISkyBoxRenderer> list, GuiSkyboxSelection parent, int x, int y, int width, int height, int entryHeight, int screenWidth, int screenHeight) {
        super(x, y, width, height, entryHeight, screenWidth, screenHeight);

        this.list = list;
        this.parent = parent;
    }

    @Override
    protected int getSize() {
        return list.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {

    }

    @Override
    protected boolean isSelected(int index) {
        return false;
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, int mouseX, int mouseY, Tessellator tess) {
        Minecraft mc = Minecraft.getMinecraft();
        ISkyBoxRenderer skybox = list.get(slotIdx);

        int x = entryRight - listWidth + 7;

        boolean hovered = !animActive && mouseX >= x &&
                mouseX <= entryRight &&
                mouseY >= slotTop &&
                mouseY <= slotTop + slotHeight - 10;
        boolean selected = slotIdx == parent.selected;

        if (hovered && Mouse.isButtonDown(0) && slotIdx != parent.selected && System.currentTimeMillis() - lastClickTime < 10) {
            parent.setSelected(slotIdx);
            selected = true;
        }

        mc.renderEngine.bindTexture(mainGuiLoc);

        Color color = Color.BLUE;

        if (selected) {
            color = color.darker().darker();
        } else if (hovered) {
            color = color.darker();
        }

        GL11.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        parent.drawTexturedModalRect512(x, slotTop, 188, 76, 262, 198, 188, 76);

        if (selected) {
            parent.drawTexturedModalRect512(x + 166, slotTop + 10, 16, 15, 262, 53, 16, 15);
        }

        GL11.glColor3f(1f, 1f, 1f);

        int iconRes = 52;

        mc.renderEngine.bindTexture(skybox.getPreview());
        Gui.drawModalRectWithCustomSizedTexture(x + 11, slotTop + 12, 0, 0, iconRes, iconRes, iconRes, iconRes);

        parent.drawString(mc.fontRenderer, skybox.getName(), x + 64 + parent.center(mc.fontRenderer.getStringWidth(skybox.getName()), 115), slotTop + 27 + mc.fontRenderer.FONT_HEIGHT / 2, 0xFFFFFF);
    }
}
