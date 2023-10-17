package survivalgames;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import arena.WorldManager;
import command.CreateArena;
import command.LoadWorld;
import command.SetArenaLobby;
import command.SetCenter;
import command.SetPlayerSpawn;
import command.SetSpectator;
import command.TeleportWorld;
import command.Vote;
import event.JoinEvent;
import file.FileManager;
import game.Game;
import game.GameManager;
import game.arena.ArenaRegistry;
import gui.VoteGUI;
import mechanics.Bandage;
import mechanics.Fireballs;
import mechanics.HealingSoup;
import mechanics.RideablePearls;
import mechanics.ThrowableBlades;
import mechanics.ThrowingAxes;
import object.TeamManager;
import scoreboard.ScoreboardManager;

public class Main extends JavaPlugin {

	private static Main instance;
    private FileManager fileManager;
    private ArenaRegistry arenaRegistry;
    private TeamManager teamManager;
    private WorldManager worldManager;
    private Game game;
    private GameManager gameManager;
    private ScoreboardManager scoreboardManager;
    private VoteGUI voteGUI;

    private static final int MIN_VERSION = 8;
	private static final int MAX_VERSION = 19;
	private static int version;

	@Override
	public void onEnable() {
	    instance = this;
	    loadServerVersion();

	    fileManager = new FileManager();
	    this.arenaRegistry = new ArenaRegistry(instance);
	    this.worldManager = new WorldManager();
	    this.game = new Game();
	    this.gameManager = new GameManager(game);
	    this.scoreboardManager = new ScoreboardManager(game, instance);
	    this.voteGUI = new VoteGUI();

	    registerCommands();
	    registerListeners();
	    getLogger().info("Your plugin has been enabled!");
	    
	    getGame().start();
	}

    private void loadServerVersion(){
		String versionString = Bukkit.getBukkitVersion();
		version = 0;

		for (int i = MIN_VERSION; i <= MAX_VERSION; i ++){
			if (versionString.contains("1." + i)){
				version = i;
			}
		}

		if (version == 0) {
			version = MIN_VERSION;
			Bukkit.getLogger().warning("[Bedwars] Failed to detect server version! " + versionString + "?");
		}else {
			Bukkit.getLogger().info("[Bedwars] 1." + version + " Server detected!");
		}
	}

	public int getVersion() {
		return version;
	}

    @Override
    public void onDisable() {
        getLogger().info("Your plugin has been disabled!");
    }

    public static Main getInstance() {
        return instance;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

	public ArenaRegistry getArenaRegistry() {
		return arenaRegistry;
	}
	
	public TeamManager getTeamManager() {
		return teamManager;
	}

	private void registerCommands() {
        getCommand("createarena").setExecutor(new CreateArena());
        getCommand("setarenalobby").setExecutor(new SetArenaLobby());
        getCommand("setspectator").setExecutor(new SetSpectator());
        getCommand("setplayerspawn").setExecutor(new SetPlayerSpawn());
        getCommand("setcenter").setExecutor(new SetCenter());
        getCommand("tpworld").setExecutor(new TeleportWorld());
        getCommand("load").setExecutor(new LoadWorld());
        getCommand("unload").setExecutor(new LoadWorld());
        getCommand("vote").setExecutor(new Vote(voteGUI));
        
	}

    private void registerListeners() {
        Listener[] listeners = {
                new HealingSoup(),
                new RideablePearls(),
                new ThrowingAxes(),
                new ThrowableBlades(),
                new Bandage(),
                new Fireballs(),
                new JoinEvent(),
                new VoteGUI(),
        };

        PluginManager pluginManager = getServer().getPluginManager();
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

	public WorldManager getWorldManager() {
		return worldManager;
	}

	public Game getGame() {
		return game;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public ScoreboardManager getScoreboardManager() {
		return scoreboardManager;
	}

	public VoteGUI getVoteGUI() {
		return voteGUI;
	}
}