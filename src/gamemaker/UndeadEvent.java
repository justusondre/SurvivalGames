package gamemaker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

import survivalgames.Main;

public class UndeadEvent {
    private final World world;
    private final Location centerLocation;
    private final int craterRadius;
    private final int craterDepth;
    private final int zombieCount;
    private final double zombieSpeed;

    public UndeadEvent(World world, Location centerLocation, int craterRadius, int craterDepth, int zombieCount, double zombieSpeed) {
        this.world = world;
        this.centerLocation = centerLocation;
        this.craterRadius = craterRadius;
        this.craterDepth = craterDepth;
        this.zombieCount = zombieCount;
        this.zombieSpeed = zombieSpeed;
    }

    public void startEvent() {
        // Start the crater formation
        new BukkitRunnable() {
            private int currentRadius = 0;

            @Override
            public void run() {
                if (currentRadius >= craterRadius) {
                    this.cancel();
                    spawnZombies();
                    return;
                }

                createCraterLayer(centerLocation, currentRadius);
                currentRadius++;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20); 
    }

    private void createCraterLayer(Location centerLocation, int radius) {
        int x = centerLocation.getBlockX();
        int z = centerLocation.getBlockZ();
        int minY = centerLocation.getBlockY() - craterDepth; 
        int maxY = centerLocation.getBlockY();

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

    private void spawnZombies() {
        for (int i = 0; i < zombieCount; i++) {
            Location spawnLocation = getRandomLocationInsideCrater();
            Zombie zombie = (Zombie) world.spawnEntity(spawnLocation, EntityType.ZOMBIE);
            zombie.setCustomName("Undead");
            zombie.setCustomNameVisible(true);
            zombie.setCanPickupItems(true);
            zombie.setAI(true);
            zombie.setSilent(true);
            zombie.setInvulnerable(false);
            zombie.setRemoveWhenFarAway(true);
            zombie.setCollidable(true);
            zombie.setVelocity(spawnLocation.getDirection().multiply(zombieSpeed));
        }
    }

    private Location getRandomLocationInsideCrater() {
        int x = centerLocation.getBlockX();
        int z = centerLocation.getBlockZ();
        int minY = centerLocation.getBlockY() - craterDepth; // Adjust depth here
        int maxY = centerLocation.getBlockY();

        int randomX = x - craterRadius + (int) (Math.random() * (2 * craterRadius + 1));
        int randomZ = z - craterRadius + (int) (Math.random() * (2 * craterRadius + 1));
        int randomY = minY + (int) (Math.random() * (maxY - minY + 1));

        return new Location(world, randomX + 0.5, randomY + 0.5, randomZ + 0.5);
    }
}