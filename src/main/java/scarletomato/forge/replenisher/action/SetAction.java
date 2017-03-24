package scarletomato.forge.replenisher.action;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameType;
import scarletomato.forge.replenisher.Configuration;
import scarletomato.forge.replenisher.Replenisher;

public class SetAction extends CommandAction {
	String configItem;
	String value;

	public SetAction(MinecraftServer server, ICommandSender sender) {
		super(server, sender);
	}

	@Override
	public CommandAction setArguments(String[] args) {
		if(args.length > 1) {
			configItem = args[1];
		}
		if(args.length > 2) {
			value = args[2];
		}
		return this;
	}

	@Override
	public CommandAction execute() {
		if("haunt".equalsIgnoreCase(configItem)) {
			Replenisher.INSTANCE.config.set(Configuration.HAUNT_TIME, value);
		} else if("livingMode".equalsIgnoreCase(configItem)) {
			Replenisher.INSTANCE.config.set(Configuration.LIVING_MODE, value);
		} else if("save".equalsIgnoreCase(configItem)) {
			Replenisher.INSTANCE.config.save();
		}
		return this;
	}
	
}
