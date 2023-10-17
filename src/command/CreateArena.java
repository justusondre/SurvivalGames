package command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import file.ArenaFile;
import survivalgames.Main;
import utils.StringUtils;

public class CreateArena implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
	    if (sender instanceof Player) {
	        if (cmd.getName().equalsIgnoreCase("createarena")) {
	            Player player = (Player) sender;

	            if (args.length != 1) {
	                player.sendMessage(ChatColor.RED + "Wrong usage: /createarena <id>");
	                return true;
	            }

	            String arenaName = args[0].toLowerCase();
	        	ArenaFile arenaFile = Main.getInstance().getFileManager().getArenaFile();

	            if (arenaFile.config.contains("instance." + arenaName.toLowerCase())) {
	                player.sendMessage(ChatColor.RED + "That arena already exists!");

	            } else {

	                try {
	                	arenaFile.createArena(arenaName);
	                	sender.sendMessage("");
	                	StringUtils.sendCenteredMessage(sender, ChatColor.GRAY + "You have created an arena ID: " + ChatColor.GREEN + arenaName.toString());
	                	sender.sendMessage("");
	                } catch (Exception ex) {
	                    player.sendMessage(ChatColor.RED + "An error occurred while creating the arena.");
	                }
	            }
	        }
	    }
	    return true;
	}
}