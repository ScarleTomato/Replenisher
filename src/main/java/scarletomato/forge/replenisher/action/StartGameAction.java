package scarletomato.forge.replenisher.action;

import scarletomato.forge.replenisher.HungerGame;
import scarletomato.forge.replenisher.Replenisher;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class StartGameAction extends CommandAction {

	public StartGameAction(MinecraftServer server, ICommandSender sender) {
		super(server, sender);
	}

	@Override
	public CommandAction setArguments(String[] args) {
		return this;
	}

	@Override
	public CommandAction execute() {
		Replenisher.runningGame = new HungerGame(server);
		new Thread(Replenisher.runningGame).start();
		return this;
	}

}
