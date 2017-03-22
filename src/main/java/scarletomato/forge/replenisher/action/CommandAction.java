package scarletomato.forge.replenisher.action;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public abstract class CommandAction {
	ICommandSender sender;
	MinecraftServer server;
	
	public CommandAction(MinecraftServer server, ICommandSender sender) {
		this.server = server;
		this.sender = sender;
	}

	public abstract CommandAction setArguments(String[] args);
	public abstract CommandAction execute();

	public static CommandAction getAction(MinecraftServer server, ICommandSender sender, String[] args) {
		if(args.length == 0){
			return new HelpAction(server, sender);
		} if("count".equalsIgnoreCase(args[0])) {
			return new CountAction(server, sender).setArguments(args);
		} else if("fill".equalsIgnoreCase(args[0])) {
			return new FillAction(server, sender).setArguments(args);
		} else {
			return new HelpAction(server, sender);
		}
	}
	
	public void say(String format, Object... args) {
		sender.sendMessage(new TextComponentString(String.format(format, args)));
	}
}
