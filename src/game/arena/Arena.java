package game.arena;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import game.Game;
import survivalgames.Main;

public class Arena {

	private static final Main plugin = JavaPlugin.getPlugin(Main.class);

	private final String id;
	private String mapName;
	private List<Location> playerSpawnPoints;
	private final Map<GameLocation, Location> gameLocations;
	private boolean ready;
	
	private final Game game = new Game();

	public Arena(String id) {
		this.id = id;
		this.mapName = id;
		this.playerSpawnPoints = new ArrayList<>();
		this.gameLocations = new EnumMap<>(GameLocation.class);
	}

	public String getId() {
		return this.id;
	}

	public String getMapName() {
		return this.mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public boolean isReady() {
		return this.ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public enum GameLocation {
		LOBBY, SPECTATOR, CENTER;
	}

	private void teleportToGameLocation(Player player, GameLocation gameLocation) {
		if (!validateLocation(gameLocation))
			return;
		player.setFoodLevel(20);
		player.setFlying(false);
		player.setAllowFlight(false);
		player.setFlySpeed(0.1F);
		player.setWalkSpeed(0.2F);
		player.teleport(this.gameLocations.get(gameLocation));
	}

	private boolean validateLocation(GameLocation gameLocation) {
	    Location location = this.gameLocations.get(gameLocation);
	    if (location == null) {
	      plugin.getLogger().log(Level.WARNING, "Lobby location isn't initialized for arena {0}!", this.id);
	      return false;
	    }
	    return true;
	  }

	public void teleportToLobby(Player player) {
		teleportToGameLocation(player, GameLocation.LOBBY);
	}
	
	public Location getCenterLocation() {
		return this.gameLocations.get(GameLocation.CENTER);
	}

	public void setCenterLocation(Location centerLocation) {
		this.gameLocations.put(GameLocation.CENTER, centerLocation);
	}

	public Location getLobbyLocation() {
		return this.gameLocations.get(GameLocation.LOBBY);
	}

	public void setLobbyLocation(Location lobbyLocation) {
		this.gameLocations.put(GameLocation.LOBBY, lobbyLocation);
	}

	public Location getSpectatorLocation() {
		return this.gameLocations.get(GameLocation.SPECTATOR);
	}

	public void setSpectatorLocation(Location spectatorLocation) {
		this.gameLocations.put(GameLocation.SPECTATOR, spectatorLocation);
	}
	
	public List<Location> getPlayerSpawnPoints() {
		return this.playerSpawnPoints;
	}

	public void setPlayerSpawnPoints(List<Location> playerSpawnPoints) {
		this.playerSpawnPoints = playerSpawnPoints;
	}

	public void teleportAllToStartLocation() {
	    int i = 0;
	    for (Player player : game.getPlayers()) {
	        if (i >= this.playerSpawnPoints.size()) {
	            plugin.getLogger().warning("Not enough spawn points for all players.");
	            break;
	        }
	        
	        Location location = this.playerSpawnPoints.get(i++);
	        if (location == null) {
	            plugin.getLogger().warning("Something went wrong while trying to teleport the players to the start locations.");
	            break;
	        }
	        player.teleport(location);
	    }
	}
}