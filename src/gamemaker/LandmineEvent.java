package gamemaker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import survivalgames.Main;

public class LandmineEvent {
    private final World world;
    private final Location centerLocation;
    private final int radius;

    public LandmineEvent(World world, Location centerLocation, int radius) {
        this.world = world;
        this.centerLocation = centerLocation;
        this.radius = radius;
    }

    public void startEvent() {
        spawnLandmines();
    }

    private void spawnLandmines() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        for (int y = -radius; y <= radius; y++) {
                            Location targetLocation = centerLocation.clone().add(x, y, z);

                            if (targetLocation.distanceSquared(centerLocation) <= radius * radius) {
                                Block targetBlock = targetLocation.getBlock();

                                if (targetBlock.getType() != Material.AIR) {
                                    if (Math.random() < 0.5) {
                                        targetBlock.setType(Material.RED_WOOL);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTask(Main.getInstance());

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : world.getPlayers()) {
                    Location playerLocation = player.getLocation();
                    Block playerBlock = playerLocation.getBlock();

                    if (playerBlock.getType() == Material.RED_WOOL) {
                        Location woolLocation = playerLocation.clone().subtract(0, 1, 0);

                        if (woolLocation.distanceSquared(centerLocation) <= radius * radius) {
                            detonateLandmine(woolLocation);

                            playerBlock.setType(Material.AIR);
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20); // Check every second (adjust as needed)
    }

    private void detonateLandmine(Location landmineLocation) {
        world.createExplosion(landmineLocation, 4.0f, true); 
    }
}