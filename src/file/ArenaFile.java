package file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import game.Mode;
import game.arena.Arena;
import survivalgames.Main;
import utils.LocationSerializer;

@SuppressWarnings("serial")
public class ArenaFile extends File {

  public File file = new File("plugins/" + Main.getInstance().getName(), "arena.yml");
  public FileConfiguration config = YamlConfiguration.loadConfiguration(this.file);

  public ArenaFile(Main plugin) {
    super("", "arena");
    writeDefaults();
  }

  public void writeDefaults() {
    this.config.options().copyDefaults(true);
    saveConfig();
    
  }
  
  public void createArena(String arenaId) {
	  	Arena arena = new Arena(arenaId);
	  	Main.getInstance().getArenaRegistry().registerArena(arena);
	    String arenaPath = "instance." + arenaId.toLowerCase();
	    config.set(arenaPath + ".mapName", arenaId);
	    config.set(arenaPath + ".mode", "SOLO");
	    config.set(arenaPath + ".minimumPlayers", 4);
	    config.set(arenaPath + ".maximumPlayers", 8);
	    config.set(arenaPath + ".gameplayTime", Integer.valueOf(270));
	    config.set(arenaPath + ".ready", Boolean.valueOf(false));
	    config.set(arenaPath + ".lobbyLocation", LocationSerializer.SERIALIZED_LOCATION);
	    config.set(arenaPath + ".spectatorLocation", LocationSerializer.SERIALIZED_LOCATION);
	    config.set(arenaPath + ".centerLocation", LocationSerializer.SERIALIZED_LOCATION);
	    config.set(arenaPath + ".playerSpawnPoints", new ArrayList<>());
	    saveConfig();
	}

  	public void setArenaLobby(Arena arena, Location loc) {
	    config.set("instance." + arena.getId() + ".lobbyLocation", LocationSerializer.toString(loc));
	    saveConfig();
	}
  	
  	public void setArenaCenter(Arena arena, Location loc) {
	    config.set("instance." + arena.getId() + ".centerLocation", LocationSerializer.toString(loc));
	    saveConfig();
	}
  	
  	public void setSpectatorLocation(Arena arena, Location loc) {
	    config.set("instance." + arena.getId() + ".spectatorLocation", LocationSerializer.toString(loc));
	    saveConfig();
	}

	public void setIronSpawn(String arenaName, int number, Location loc) {
	    config.set("instance." + arenaName + ".ironSpawnPoints." + number, LocationSerializer.toString(loc));
	    saveConfig();
	}

	public void setGoldSpawn(String arenaName, int number, Location loc) {
	    config.set("instance." + arenaName + ".goldSpawnPoints." + number, LocationSerializer.toString(loc));
	    saveConfig();
	}

	public void setDiamondSpawn(String arenaName, int number, Location loc) {
	    config.set("instance." + arenaName + ".diamondSpawnPoints." + number, LocationSerializer.toString(loc));
	    saveConfig();
	}

	public void setEmeraldSpawn(String arenaName, int number, Location loc) {
	    config.set("instance." + arenaName + ".emeraldSpawnPoints." + number, LocationSerializer.toString(loc));
	    saveConfig();
	}
	
	public void addPlayerSpawnPoint(String arenaName, Location loc) {
	    List<String> spawnPoints = config.getStringList("instance." + arenaName + ".playerSpawnPoints");
	    spawnPoints.add(LocationSerializer.toString(loc));	    
	    config.set("instance." + arenaName + ".playerSpawnPoints", spawnPoints);
	    saveConfig();
	}

	public void setMode(String arenaName, Mode selectedMode) {
	    config.set("instance." + arenaName + ".mode", selectedMode.name()); // Save the mode as an enum constant
	    saveConfig();
	}

	public Mode getMode(Arena arena) {
	    String modeName = config.getString("instance." + arena.getId() + ".mode");

	    if (modeName != null) {
	        modeName = modeName.toUpperCase();

	        try {
	            return Mode.valueOf(modeName);

	        } catch (IllegalArgumentException e) {

	        	return null;
	        }
	    }
	    return null;
	}

	public void saveConfig() {
	    try {
	        config.save(file);
	    	} catch (IOException ignored) {
	    }
	}

	public void getArenas() {
	    for (String key : config.getConfigurationSection("instance.").getKeys(false)) {
	        Bukkit.broadcastMessage(key);
	    }
	}

	public List<String> getArenasList() {
	    return new ArrayList<>(config.getConfigurationSection("instance.").getKeys(false));
	}
}