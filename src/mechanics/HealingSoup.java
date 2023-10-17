package mechanics;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HealingSoup implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if ((((event.getAction() == Action.RIGHT_CLICK_AIR) ? 1 : 0)
				| ((event.getAction() == Action.RIGHT_CLICK_BLOCK) ? 1 : 0)) != 0
				&& event.getMaterial().equals(Material.MUSHROOM_STEW)) {
			event.setCancelled(true);

			if (player.getFoodLevel() < 20) {
				player.setFoodLevel(player.getFoodLevel() + 3);
				ItemStack bowl = new ItemStack(Material.BOWL, 1);
				player.getInventory().setItemInHand(bowl);
				player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 2.0F, 2.0F);
				player.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 20 * 5, 1)));
			}
		}
	}
}
