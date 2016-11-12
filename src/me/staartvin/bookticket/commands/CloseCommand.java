package me.staartvin.bookticket.commands;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CloseCommand implements CommandExecutor {

	private BookTicket plugin;

	public CloseCommand(BookTicket instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (args[0].equalsIgnoreCase("close")) {

			if (args.length != 2) {
				sender.sendMessage(ChatColor.RED + "Invalid command usage!");
				sender.sendMessage(ChatColor.YELLOW
						+ "Usage: /ticket close <ticketID>");
				return true;
			}

			int ticket = 1;

			try {
				ticket = Integer.parseInt(args[1]);
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED + args[1]
						+ " is not a valid ticket id");
				return true;
			}

			if (!plugin.getTicketHandler().validateTicket(ticket, sender)) return true;

			String author = plugin.getMainConfig().getAuthor(ticket);
			
			// If players are not allowed to close their own tickets, check their permissions first.
			if (!plugin.getMainConfig().allowClosingOwnTickets()) {
				if (!sender.hasPermission("bookticket.close")) {
					sender.sendMessage(ChatColor.RED
							+ "You are not allowed to close tickets!");
					return true;
				}
			} else {
				// Check if sender is also author - hence closing its own book
				
				// If sender is NOT closing its own book, check for permission
				if (!sender.getName().equalsIgnoreCase(author)) {
					if (!sender.hasPermission("bookticket.close")) {
						sender.sendMessage(ChatColor.RED
								+ "You are not allowed to close tickets!");
						return true;
					}
				} // Else, sender is closing its own book (which is allowed).
			}

			if (plugin.getTicketHandler().closeTicket(ticket)) {
				sender.sendMessage(ChatColor.AQUA + "Ticket #" + ChatColor.GOLD
						+ ticket + ChatColor.AQUA + " of " + ChatColor.GOLD
						+ author + ChatColor.AQUA + " has been closed!");

				// Check if the author of the ticket is online and he did not close it himself.
				if (plugin.getServer().getPlayer(author) != null && !sender.getName().equalsIgnoreCase(author)) {
					plugin.getServer()
							.getPlayer(author)
							.sendMessage(
									ChatColor.GOLD + sender.getName()
											+ ChatColor.AQUA
											+ " has closed your ticket #"
											+ ChatColor.GOLD + ticket);
				}
			} else {
				sender.sendMessage(ChatColor.RED
						+ "ERROR: Could not close ticket. File problems?");
			}
		}
		return true;
	}
}
