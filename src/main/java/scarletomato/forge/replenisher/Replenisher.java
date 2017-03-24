package scarletomato.forge.replenisher;

import java.util.HashMap;

import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

@Mod(modid = Replenisher.MODID, version = Replenisher.VERSION, acceptableRemoteVersions="*", name = "Replenisher")
public class Replenisher
{
	public static final SoundEvent WITHER_SOUND = SoundEvent.REGISTRY.getObject(new ResourceLocation("minecraft", "entity.wither.spawn"));
	public static Replenisher INSTANCE;
    public static final String MODID = "replenisher";
    public static final String VERSION = "1.0";
	private MinecraftServer server;
	public static HungerGame runningGame;
	public static final Resurrector resurrector = new Resurrector();
	public static final Haunter haunter = new Haunter();


	HashMap<World, HashMap<BlockPos, Long>> worldStamps = new HashMap<>();
	public Configuration config;
	private String lootTable;
	
	public Replenisher() {
		if(null != INSTANCE) {
			throw new RuntimeException("Replenisher tried to start a second instance");
		}
		INSTANCE = this;
	}
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	config = new Configuration(event.getSuggestedConfigurationFile());
    	config.save();
    	lootTable = config.getString(Configuration.CHEST_LOOT_TABLE);
//    	new EventLogger().register();
    	MinecraftForge.EVENT_BUS.register(this);
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
    	server = event.getServer();
    	event.registerServerCommand(new ReplenishCmd()); 
    }

	@SubscribeEvent
	public void playerRespawn(PlayerRespawnEvent event) {
		event.player.setGameType(GameType.SPECTATOR);
		event.player.world.playSound(null, event.player.getPosition(), WITHER_SOUND, SoundCategory.HOSTILE, Float.MAX_VALUE, Float.MAX_VALUE/2);
	}

	public static void tp(EntityPlayer player, BlockPos pos) {
		player.attemptTeleport(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5);
	}

	@SubscribeEvent
	public void playerOpenContainer(PlayerInteractEvent.RightClickBlock event) {
		TileEntity te = event.getWorld().getTileEntity(event.getPos());
		if(null != te && te instanceof TileEntityChest) {
			refill((TileEntityChest) te);
		}
	}

	@SubscribeEvent
	public void playerPlaced(BlockEvent.MultiPlaceEvent event) {
		if(event.getPlacedBlock().getBlock() instanceof BlockChest) {
			touchChest(event.getWorld(), event.getPos());
		}
	}
	
	HashMap<BlockPos, Long> getOrCreate(World world) {
		if(!worldStamps.containsKey(world)) {
			worldStamps.put(world, new HashMap<BlockPos, Long>());
		}
		return worldStamps.get(world);
	}
	
	void touchChest(World world, BlockPos pos) {
		getOrCreate(world).put(pos, System.currentTimeMillis() + 300000L);
	}
	
	void refill(TileEntityChest chest) {
		BlockPos pos = chest.getPos();
		Long exp = getOrCreate(chest.getWorld()).get(pos);
		if(exp == null || exp < System.currentTimeMillis()) {
			touchChest(chest.getWorld(), pos);
			chest.clear();
			chest.setLootTable(new ResourceLocation(lootTable), System.currentTimeMillis());
		}
	}
	
	public static String toString(BlockPos pos) {
		return String.format("%s,%s,%s", pos.getX(), pos.getY(), pos.getZ());
	}
	
	public static BlockPos fromString(String str) {
		String[] strs = str.split(",");
		return new BlockPos(Integer.valueOf(strs[0]), Integer.valueOf(strs[1]), Integer.valueOf(strs[2]));
	}
	
	public void cmd(String format, Object... args) {
		String cmd = String.format(format, args);
		System.out.println(cmd);
		server.getCommandManager().executeCommand(server, cmd);
	}
}
