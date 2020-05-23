package ru.alfomine.afmsm;

import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;
import ru.alfomine.afmsm.gui.GuiPlanetSelection;
import ru.alfomine.afmsm.keybinds.KeyBindDebug;
import ru.alfomine.afmsm.keybinds.KeyBindMinigame;
import ru.alfomine.afmsm.keybinds.KeyBinder;
import ru.alfomine.afmsm.keybinds.KeyBinder.KeyBind;


@Mod(
modid = AFMSpaceUnionMod.MOD_ID,
name = AFMSpaceUnionMod.MOD_NAME,
version = AFMSpaceUnionMod.VERSION
)
public class AFMSpaceUnionMod {
	
	public static final String MOD_ID = "afmsm";
	public static final String MOD_NAME = "AFMSpaceUnionMod";
	public static final String VERSION = "2019.3-1.3.2";
	
	@Mod.Instance(MOD_ID)
	public static AFMSpaceUnionMod INSTANCE;
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		KeyBinder.register(new KeyBindDebug());
		KeyBinder.register(new KeyBindMinigame());
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		for (KeyBind keyBind : KeyBinder.keyBinds) {
			if (Keyboard.isKeyDown(keyBind.getBinding().getKeyCode())) {
				keyBind.onPress(event);
			}
		}
	}
}
