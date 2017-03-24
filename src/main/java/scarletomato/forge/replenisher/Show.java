package scarletomato.forge.replenisher;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Show {

	long end;
	long nextStrikeTime;
	private MinecraftServer server;
	int max = 300;
	BlockPos center;
	Random r = new Random();

	public Show(long end, MinecraftServer server, BlockPos center) {
		this.server = server;
		this.end = end;
		this.center = center;
	}
	
	public void activate() {
		MinecraftForge.EVENT_BUS.register(this);
		restartTimer();
	}
	
	public void deactivate() {
    	MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void playerResurrect(PlayerTickEvent event) {
		if(System.currentTimeMillis() > nextStrikeTime) {
			strike();
			restartTimer();
		}
	}

	private void restartTimer() {
		nextStrikeTime = System.currentTimeMillis() + ((int)r.nextDouble()*20);
	}
	
	private void strike() {
		int lx = center.getX()+(int)(r.nextDouble()*max*2)-max;
		int lz = center.getY()+(int)(r.nextDouble()*max*2)-max;
		String cmd = String.format("summon minecraft:lightning_bolt %s %s %s", lx, center.getY(), lz);
		System.out.println(cmd);
		server.getCommandManager().executeCommand(server, cmd);
	}
}
