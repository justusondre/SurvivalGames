package mechanics;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;


public class RideablePearls implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
    public void enderpearlLand(PlayerTeleportEvent e) {
        if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            e.setCancelled(true);
            Location tpLoc = e.getTo();

            if (tpLoc.getBlock().getType().equals(Material.AIR) && tpLoc.getBlock().getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                e.getPlayer().teleport(new Location(tpLoc.getWorld(), tpLoc.getX(), tpLoc.getY(), tpLoc.getZ(),
                        tpLoc.getYaw(), tpLoc.getPitch()));
            } else {
                e.getPlayer().teleport(e.getFrom());
            }
        }
    }
	
	@EventHandler
    public void throwEnder(ProjectileLaunchEvent e) {
        if(e.getEntity().getShooter() instanceof Player) {
            Player p = (Player) e.getEntity().getShooter();
            if(e.getEntity() instanceof EnderPearl) {
                EnderPearl ender = (EnderPearl) e.getEntity();
                ArmorStand as = (ArmorStand) ender.getWorld().spawnEntity(ender.getLocation(), EntityType.ARMOR_STAND);
                as.setVisible(false);
                as.setGravity(false);
                as.setSmall(true);
                as.setCanPickupItems(false);
                as.setArms(false);
                as.setBasePlate(false);
                as.setMarker(true);
                as.setMaxHealth(1);
                as.setHealth(1);
                ender.setPassenger(as);
                as.setPassenger(p);
            }
        }
    }

    @EventHandler
    public void hit(ProjectileHitEvent e) {
        if(e.getEntity() instanceof EnderPearl) {
            if(e.getEntity().getPassenger() instanceof ArmorStand) {
                e.getEntity().getPassenger().remove();
            }
        }
    }
}
