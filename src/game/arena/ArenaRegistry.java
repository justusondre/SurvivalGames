package game.arena;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import survivalgames.Main;

import utils.ConfigUtils;
import utils.LocationSerializer;

public class ArenaRegistry {

	private final Main plugin;
	private final Set<Arena> arenas;

	public ArenaRegistry(Main plugin) {
		this.plugin = plugin;
		this.arenas = new HashSet<>();
		registerArenas();
	}

	public void registerArena(Arena arena) {
		this.arenas.add(arena);
	}

	public void unRegisterArena(Arena arena) {
		this.arenas.remove(arena);
	}

	public Set<Arena> getArenas() {
	    Set<Arena> copy = new HashSet<>(this.arenas);
	    return copy;
	}

	public Arena getArena(String id) {
		if (id == null) {
			return null;
		}
		return this.arenas.stream().filter(arena -> arena.getId().equals(id)).findFirst().orElse(null);
	}

	public boolean isArena(String arenaId) {
		return (arenaId != null && getArena(arenaId) != null);
	}

	private void registerArenas() {
		this.arenas.clear();
		FileConfiguration config = ConfigUtils.getConfig((JavaPlugin) this.plugin, "arena");
		ConfigurationSection section = config.getConfigurationSection("instance");
		
		if (section == null) {
			this.plugin.getLogger().warning("Couldn't find 'instance' section in arena.yml, delete the file to regenerate it!");
			return;
		}
		
		for (String id : section.getKeys(false)) {
			if (id.equals("default"))
				continue;
			
			String path = String.format("instance.%s.", id);
			Arena arena = new Arena(id);
			registerArena(arena);
			arena.setMapName(config.getString(path + "mapName"));
			arena.setReady(config.getBoolean(path + "ready"));
			arena.setLobbyLocation(LocationSerializer.fromString(config.getString(path + "lobbyLocation")));
			arena.setSpectatorLocation(LocationSerializer.fromString(config.getString(path + "spectatorLocation")));
			arena.setCenterLocation(LocationSerializer.fromString(config.getString(path + "centerLocation")));
			arena.setPlayerSpawnPoints((List<Location>)config.getStringList(path + "playerSpawnPoints").stream().map(LocationSerializer::fromString).collect(Collectors.toList()));
			
			if (!arena.isReady()) {
				this.plugin.getLogger().log(Level.WARNING, "Setup of arena ''{0}'' is not finished yet!", id);
				return;
			}
		}
	}
}