package scarletomato.forge.replenisher;
import java.util.LinkedList;
import java.util.List;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventLogger {
	
	private static final Logger LOG = LogManager.getLogger(EventLogger.class);

	private List<Class<?>> eventClasses = new LinkedList<>();

	@SubscribeEvent
	public void onEvent(Event event) {
		Class<?> eventClass = event.getClass();
		if(!eventClasses.contains(eventClass)){
			LOG.info("Event: " + event.getClass());
			eventClasses.add(eventClass);
		}
    	
//    	for(ResourceLocation k : Potion.REGISTRY.getKeys()){
//    		System.out.println(k);
//    	}
	}

	public void register() {
    	//Most events get posted to this bus
    	MinecraftForge.EVENT_BUS.register(this);
    	//Most world generation events happen here, such as Populate, Decorate, etc., with the strange exception that Pre and Post events are on the regular EVENT_BUS
    	MinecraftForge.TERRAIN_GEN_BUS.register(this);
    	//Ore generation, obviously
    	MinecraftForge.ORE_GEN_BUS.register(this);
	}

}