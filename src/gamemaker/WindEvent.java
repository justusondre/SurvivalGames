package gamemaker;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import survivalgames.Main;

public class WindEvent extends BukkitRunnable {
    private final World world;
    private int duration;
    private final Random random;

    public WindEvent(World world, int duration) {
        this.world = world;
        this.duration = duration;
        this.random = new Random();
    }

    @Override
    public void run() {
        if (duration <= 0) {
            this.cancel(); // Stop the event
            return;
        }

        // Play wind sound effects
        for (Player player : world.getPlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_CAT_PURR, SoundCategory.AMBIENT, 1.0f, 1.0f);
        }

        // Apply wind forces to players
        for (Player player : world.getPlayers()) {
            // Calculate a random wind force
            double windX = random.nextDouble() * 0.4 - 0.2;
            double windZ = random.nextDouble() * 0.4 - 0.2;

            // Apply the wind force to the player's velocity
            player.setVelocity(player.getVelocity().add(new Vector(windX, 0.0, windZ)));
        }

        // Spawn wind particles
        for (Player player : world.getPlayers()) {
            Location playerLocation = player.getLocation();

            // Spawn wind particles around the player
            for (int i = 0; i < 10; i++) { // You can adjust the number of particles
                double xOffset = random.nextDouble() * 0.6 - 0.3;
                double zOffset = random.nextDouble() * 0.6 - 0.3;

                Location particleLocation = playerLocation.clone().add(xOffset, 1, zOffset);
                world.spawnParticle(Particle.CLOUD, particleLocation, 1);
            }
        }

        // Decrease the remaining duration
        duration--;
    }

    public void startEvent() {
        runTaskTimer(Main.getInstance(), 0, 20); // Adjust timing as needed (e.g., every second)
    }
}