package ru.alfomine.afmsm.client.gui;

import cr0s.warpdrive.data.CelestialObject;
import cr0s.warpdrive.data.CelestialObjectManager;
import net.minecraft.util.math.AxisAlignedBB;
import ru.alfomine.afmsm.planet.Planet;

public class GuiSolarAtlasPlanet {
    public final CelestialObject celestial;
    public final AxisAlignedBB area;
    public final Planet planet;

    public int x;
    public int y;
    public int realSize;
    public int sizeX;
    public int sizeY;

    public int overthrowXNegative;
    public int overthrowYNegative;

    public boolean highlight;

    public GuiSolarAtlasPlanet(Planet planet) {
        this.celestial = CelestialObjectManager.get(false, planet.warpId);
        this.planet = planet;
        this.area = celestial.getAreaInParent();
        this.realSize = (int) Math.round(Math.abs((area.maxX - area.minX)) / GuiSolarAtlas.scale * GuiSolarAtlas.zoom);
        this.sizeX = this.realSize;
        this.sizeY = this.realSize;
    }

    public void calculate() {
        int x = (int) (GuiSolarAtlas.lX+259/2+(area.minX / GuiSolarAtlas.scale * GuiSolarAtlas.zoom) + Math.round(GuiSolarAtlas.offsetX));
        int y = (int) (GuiSolarAtlas.lY+GuiSolarAtlas.lHeight/2+(area.minZ / GuiSolarAtlas.scale * GuiSolarAtlas.zoom) + Math.round(GuiSolarAtlas.offsetY));
        overthrowXNegative = 0;
        overthrowYNegative = 0;
        this.realSize = (int) Math.round(Math.abs((area.maxX - area.minX)) / GuiSolarAtlas.scale * GuiSolarAtlas.zoom);

        overthrownCalculation(x, y);
    }

    public boolean isPointOverlapping(int x, int y) {
        return  ((x > 303 && x < 548) && (y > 144 && y < 387) &&
                (x > this.x-overthrowXNegative && x < this.x-overthrowXNegative+realSize) &&
                (y > this.y-overthrowYNegative && y < this.y-overthrowYNegative+realSize));

    }

    private void overthrownCalculation(int x, int y) {
        // Overthrow X calculation
        // Overthrow over borders of the display area
        int overthrowX = 0;
        int overthrowY = 0;

        if (x > GuiSolarAtlas.lX+252-realSize) {
            overthrowX = x-(GuiSolarAtlas.lX+252-realSize);
            // If we're overthrowing over size of the planet we're getting into negative numbers we don't want
            if (overthrowX > realSize)
                overthrowX = realSize;
        }

        if (x < GuiSolarAtlas.lX) {
            overthrowXNegative = GuiSolarAtlas.lX-x;
            if (overthrowXNegative > realSize)
                overthrowXNegative = realSize;
        }

        // Overthrow Y calculation
        if (y > GuiSolarAtlas.lY+269-realSize) {
            overthrowY = y - (GuiSolarAtlas.lY + 269 - realSize);
            if (overthrowY > realSize)
                overthrowY = realSize;
        }

        if (y < GuiSolarAtlas.lY + 22) {
            overthrowYNegative = GuiSolarAtlas.lY+22-y;
            if (overthrowYNegative > realSize)
                overthrowYNegative = realSize;
        }

        // Debug coordinates on planets
        // drawString(fontRenderer, String.format("%s %s", reaGuiSolarAtlas.lX+overthrowXNegative, mouseX), reaGuiSolarAtlas.lX, GuiSolarAtlas.lY, 0xFFFFFF);
        // drawString(fontRenderer, String.format("%s %s", GuiSolarAtlas.lY-overthrowYNegative, mouseY), reaGuiSolarAtlas.lX, GuiSolarAtlas.lY+fontRenderer.FONT_HEIGHT+3, 0xFFFFFF);

        this.x = x + overthrowXNegative;
        this.y = y + overthrowYNegative;
        this.sizeX = this.realSize-overthrowX-overthrowXNegative;
        this.sizeY = this.realSize-overthrowY-overthrowYNegative;
    }
}
