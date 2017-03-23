package scarletomato.forge.replenisher.action;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import scarletomato.forge.replenisher.Replenisher;

public class SetResAction extends CommandAction {
	
	boolean doActivate = false;
	boolean setSpawn = false;

	public SetResAction(MinecraftServer server, ICommandSender sender) {
		super(server, sender);
	}

	@Override
	public CommandAction setArguments(String[] args) {
		if(args.length > 1) {
			doActivate = "on".equalsIgnoreCase(args[1]);
			setSpawn = "set".equalsIgnoreCase(args[1]);
		}
		return this;
	}

	@Override
	public CommandAction execute() {
		if(doActivate) {
	    	Replenisher.resurrector.activate();
		} else {
	    	Replenisher.resurrector.deactivate();
		}
		if(setSpawn) {
			Replenisher.resurrector.setResPoint(sender.getPosition());
		}
		return this;
	}

}
