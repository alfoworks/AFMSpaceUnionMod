package ru.alfomine.afmsm.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.alfomine.afmsm.client.gui.api.GuiCustomScrollingList;
import ru.alfomine.afmsm.space.Planet;

import java.awt.*;
import java.util.List;

public class GuiSlotPlanetSelection extends GuiCustomScrollingList {
    List<Planet> list;
    GuiPlanetSelection parent;

    private ResourceLocation mainGuiLoc = new ResourceLocation("afmsm", "textures/gui_selection.png");

    public GuiSlotPlanetSelection(List<Planet> list, GuiPlanetSelection parent, int x, int y, int width, int height, int entryHeight, int screenWidth, int screenHeight) {
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
        return parent.selected == index;
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, int mouseX, int mouseY, Tessellator tess) {
        Minecraft mc = Minecraft.getMinecraft();
        Planet planet = list.get(slotIdx);

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

        mc.renderEngine.bindTexture(mainGuiLoc);

        Color color = planet.difficulty.color;

        if (selected) { // select
            color = planet.difficulty.colorPressed;
        } else if (hovered) { // hover
            color = planet.difficulty.colorHovered;
        }

        GL11.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        parent.drawTexturedModalRect512(x, slotTop, 188, 53, 262, 0, 188, 53);

        if (selected) {
            parent.drawTexturedModalRect512(x + 166, slotTop + 10, 16, 15, 262, 53, 16, 15);
        }

        GL11.glColor3f(1f, 1f, 1f);

        int iconRes = 34;

        mc.renderEngine.bindTexture(planet.iconResource);
        Gui.drawModalRectWithCustomSizedTexture(x + 12, slotTop + 10, 0, 0, iconRes, iconRes, iconRes, iconRes);

        parent.drawString(mc.fontRenderer, planet.name, x + 12 + parent.center(mc.fontRenderer.getStringWidth(planet.name), 188), slotTop + 8, 0xFFFFFF);
        parent.drawString(mc.fontRenderer, String.format("Сложность: %s", planet.difficulty.name), x + 52, slotTop + 20, 0xFFFFFF);
        parent.drawString(mc.fontRenderer, String.format("Размер: %sx%s", planet.size, planet.size), x + 52, slotTop + 20 + mc.fontRenderer.FONT_HEIGHT, 0xFFFFFF);
    }
}
