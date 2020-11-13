package ru.alfomine.afmsm.proxy;

import cr0s.warpdrive.client.SkyBoxManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import ru.alfomine.afmsm.client.gui.GuiPlanetSelection;
import ru.alfomine.afmsm.client.gui.GuiSkyboxSelection;
import ru.alfomine.afmsm.client.gui.GuiSolarAtlas;
import ru.alfomine.afmsm.client.keybinds.KeyBinder;
import ru.alfomine.afmsm.network.message.MessagePlanetaryGui;
import ru.alfomine.afmsm.space.Space;

import java.util.ArrayList;

public class ClientProxy implements IProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void planetaryGuiMessage(Space space, int gui) {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen screen = null;

		if (gui == 0) {
			screen = new GuiSolarAtlas(space.planets, space.spaceSize);
		} else if (gui == 1) {
			screen = new GuiPlanetSelection(space.planets);
		} else {
			mc.player.sendMessage(new TextComponentString(String.format("Wrong planetary screen: %s", gui)));
		}

		GuiScreen finalScreen = screen;
		mc.addScheduledTask(() -> mc.displayGuiScreen(finalScreen));
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		for (KeyBinder.KeyBind keyBind : KeyBinder.keyBinds) {
			if (Keyboard.isKeyDown(keyBind.getBinding().getKeyCode())) {
				keyBind.onPress(event);
			}
		}
	}

	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent.Chat event) {
		if (GuiPlanetSelection.active) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent event) {
		if (GuiPlanetSelection.active && (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR || event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onScreenInit(GuiScreenEvent.InitGuiEvent.Post event) {
		if (!(event.getGui() instanceof GuiOptions)) return;

		event.getButtonList().add(new GuiButton(1337, 0, event.getGui().height - 20, 100, 20, "WarpDrive Skybox"));
	}

	@SubscribeEvent
	public void onGuiScreenAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
		if (event.getButton().id != 1337) return;

		Minecraft.getMinecraft().displayGuiScreen(new GuiSkyboxSelection(new ArrayList<>(SkyBoxManager.skyboxes.values())));
	}
}
