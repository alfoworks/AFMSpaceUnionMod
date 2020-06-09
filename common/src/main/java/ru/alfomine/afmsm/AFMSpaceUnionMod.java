package ru.alfomine.afmsm;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import ru.alfomine.afmsm.client.gui.GuiPlanetSelection;
import ru.alfomine.afmsm.client.keybinds.KeyBindDebug;
import ru.alfomine.afmsm.client.keybinds.KeyBindMinigame;
import ru.alfomine.afmsm.client.keybinds.KeyBinder;
import ru.alfomine.afmsm.client.keybinds.KeyBinder.KeyBind;
import ru.alfomine.afmsm.network.AFMSMPacketHandler;
import ru.alfomine.afmsm.proxy.IProxy;


@Mod(
modid = AFMSpaceUnionMod.MOD_ID,
name = AFMSpaceUnionMod.MOD_NAME,
version = AFMSpaceUnionMod.VERSION
)
public class AFMSpaceUnionMod {
	
	public static final String MOD_ID = "afmsm";
	public static final String MOD_NAME = "AFMSpaceUnionMod";
	public static final String VERSION = "2019.3-1.3.2";
	public static final String CLIENT = "ru.alfomine.afmsm.proxy.ClientProxy";
	public static final String SERVER = "ru.alfomine.afmsm.proxy.ServerProxy";
	@Mod.Instance(MOD_ID)
	public static AFMSpaceUnionMod INSTANCE;
	@SidedProxy(clientSide = CLIENT, serverSide = SERVER)
	public static IProxy proxy;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ModItems());
		MinecraftForge.EVENT_BUS.register(this);
		
		ModItems.init();
		AFMSMPacketHandler.init();
		
		proxy.preInit(event);
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		KeyBinder.register(new KeyBindDebug());
		KeyBinder.register(new KeyBindMinigame());
		
		proxy.postInit(event);
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		for (KeyBind keyBind : KeyBinder.keyBinds) {
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
		if (GuiPlanetSelection.active && (event.getType() == ElementType.HOTBAR || event.getType() == ElementType.CROSSHAIRS)) {
			event.setCanceled(true);
		}
	}
}