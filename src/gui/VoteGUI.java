package gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import game.arena.Arena;
import game.arena.ArenaVote;
import survivalgames.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VoteGUI implements Listener {

	private static List<String> randomMaps; // Shared list for random maps
    private final List<String> mapNames;
    private final Map<UUID, String> playerVotes;
    private final Map<UUID, Integer> playerSelections;
    private final ArenaVote arenaVote; // ArenaVote instance

    public VoteGUI() {
        this.playerVotes = new HashMap<>();
        this.playerSelections = new HashMap<>();
        this.mapNames = Main.getInstance().getFileManager().getArenaFile().getArenasList();

        // Generate random maps once when the server starts
        if (randomMaps == null) {
            randomMaps = getRandomMaps(5);
        }

        // Initialize ArenaVote
        this.arenaVote = new ArenaVote();
    }

    public void openVoteGUI(Player player) {
        Inventory gui = Bukkit.createInventory(player, 54, "Vote for a Map");
        UUID playerId = player.getUniqueId();

        // Fill all slots with gray stained glass panes
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, createItemStack(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        // Use the shared list of random maps for slots 20-24
        int slot = 20;
        for (String mapName : randomMaps) {
            ItemStack mapItem = createMapItem(mapName, playerVotes.get(playerId));
            gui.setItem(slot, mapItem);
            slot++;
        }

        // Update the lore for all map items to reflect the current vote counts
        for (String mapName : randomMaps) {
            updateMapLore(mapName, arenaVote.getVotes(new Arena(mapName)), gui);
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Vote for a Map")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            UUID playerId = player.getUniqueId();

            if (clickedItem != null && clickedItem.getType() == Material.MAP) {
                String mapName = clickedItem.getItemMeta().getDisplayName();

                // Check if the player has already voted for this map
                if (playerVotes.containsKey(playerId) && playerVotes.get(playerId).equals(mapName)) {
                    player.sendMessage("You have already voted for " + mapName + "!");
                    return; // Exit early, no need to change the vote or lore
                }

                // Check if the player has already voted for a different map
                if (playerVotes.containsKey(playerId)) {
                    String previousVote = playerVotes.get(playerId);
                    Arena previousArena = new Arena(previousVote);

                    // Subtract the previous vote from the old map
                    arenaVote.removeVote(previousArena);

                    // Update the lore of the previous map to reflect the new vote count
                    updateMapLore(previousVote, arenaVote.getVotes(previousArena), player.getOpenInventory().getTopInventory());
                }

                // Unselect all other maps
                for (int slot = 20; slot <= 24; slot++) {
                    ItemStack mapItem = player.getOpenInventory().getItem(slot);
                    if (mapItem != null) {
                        mapItem.removeEnchantment(Enchantment.DURABILITY);
                    }
                }

                // Select the clicked map
                clickedItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                playerVotes.put(playerId, mapName);
                playerSelections.put(playerId, event.getSlot());
                player.sendMessage("You have voted for " + mapName + "!");

                // Increment the vote count for the corresponding Arena
                Arena arena = new Arena(mapName); // Create an Arena instance based on mapName
                arenaVote.addVote(arena);
                updateMapLore(mapName, arenaVote.getVotes(arena), player.getOpenInventory().getTopInventory());
            }
        }
    }

    private ItemStack createItemStack(Material material, String displayName) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack createMapItem(String mapName, String selectedMap) {
        ItemStack mapItem = new ItemStack(Material.MAP);
        ItemMeta itemMeta = mapItem.getItemMeta();
        itemMeta.setDisplayName(mapName);

        // Add lore displaying total votes
        int votes = arenaVote.getVotes(new Arena(mapName));
        List<String> lore = new ArrayList<>();
        lore.add("Total Votes: " + votes);
        itemMeta.setLore(lore);

        // Check if this map is selected
        if (mapName.equals(selectedMap)) {
            mapItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }

        mapItem.setItemMeta(itemMeta);
        return mapItem;
    }

    private List<String> getRandomMaps(int count) {
        List<String> randomMaps = new ArrayList<>();
        Random random = new Random();

        List<String> copyMapNames = new ArrayList<>(mapNames);
        while (randomMaps.size() < count && !copyMapNames.isEmpty()) {
            int randomIndex = random.nextInt(copyMapNames.size());
            String mapName = copyMapNames.get(randomIndex);
            randomMaps.add(mapName);
            copyMapNames.remove(randomIndex);
        }

        return randomMaps;
    }

    // Update the lore of the map item to display total votes
    private void updateMapLore(String mapName, int votes, Inventory inventory) {
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() == Material.MAP) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta.getDisplayName().equals(mapName)) {
                    List<String> lore = new ArrayList<>();
                    lore.add("Total Votes: " + votes);
                    itemMeta.setLore(lore);
                    itemStack.setItemMeta(itemMeta);
                    break;
                }
            }
        }
    }
}