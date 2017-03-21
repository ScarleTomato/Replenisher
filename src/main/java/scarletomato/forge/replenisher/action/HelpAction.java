package scarletomato.forge.replenisher.action;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class HelpAction extends CommandAction {

	@Override
	public CommandAction setArguments(String[] args) {
		return this;
	}

	@Override
	public CommandAction execute(MinecraftServer server, ICommandSender sender) {
		sender.addChatMessage(new TextComponentString("hey"));
		return this;
	}
	
}
