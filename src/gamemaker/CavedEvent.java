package gamemaker;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import survivalgames.Main;

public class CavedEvent {
    private final World world;
    private final int eventDuration;
    private int ticksElapsed;

    public CavedEvent(World world, int eventDuration) {
        this.world = world;
        this.eventDuration = eventDuration;
        this.ticksElapsed = 0;
    }

    public void startEvent() {
        // Create a task to spawn falling blocks at random locations within a radius
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ticksElapsed >= eventDuration) {
                    // Event duration has ended, cancel the task
                    this.cancel();
                    return;
                }

                for (Player player : world.getPlayers()) {
                    Location playerLocation = player.getLocation();

                    // Determine the number of falling blocks and the radius (adjust as needed)
                    int numberOfBlocks = 20; // Adjust as needed
                    int radius = 10; // Adjust as needed

                    for (int i = 0; i < numberOfBlocks; i++) {
                        // Determine a random block type (use your desired types)
                        Material[] blockTypes = {Material.STONE, Material.DIRT, Material.GRASS};
                        Material randomBlock = blockTypes[new Random().nextInt(blockTypes.length)];

                        // Calculate random coordinates within the radius
                        double randomX = playerLocation.getX() + (Math.random() - 0.5) * 2 * radius;
                        double randomZ = playerLocation.getZ() + (Math.random() - 0.5) * 2 * radius;

                        // Find the highest non-air block above the random coordinates
                        Location fallingBlockLocation = new Location(world, randomX, playerLocation.getY(), randomZ);
                        Block highestBlock = world.getHighestBlockAt(fallingBlockLocation);
                        
                        if (highestBlock.getType() != Material.AIR) {
                            // Create a falling block of the chosen block type above the highest block
                            FallingBlock fallingBlock = world.spawnFallingBlock(highestBlock.getLocation(), randomBlock.createBlockData());
                            fallingBlock.setDropItem(false); // Prevent it from dropping as an item
                            fallingBlock.setHurtEntities(true); // Allow it to hurt entities (players)
                        }
                    }
                }

                ticksElapsed++;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20); // Check every second (adjust as needed)
    }
}