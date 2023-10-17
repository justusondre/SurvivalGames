package game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import game.arena.Arena;
import scoreboard.ScoreboardManager;
import survivalgames.Main;
import utils.Title;

public class Game extends BukkitRunnable {
	
	private List<Player> players;
	private final List<Player> spectators;
	private GameState gameState;
	private Mode mode;
	private Arena arena;
	private final Map<GameOption, Integer> gameOptions;
	
	ScoreboardManager scoreboardManager = Main.getInstance().getScoreboardManager();
	
	public Game() {
		this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.gameState = GameState.WAITING_FOR_PLAYERS;
        this.mode = Mode.SOLO;    
		this.gameOptions = new EnumMap<>(GameOption.class);
		
		for (GameOption option : GameOption.values()) {
			this.gameOptions.put(option, Integer.valueOf(option.getIntegerValue()));
		}
	}
	
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public Mode getMode() {
	    return this.mode;
	}

	public void setMode(String mode) {
	    try {
	        this.mode = Mode.valueOf(mode.toUpperCase());
	    } catch (IllegalArgumentException e) {
	        this.mode = Mode.SOLO;
	    }
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public List<Player> getSpectators() {
		return spectators;
		
	}
	
	private void setOptionValue(GameOption option, int value) {
		this.gameOptions.put(option, Integer.valueOf(value));
	}
	
	public int getMaximumPlayers() {
		return getOption(GameOption.MAXIMUM_PLAYERS);
	}

	public void setMaximumPlayers(int maximumPlayers) {
		setOptionValue(GameOption.MAXIMUM_PLAYERS, maximumPlayers);
	}

	public int getMinimumPlayers() {
		return getOption(GameOption.MINIMUM_PLAYERS);
	}

	public void setMinimumPlayers(int minimumPlayers) {
		setOptionValue(GameOption.MINIMUM_PLAYERS, minimumPlayers);
	}

	private int getOption(GameOption option) {
		return this.gameOptions.get(option).intValue();
	}

	public int getGameplayTime() {
		return getOption(GameOption.GAMEPLAY_TIME);
	}

	public void setGameplayTime(int gameplayTime) {
		setOptionValue(GameOption.GAMEPLAY_TIME, gameplayTime);
	}
	
	public void start() {
		runTaskTimer(Main.getInstance(), 20L, 20L);
		setGameState(GameState.WAITING_FOR_PLAYERS);
	}

	public void stop() {
		if (this.gameState != GameState.INACTIVE) {
			cancel();
		}
		this.players.forEach(null);
	}
	
	public int getTimer() {
		return getOption(GameOption.TIMER);
	}

	public void setTimer(int timer) {
		setOptionValue(GameOption.TIMER, timer);
	}
	
	public void run() {
	    if (this.players.isEmpty() && this.gameState == GameState.WAITING_FOR_PLAYERS) {
	        return;
	    }

	    //int minPlayers = getMinimumPlayers();
	    int waitingTime = getOption(GameOption.LOBBY_WAITING_TIME);
	    int startingTime = getOption(GameOption.LOBBY_STARTING_TIME);
	    int pregameTime = getOption(GameOption.PREGAME_TIME);
	    int ingameTime = getOption(GameOption.GAMEPLAY_TIME);
	    int endingTime = getOption(GameOption.GAME_ENDING_TIME);

	    switch (this.gameState) {
	        case WAITING_FOR_PLAYERS:
	            if (this.players.size() == 1) {
	                setGameState(GameState.STARTING);
	                setTimer(startingTime);
                	broadcastMessage(ChatColor.GREEN + "Enough Players! "
	                 + ChatColor.GRAY + "The game will begin in " + ChatColor.AQUA + startingTime + ChatColor.GRAY + " seconds.");
	               
	            } else {
	            	
	                if (getTimer() <= 0) {
	                    setTimer(waitingTime);
	                    broadcastMessage(ChatColor.RED + "Cancelled! " + ChatColor.GRAY + "There are not enough players.");
	                }
	            }

	            setTimer(getTimer() - 1);
	            break;

	        case STARTING:
	            if (this.players.size() == 0) {
	                setGameState(GameState.WAITING_FOR_PLAYERS);
	                setTimer(waitingTime);
                    broadcastMessage(ChatColor.RED + "Cancelled! " + ChatColor.GRAY + "There are not enough players.");
	                break;
	            }

	            if (getTimer() == 15 || getTimer() == 10 || getTimer() <= 5 && getTimer() >= 1) {
	            	broadcastMessage(ChatColor.GRAY + "The game will begin in " + ChatColor.AQUA + getTimer() + ChatColor.GRAY + " seconds.");
	            	this.players.forEach(player -> Title.sendTitle(player, ChatColor.RED + "" + ChatColor.BOLD + getTimer(), "", 0, 20*2, 20*1));

	            }

	            if (getTimer() == 0) {
	                setGameState(GameState.PREGAME);
	                setTimer(pregameTime);
	                arena.teleportAllToStartLocation();
	            }

	            setTimer(getTimer() - 1);
	            break;
	            
	        case PREGAME:
	            if (getTimer() == 15 || getTimer() == 10 || getTimer() <= 5 && getTimer() >= 1) {
	                broadcastMessage("Game starting in " + getTimer() + " seconds.");
	            }	         

	            if (getTimer() == 0) {
	                setGameState(GameState.IN_GAME);
	                setTimer(ingameTime);
	        	    Bukkit.broadcastMessage(ChatColor.RED + "Happy Hunger Games & May the Odds be Ever in your Favor!");
	            }

	            setTimer(getTimer() - 1);
	            break;

	        case IN_GAME:
	            if (getTimer() == 30 || getTimer() == 60 || getTimer() == 120 || getTimer() == 270) {
	                broadcastMessage("Game time remaining: " + getTimer() + " seconds.");
	            }
	            
	            if (getTimer() == 60 || getTimer() == 30 || getTimer() == 10 || getTimer() <=5) {
	                setGameState(GameState.ENDING);
	                setTimer(endingTime);
	                broadcastMessage("Game is ending in " + endingTime + " seconds.");
	            }

	            setTimer(getTimer() - 1);
	            break;

	        case ENDING:
	            if (getTimer() == 60 || getTimer() == 30 || getTimer() == 10 || getTimer() <=5) {
	                broadcastMessage("Game ending in " + getTimer() + " seconds.");
	            }
	            
	            if(getTimer() == 0) {
	                setGameState(GameState.RESTARTING);
	                broadcastMessage("The game has ended!");
	            }

	            setTimer(getTimer() - 1);
	            break;

	        case RESTARTING:
                setGameState(GameState.WAITING_FOR_PLAYERS);
	            broadcastMessage("Arena is restarting. Get ready for the next game!");
	            break;

	        case INACTIVE:
	            break;

	        default:
	            break;
	    }
	}
	
	private void broadcastMessage(String message) {
	    this.players.forEach(player -> player.getPlayer().sendMessage(message));
	    this.players.forEach(player -> player.getPlayer().playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F));
    }
}
