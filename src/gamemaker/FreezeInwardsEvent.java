package gamemaker;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import survivalgames.Main;

public class FreezeInwardsEvent extends BukkitRunnable {
    private final Location centerLocation;
    private final int startRadius;
    private final int endRadius;
    private int currentRadius;

    public FreezeInwardsEvent(World world, Location centerLocation, int startRadius, int endRadius) {
        this.centerLocation = centerLocation;
        this.startRadius = startRadius;
        this.endRadius = endRadius;
        this.currentRadius = startRadius; // Set the initial radius to the startRadius
    }

    @Override
    public void run() {
        // Check if the event has reached its maximum radius
        if (currentRadius > endRadius) {
            this.cancel(); // Stop the event
            return;
        }

        // Replace blocks in the inner circle of the current radius
        if (currentRadius < startRadius) {
            replaceInnerCircleWithPackedIce();
        }

        // Increase the radius to move the freezing blocks inwards
        currentRadius++;

        // Broadcast a message or perform other actions as the event progresses
        Bukkit.broadcastMessage("Ice is spreading!");

        // Check if the event has reached its maximum radius
        if (currentRadius > endRadius) {
            this.cancel(); // Stop the event
        }
    }

    private void replaceInnerCircleWithPackedIce() {
        for (int x = -currentRadius; x <= currentRadius; x++) {
            for (int z = -currentRadius; z <= currentRadius; z++) {
                for (int y = -currentRadius; y <= currentRadius; y++) {
                    // Calculate the absolute coordinates
                    int absX = centerLocation.getBlockX() + x;
                    int absY = centerLocation.getBlockY() + y;
                    int absZ = centerLocation.getBlockZ() + z;

                    // Check if the block is inside the inner circle of the current radius
                    if (x * x + y * y + z * z <= currentRadius * currentRadius) {
                        Block block = new Location(centerLocation.getWorld(), absX, absY, absZ).getBlock();

                        // Check if the block is not Packed Ice, not air, and not bedrock
                        if (block.getType() != Material.OBSIDIAN && block.getType() != Material.AIR
                                && block.getType() != Material.BEDROCK) {
                            block.setType(Material.OBSIDIAN);
                        }
                    }
                }
            }
        }
    }

    public void startEvent() {
        // Start the event when needed
        this.runTaskTimer(Main.getInstance(), 0, 20); // Run the event every second (adjust timing as needed)
    }
}