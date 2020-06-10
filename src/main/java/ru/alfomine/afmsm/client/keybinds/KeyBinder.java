package ru.alfomine.afmsm.client.keybinds;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.ArrayList;
import java.util.List;

public class KeyBinder {
	
	public static List<KeyBind> keyBinds = new ArrayList<>();
	
	public static void register(KeyBind keyBind) {
		keyBinds.add(keyBind);
		ClientRegistry.registerKeyBinding(keyBind.getBinding());
	}
	
	public interface KeyBind {
		
		void onPress(InputEvent.KeyInputEvent event);
		
		KeyBinding getBinding();
	}
}