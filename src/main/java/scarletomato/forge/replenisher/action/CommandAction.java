package scarletomato.forge.replenisher.action;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public abstract class CommandAction {

	public abstract CommandAction setArguments(String[] args);
	public abstract CommandAction execute(MinecraftServer server, ICommandSender sender);

	public static CommandAction getAction(String[] args) {
		if(args.length == 0){
			return new HelpAction();
		}
		if("count".equalsIgnoreCase(args[0])) {
			return new CountAction().setArguments(args);
		} else {
			return new HelpAction();
		}
	}
}
