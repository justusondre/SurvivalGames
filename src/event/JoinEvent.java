package event;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import game.Game;
import game.GameManager;
import game.GameState;
import game.arena.ArenaRegistry;
import net.md_5.bungee.api.ChatColor;
import survivalgames.Main;

public class JoinEvent implements Listener {
	
	Game game = Main.getInstance().getGame();
	GameManager gameManager = Main.getInstance().getGameManager();
	ArenaRegistry arenaRegistry = Main.getInstance().getArenaRegistry();
	
	@EventHandler 
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		
		if(game.getGameState() == GameState.WAITING_FOR_PLAYERS) {
			gameManager.addPlayer(event.getPlayer());		
			Bukkit.broadcastMessage(ChatColor.RED + "AVAILABLE MAPS: " + arenaRegistry.getArenas());
			
		} else {
			gameManager.addSpectator(event.getPlayer());
		}
	}
}
