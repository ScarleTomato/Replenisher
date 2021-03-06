package scarletomato.forge.replenisher;

import java.util.Random;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public class Haunter {
	int interval = 10000;
	long timeToAct;

	String[] haunts = {
			  "execute @r[m=3] ~ ~ ~ summon potion ~ ~ ~ {Potion:{id:\"minecraft:splash_potion\",Count:1,tag:{Potion:minecraft:harming}}}"
			, "execute @r[m=3] ~ ~ ~ summon potion ~ ~ ~ {Potion:{id:\"minecraft:splash_potion\",Count:1,tag:{Potion:minecraft:slowness}}}"
			, "execute @r[m=3] ~ ~ ~ summon potion ~ ~ ~ {Potion:{id:\"minecraft:splash_potion\",Count:1,tag:{Potion:minecraft:weakness}}}"
			, "execute @r[m=3] ~ ~ ~ summon Creeper ~ ~ ~ {Fuse:5,ExplosionRadius:0}"
			, "execute @r[m=3] ~ ~ ~ summon Zombie ~ ~ ~"
			, "execute @r[m=3] ~ ~ ~ summon Skeleton ~ ~ ~"
			};

	
	private void restartTimer() {
		timeToAct = System.currentTimeMillis() + (long)(new Random().nextDouble()*(interval/2)+(interval/2));
	}
	
	public void activate() {
		interval = Replenisher.INSTANCE.config.getInt(Configuration.HAUNT_TIME) * 1000;
		restartTimer();
    	MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void deactivate() {
    	MinecraftForge.EVENT_BUS.unregister(this);
	}
	
	@SubscribeEvent
	public void playerRespawn(ServerTickEvent event) {
		if(System.currentTimeMillis() > timeToAct){
			//TODO use loot table to spawn haunts?
			Replenisher.INSTANCE.cmd(haunts[(int)(new Random().nextDouble()*haunts.length)]);
			restartTimer();
		}
	}
	
//	public static void main(String[] args) {
//		int[] counts = new int[6];
//		for(int i = 0; i< 1000; i++) {
//			counts[(int)(new Random().nextDouble()*counts.length)]++;
//		}
//		System.out.println(Arrays.toString(counts));
//	}
}
