package command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import file.ArenaFile;
import net.md_5.bungee.api.ChatColor;
import survivalgames.Main;

public class SetPlayerSpawn implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("setplayerspawn")) {
                Player player = (Player) sender;

                if (args.length != 1) {
                    player.sendMessage(ChatColor.RED + "Wrong usage: /setplayerspawn <arena id>");
                    return true;
                }

                try {
                    String arenaName = args[0].toLowerCase();

                    ArenaFile arenaFile = Main.getInstance().getFileManager().getArenaFile();

                    if (arenaFile.config.contains("instance." + arenaName)) {
                        arenaFile.addPlayerSpawnPoint(arenaName, player.getLocation());
                        player.sendMessage(ChatColor.GRAY + "You have added spawnpoint for " + ChatColor.GREEN + arenaName);
                    } else {
                        player.sendMessage(ChatColor.RED + "The specified arena does not exist!");
                    }
                } catch (NumberFormatException ex) {
                    player.sendMessage(ChatColor.RED + "You need to identify a valid arena name!");
                }
            }
        }
        return true;
    }
}