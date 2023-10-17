package mechanics;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import survivalgames.Main;

public class ThrowableBlades implements Listener {
	
	ArrayList<ArmorStand> axes = new ArrayList<>();

	@EventHandler
	public void noManipulate(PlayerArmorStandManipulateEvent event) {
		if (axes.contains(event.getRightClicked()))
			event.setCancelled(true);
	}

	@EventHandler
	public void axeThrow(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR && event.getItem() != null
				&& (event.getPlayer().getInventory().getItemInHand().getType() == Material.NETHER_STAR)
				&& (event.getItem().getType() == Material.NETHER_STAR)) {
			Player p = event.getPlayer();
			Vector v = p.getLocation().add(p.getLocation().getDirection().multiply(10)).toVector()
					.subtract(p.getLocation().toVector()).normalize();
			axe((Entity) p, v, event.getItem());

			ItemStack is = p.getItemInHand();
			if (is.getAmount() > 1) {
				is.setAmount(is.getAmount() - 1);

			}

			else {
				p.setItemInHand(null);
			}
		}
	}

	@EventHandler
	public void axeThrow2(PlayerInteractEntityEvent event) {
		if (event.getPlayer().getInventory().getItemInHand() != null
				&& (event.getPlayer().getInventory().getItemInHand().getType() == Material.NETHER_STAR)) {
			Player p = event.getPlayer();
			Vector v = p.getLocation().add(p.getLocation().getDirection().multiply(10)).toVector()
					.subtract(p.getLocation().toVector()).normalize();
			axe((Entity) p, v, event.getPlayer().getInventory().getItemInHand());

			ItemStack is = p.getItemInHand();
			if (is.getAmount() > 1) {
				is.setAmount(is.getAmount() - 1);

			}

			else {
				p.setItemInHand(null);
			}
		}
	}

	public void axe(Entity e, Vector v, ItemStack m) {
		Location to = new Location(e.getWorld(), e.getLocation().getX(), e.getLocation().getY() + 1.0D,
				e.getLocation().getZ());
		ArmorStand a = (ArmorStand) e.getWorld().spawnEntity(to, EntityType.ARMOR_STAND);
		a.setVisible(false);
		a.setSmall(true);
		a.setItemInHand(m);
		axes.add(a);
		axe2(e, a, v.multiply(1.2D), 0, m);
	}

	public void axe2(final Entity ef, final ArmorStand a, final Vector v, final int recurse, final ItemStack m) {
		if (recurse < 300) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
				public void run() {
					for (Entity e : a.getNearbyEntities(1.0D, 1.0D, 1.0D)) {
						if (e != ef && e != a && e instanceof Damageable) {
							if (m.getType() == Material.NETHER_STAR)
								((Damageable) e).damage(6.0D);
							if (m.hasItemMeta() && m.getItemMeta().hasEnchant(Enchantment.FIRE_ASPECT))
								((Damageable) e).setFireTicks(20);
								a.getLocation().getWorld().dropItem(a.getLocation(), new ItemStack(Material.NETHER_STAR));
							axes.remove(a);
							a.remove();
							break;
						}
					}
					double x = a.getRightArmPose().getX();
					// X + 0.0D = makes it slanted/sideways
					a.setRightArmPose(new EulerAngle(x + 0.0D, 0.0D, 0.0D));
					Vector vec = new Vector(v.getX(), v.getY() - 0.03D, v.getZ());
					a.setVelocity(vec);
					if (!a.isDead() && a.isOnGround()) {
						a.getLocation().getWorld().dropItem(a.getLocation(), new ItemStack(Material.NETHER_STAR));
						axes.remove(a);
						a.remove();
					} else if (!a.isDead()) {
						axe2(ef, a, vec, recurse + 1, m);
					}
				}
			}, 2L);
		} else {
			a.getLocation().getWorld().dropItem(a.getLocation(), new ItemStack(Material.NETHER_STAR));
			axes.remove(a);
			a.remove();
		}
	}
}