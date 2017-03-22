package scarletomato.forge.replenisher.action;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.WorldServer;

public class CountAction extends CommandAction {

	public CountAction(MinecraftServer server, ICommandSender sender) {
		super(server, sender);
	}

	@Override
	public CommandAction execute() {
		List<TileEntityChest> tes = countAllChestsOnServer(server);
		say("found %s chests", tes.size());
		for(TileEntity te : tes) {
			say(te.getBlockType().toString() + ":" + te.getPos().toString());
		}
		return this;
	}

	@Override
	public CommandAction setArguments(String[] args) {
		return this;
	}

	static List<TileEntityChest> countAllChestsOnServer(MinecraftServer server) {
		List<TileEntityChest> count = new LinkedList<>();
		for(WorldServer ws : server.worlds) {
			for(TileEntity te : ws.loadedTileEntityList) {
				if(te instanceof TileEntityChest) {
					count.add((TileEntityChest) te);
				}
			}
		}
		return count;
	}
}
