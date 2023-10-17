package game.arena;

import java.util.HashMap;
import java.util.Map;

public class ArenaVote {
    private Map<Arena, Integer> arenaVotes = new HashMap<>();

    public void addVote(Arena arena) {
        arenaVotes.put(arena, arenaVotes.getOrDefault(arena, 0) + 1);
    }

    public void removeVote(Arena arena) {
        if (arenaVotes.containsKey(arena)) {
            int currentVotes = arenaVotes.get(arena);
            if (currentVotes > 1) {
                arenaVotes.put(arena, currentVotes - 1);
            } else {
                arenaVotes.remove(arena);
            }
        }
    }

    public int getVotes(Arena arena) {
        return arenaVotes.getOrDefault(arena, 0);
    }

    public void displayVotes(Arena arena) {
        int votes = getVotes(arena);
        System.out.println("Votes for Arena " + arena.getId() + ": " + votes);
    }
}