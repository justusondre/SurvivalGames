package gamemaker;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class SinkholeEvent extends BukkitRunnable {
    private Location centerLocation;
    private int duration;
    private int maxRadius;
    private int currentRadius;
    private Material fallingBlockMaterial;

    public SinkholeEvent(Location centerLocation, int duration, int maxRadius, Material fallingBlockMaterial) {
        this.centerLocation = centerLocation;
        this.duration = duration;
        this.maxRadius = maxRadius;
        this.currentRadius = 1;
        this.fallingBlockMaterial = fallingBlockMaterial;
    }

    @Override
    public void run() {
        if (duration <= 0 || currentRadius > maxRadius) {
            this.cancel(); // Stop the event
            return;
        }

        for (int x = -currentRadius; x <= currentRadius; x++) {
            for (int z = -currentRadius; z <= currentRadius; z++) {
                for (int y = centerLocation.getBlockY() - currentRadius; y <= centerLocation.getBlockY() + currentRadius; y++) {
                    double distance = Math.sqrt(x * x + z * z);

                    if (distance <= currentRadius) {
                        Location blockLocation = centerLocation.clone().add(x, y - centerLocation.getBlockY(), z);
                        Block block = blockLocation.getBlock();

                        if (block.getType() != Material.AIR) {
                            block.setType(Material.AIR);
                            block.getWorld().spawnFallingBlock(blockLocation, fallingBlockMaterial.createBlockData());
                        }
                    }
                }
            }
        }

        currentRadius++;
        duration--;
        
        Bukkit.broadcastMessage("The sinkhole is expanding!");

        if (duration <= 0 || currentRadius > maxRadius) {
            this.cancel(); 
        }
    }
}