package me.staartvin.bookticket.commands;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NewCommand implements CommandExecutor {

	private BookTicket plugin;

	public NewCommand(BookTicket instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args[0].equalsIgnoreCase("new")) {
			
			if (!sender.hasPermission("bookticket.new")) {
				sender.sendMessage(ChatColor.RED + "You are not allowed to create tickets!");
				return true;
			}
			
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can create tickets.");
				return true;
			}
			
			Player player = (Player) sender;
			
			
			// If a player has a full inventory
			if (player.getInventory().firstEmpty() < 0) {
				sender.sendMessage(ChatColor.RED + "You don't have space for a book!");
				return true;
			}
			
			// If a player hasn't sent their last book they requested.
			if (!plugin.getBookHandler().isNewTicket(player.getName())) {
				player.sendMessage(ChatColor.RED + "You haven't send the last book you requested.");
				return true;
			}

			// Give book
			player.getInventory().addItem(new ItemStack(Material.BOOK_AND_QUILL));
			
			// Set /ticket new use
			plugin.getBookHandler().setNewTicket(player.getName(), false);
			
			player.sendMessage(ChatColor.GOLD + "----------------------------");
			player.sendMessage(ChatColor.GREEN + "You have been given an empty book.");
			player.sendMessage(ChatColor.AQUA + "When you are done writing your problems, type " + ChatColor.GOLD + "'/ticket send (name)'");
			player.sendMessage(ChatColor.YELLOW + "You can add a name but it is not necessary. This helps staff to prioritise.");
			
			return true;
		}
		
		return true;
	}
}
