package scarletomato.forge.replenisher;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;

public class HungerGame implements Runnable {
	private MinecraftServer server;
	List<Cage> cages = new LinkedList<>();

	public HungerGame(MinecraftServer server) {
		this.server = server;
	}

	@Override
	public void run() {
		say("Moving players in 10 seconds");
		sleep(10000);
		distributePlayers();
		say("The Hunger Games begin in 10 seconds");
		sleep(10000);
		say("Begin!");
		release();
		Replenisher.haunter.activate();
	}
	
	private void release() {
		for(Cage c : cages) {
			c.destroy();
		}
		cages.clear();
	}
	
	private void distributePlayers() {
		GameType livingMode = GameType.parseGameTypeWithDefault(Replenisher.INSTANCE.config.getString(Configuration.LIVING_MODE), GameType.ADVENTURE);
		Queue<String> spawns = new LinkedList<>(Replenisher.INSTANCE.config.getStringList(Configuration.GAME_SPAWNS));
		
		for(EntityPlayerMP pl : server.getPlayerList().getPlayers()){
			String s = spawns.poll();
			BlockPos pos = Replenisher.fromString(s);
			cages.add(new Cage(pl.getEntityWorld(), pos).create());
			pl.setGameType(livingMode);
			Replenisher.tp(pl, pos);
			spawns.add(s);
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
