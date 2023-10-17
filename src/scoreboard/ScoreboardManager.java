package scoreboard;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import game.Game;
import game.GameState;
import survivalgames.GameIdGenerator;
import survivalgames.Main;

public class ScoreboardManager {
		
    String formattedDate = new SimpleDateFormat("dd/MM/YY").format(new Date());
    String id = GameIdGenerator.generateUniqueArenaId();
    
    private final Game game;
        
    public ScoreboardManager (Game game, Main main) {
    	this.game = game;
    }

    public Scoreboard createScoreboard() {
        return org.bukkit.Bukkit.getScoreboardManager().getNewScoreboard();
    }
    
    public void updateTitle(Player player, Scoreboard scoreboard) {
    	ScoreboardStatus scoreboardStatus = new ScoreboardStatus(player);
		Objective objective = scoreboardStatus.getObjective();
	    startGlitchingTitleAnimation(objective);
    }
    
    public void updateLobbyObjectives(Player player, Scoreboard scoreboard) {
    	
    }
    
    public void updateWaitingObjectives(Player player, ScoreboardStatus scoreboard) {
    	Objective objective = scoreboard.getObjective();
	    startGlitchingTitleAnimation(objective);
         
        scoreboard.updateLine(11, ChatColor.DARK_GRAY + formattedDate + " " + ChatColor.GRAY + "" + ChatColor.ITALIC + "" + id);
        scoreboard.updateLine(10, "");
        
        new BukkitRunnable() {
            @Override
            public void run() {
            	scoreboard.updateLine(9, "Map: Voting");
                scoreboard.updateLine(8, "Players: " + ChatColor.GREEN + "" + game.getPlayers().size());
                scoreboard.updateLine(7, "");
                scoreboard.updateLine(6, "Starting in " + ChatColor.GREEN + (game.getTimer()+1 + "s"));
                scoreboard.updateLine(5, "");
                scoreboard.updateLine(4, "Mode: " + ChatColor.GREEN + game.getMode());
                scoreboard.updateLine(3, "Ver: " + ChatColor.GOLD + "Prim-1.20");
                scoreboard.updateLine(2, "");
                scoreboard.updateLine(1, ChatColor.AQUA + "www.sg-game.net");

                if (game.getGameState() == GameState.IN_GAME) {
                	
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 0L); // Run every second (20 ticks)
    }

    public void updateGameObjectives(Player player, ScoreboardStatus scoreboard) {
    	Objective objective = scoreboard.getObjective();
	    startGlitchingTitleAnimation(objective);
    	
        scoreboard.updateLine(12, ChatColor.DARK_GRAY + formattedDate + " " + ChatColor.GRAY + "" + ChatColor.ITALIC + "" + id);
        scoreboard.updateLine(11, "");
        scoreboard.updateLine(10, "You: " + ChatColor.GREEN + "[" + "District 12" + "]");
        
        new BukkitRunnable() {
        	
            @Override
            public void run() {
                scoreboard.updateLine(9, "");
                scoreboard.updateLine(8, "Status: " + ChatColor.GOLD + "Morning ☀");
                scoreboard.updateLine(7, "");
                scoreboard.updateLine(6, "Kills: " + ChatColor.GREEN + "1");
                scoreboard.updateLine(5, "Tributes: " + ChatColor.GREEN + "1");
                scoreboard.updateLine(4, "");
                scoreboard.updateLine(3, "Event: " + ChatColor.RED + "Bloodbath");
                scoreboard.updateLine(2, "");
                scoreboard.updateLine(1, ChatColor.AQUA + "www.sg-game.net");
                
            }
        }.runTaskTimer(Main.getInstance(), 0L, 0L); // Run every second (20 ticks)
    }

    public void clearScoreboard(Player player) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
            public void run() {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

            }
        }, 0L);
    }
    
    public void startGlitchingTitleAnimation(Objective objective) {
        BukkitRunnable animationTask = new BukkitRunnable() {
            int tick = 0;
            boolean isGlitching = false;

            @Override
            public void run() {
                tick++;

                if (tick % 200 == 0) { // Every 10 seconds (200 ticks)
                    isGlitching = true;
                }

                if (isGlitching) {
                    if (tick % 20 >= 0 && tick % 20 < 10) { // Glitch for 1 second (20 ticks)
                        if (tick % 2 == 0) { // Switch every 2 ticks
                            objective.setDisplayName(ChatColor.WHITE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + " SURVIVAL GAMES ");
                        } else {
                            objective.setDisplayName(ChatColor.RED + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + " SURVIVAL GAMES ");
                        }
                    } else {
                        objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + " SURVIVAL GAMES ");
                        isGlitching = false;
                    }
                } else {
                    objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + " SURVIVAL GAMES ");
                }
            }
        };
        animationTask.runTaskTimer(Main.getInstance(), 0L, 1L); // Run every tick
    }
}