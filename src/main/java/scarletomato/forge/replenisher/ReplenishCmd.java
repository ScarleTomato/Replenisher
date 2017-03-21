package scarletomato.forge.replenisher;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import scala.actors.threadpool.Arrays;
import scarletomato.forge.replenisher.action.CommandAction;

public class ReplenishCmd implements ICommand {

	@Override
	public int compareTo(ICommand other) {
		return getCommandName().compareTo(other.getCommandName());
	}

	@Override
	public String getCommandName() {
		return "replenish";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "";
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getCommandAliases() {
		return Arrays.asList(new String[]{"repl", "r"});
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		CommandAction.getAction(server, sender, args).execute();
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
}
