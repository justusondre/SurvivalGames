package mechanics;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ThrowableTNT implements Listener {

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		event.setCancelled(true);
		event.getLocation().getBlock().getWorld().playEffect(event.getLocation(), Effect.EXTINGUISH, 1);
		event.getLocation().getBlock().getWorld().playEffect(event.getLocation(), Effect.MOBSPAWNER_FLAMES, 3);	}

	 @EventHandler
	 public void onTNTPlace(BlockPlaceEvent event){

		 if(event.getBlock().getType().equals(Material.TNT)){
			 event.getBlock().setType(Material.AIR);
		 ((TNTPrimed)event.getBlock().getLocation().getWorld().spawn(event.getBlock().getLocation(), TNTPrimed.class)).setFuseTicks(35);

	 	}
	}
	 
	 @EventHandler
	    public void onEntityExplode(ExplosionPrimeEvent event){
	        if(event.getEntity().getType() == EntityType.PRIMED_TNT){
	            event.setRadius(4.0F);
	        }
	    }
	 
	 @EventHandler
	    public void onDamage(EntityDamageEvent e){
	        if(e.getCause()== EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
	            e.setDamage(4);
	        }
	    }
	 
	@EventHandler
	  public void onTNT(EntityExplodeEvent e) {
	    Location loc = e.getLocation();
	    for (Entity entity : loc.getWorld().getEntities()) {
	      if (entity.getLocation().getWorld() != loc.getWorld())
	        return; 
	      double distance = (e.getYield() * 16.0F);
	      distance *= 2; //change this variable
	      if (loc.distance(entity.getLocation()) <= distance) {
	        if (entity instanceof Player) {
	          EntityDamageEvent DamageEvent = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, 
	              distance - loc.distance(entity.getLocation()));
	          Bukkit.getPluginManager().callEvent((Event)DamageEvent);
	          if (!DamageEvent.isCancelled())
	            ((Player)entity).damage(DamageEvent.getFinalDamage()); 
	        } 
	        double difZ = entity.getLocation().getZ() - loc.getZ();
	        double difX = entity.getLocation().getX() - loc.getX();
	        double Z = difZ;
	        double X = difX;
	        if (X > 0.0D) {
	          if (X > distance / 2.0D)
	            X = distance / 2.0D - X - distance / 2.0D; 
	        } else if (X < -(distance / 2.0D)) {
	          X = -(distance / 2.0D) - distance / 2.0D + X;
	        } 
	        if (Z > 0.0D) {
	          if (Z > distance / 2.0D)
	            Z = distance / 2.0D - Z - distance / 2.0D; 
	        } else if (Z < -(distance / 2.0D)) {
	          Z = -(distance / 2.0D) - distance / 2.0D + Z;
	        } 
	        double Y = distance - loc.distance(entity.getLocation()) - Math.sqrt(difX * difX + difZ * difZ);
	        if (X < 0.5D && X > -0.5D)
	          X *= 0.5D; 
	        if (Z < 0.5D && Z > -0.5D)
	          Z *= 0.5D; 
	        Y *= e.getYield() * 0.2D;
	        X *= 6; //change these variables
	        Y *= 3; //change these variables
	        Z *= 6; //change these variables
	        Vector v = entity.getVelocity();
	        if(e.getEntity().getType().equals(EntityType.PRIMED_TNT)) {
	        entity.setVelocity(new Vector(X + v.getX(), Y + v.getY(), Z + v.getZ()));
	      } 
	    } 
	  }
	}
	 
	 @EventHandler
	    public void PlayerAbilities(PlayerInteractEvent event) {
	        Player player = event.getPlayer();
	        if (event.getAction() == Action.LEFT_CLICK_AIR) {
	            if (player.getInventory().getItemInMainHand().getType() == Material.TNT) {
	                TNTPrimed tnt = (TNTPrimed) player.getWorld().spawn(
	                        player.getEyeLocation(), TNTPrimed.class);
	                tnt.setVelocity(player.getEyeLocation().getDirection()
	                        .multiply(2));
	                tnt.setFuseTicks(40);
	                int amount = player.getInventory().getItemInMainHand()
	                        .getAmount();
	                amount--;
	                if (amount == 0) {
	                    player.getInventory()
	                    .removeItem(
	                            new ItemStack[] { new ItemStack(
	                                    Material.TNT) });
	                } else
	                    player.getInventory().getItemInMainHand().setAmount(amount);
	            }
	        }
	    }
	}