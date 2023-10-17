package gamemaker;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import survivalgames.Main;

public class CorruptArenaEvent {
    private final Location centerLocation;
    private final int initialRadius;
    private final int finalRadius;
    private final Set<Location> corruptedLocations; // Store locations of corrupted blocks

    public CorruptArenaEvent(World world, Location centerLocation, int initialRadius, int finalRadius) {
        this.centerLocation = centerLocation;
        this.initialRadius = initialRadius;
        this.finalRadius = finalRadius;
        this.corruptedLocations = new HashSet<>();
    }

    public void startEvent() {
        spreadCorruption();
    }

    private void spreadCorruption() {
        new BukkitRunnable() {
            private int currentRadius = initialRadius;

            @Override
            public void run() {
                if (currentRadius >= finalRadius) {
                    this.cancel();
                    return;
                }

                for (int x = -currentRadius; x <= currentRadius; x++) {
                    for (int z = -currentRadius; z <= currentRadius; z++) {
                        for (int y = -currentRadius; y <= currentRadius; y++) {
                            Location targetLocation = centerLocation.clone().add(x, y, z);

                            // Check if the location is within the current radius
                            if (targetLocation.distanceSquared(centerLocation) <= currentRadius * currentRadius) {
                                // Get the block at the target location
                                Block targetBlock = targetLocation.getBlock();

                                // Check if the block is not air and is not already corrupted
                                if (targetBlock.getType() != Material.AIR && !isCorrupted(targetLocation)) {
                                    // Replace the block with Nether Wart Block or Magma Block
                                    Material replaceMaterial = getRandomMaterial();
                                    targetBlock.setType(replaceMaterial);

                                    corruptedLocations.add(targetLocation);
                                }
                            }
                        }
                    }
                }

                currentRadius++; // Increase the radius for the next iteration
            }
        }.runTaskTimer(Main.getInstance(), 0, 20 * 1); // Check every 5 seconds (adjust as needed)
    }

    private Material getRandomMaterial() {
        return Math.random() < 0.5 ? Material.NETHER_WART_BLOCK : Material.MAGMA_BLOCK;
    }

    private boolean isCorrupted(Location location) {
        return corruptedLocations.contains(location);
    }
}