package ru.alfomine.afmsm.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import ru.alfomine.afmsm.network.message.MessageAdminItem;
import ru.alfomine.afmsm.network.message.MessagePlanetaryGui;

public class AFMSMPacketHandler {
	
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("afmsm");
	private static int messageId = 0;
	
	public static void init() {
		registerMessage(MessagePlanetaryGui.class, MessagePlanetaryGui.class, Side.CLIENT);
		registerMessage(MessageAdminItem.class, MessageAdminItem.class, Side.SERVER);
	}
	
	private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
		INSTANCE.registerMessage(messageHandler, requestMessageType, messageId++, side);
	}
}
