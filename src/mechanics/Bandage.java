package mechanics;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import game.GameState;
import game.arena.Arena;

public class Bandage implements Listener {

	boolean blockNextHeal = false;
	private Arena arena;

	@EventHandler
	public void onPlayerUse(PlayerInteractEvent event) {
		if (!this.blockNextHeal) {
			Player p = event.getPlayer();
			ItemStack pItemInHand = p.getInventory().getItemInMainHand();
			if (((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
					&& pItemInHand.getType() == Material.PAPER) && arena.getArenaState() != GameState.WAITING_FOR_PLAYERS) {
				event.setCancelled(true);
				Player player = p;
				double pMaxHealth = player.getMaxHealth();
				double pCurrentHealth = player.getHealth();
				if (pMaxHealth != pCurrentHealth) {
					if (pMaxHealth - pCurrentHealth < 4.0D) {
						p.setHealth(pMaxHealth);
					} else {
						p.setHealth(pCurrentHealth + 4.0D);
					}
					if (pItemInHand.getAmount() > 1) {
						pItemInHand.setAmount(pItemInHand.getAmount() - 1);
					} else {
						p.getInventory().remove(pItemInHand);
					}
					p.sendMessage(ChatColor.YELLOW + "You have healed yourself!");
					p.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 4);
					p.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 2.0F, 2.0F);
				} else {
					p.sendMessage(ChatColor.YELLOW + "You already have full health!");
				}
			}
		} else {
			this.blockNextHeal = false;
		}
	}

	@EventHandler
	public void onPlayerClick(PlayerInteractEntityEvent event) {
		Entity cE = event.getRightClicked();
		if (cE instanceof Player) {
			Player cP = (Player) event.getRightClicked();
			Player p = event.getPlayer();
			if (cP instanceof Player) {
				ItemStack pItemInHand = p.getInventory().getItemInMainHand();
				if (pItemInHand.getType() == Material.PAPER) {
					Player player = cP;
					double cpMaxHealth = player.getMaxHealth();
					double cpCurrentHealth = player.getHealth();
					if (cpMaxHealth != cpCurrentHealth) {
						if (cpMaxHealth - cpCurrentHealth < 4.0D) {
							cP.setHealth(cpMaxHealth);
						} else {
							cP.setHealth(cpCurrentHealth + 4.0D);
						}
						if (pItemInHand.getAmount() > 1) {
							pItemInHand.setAmount(pItemInHand.getAmount() - 1);
							this.blockNextHeal = true;
						} else {
							p.getInventory().remove(pItemInHand);
						}
						p.sendMessage(ChatColor.YELLOW + cP.getName() + " has been healed!");
						p.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 2.0F, 2.0F);

						cP.sendMessage(ChatColor.YELLOW + "You have been healed!");
						cP.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 4);
						cP.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 2.0F, 2.0F);
					} else {
						p.sendMessage(ChatColor.YELLOW + cP.getName() + " already has full health!");
					}
				}
			}
		}
	}
}