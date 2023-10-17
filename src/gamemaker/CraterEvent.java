package gamemaker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import survivalgames.Main;

public class CraterEvent {
    private final World world;
    private final Location centerLocation;
    private final int craterRadius;
    private final int craterDepth;

    public CraterEvent(World world, Location centerLocation, int craterRadius, int craterDepth) {
        this.world = world;
        this.centerLocation = centerLocation;
        this.craterRadius = craterRadius;
        this.craterDepth = craterDepth;
    }

    public void startEvent() {
        new BukkitRunnable() {
            private int currentRadius = 0;

            @Override
            public void run() {
                if (currentRadius >= craterRadius) {
                    this.cancel();
                    return;
                }

                createCraterLayer(centerLocation, currentRadius);
                currentRadius++;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20/3); 
    }

    private void createCraterLayer(Location centerLocation, int radius) {
        int x = centerLocation.getBlockX();
        int z = centerLocation.getBlockZ();
        int minY = centerLocation.getBlockY() - craterDepth; 
        int maxY = centerLocation.getBlockY() + craterDepth; 

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = minY; dy <= maxY; dy++) {
                    Location blockLocation = new Location(world, x + dx, dy, z + dz);
                    Block block = blockLocation.getBlock();

                    if (block.getType() != Material.AIR && blockLocation.distanceSquared(centerLocation) <= radius * radius) {
                        world.playSound(blockLocation, Sound.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }
}