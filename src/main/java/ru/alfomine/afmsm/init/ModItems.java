package ru.alfomine.afmsm.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.alfomine.afmsm.item.ItemSolarAtlas;

import java.util.Objects;

public class ModItems {

	public static Item itemSolarAtlas;

	public static void init() {
		itemSolarAtlas = new ItemSolarAtlas("solaratlas");
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(itemSolarAtlas);
	}

	@SubscribeEvent
	public void registerRender(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(itemSolarAtlas, 0, new ModelResourceLocation(Objects.requireNonNull(itemSolarAtlas.getRegistryName()), "inventory"));
	}
}
