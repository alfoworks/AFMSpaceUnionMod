package ru.alfomine.afmsm.keybinds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;
import ru.alfomine.afmsm.gui.CustomGui;
import ru.alfomine.afmsm.gui.GuiMinigame;
import ru.alfomine.afmsm.keybinds.KeyBinder.KeyBind;

public class KeyBindMinigame implements KeyBind {
	
	@Override
	public void onPress(KeyInputEvent event) {
		CustomGui screen = new GuiMinigame();
		
		Minecraft.getMinecraft().displayGuiScreen(screen);
		MinecraftForge.EVENT_BUS.register(screen);
	}
	
	@Override
	public KeyBinding getBinding() {
		return new KeyBinding("MiniGame", Keyboard.KEY_NUMPAD7, "AFMSM");
	}
}
