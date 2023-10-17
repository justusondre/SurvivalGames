package game;

public enum GameState {

	WAITING_FOR_PLAYERS("Waiting"),
	STARTING("Starting"),
	PREGAME("Pregame"),
	IN_GAME("Playing"),
	ENDING("Ending"),
	RESTARTING("Restarting"),
	INACTIVE("Inactive");

	public final String name;

	private GameState(String name) {
		this.name = name;
	}
}
