package mechanics;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Fireballs implements Listener {

	FireballCooldown fireballCooldown = new FireballCooldown();

	@EventHandler
	public void FireBallThrow(PlayerInteractEvent event) {
		if (event.getPlayer().getInventory().getItemInHand() != null
				&& (event.getPlayer().getInventory().getItemInHand().getType() == Material.LEGACY_FIREBALL)) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK
					|| event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
				Player p = event.getPlayer();

				if (!fireballCooldown.players.contains(event.getPlayer())) {
					p.launchProjectile(Fireball.class).setVelocity(p.getLocation().getDirection().multiply(1.5));
					event.setCancelled(true);

					fireballCooldown.addPlayer(event.getPlayer());

					ItemStack is = p.getItemInHand();
					if (is.getAmount() > 1) {
						is.setAmount(is.getAmount() - 1);

					}

					else {
						p.setItemInHand(null);
					}

				} else {
					event.getPlayer().sendMessage(ChatColor.RED + "Please wait 0.5s to use that again");
				}
			}
		}
	}

	@EventHandler
	public void hitfireball(ProjectileHitEvent event) {
		EntityType fball = event.getEntityType();
		if (fball != null && fball.equals(EntityType.FIREBALL)) {
			Fireball f = (Fireball) event.getEntity();
			f.setBounce(false);

		}
	}

	public void onExplosionPrime(ExplosionPrimeEvent event) {
		event.setFire(false);
		Entity ent = event.getEntity();
		if (ent instanceof Fireball)
			event.setRadius(5);
	}
}