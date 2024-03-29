package ru.alfomine.afmsm.client.gui;

import cr0s.warpdrive.client.SkyBoxManager;
import cr0s.warpdrive.render.RenderSpaceSky;
import cr0s.warpdrive.render.skybox.ISkyBoxRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import ru.alfomine.afmsm.client.gui.api.CustomButton;
import ru.alfomine.afmsm.client.gui.api.CustomGui;

import java.io.IOException;
import java.util.List;

public class GuiSkyboxSelection extends CustomGui {
    static ResourceLocation mainGuiLoc = new ResourceLocation("afmsm", "textures/gui_selection.png");
    GuiSlotSkyboxSelection list;
    List<ISkyBoxRenderer> skyboxes;
    int selected = -1;
    private int lWidth = 262;
    private int lHeight = 512;
    List<String> warnList;

    public GuiSkyboxSelection(List<ISkyBoxRenderer> skyboxes) {
        this.skyboxes = skyboxes;
    }

    @Override
    public void initGui() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        GuiButton butt = new CustomButton(0, res.getScaledWidth() - lWidth + 7, 187, 50, 20, I18n.format("afmsm.text.gui_planetselection.button_select"));
        butt.enabled = false;

        this.selected = skyboxes.indexOf(RenderSpaceSky.getInstance().currentRenderer);

        buttonList.add(butt);

        int listY = 37;
        int listHeight = Math.min(lHeight, res.getScaledHeight() - listY);

        list = new GuiSlotSkyboxSelection(skyboxes, this, res.getScaledWidth() - 200, listY, 200, listHeight, 86, width, height);
        this.warnList = fontRenderer.listFormattedStringToWidth(I18n.format("afmsm.text.gui_skyboxselection.warntext"), 180);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        list.handleMouseInput(mouseX, mouseY);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        int lX = res.getScaledWidth() - lWidth;

        mc.renderEngine.bindTexture(mainGuiLoc);
        drawTexturedModalRect512(lX, 0, lWidth, lHeight, 0, 0, lWidth, lHeight);

        drawTexturedModalRect512(0, 0, 189, 51, 262, 274, 189, 51);

        int titleX = lX + 15;
        drawString(fontRenderer, I18n.format("afmsm.text.gui_skyboxselection.title"), titleX, 5, 0xFFFFFF);

        for (int i = 0; i < warnList.size(); i++) {
            String toDraw = warnList.get(i);

            drawString(fontRenderer, toDraw, 5, i * fontRenderer.FONT_HEIGHT + 5, 0xFFFFFF);
        }

        list.drawScreen(mouseX, mouseY, partialTicks);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void setSelected(int slotIdx) {
        selected = slotIdx;

        buttonList.get(0).enabled = slotIdx != -1;

        SkyBoxManager.setSkyBox(skyboxes.get(selected).getID());
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 0) {
            SkyBoxManager.saveSelectedSkyBox(skyboxes.get(selected).getID());

            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }
}
