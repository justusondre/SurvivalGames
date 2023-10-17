package gamemaker;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import survivalgames.Main;

public class FreezeArenaEvent extends BukkitRunnable {
    private final Location centerLocation;
    private final int endRadius;
    private int currentRadius;

    public FreezeArenaEvent(World world, Location centerLocation, int startRadius, int endRadius) {
        this.centerLocation = centerLocation;
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

        // Replace nearby non-Packed Ice blocks within the current radius with Packed Ice
        for (int x = -currentRadius; x <= currentRadius; x++) {
            for (int z = -currentRadius; z <= currentRadius; z++) {
                for (int y = -currentRadius; y <= currentRadius; y++) {
                    Location blockLocation = centerLocation.clone().add(x, y, z);
                    Block block = blockLocation.getBlock();

                    // Check if the block is not Packed Ice, not air, and is within the current radius
                    if (block.getType() != Material.PACKED_ICE && block.getType() != Material.AIR
                            && blockLocation.distanceSquared(centerLocation) <= currentRadius * currentRadius) {
                        block.setType(Material.PACKED_ICE);
                    }
                }
            }
        }

        // Increase the radius
        currentRadius++;

        // Broadcast a message or perform other actions as the event progresses
        Bukkit.broadcastMessage("Ice is spreading across the arena!");

        // Check if the event has reached its maximum radius
        if (currentRadius > endRadius) {
            this.cancel(); // Stop the event
        }
    }

    public void startEvent() {
        this.runTaskTimer(Main.getInstance(), 0, 10); // Run the event every second
    }
}