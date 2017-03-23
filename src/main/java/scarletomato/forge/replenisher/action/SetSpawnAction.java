package scarletomato.forge.replenisher.action;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import scarletomato.forge.replenisher.Replenisher;

public class SetSpawnAction extends CommandAction {
	
	String predicate;

	public SetSpawnAction(MinecraftServer server, ICommandSender sender) {
		super(server, sender);
	}

	@Override
	public CommandAction setArguments(String[] args) {
		if(args.length > 1) {
			predicate = args[1];
		}
		return this;
	}

	@Override
	public CommandAction execute() {
		if(null==predicate) {
			Replenisher.spawnPoint = sender.getPosition();
		} else if ("clear".equalsIgnoreCase(predicate)) {
			Replenisher.spawnPoint = null;
			Replenisher.gameSpawns.clear();
		} else if ("g".equalsIgnoreCase(predicate)) {
			Replenisher.gameSpawns.add(sender.getPosition());
		}
		return this;
	}

}
