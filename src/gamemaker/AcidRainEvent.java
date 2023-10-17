package gamemaker;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import survivalgames.Main;

public class AcidRainEvent {
    private final World world;
    private final int duration; // Duration of the event in seconds

    public AcidRainEvent(World world, int duration) {
        this.world = world;
        this.duration = duration;
    }

    public void startEvent() {
        world.setStorm(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                world.setStorm(false);
                this.cancel();
            }
        }.runTaskLater(Main.getInstance(), duration * 20); // Convert seconds to ticks

        new BukkitRunnable() {
            @Override
            public void run() {
                applyAcidRainEffect();
            }
        }.runTaskTimer(Main.getInstance(), 0, 20); // Apply effect every second (adjust as needed)
    }

    private void applyAcidRainEffect() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location playerLocation = player.getLocation();
            if (world.hasStorm() && world.getHighestBlockYAt(playerLocation) <= playerLocation.getBlockY()) {
                player.damage(2.0);
                playerLocation.getWorld().playEffect(playerLocation, Effect.SMOKE, 1);
            }
        }
    }
}