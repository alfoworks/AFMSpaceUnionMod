package ru.alfomine.afmsm;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.alfomine.afmsm.command.CommandPlanetSelectionGui;
import ru.alfomine.afmsm.network.AFMSMPacketHandler;
import ru.alfomine.afmsm.proxy.IProxy;
import ru.alfomine.afmsm.server.PlanetConfig;


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
	public static Logger logger = LogManager.getLogger(MOD_ID);
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ModItems());
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(proxy);

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
		try {
			PlanetConfig.init();
		} catch (NoClassDefFoundError ignored) {

		}

		proxy.postInit(event);
	}
	
	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandPlanetSelectionGui());
	}
}
