package game;

import org.bukkit.plugin.java.JavaPlugin;

import survivalgames.Main;

public enum GameOption {
	TIMER(15), MINIMUM_PLAYERS(11), MAXIMUM_PLAYERS(16),
	LOBBY_WAITING_TIME("Time-Settings.Lobby-Waiting-Time", 15),
	LOBBY_STARTING_TIME("Time-Settings.Lobby-Starting-Time", 15),
	LOBBY_ENDING_TIME("Time-Settings.Ending-Time", 10),
	PREGAME_TIME("Time-Settings.Ending-Time", 25),
	GAMEPLAY_TIME("Time-Settings.Default-Gameplay-Time", 270),
	GAME_ENDING_TIME("Time-Settings.Default-Gameplay-Time", 60);

	int integerValue;
	boolean booleanValue;

	GameOption(int defaultValue) {
		this.integerValue = defaultValue;
	}

	GameOption(String path, boolean defaultValue) {
		Main plugin = JavaPlugin.getPlugin(Main.class);
		this.booleanValue = plugin.getConfig().getBoolean(path, defaultValue);
	}

	GameOption(String path, int defaultValue) {
		Main plugin = JavaPlugin.getPlugin(Main.class);
		int value = plugin.getConfig().getInt(path, defaultValue);
		this.integerValue = (value < 0) ? defaultValue : value;
	}

	public boolean getBooleanValue() {
		return this.booleanValue;
	}

	public int getIntegerValue() {
		return this.integerValue;
	}
}
