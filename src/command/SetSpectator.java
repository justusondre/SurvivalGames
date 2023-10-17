package command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import file.ArenaFile;
import survivalgames.Main;

public class SetSpectator implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (cmd.getName().equalsIgnoreCase("setspectator")) {
				if (args.length != 1) {
					player.sendMessage(ChatColor.RED + "Wrong usage: /setspectator <arena id>");
					return true;
				}

	            String arenaName = args[0].toLowerCase();
				ArenaFile arenaFile = Main.getInstance().getFileManager().getArenaFile();

				if (arenaFile.config.contains("instance." + arenaName)) {
					arenaFile.setArenaLobby(Main.getInstance().getArenaRegistry().getArena(arenaName), player.getLocation());
					player.sendMessage(ChatColor.GRAY + "You have added a lobby spawn for " + ChatColor.GREEN + arenaName);
				} else {
					player.sendMessage(ChatColor.RED + "The specified arena does not exist!");
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Only players can use this command.");
		}
		return true;
	}
}