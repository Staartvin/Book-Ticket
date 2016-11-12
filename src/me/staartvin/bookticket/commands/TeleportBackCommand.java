package me.staartvin.bookticket.commands;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class TeleportBackCommand implements CommandExecutor {

	private BookTicket plugin;

	public TeleportBackCommand(BookTicket instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (args[0].equalsIgnoreCase("back")) {

			if (!sender.hasPermission("bookticket.tp.back")) {
				sender.sendMessage(ChatColor.RED
						+ "You are not allowed to teleport back!");
				return true;
			}

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED
						+ "Only players can teleport.");
				return true;
			}

			Player player = (Player) sender;
			
			Location oldLoc = plugin.getLocStorage().getLastLocation(player.getName());
			
			if (oldLoc == null) {
				sender.sendMessage(ChatColor.RED + "You do not have an old location!");
				return true;
			}

			// Teleport player back
			player.teleport(oldLoc);
			
			player.sendMessage(ChatColor.GREEN + "You have been teleported back to where you were before.");
		}
		return true;
	}
}
