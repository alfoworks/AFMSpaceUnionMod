package ru.alfomine.afmsm.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePlanetaryGui implements IMessage, IMessageHandler<MessagePlanetaryGui, IMessage> {
	
	public MessagePlanetaryGui() {
	
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
	
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
	
	}
	
	@Override
	public IMessage onMessage(MessagePlanetaryGui message, MessageContext ctx) {
		return null;
	}
}
