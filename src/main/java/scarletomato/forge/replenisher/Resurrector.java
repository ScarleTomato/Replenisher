package scarletomato.forge.replenisher;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Resurrector {
	private static final int TPS = 20;
	private static final int MPS = 1000;
	BlockPos resurrectionPoint;
	int radius = 5;
	long expiration;
	long notify;
	private GameType livingMode;
	private int duration;
	private static final String[] COLORS = {"black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple", "gold", "gray", "dark_gray", "blue", "green", "aqua", "red", "light_purple", "yellow", "white"};
	
	public void activate() {
		livingMode = GameType.parseGameTypeWithDefault(Replenisher.INSTANCE.config.getString(Configuration.LIVING_MODE), GameType.ADVENTURE);
		duration = Replenisher.INSTANCE.config.getInt(Configuration.RES_TIME);
		resurrectionPoint = Replenisher.fromString(Replenisher.INSTANCE.config.getString(Configuration.RES_POINT));
		
		MinecraftForge.EVENT_BUS.register(this);
    	Replenisher.INSTANCE.cmd("title @a[m=3] times 20 200 20");
    	Replenisher.INSTANCE.cmd("title @a[m=3] subtitle {\"text\":\"Head to the center to respawn\"}");
    	Replenisher.INSTANCE.cmd("title @a[m=3] title {\"text\":\"Resurrection\"}");
		restartTimer();
	}
	
	public void deactivate() {
    	MinecraftForge.EVENT_BUS.unregister(this);
	}
	
	private void restartTimer() {
		expiration = System.currentTimeMillis() + duration*MPS;
	}

	@SubscribeEvent
	public void playerResurrect(PlayerTickEvent event) {
		if(notify < System.currentTimeMillis()) {
	    	Replenisher.INSTANCE.cmd("title @a[m=3] actionbar {\"text\":\"Resurrection\",\"color\":\"%s\"}", COLORS[(int)(new Random().nextDouble()*COLORS.length)]);
			notify = System.currentTimeMillis() + MPS;
		}
		if(event.player.isSpectator() && event.player.getPosition().distanceSq(resurrectionPoint) < radius){
			event.player.setGameType(livingMode);
			potion(event.player, "glowing", 30);
			potion(event.player, "night_vision", 60);
			potion(event.player, "jump_boost", 60);
			potion(event.player, "saturation", 60);
			potion(event.player, "fire_resistance", 60);
			potion(event.player, "speed", 60);
			potion(event.player, "strength", 60);
			potion(event.player, "health_boost", 60);
			potion(event.player, "resistance", 60);
		}
		if(System.currentTimeMillis() > expiration) {
	    	deactivate();
		}
	}

	private void potion(EntityPlayer player, String pot, int duration) {
		player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("minecraft",pot)), duration*TPS));
	}
}
