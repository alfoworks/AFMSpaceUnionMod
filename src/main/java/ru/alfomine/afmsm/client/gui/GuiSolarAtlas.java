package ru.alfomine.afmsm.client.gui;

import cr0s.warpdrive.data.CelestialObject;
import cr0s.warpdrive.data.CelestialObjectManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import org.lwjgl.input.Mouse;
import ru.alfomine.afmsm.AFMSpaceUnionMod;
import ru.alfomine.afmsm.client.gui.api.CustomGui;
import ru.alfomine.afmsm.init.ModSounds;
import ru.alfomine.afmsm.space.Planet;
import ru.alfomine.afmsm.space.Space;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GuiSolarAtlas extends CustomGui {

    static int lWidth = 366;
    static int lHeight = 275;
    List<GuiSolarAtlasPlanet> guiPlanets = new ArrayList<>();
    int prevScale = -1;

    private final Space space;

    static float zoom;
    private float initialZoom;
    private float transitionZoom;
    private float initialX;
    private float initialY;
    private float transitionX;
    private float transitionY;
    static int offsetX;
    static int offsetY;
    private GuiSolarAtlasScroll scroll;

    public GuiSolarAtlas(Space space) {
        this.space = space;

        zoom = 1.0f;
        transitionZoom = 1.0f;
        initialZoom = -1.0f;
        offsetX = 0;
        offsetY = 0;
    }

    String hoveringOver = null;
    boolean grab = false;
    boolean pressed = false;
    static final int scale = 10000;
    static int lX;
    static int lY;


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        ScaledResolution res = new ScaledResolution(mc);

        lX = center(lWidth, res.getScaledWidth());
        lY = center(lHeight, res.getScaledHeight());

        // Отрисовка фона
        mc.renderEngine.bindTexture(new ResourceLocation("afmsm", "textures/gui_solaratlas_bg.png"));
        drawTexturedModalRect512(lX, lY + 22, lWidth - 114, lHeight - 26, -offsetX / zoom / 5f, -offsetY / zoom / 5f, lWidth / zoom, lHeight / zoom);

        // Отрисовка сетки
        float gridSize = 10 * zoom;
        gridSize /= (gridSize > 80) ? 5 : (gridSize <= 10) ? 0.2 : 1;

        {
            int i = 0;
            for (int x = Math.round(lX + 255 / 2f + offsetX % gridSize); x < lX + 255 + offsetX % gridSize + 60; x += gridSize) {
                // Vertical right
                if (x > lX && x < lX + 255)
                    drawVerticalLine(x, lY + 20, lY + lHeight - 5, 0xAF00BFFF);
                // Vertical left
                if (i != 0 && x - gridSize * i * 2 < lX + 255 && x - gridSize * i * 2 > lX)
                    drawVerticalLine(Math.round(x - gridSize * i * 2f), lY + 20, lY + lHeight - 5, 0xAF00BFFF);
                i++;
            }

            i = 0;
            for (int y = Math.round(lY + lHeight / 2f + offsetY % gridSize); y < lY + lHeight + offsetY % gridSize + 30; y += gridSize) {
                // Horizontal down
                if (y < lY + lHeight - 6 && y > lY + 20)
                    drawHorizontalLine(lX, lX + 255, y, 0xAF00BFFF);
                // Horizontal up
                if (i != 0 && y - gridSize * i * 2 > lY + 20)
                    drawHorizontalLine(lX, lX + 255, Math.round(y - gridSize * i * 2f), 0xAF00BFFF);
                i++;
            }
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Отрисовка планет
        for (GuiSolarAtlasPlanet planet : guiPlanets) {
            mc.renderEngine.bindTexture(planet.planet.iconResource);
            planet.calculate();
            //Debug string
            //drawString(fontRenderer, "" + planet.sizeX + " " + planet.sizeY + " " + planet.realSize, lX+40, lY + 40, 0xFFFFFF);
            drawModalRectWithCustomSizedTexture(planet.x, planet.y, planet.overthrowXNegative, planet.overthrowYNegative, planet.sizeX, planet.sizeY, planet.realSize, planet.realSize);
        }

        // Zoom block render
        drawVerticalLine(lX + 12, lY + lHeight - 22, lY + lHeight - 25, 0xBBFFFFFF);
        drawHorizontalLine(lX + 12, lX + 12 + Math.round(gridSize), lY + lHeight - 22, 0xBBFFFFFF);
        drawVerticalLine(lX + 12 + Math.round(gridSize), lY + lHeight - 22, lY + lHeight - 25, 0xBBFFFFFF);
        drawString(fontRenderer, String.format("1:%s", Math.round(scale / zoom * gridSize)), lX + 12, lY + lHeight - 35, 0xFFFFFF);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Отрисовка контейнера
        mc.renderEngine.bindTexture(new ResourceLocation("afmsm", "textures/gui_solaratlas.png"));
        drawTexturedModalRect512(lX, lY, lWidth, lHeight, 0, 0, lWidth, lHeight);

        // Отрисовка заголовка
        assert mc.currentScreen != null;
        // 109/1920 = 0.05677 - Distance on the fullscreen by width to the center of SolarAtlas display area
        drawString(fontRenderer, "SolarAtlas", lX + 4, lY + 8, 0xFFFFFF);
        int uniOffset = Math.round(14 * scroll.sliderValue - 14);
        // Отрисовка имён планет в списке
        for (float i = uniOffset; i < space.planets.size(); i++) {
            if (i > Math.round(14 * scroll.sliderValue)) {
                continue;
            }
            drawString(fontRenderer, space.planets.get(Math.round(i)).name, lX + 259, Math.round((i * fontRenderer.FONT_HEIGHT + 3) + lY + 24 - uniOffset * fontRenderer.FONT_HEIGHT), 0xFFFFFF);
        }

        // Mouse over planet actions
        for (GuiSolarAtlasPlanet planet : guiPlanets) {
            if (planet.isPointOverlapping(mouseX, mouseY)) {
                hoveringOver = planet.celestial.id;
                drawHoveringText((pressed) ? "Coordinates copied to clipboard!" : planet.planet.name.toUpperCase(), mouseX, mouseY);

            } else if (planet.celestial.id.equalsIgnoreCase(hoveringOver)) {
                hoveringOver = null;
                pressed = false;
            }
        }

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        // Zoom modification
        int temp = Mouse.getDWheel();
        if (temp != 0 && Mouse.isInsideWindow() && initialZoom == -1.0f) {
            initialZoom = zoom;
            transitionZoom = temp < 0 ?
                    (transitionZoom > 0.25) ? transitionZoom / 2f : transitionZoom
                    :
                    (transitionZoom < 64) ? transitionZoom * 2 : transitionZoom;
            initialX = offsetX;
            initialY = offsetY;
            // Finding desired point on the next zoom level
            transitionX = (temp > 0) ? (float) ((float) offsetX * 2 + getRelativeX()) : offsetX / 2f;
            transitionY = (temp > 0) ? (float) ((float) offsetY * 2 - getRelativeY()) : offsetY / 2f;
            if (initialZoom != transitionZoom)
                mc.player.playSound(ModSounds.SOLARATLAS_ZOOM, mc.gameSettings.getSoundLevel(SoundCategory.MASTER), 1.0F);
        }

        // Zoom transition
        if (zoom > 0.2) {
            if (zoom != transitionZoom) {
                zoom = (zoom * 10 + (transitionZoom-initialZoom)) / 10f;
                // Transition from point A to B, not from the center of the map
                offsetX = (int) ((transitionX-initialX) * ((zoom-initialZoom)/(transitionZoom-initialZoom)) + initialX);
                offsetY = (int) ((transitionY-initialY) * ((zoom-initialZoom)/(transitionZoom-initialZoom)) + initialY);
            } else {
                initialZoom = -1.0f; // Anti zoom-spam
            }
        }

        // Grab check
        if (Mouse.isButtonDown(0)) {
            if (hoveringOver != null) {
                pressed = true;
                CelestialObject planet = CelestialObjectManager.get(false, hoveringOver);
                setClipboardString(String.format("%s %s",
                        (int) (planet.getAreaInParent().minX + planet.getAreaInParent().maxX) / 2,
                        (int) (planet.getAreaInParent().minZ + planet.getAreaInParent().maxZ) / 2));
            }

            if (!((GuiSolarAtlasScroll) (buttonList.stream().filter(b -> b.id == "AFMAtlasSlider".hashCode()).findFirst().get())).dragging) {
                if (!grab) {
                    grab = true;
                    Mouse.getDX();
                    Mouse.getDY();
                }
                offsetX += Mouse.getDX();
                offsetY -= Mouse.getDY();
            }
        } else {
            grab = false;
        }
    }

    private double getRelativeX() {
        return mc.displayWidth / 2f - mc.displayWidth * 0.05677 - Mouse.getX();
    }

    private double getRelativeY() {
        return mc.displayHeight / 2f - Mouse.getY();
    }

    @Override
    public void initGui() {
        super.initGui();

        /*mc.getSoundHandler().addListener((soundIn, accessor) -> accessor.addSound(soundIn.getSound()));
        mc.getSoundHandler().getAccessor(new ResourceLocation("afmsm", "sounds/gui_solaratlas_zoom.ogg"));*/
        this.scroll = new GuiSolarAtlasScroll("AFMAtlasSlider".hashCode(), lX + 338, lY + 24, 20, 130, 1.0f, 1 + 14f / space.planets.size(), 1.0f, "a");
        addButton(scroll);

        ScaledResolution res = new ScaledResolution(mc);

        // Initialization of planets
        for (Planet p : space.planets) {
            //Debug case
            //if (p.name.equalsIgnoreCase("jupiter"))
            GuiSolarAtlasPlanet planet = new GuiSolarAtlasPlanet(p);
            planet.calculate();
            guiPlanets.add(planet);
        }

		/*
		Грязный хак, уменьшает размер интерфейса если контейнер не помещается в окне.
		 */

        if (lHeight > res.getScaledHeight()) {
            prevScale = mc.gameSettings.guiScale;
            mc.gameSettings.guiScale = 1;
        } else if (prevScale != -1) {
            mc.gameSettings.guiScale = prevScale;
        }
		
		/*
		Т.к. после изменения интерфейса меняется разрешение, то и тут нужно его поменять.
		 */
        res = new ScaledResolution(mc);
        height = res.getScaledHeight();
        width = res.getScaledWidth();
        scroll.x = center(lWidth, res.getScaledWidth()) + 338;
        scroll.y = center(lHeight, res.getScaledHeight()) + 24;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        if (prevScale != -1) {
            mc.gameSettings.guiScale = prevScale;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
