package ru.alfomine.afmsm.client.gui;

import cr0s.warpdrive.render.RenderSpaceSky;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import ru.alfomine.afmsm.client.gui.api.CustomButton;
import ru.alfomine.afmsm.client.gui.api.CustomGui;
import ru.alfomine.afmsm.space.Planet;
import ru.alfomine.afmsm.space.Space;

import java.io.IOException;
import java.util.List;

public class GuiPlanetSelection extends CustomGui {

    public static boolean active = false;

    private ResourceLocation mainGuiLoc = new ResourceLocation("afmsm", "textures/gui_selection.png");
    public GuiSlotPlanetSelection list;
    public int selected = -1;
    Space space;
    private int lWidth = 262;
    private int lHeight = 512;

    private int test = 0;

    public GuiPlanetSelection(Space space) {
        this.space = space;
    }

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawRect(0, 0, width, height, 0xFF000000);
		GL11.glColor3f(1, 1, 1);

		// =======3d stuff====== //

		if (selected != -1) {
			GL11.glPushMatrix();

			test ++;

			if (test >= 359) {
				test = 0;
			}

			mc.renderEngine.bindTexture(space.planets.get(selected).iconResource);

			int sX = 50;
			int sY = 50;
			double sZ = 0.5;
			int sU = 0;
			int sV = 0;
			int sWidth = 200;
			int sHeight = 200;
			int rr = 100;

			float f = 1.0F / sWidth;
			float f1 = 1.0F / sHeight;

			Matrix4f camMatrix = new Matrix4f();
			Matrix4f.translate(new Vector3f(0.0F, 0.0F, -9000.0F), camMatrix, camMatrix);
			Matrix4f viewMatrix = new Matrix4f();
			viewMatrix.m00 = 2.0F / width;
			viewMatrix.m11 = 2.0F / -height;
			viewMatrix.m22 = -2.0F / 9000.0F;
			viewMatrix.m30 = -1.0F;
			viewMatrix.m31 = 1.0F;
			viewMatrix.m32 = -2.0F;

			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GlStateManager.disableFog();
			GlStateManager.disableAlpha();
			GlStateManager.enableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();

			bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
			bufferbuilder.pos(rr, -rr, rr).tex(0.0D, 0.0D).endVertex();
			bufferbuilder.pos(rr, -rr, -rr).tex(0.0D, 1).endVertex();
			bufferbuilder.pos(-rr, -rr, -rr).tex(1, 1).endVertex();
			bufferbuilder.pos(-rr, -rr, rr).tex(1, 0.0D).endVertex();
			tessellator.draw();

			GlStateManager.enableTexture2D();
			GlStateManager.enableAlpha();

			GL11.glPopMatrix();
		}

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
		return true;
	}

	public void setSelected(int slotIdx) {
		selected = slotIdx;

		buttonList.get(0).enabled = slotIdx != -1;
	}
}
