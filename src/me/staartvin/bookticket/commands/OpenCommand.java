package me.staartvin.bookticket.commands;

import java.io.File;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCommand implements CommandExecutor {

	private BookTicket plugin;
	
	public OpenCommand(BookTicket instance) {
		plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args[0].equalsIgnoreCase("open")) {
			
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can open tickets.");
				return true;
			}
			
			if (args.length != 2) {
				sender.sendMessage(ChatColor.RED + "Invalid command usage!");
				sender.sendMessage(ChatColor.YELLOW + "Usage: /ticket open <ticketID>");
				return true;
			}
			
			Player player = (Player) sender;
			
			int ticket = 1;
			
			try {
				ticket = Integer.parseInt(args[1]);
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED + args[1] + " is not a valid ticket id");
				return true;
			}
			
			if (!plugin.getTicketHandler().validateTicket(ticket, sender)) return true;
			
			String author = plugin.getMainConfig().getAuthor(ticket);
			
			// Own ticket
			if (author.equalsIgnoreCase(player.getName())) {
				if (!sender.hasPermission("bookticket.open.self")) {
					sender.sendMessage(ChatColor.RED + "You are not allowed to open your own tickets!");
					return true;
				}
			} else {
				if (!sender.hasPermission("bookticket.open.other")) {
					sender.sendMessage(ChatColor.RED + "You are not allowed to open other tickets!");
					return true;
				}
			}
				
			
			File bookFile = new File(plugin.getDataFolder() + "/books", plugin.getMainConfig().getBookName(ticket) + ".txt");
			
			// If the player already has the ticket in his hand, don't give it again.
			
			if (!player.getItemInHand().hasItemMeta() || !player.getItemInHand().getItemMeta().hasDisplayName()) {
				plugin.getBookHandler().loadBook(player, bookFile);
			} else if (!BookTicket.fixName(player.getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Ticket " + ticket + " of " + plugin.getMainConfig().getAuthor(ticket))) {
				plugin.getBookHandler().loadBook(player, bookFile);
			}
			
			player.sendMessage(ChatColor.GOLD + "----------------------------");
			player.sendMessage(ChatColor.AQUA + "This ticket was created on: " + ChatColor.GOLD + plugin.getMainConfig().getTime(ticket));
			
			if (plugin.getMainConfig().isOpen(ticket)) {
				player.sendMessage(ChatColor.AQUA + "You can do " + ChatColor.GOLD + "/ticket tp " + ticket + ChatColor.AQUA + " to get to the location.");
				player.sendMessage(ChatColor.AQUA + "To reply, edit the book and do " + ChatColor.GOLD + "/ticket reply");	
			} else {
				player.sendMessage(ChatColor.AQUA + "This ticket is " + ChatColor.RED + "closed" + ChatColor.AQUA + ". You do not have to reply anymore.");
			}
		}
		
		return true;
	}
}
