package game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import scoreboard.ScoreboardManager;
import scoreboard.ScoreboardStatus;
import survivalgames.Main;

public class GameManager {
	
	private Game game;
	
	public GameManager (Game game) {
		this.game = game;
	}
	
	public void addPlayer(Player player) {
		player.setFireTicks(0);
        player.setNoDamageTicks(200);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(10);
        player.setFlySpeed(0.1f);
        player.setLevel(0);
        player.setExp(0);
        player.setGameMode(GameMode.ADVENTURE);
        
        game.getPlayers().add(player);
        game.getSpectators().remove(player);
        

        for(PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }

        player.setItemOnCursor(null);
        player.closeInventory();
        
        ScoreboardManager scoreboardManager = new ScoreboardManager(game, Main.getInstance());
        ScoreboardStatus scoreboardStatus = new ScoreboardStatus(player);
        scoreboardManager.updateWaitingObjectives(player, scoreboardStatus);
        
        broadcastMessage(ChatColor.AQUA + player.getDisplayName() + ChatColor.GRAY + " has joined the game! " + ChatColor.GOLD + "(" + game.getPlayers().size() + "/24)");
        
	}
	
	public void addSpectator(Player player) {
		player.setFireTicks(0);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(10);
        player.setFlySpeed(0.1f);
        player.setLevel(0);
        player.setExp(0);
        player.setAllowFlight(true);
        player.setFlying(true);
        
        game.getPlayers().remove(player);
        game.getSpectators().add(player);

        for(Player p : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(player);
        }
	}
	
	public void broadcastMessage(String message) {
	    game.getPlayers().forEach(player -> player.getPlayer().sendMessage(message));
	    game.getPlayers().forEach(player -> player.getPlayer().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1F, 1F));
    }
}
