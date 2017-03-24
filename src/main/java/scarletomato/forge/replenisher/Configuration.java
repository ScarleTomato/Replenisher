package scarletomato.forge.replenisher;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.world.GameType;
import net.minecraftforge.common.config.Property;

public class Configuration {
	public static final String CATEGORY = "main";
	public static final String GAME_SPAWNS = "gameSpawns";
	public static final String HAUNT_TIME = "hauntInterval";
	public static final String RES_TIME = "resTime";
	public static final String LIVING_MODE = "livingGameType";
	public static final String RES_POINT = "resurrectionPoint";
	public static final String CHEST_LOOT_TABLE = "chestLoot";
	private final net.minecraftforge.common.config.Configuration forgeConfig;
	private final Map<String, Property> props = new HashMap<>();
	private final Map<String, List<String>> listProps = new HashMap<>();
	
	
	public Configuration(File file) {
		forgeConfig = new net.minecraftforge.common.config.Configuration(file);
		loadFromFile();
	}
	
	public List<String> getStringList(String name) {
		return listProps.get(name);
	}

	public void set(String key, String val) {
		props.get(key).setValue(val);
	}
	
	public Integer getInt(String key) {
		return props.get(key).getInt();
	}

	public String getString(String key) {
		return props.get(key).getString();
	}
	
	public void set(String key, int val) {
		props.get(key).setValue(val);
	}

	private void loadFromFile() {
		forgeConfig.load();
		
		props.put(GAME_SPAWNS, forgeConfig.get(CATEGORY, GAME_SPAWNS, new String[]{"0,0,0"}, "These are all the positions players will be placed when the game starts"));
		listProps.put(GAME_SPAWNS, asList(props.get(GAME_SPAWNS).getStringList()));
		
		props.put(HAUNT_TIME, forgeConfig.get(CATEGORY, HAUNT_TIME, 60, "This is the approximate time between haunts"));
		
		props.put(RES_TIME, forgeConfig.get(CATEGORY, RES_TIME, 60, "This is how long a resurrection stone will remain active"));
		
		props.put(LIVING_MODE, forgeConfig.get(CATEGORY, LIVING_MODE, GameType.ADVENTURE.toString(), "This is the mode players will be set to once the game begins"));
		
		props.put(CHEST_LOOT_TABLE, forgeConfig.get(CATEGORY, CHEST_LOOT_TABLE, "replenisher:chest", "This is the resource location of the loot table that chests will use to get refilled"));
		
		props.put(RES_POINT, forgeConfig.get(CATEGORY, RES_POINT, "0,0,0", "This point where ghosts will be resurrected while a stone is activated"));
		
		save();
	}
	
	List<String> asList(String[] a) {
		List<String> ret = new LinkedList<>();
		for(String s : a){
			ret.add(s);
		}
		return ret;
	}
	
	public void save() {
		writeListPropsToConfig();
		forgeConfig.save();
	}
	
	private void writeListPropsToConfig() {
		for(Map.Entry<String, List<String>> e : listProps.entrySet()) {
			props.get(e.getKey()).set(e.getValue().toArray(new String[e.getValue().size()]));
		}
	}
}
