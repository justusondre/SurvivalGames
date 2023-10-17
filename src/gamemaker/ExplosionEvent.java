package gamemaker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import survivalgames.Main;

public class ExplosionEvent extends BukkitRunnable {
    private final World world;
    private final Location location;
    private final int maxRadius;
    private final int maxDepth;
    private int currentRadius;
    private int currentDepth;

    public ExplosionEvent(World world, Location location, int maxRadius, int maxDepth) {
        this.world = world;
        this.location = location;
        this.maxRadius = maxRadius;
        this.maxDepth = maxDepth;
        this.currentRadius = 1;
        this.currentDepth = 1;
    }

    @Override
    public void run() {
        if (currentRadius <= maxRadius && currentDepth <= maxDepth) {
            simulateExplosion(location, currentRadius, currentDepth);
            playExplosionEffects(location, currentRadius);
            currentRadius++;
            currentDepth++;
        } else {
            this.cancel(); // Stop the event
        }
    }
    
    private void simulateExplosion(Location centerLocation, int radius, int depth) {
        int x = centerLocation.getBlockX();
        int y = centerLocation.getBlockY();
        int z = centerLocation.getBlockZ();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -depth; dy <= depth; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    Location blockLocation = new Location(world, x + dx, y + dy, z + dz);
                    Block block = blockLocation.getBlock();

                    if (block.getType() != Material.AIR && blockLocation.distanceSquared(centerLocation) <= radius * radius) {
                        block.setType(Material.AIR);
                     }
                }
            }
        }
    }

    private void playExplosionEffects(Location centerLocation, int radius) {
        for (double angle = 0; angle < 2 * Math.PI; angle += Math.PI / 16) {
            double xOffset = Math.cos(angle) * radius;
            double zOffset = Math.sin(angle) * radius;
            Location soundLocation = centerLocation.clone().add(xOffset, 0, zOffset);

            world.playSound(soundLocation, Sound.AMBIENT_BASALT_DELTAS_ADDITIONS, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.playSound(soundLocation, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.1f, 0.1f);
        }

        int particleCount = 1000; 
        for (int i = 0; i < particleCount; i++) {
            double theta = Math.random() * Math.PI;
            double phi = Math.random() * 2 * Math.PI;
            double r = Math.pow(Math.random(), 1.0 / 3.0) * radius;

            double x = r * Math.sin(theta) * Math.cos(phi);
            double y = r * Math.sin(theta) * Math.sin(phi);
            double z = r * Math.cos(theta);

            Location particleLocation = centerLocation.clone().add(x, y, z);

            world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, particleLocation, 1, 0, 0, 0, 0);
            world.spawnParticle(Particle.EXPLOSION_NORMAL, particleLocation, 1, 0, 0, 0, 0);        }

        for (Player player : world.getPlayers()) {
            Location playerLocation = player.getLocation();

            if (playerLocation.distance(centerLocation) <= radius) {
                Vector direction = playerLocation.toVector().subtract(centerLocation.toVector()).normalize();
                player.setVelocity(direction.multiply(2.0)); 
            }
        }
    }
    
    public void startEvent() {
        runTaskTimer(Main.getInstance(), 0, 5);
    }
}