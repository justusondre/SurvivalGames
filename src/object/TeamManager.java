package object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import file.ArenaFile;
import game.Mode;
import game.arena.Arena;
import survivalgames.Main;
import utils.StringUtils;

public class TeamManager {
	
	private Map<ChatColor, Team> teams;
    private Map<String, Map<Player, Team>> arenaPlayerTeams = new HashMap<>();
    private final Arena arena;
    
    ArenaFile arenaFile = Main.getInstance().getFileManager().getArenaFile();

    public TeamManager(Arena arena) {
        teams = new HashMap<>();
        this.arena = arena;
        initializeTeams();
    }

    private void initializeTeams() {
        for (TeamDistrict color : TeamDistrict.values()) {
            teams.put(color.getChatColor(), new Team(color));
        }
    }

    public Team getTeam(ChatColor teamColor) {
        return teams.get(teamColor);
    }

    public List<Team> getAllTeams() {
        return new ArrayList<>(teams.values());
    }

    public void addPlayerToTeam(Player player, Team team) {
        Map<Player, Team> playerTeams = arenaPlayerTeams.computeIfAbsent(arena.getId(), k -> new HashMap<>());

        ArenaFile arenaFile = Main.getInstance().getFileManager().getArenaFile();
        Mode gameMode = arenaFile.getMode(arena);
        removeFromTeam(player, arena.getId());
        if (isTeamFull(team, gameMode)) {
            player.sendMessage(ChatColor.RED + "The " + team.getTeamColor() + team.getTeamName() + ChatColor.RED + " team is full.");
            player.sendMessage(ChatColor.RED + "Current team size: " + team.getSize() + "/" + gameMode.getTeamSize());

            return;
        }

        playerTeams.put(player, team);
        team.addPlayer(player);
        
		StringUtils.sendCenteredMessage(player, ChatColor.DARK_GRAY + "");
        StringUtils.sendCenteredMessage(player, ChatColor.GRAY + "You have joined " + ChatColor.GREEN + ""  + ChatColor.BOLD + "" +  team.getTeamName() + ChatColor.GRAY + "!");
        StringUtils.sendCenteredMessage(player, ChatColor.GRAY + "Your district specialty is: " + ChatColor.GOLD + "" +  team.getDescription() + ChatColor.GRAY + ".");
        StringUtils.sendCenteredMessage(player, ChatColor.GRAY + "Use your specialty to fight the other tributes.");
        StringUtils.sendCenteredMessage(player, ChatColor.GRAY + "May the odds be ever in your favor!");
		StringUtils.sendCenteredMessage(player, ChatColor.DARK_GRAY + "");
        
    }

    public void assignPlayerToRandomTeam(Player player, String arenaName) {
        TeamManager teamManager = Main.getInstance().getTeamManager();
        List<Team> availableTeams = teamManager.getAllTeams();
        List<Team> eligibleTeams = new ArrayList<>();

        Mode gameMode = arenaFile.getMode(arenaName); // Assuming arenaFile is already defined

        for (Team team : availableTeams) {
            if (!isTeamFull(team, gameMode)) {
                eligibleTeams.add(team);
            }
        }

        if (!eligibleTeams.isEmpty()) {
            Team randomTeam = eligibleTeams.get(new Random().nextInt(eligibleTeams.size()));
            addPlayerToTeam(player, randomTeam, arenaName);
        } else {
            player.sendMessage(ChatColor.RED + "All eligible teams are full for the " + arenaName + " arena.");
        }
    }

    private boolean isTeamFull(Team team, Mode gameMode) {
        int maxTeamSize = gameMode.getTeamSize();
        return team.getSize() >= maxTeamSize;
    }

    public void removeFromTeam(Player player) {
        Map<Player, Team> playerTeams = arenaPlayerTeams.get(arena.getId());
        if (playerTeams != null) {
            Team team = playerTeams.remove(player);
            if (team != null) {
                team.removePlayer(player);
            }
        }
    }

    public Team getPlayerTeam(Player player, String arenaName) {
        Map<Player, Team> playerTeams = arenaPlayerTeams.get(arenaName);
        return playerTeams != null ? playerTeams.get(player) : null;
    }

    public List<Team> getTeamsRemaining(String arenaName) {
        List<Team> remainingTeams = new ArrayList<>();
        if (arenaName != null) {
            Map<Player, Team> playerTeams = arenaPlayerTeams.get(arenaName);
            
            for (Team team : getAllTeams()) {
                if (team.getSize() > 0 && playerTeams.containsValue(team)) {
                    remainingTeams.add(team);
                }
            }
        }
        
        return remainingTeams;
    }

    public List<Player> getPlayersInTeam(String arenaName, TeamDistrict teamDistrict) {
        Map<Player, Team> playerTeams = arenaPlayerTeams.get(arenaName);
        List<Player> playersInTeam = new ArrayList<>();

        if (playerTeams != null) {
            for (Map.Entry<Player, Team> entry : playerTeams.entrySet()) {
                if (entry.getValue().getTeamColor() == teamDistrict) {
                    playersInTeam.add(entry.getKey());
                }
            }
        }

        return playersInTeam;
    }

    public boolean isPlayerOnTeam(String arenaName, Player player) {
        Map<Player, Team> playerTeams = arenaPlayerTeams.get(arenaName);
        if (playerTeams != null) {
            Team team = playerTeams.get(player);
            if (team != null) {
            	Bukkit.broadcastMessage(player.getDisplayName() + " is in a team!");
            }
        }
        return false;
    }

    public TeamDistrict getPlayerTeamColor(Player player, String arenaName) {
        Map<Player, Team> playerTeams = arenaPlayerTeams.get(arenaName);
        if (playerTeams != null) {
            Team team = playerTeams.get(player);
            if (team != null) {
                return team.getTeamColor();
            }
        }
        return null;
    }

    public int getTeamSize(String arenaName, Team team) {
        Map<Player, Team> playerTeams = arenaPlayerTeams.get(arenaName);
        if (playerTeams != null) {
            int size = 0;
            for (Map.Entry<Player, Team> entry : playerTeams.entrySet()) {
                if (entry.getValue().equals(team)) {
                    size++;
                }
            }
            return size;
        }
        return 0;
    }

    public void checkForEmptyTeams(String arenaName) {        
        StringBuilder emptyTeamsMessage = new StringBuilder("Empty Teams in Arena '" + arenaName + "': ");
        boolean emptyTeamsFound = false; // Track if any empty teams were found

        for (Team team : getAllTeams()) {
            int teamSize = getTeamSize(arenaName, team);
            if (teamSize == 0) {
                emptyTeamsMessage.append(team.getTeamName()).append(", "); // Use team.getTeamName()
                emptyTeamsFound = true; // Mark that an empty team was found
            }
        }

        if (emptyTeamsFound) { // Check if any empty teams were found
            emptyTeamsMessage.delete(emptyTeamsMessage.length() - 2, emptyTeamsMessage.length()); // Remove trailing comma and space
            Bukkit.broadcastMessage(emptyTeamsMessage.toString());
        }
    }
}