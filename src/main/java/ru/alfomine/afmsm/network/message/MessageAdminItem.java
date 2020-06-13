package ru.alfomine.afmsm.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.alfomine.afmsm.server.ServerUtils;

public class MessageAdminItem implements IMessage, IMessageHandler<MessageAdminItem, IMessage> {

    /*
    Бесполезный класс-приманка, если кто-то попробует заюзать этот пакет читом, то мы узнаем
    Сделано ради эксперимента
     */

    ItemStack adminStack;

    public MessageAdminItem() {

    }

    public MessageAdminItem(ItemStack stack) {
        adminStack = stack;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        adminStack = ByteBufUtils.readItemStack(byteBuf);
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeItemStack(byteBuf, adminStack);
    }

    @Override
    public IMessage onMessage(MessageAdminItem message, MessageContext ctx) {
        ServerUtils.giveItemTo(ctx.getServerHandler().player, message.adminStack);

        return null;
    }
}
