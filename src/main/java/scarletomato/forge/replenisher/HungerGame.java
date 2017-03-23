package scarletomato.forge.replenisher;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

public class HungerGame implements Runnable {
	private MinecraftServer server;
	List<Cage> cages = new LinkedList<>();

	public HungerGame(MinecraftServer server) {
		this.server = server;
	}

	@Override
	public void run() {
		say("Moving players in 10 seconds");
		Replenisher.gamestate = 1;
		sleep(10000);
		Replenisher.gamestate = 2;
		distributePlayers();
		say("The Hunger Games begin in 10 seconds");
		sleep(10000);
		say("Begin!");
		release();
		Replenisher.gamestate = 3;
		Replenisher.haunter.activate();
	}
	
	private void release() {
		for(Cage c : cages) {
			c.destroy();
		}
		cages.clear();
	}
	
	private void distributePlayers() {
		for(EntityPlayerMP pl : server.getPlayerList().getPlayers()){
			BlockPos pos = Replenisher.gameSpawns.poll();
			cages.add(new Cage(pl.getEntityWorld(), pos).create());
			pl.setGameType(Replenisher.livingMode);
			Replenisher.tp(pl, pos);
			Replenisher.gameSpawns.add(pos);
		}
	}

	private void say(String msg) {
		server.commandManager.executeCommand(server, "say " + msg);
	}

	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
