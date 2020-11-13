package ru.alfomine.afmsm.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import ru.alfomine.afmsm.network.AFMSMPacketHandler;
import ru.alfomine.afmsm.network.message.MessagePlanetaryGui;
import ru.alfomine.afmsm.space.SpaceManager;

@SuppressWarnings("NullableProblems")
public class CommandPlanetSelectionGui extends CommandBase {
	
	@Override
	public String getName() {
		return "psgui";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "psgui";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayerMP)) {
			sender.sendMessage(new TextComponentString("Пошел нахуй!"));
			
			return;
		}
		
		AFMSMPacketHandler.INSTANCE.sendTo(new MessagePlanetaryGui(SpaceManager.getSpace(false), 1), (EntityPlayerMP) sender);
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}
}
