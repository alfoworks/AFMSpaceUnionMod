package ru.alfomine.afmsm.client.gui;

import cr0s.warpdrive.data.CelestialObject;
import cr0s.warpdrive.data.CelestialObjectManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.input.Mouse;
import ru.alfomine.afmsm.client.gui.api.CustomGui;
import ru.alfomine.afmsm.planet.Planet;
import ru.alfomine.afmsm.planet.PlanetDifficulty;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GuiSolarAtlas extends CustomGui {

    static int lWidth = 366;
    static int lHeight = 275;
    List<Planet> planets;
    List<GuiSolarAtlasPlanet> guiPlanets = new ArrayList<>();
    int spaceSize;
    int prevScale = -1;

    static float zoom;
    private float transitionZoom;
    private float transitionX;
    private float transitionY;
    private boolean zoomingIn;
    static int offsetX;
    static int offsetY;
    private GuiSolarAtlasScroll scroll;

    public GuiSolarAtlas(List<Planet> planets, int spaceSize) {
        this.planets = planets;
        this.spaceSize = spaceSize;

        zoom = 1.0f;
        transitionZoom = 1.0f;
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

        // Zoom block render
        float gridSize = 10*zoom;
        gridSize /= (gridSize > 80) ? 5 : (gridSize <= 10) ? 0.2 : 1;

        // Grid block
        {
            int i = 0;
            for (int x = Math.round(lX + 255 / 2f + offsetX % gridSize); x < lX + 255 + offsetX % gridSize + 60; x += gridSize) {
                // Vertical right
                if (x > lX && x < lX + 255)
                    drawVerticalLine(x, lY + 20,lY + lHeight - 5, 0xAF00BFFF);
                // Vertical left
                if (i != 0 && x - gridSize * i * 2 < lX + 255 && x - gridSize * i * 2 > lX)
                    drawVerticalLine(Math.round(x - gridSize * i * 2f), lY + 20,lY + lHeight - 5, 0xAF00BFFF);
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

        drawVerticalLine(lX + 12, lY + lHeight - 20, lY + lHeight - 23, 0xBB035efc);
        drawHorizontalLine(lX + 12, lX + 12 + Math.round(gridSize),lY + lHeight - 20, 0xBB035efc);
        drawVerticalLine(lX + 12 + Math.round(gridSize), lY + lHeight - 20, lY + lHeight - 23, 0xBB035efc);
        GlStateManager.color(1.0F, 1.0f, 1.0f, 1.0f);
        drawString(fontRenderer, String.format("1:%s %s:%s:%s %s:%s:%s", scale / zoom * gridSize, offsetX, Math.round(transitionX),
                Math.round(getRelativeX()), offsetY, Math.round(transitionY), Math.round(getRelativeY())), lX + 12, lY + lHeight - 35, 0xFFFFFF);

        // Отрисовка планет
        for (GuiSolarAtlasPlanet planet : guiPlanets) {
            mc.renderEngine.bindTexture(planet.planet.iconResource);
            planet.calculate();
            //Debug string
            //drawString(fontRenderer, "" + planet.sizeX + " " + planet.sizeY + " " + planet.realSize, lX+40, lY + 40, 0xFFFFFF);
            drawModalRectWithCustomSizedTexture(planet.x, planet.y, planet.overthrowXNegative, planet.overthrowYNegative, planet.sizeX, planet.sizeY, planet.realSize, planet.realSize);
        }

        // Отрисовка контейнера
        mc.renderEngine.bindTexture(new ResourceLocation("afmsm", "textures/gui_solaratlas.png"));
        drawTexturedModalRect512(lX, lY, lWidth, lHeight, 0, 0, lWidth, lHeight);

        // Отрисовка заголовка
        assert mc.currentScreen != null;
        // 109/1920 = 0.05677 - Distance on the fullscreen by width to the center of SolarAtlas display area
        drawString(fontRenderer, "SolarAtlas", lX + 4, lY + 8, 0xFFFFFF);
        int uniOffset = Math.round(14 * scroll.sliderValue - 14);
        // Отрисовка имён планет в списке
        for (float i = uniOffset; i < planets.size(); i++) {
            if (i > Math.round(14 * scroll.sliderValue)) {
                continue;
            }
            drawString(fontRenderer, planets.get(Math.round(i)).name, lX + 259, Math.round((i * fontRenderer.FONT_HEIGHT + 3) + lY + 24 - uniOffset * fontRenderer.FONT_HEIGHT), 0xFFFFFF);
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
        if (temp != 0 && Mouse.isInsideWindow()) {
            ScaledResolution res = new ScaledResolution(mc);

            transitionZoom = temp < 0 ?
                    (transitionZoom > 0.25) ? transitionZoom / 2f : transitionZoom
                    :
                    (transitionZoom < 64) ? transitionZoom * 2 : transitionZoom;
            transitionX = (float) ((temp > 0) ? offsetX*2+getRelativeX() : transitionX/4f);
            transitionY = (float) ((temp > 0) ? offsetY*2-getRelativeY() : transitionY/4f);
        }

        // Zoom transition

        if (zoom < transitionZoom) {
            /*mc.getSoundHandler().playSound(new ISound() {
                @Override
                public ResourceLocation getSoundLocation() {
                    return new ResourceLocation("afmsm", "sounds/gui_solaratlas_zoom.ogg");
                }

                @Nullable
                @Override
                public SoundEventAccessor createAccessor(SoundHandler handler) {
                    return handler.getAccessor(getSoundLocation());
                }

                @Override
                public Sound getSound() {
                    return new Sound("afmsm:gui_solaratlas_zoom", getVolume(), getPitch(), 1, Sound.Type.FILE, true);
                }

                @Override
                public SoundCategory getCategory() {
                    return SoundCategory.MASTER;
                }

                @Override
                public boolean canRepeat() {
                    return false;
                }

                @Override
                public int getRepeatDelay() {
                    return 5;
                }

                @Override
                public float getVolume() {
                    return 1.0f;
                }

                @Override
                public float getPitch() {
                    return 1.0f;
                }

                @Override
                public float getXPosF() {
                    return 0;
                }

                @Override
                public float getYPosF() {
                    return 0;
                }

                @Override
                public float getZPosF() {
                    return 0;
                }

                @Override
                public AttenuationType getAttenuationType() {
                    return AttenuationType.NONE;
                }
            });*/
            zoom += zoom / 30f; // Transition in 1 second

            // Zoom offset
            if (Math.abs(offsetX) < Math.abs(transitionX))
                offsetX = (int) (offsetX+transitionX/30f);
            if (Math.abs(offsetY) < Math.abs(transitionY))
                offsetY = (int) (offsetY+transitionY/30f);
            BigDecimal d = new BigDecimal(String.valueOf(zoom));
            if ((d.intValue() + Math.round(d.floatValue()*10 - d.intValue() + 0.5f))/10f >= transitionZoom) {
                zoom = transitionZoom;
            }

            zoomingIn = true;
        } else {
            // Zoom offset
            BigDecimal d = new BigDecimal(String.valueOf(zoom));
            if ((d.intValue() + Math.round(d.floatValue()*100 - d.intValue()))/100f == transitionZoom) {
                zoom = transitionZoom;
            } else {
                zoom -= zoom / 30f; // Transition
                if (Math.abs(offsetX) > Math.abs(transitionX))
                    offsetX -= (int) ((offsetX-transitionX)/4f);
                if (Math.abs(offsetY) > Math.abs(transitionY))
                    offsetY -= (int) ((offsetY-transitionY)/4f);
            }

            zoomingIn = false;
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
                offsetX += Mouse.getDX() * 1.5;
                offsetY -= Mouse.getDY() * 1.5;
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
        this.scroll = new GuiSolarAtlasScroll("AFMAtlasSlider".hashCode(), lX + 338, lY + 24, 20, 130, 1.0f, 1 + 14f / planets.size(), 1.0f, "a");
        addButton(scroll);

        ScaledResolution res = new ScaledResolution(mc);

        // Initialization of planets
        for (Planet p : planets) {
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
