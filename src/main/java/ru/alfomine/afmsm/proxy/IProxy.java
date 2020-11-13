package ru.alfomine.afmsm.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.alfomine.afmsm.network.message.MessagePlanetaryGui;
import ru.alfomine.afmsm.space.Space;

public interface IProxy {

	public void preInit(FMLPreInitializationEvent event);

	public void init(FMLInitializationEvent event);

	public void postInit(FMLPostInitializationEvent event);

	public void planetaryGuiMessage(Space space, int gui);
}
