package ru.alfomine.afmsm.network.message;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.alfomine.afmsm.AFMSpaceUnionMod;
import ru.alfomine.afmsm.space.Planet;
import ru.alfomine.afmsm.space.PlanetDifficulty;
import ru.alfomine.afmsm.space.Space;

import java.util.ArrayList;
import java.util.List;

public class MessagePlanetaryGui implements IMessage, IMessageHandler<MessagePlanetaryGui, IMessage> {

    public Space space;
    public int gui;

    public MessagePlanetaryGui() {

    }

    public MessagePlanetaryGui(Space space, int gui) {
        this.space = space;
        this.gui = gui;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound compound = ByteBufUtils.readTag(buf);

        space = new Space(compound.getCompoundTag("space"));
        gui = compound.getInteger("gui");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setTag("space", space.writeToNBT(new NBTTagCompound()));
        compound.setInteger("gui", gui);

        ByteBufUtils.writeTag(buf, compound);
    }

    @Override
    public IMessage onMessage(MessagePlanetaryGui message, MessageContext ctx) {
        AFMSpaceUnionMod.proxy.planetaryGuiMessage(message.space, message.gui);

        return null;
    }
}
