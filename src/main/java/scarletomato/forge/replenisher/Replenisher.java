package scarletomato.forge.replenisher;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

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
import net.minecraftforge.common.config.Configuration;
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
	public static Replenisher INSTANCE;
    public static final String MODID = "replenisher";
    public static final String VERSION = "1.0";
	private MinecraftServer server;
	SoundEvent witherSound;
	public static BlockPos spawnPoint;
	private Queue<BlockPos> gameSpawns = new LinkedList<>();
	public static HungerGame runningGame;
	public static GameType livingMode = GameType.SURVIVAL;
	public static final Resurrector resurrector = new Resurrector();
	public static final Haunter haunter = new Haunter();
	
	/**
	 * State of the hunger game<br/>
	 * 0 = off<br/>
	 * 1 = players in lobby, waiting to distribute<br/>
	 * 2 = moved to starting positions, waiting to begin. Players can't move<br/>
	 * 3 = game running - resurrection is off<br/>
	 * 4 = game running - resurrection is on
	 * 
	 */
	public static int gamestate = 0;

	String lootTable = "replenisher:chest";
	HashMap<World, HashMap<BlockPos, Long>> worldStamps = new HashMap<>();
	private Configuration config;
	
	public Replenisher() {
		if(null != INSTANCE) {
			throw new RuntimeException("Replenisher tried to start a second instance");
		}
		INSTANCE = this;
	}
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	loadConfig(event.getSuggestedConfigurationFile());
    	new EventLogger().register();
    	//Most events get posted to this bus
    	MinecraftForge.EVENT_BUS.register(this);
    	//Most world generation events happen here, such as Populate, Decorate, etc., with the strange exception that Pre and Post events are on the regular EVENT_BUS
    	MinecraftForge.TERRAIN_GEN_BUS.register(this);
    	//Ore generation, obviously
    	MinecraftForge.ORE_GEN_BUS.register(this);
    }
    
    private void loadConfig(File configFile) {
		config = new Configuration(configFile);
//		for(String s : config.getStringList("spawnPoints", "h", new String[]{"0,0,0"})){
//			gameSpawns.add(fromString(s));
//		}
    	config.save();
	}
    
    public void saveConfig() {
//    	ConfigCategory h = config.getCategory("h");
    	
    	String[] strs = new String[gameSpawns.size()];
    	Iterator<BlockPos> it = gameSpawns.iterator();
    	for(int i = 0; i < strs.length; i++) {
    		strs[i] = toString(it.next());
    	}
    	
//    	config.get("h", "spawnPoints", null, "Positions where players will be placed before the game begins").set(strs);
    	config.save();
    }

	void distributePlayer(EntityPlayer player) {
    	BlockPos pos = gameSpawns.poll();
    	tp(player, pos);
    	gameSpawns.add(pos);
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
    	server = event.getServer();
    	event.registerServerCommand(new ReplenishCmd());
    	witherSound = SoundEvent.REGISTRY.getObject(new ResourceLocation("minecraft", "entity.wither.spawn"));
    	
    	for(ResourceLocation k : Potion.REGISTRY.getKeys()){
    		System.out.println(k);
    	}
    }

	@SubscribeEvent
	public void playerRespawn(PlayerRespawnEvent event) {
		event.player.setGameType(GameType.SPECTATOR);
		event.player.world.playSound(null, event.player.getPosition(), witherSound, SoundCategory.HOSTILE, Float.MAX_VALUE, Float.MAX_VALUE/2);
		if(null != spawnPoint) {
			tp(event.player, spawnPoint);
		}
	}

	public static void tp(EntityPlayer player, BlockPos pos) {
		player.attemptTeleport(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5);
	}

	@SubscribeEvent
	public void playerOpenContainer(PlayerInteractEvent.RightClickBlock event) {
		TileEntity te = event.getWorld().getTileEntity(event.getPos());
		if(null != te && te instanceof TileEntityChest) {
			broadcast(event.getEntityPlayer().getName() + " right clicked " + event.getPos().toString());
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
		broadcast("touching " + pos);
		getOrCreate(world).put(pos, System.currentTimeMillis() + 300000L);
	}
	
	void refill(TileEntityChest chest) {
		BlockPos pos = chest.getPos();
		broadcast("checking " + pos);
		Long exp = getOrCreate(chest.getWorld()).get(pos);
		if(exp == null || exp < System.currentTimeMillis()) {
			broadcast("refilling " + pos);
			touchChest(chest.getWorld(), pos);
			server.getCommandManager().executeCommand(server, String.format("setblock %s %s %s minecraft:chest 0 replace {LootTable:\"%s\"}", pos.getX(), pos.getY(), pos.getZ(), lootTable));
		}
	}
	
	void broadcast(Object msg) {
		server.getCommandManager().executeCommand(server, "say " + String.valueOf(msg));
	}
	
	public String toString(BlockPos pos) {
		return String.format("%s,%s,%s", pos.getX(), pos.getY(), pos.getZ());
	}
	
	public BlockPos fromString(String str) {
		String[] strs = str.split(",");
		return new BlockPos(Integer.valueOf(strs[0]), Integer.valueOf(strs[1]), Integer.valueOf(strs[2]));
	}
	
	public void cmd(String format, Object... args) {
		String cmd = String.format(format, args);
		System.out.println(cmd);
		server.getCommandManager().executeCommand(server, cmd);
	}

	public BlockPos rotateGameSpawns() {
		BlockPos ret = gameSpawns.poll();
		gameSpawns.add(ret);
		return ret;
	}

	public void addGameSpawn(BlockPos position) {
		gameSpawns.add(position);
	}

	public void clearSpawns() {
		gameSpawns.clear();
	}
}
