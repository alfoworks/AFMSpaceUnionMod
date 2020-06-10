package ru.alfomine.afmsm.network.message;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.alfomine.afmsm.client.gui.GuiPlanetSelection;
import ru.alfomine.afmsm.client.gui.GuiSolarAtlas;
import ru.alfomine.afmsm.planet.PlanetData;
import ru.alfomine.afmsm.planet.PlanetDifficulty;

import java.util.ArrayList;

public class MessagePlanetaryGui implements IMessage, IMessageHandler<MessagePlanetaryGui, IMessage> {
	
	private ArrayList<PlanetData> planets;
	private int gui;
	private int spaceSize;
	
	public MessagePlanetaryGui() {
	
	}
	
	public MessagePlanetaryGui(ArrayList<PlanetData> planets, int gui, int spaceSize) {
		this.planets = planets;
		this.gui = gui;
		this.spaceSize = spaceSize;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		JsonArray jsonPlanets = new Gson().fromJson(ByteBufUtils.readUTF8String(buf), JsonArray.class);
		
		planets = new ArrayList<>();
		
		for (JsonElement element : jsonPlanets) {
			JsonObject jsonPlanet = element.getAsJsonObject();
			
			planets.add(new PlanetData(
			new ResourceLocation(jsonPlanet.get("iconResource").getAsString()),
			jsonPlanet.get("name").getAsString(),
			PlanetDifficulty.values()[jsonPlanet.get("difficulty").getAsInt()],
			jsonPlanet.get("size").getAsInt()));
		}
		
		gui = buf.readInt();
		spaceSize = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		JsonArray jsonPlanets = new JsonArray();
		
		for (PlanetData planet : planets) {
			JsonObject jsonPlanet = new JsonObject();
			
			jsonPlanet.addProperty("iconResource", planet.iconResource.toString());
			jsonPlanet.addProperty("name", planet.name);
			jsonPlanet.addProperty("difficulty", planet.difficulty.ordinal());
			jsonPlanet.addProperty("size", planet.size);
			
			jsonPlanets.add(jsonPlanet);
		}
		
		ByteBufUtils.writeUTF8String(buf, new Gson().toJson(jsonPlanets));
		buf.writeInt(gui);
		buf.writeInt(spaceSize);
	}
	
	@Override
	public IMessage onMessage(MessagePlanetaryGui message, MessageContext ctx) {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen screen = null;
		
		if (message.gui == 0) {
			screen = new GuiSolarAtlas(message.planets, message.spaceSize);
		} else if (message.gui == 1) {
			screen = new GuiPlanetSelection(message.planets);
		} else {
			mc.player.sendMessage(new TextComponentString(String.format("Wrong planetary screen: %s", message.gui)));
		}
		
		GuiScreen finalScreen = screen;
		mc.addScheduledTask(() -> {
			mc.displayGuiScreen(finalScreen);
		});
		
		return null;
	}
}
