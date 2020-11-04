package ru.alfomine.afmsm.network.message;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.alfomine.afmsm.AFMSpaceUnionMod;
import ru.alfomine.afmsm.planet.Planet;
import ru.alfomine.afmsm.planet.PlanetDifficulty;

import java.util.ArrayList;
import java.util.List;

public class MessagePlanetaryGui implements IMessage, IMessageHandler<MessagePlanetaryGui, IMessage> {

    public List<Planet> planets;
    public int gui;
    public int spaceSize;

    public MessagePlanetaryGui() {

    }

    public MessagePlanetaryGui(List<Planet> planets, int gui, int spaceSize) {
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

            planets.add(new Planet(
                    new ResourceLocation(jsonPlanet.get("iconResource").getAsString()),
                    jsonPlanet.get("name").getAsString(),
                    jsonPlanet.get("warpId").getAsString(),
                    PlanetDifficulty.values()[jsonPlanet.get("difficulty").getAsInt()],
                    jsonPlanet.get("size").getAsInt()));
        }

        gui = buf.readInt();
        spaceSize = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        JsonArray jsonPlanets = new JsonArray();

        for (Planet planet : planets) {
            JsonObject jsonPlanet = new JsonObject();

            jsonPlanet.addProperty("iconResource", planet.iconResource.toString());
            jsonPlanet.addProperty("name", planet.name);
            jsonPlanet.addProperty("warpId", planet.warpId);
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
        AFMSpaceUnionMod.proxy.planetaryGuiMessage(message);

        return null;
    }
}
