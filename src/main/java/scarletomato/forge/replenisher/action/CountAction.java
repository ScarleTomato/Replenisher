package scarletomato.forge.replenisher.action;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;

public class CountAction extends CommandAction {

	@Override
	public CommandAction execute(MinecraftServer server, ICommandSender sender) {
		for(TileEntity te : countAllChestsOnServer(server)) {
			
			sender.addChatMessage(new TextComponentString(te.getBlockType().toString() + ":" + te.getPos().toString()));
		}
		return this;
	}

	@Override
	public CommandAction setArguments(String[] args) {
		return this;
	}

	List<TileEntity> countAllChestsOnServer(MinecraftServer server) {
		List<TileEntity> count = new LinkedList<>();
		for(WorldServer ws : server.worldServers) {
			for(TileEntity te : ws.loadedTileEntityList) {
				count.add(te);
			}
		}
		return count;
	}
}
