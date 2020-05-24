package ru.alfomine.afmsm.keybinds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;
import ru.alfomine.afmsm.MenuPlanet;
import ru.alfomine.afmsm.gui.GuiPlanetSelection;
import ru.alfomine.afmsm.keybinds.KeyBinder.KeyBind;

import java.util.ArrayList;

public class KeyBindDebug implements KeyBind {
	
	@Override
	public void onPress(KeyInputEvent event) {
		ArrayList<MenuPlanet> testList = new ArrayList<>();
		
		testList.add(new MenuPlanet("earth", 0));
		testList.add(new MenuPlanet("moon", 1));
		testList.add(new MenuPlanet("mars", 2));
		testList.add(new MenuPlanet("123", 0));
		
		Minecraft.getMinecraft().displayGuiScreen(new GuiPlanetSelection(testList));
	}
	
	@Override
	public KeyBinding getBinding() {
		return new KeyBinding("DebugKey", Keyboard.KEY_NUMPAD8, "AFMSM");
	}
}
