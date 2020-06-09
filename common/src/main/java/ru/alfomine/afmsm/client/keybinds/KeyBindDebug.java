package ru.alfomine.afmsm.client.keybinds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;
import ru.alfomine.afmsm.client.PlanetData;
import ru.alfomine.afmsm.client.gui.GuiPlanetSelection;
import ru.alfomine.afmsm.client.keybinds.KeyBinder.KeyBind;

import java.util.ArrayList;

public class KeyBindDebug implements KeyBind {
	
	@Override
	public void onPress(KeyInputEvent event) {
		ArrayList<PlanetData> testList = new ArrayList<>();
		
		testList.add(new PlanetData("earth", 0));
		testList.add(new PlanetData("moon", 1));
		testList.add(new PlanetData("mars", 1));
		testList.add(new PlanetData("venus", 2));
		
		Minecraft.getMinecraft().displayGuiScreen(new GuiPlanetSelection(testList));
	}
	
	@Override
	public KeyBinding getBinding() {
		return new KeyBinding("DebugKey", Keyboard.KEY_NUMPAD8, "AFMSM");
	}
}
