package gamemaker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import survivalgames.Main;

public class FloorRemoveEvent {
    private final World world;
    private final Location centerLocation;
    private final int startY;
    private final int endY;
    private final int maxRadius;

    public FloorRemoveEvent(World world, Location centerLocation, int startY, int endY, int maxRadius) {
        this.world = world;
        this.centerLocation = centerLocation;
        this.startY = startY;
        this.endY = endY;
        this.maxRadius = maxRadius;
    }

    public void startEvent() {
        new BukkitRunnable() {
            private int currentY = startY;

            @Override
            public void run() {
                if (currentY > endY) {
                    this.cancel();
                    return;
                }

                removeFloorLayer(centerLocation, currentY, maxRadius);
                currentY++;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

    private void removeFloorLayer(Location centerLocation, int currentY, int radius) {
        int x = centerLocation.getBlockX();
        int z = centerLocation.getBlockZ();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = currentY; dy <= currentY; dy++) {
                    Location blockLocation = new Location(world, x + dx, dy, z + dz);
                    Block block = blockLocation.getBlock();

                    if (block.getType() != Material.AIR && blockLocation.distanceSquared(centerLocation) <= radius * radius) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }
}