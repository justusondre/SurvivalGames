package object;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class Team {
	
	private TeamDistrict teamDistrict;
    private List<Player> players;

    public Team(TeamDistrict teamDistrict) {
        this.teamDistrict = teamDistrict;
        this.players = new ArrayList<>();
    }

    public TeamDistrict getTeamColor() {
        return teamDistrict;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    public void removePlayer(Player player) {
        if (players.contains(player)) {
            players.remove(player);
        }
    }

    public String getTeamName() {
        return teamDistrict.getDisplayName();
    }
    
    public String getDescription() {
    	return teamDistrict.getDescription();
    }

    public int getSize() {
        return players.size();
    }
}