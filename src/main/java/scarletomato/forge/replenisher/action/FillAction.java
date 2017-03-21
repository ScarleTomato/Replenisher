package scarletomato.forge.replenisher.action;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;

public class FillAction extends CommandAction {
	public FillAction(MinecraftServer server, ICommandSender sender) {
		super(server, sender);
	}

	String lootTable = "replenisher:chests/start";

	@Override
	public CommandAction setArguments(String[] args) {
		if(args.length > 1) {
			lootTable = args[1];
		}
		return this;
	}

	@Override
	public CommandAction execute() {
		List<TileEntityChest> chests = CountAction.countAllChestsOnServer(server);
		say("Filling %s chests with loot from %s", chests.size(), lootTable);
		for(TileEntityChest chest : chests) {
			BlockPos pos = chest.getPos();
			server.getCommandManager().executeCommand(sender, String.format("setblock %s %s %s minecraft:chest 0 replace {LootTable:\"%s\"}", pos.getX(), pos.getY(), pos.getZ(), lootTable));
		}
		return this;
	}

}
