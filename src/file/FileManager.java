package file;

import survivalgames.Main;

public class FileManager {

	public LobbyFile lobbyFile;
	public ArenaFile arenaFile;

	public FileManager() {
		this.lobbyFile = new LobbyFile();
		this.arenaFile = new ArenaFile(Main.getInstance());

	}

	public LobbyFile getLobbyFile() {
		return this.lobbyFile;
	}

	public ArenaFile getArenaFile() {
		return this.arenaFile;
	}
}