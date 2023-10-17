package mechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import survivalgames.Main;

import java.util.ArrayList;

public class FireballCooldown {
    ArrayList<Player> players = new ArrayList<>();

    public void addPlayer(Player player) {
        players.add(player);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> players.remove(player), 10);
    }
}