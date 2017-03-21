package scarletomato.forge.replenisher.action;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class HelpAction extends CommandAction {

	public HelpAction(MinecraftServer server, ICommandSender sender) {
		super(server, sender);
	}

	@Override
	public CommandAction setArguments(String[] args) {
		return this;
	}

	@Override
	public CommandAction execute() {
		say("hey");
		return this;
	}
	
}
