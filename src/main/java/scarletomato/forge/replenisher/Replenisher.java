package scarletomato.forge.replenisher;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Replenisher.MODID, version = Replenisher.VERSION, acceptableRemoteVersions="*", name = "Replenisher")
public class Replenisher
{
    public static final String MODID = "replenisher";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
    	event.registerServerCommand(new ReplenishCmd());
    }
}
