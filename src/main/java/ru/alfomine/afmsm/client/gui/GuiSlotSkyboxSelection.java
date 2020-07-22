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

        if (hovered && Mouse.isButtonDown(0)) {
            parent.setSelected(slotIdx);
            selected = true;
        }

        mc.renderEngine.bindTexture(GuiPlanetSelection.mainGuiLoc);

        Color color = Color.BLUE;

        GL11.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        parent.drawTexturedModalRect512(x, slotTop, 188, 53, 262, 0, 188, 53);

        if (selected) {
            parent.drawTexturedModalRect512(x + 166, slotTop + 10, 16, 15, 262, 53, 16, 15);
        }

        GL11.glColor3f(1f, 1f, 1f);

        int iconRes = 34;

        mc.renderEngine.bindTexture(new ResourceLocation(""));
        Gui.drawModalRectWithCustomSizedTexture(x + 10, slotTop + 10, 0, 0, iconRes, iconRes, iconRes, iconRes);

        parent.drawString(mc.fontRenderer, skybox.getName(), x + 12 + parent.center(mc.fontRenderer.getStringWidth(skybox.getName()), 188), slotTop + 8, 0xFFFFFF);
    }
}
