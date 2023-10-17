package command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gui.VoteGUI;

public class Vote implements CommandExecutor {
	
    private final VoteGUI voteGUI;

    public Vote(VoteGUI voteGUI) {
        this.voteGUI = voteGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;
        voteGUI.openVoteGUI(player); // Open the VoteGUI for the player.
        return true;
    }
}