package me.staartvin.bookticket.commands;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CloseCommand {

	private BookTicket plugin;

	public CloseCommand(BookTicket instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (args[0].equalsIgnoreCase("close")) {

			if (!sender.hasPermission("bookticket.close")) {
				sender.sendMessage(ChatColor.RED
						+ "You are not allowed to close tickets!");
				return true;
			}

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

			if (plugin.getTicketHandler().closeTicket(ticket)) {
				sender.sendMessage(ChatColor.AQUA + "Ticket #" + ChatColor.GOLD
						+ ticket + ChatColor.AQUA + " of " + ChatColor.GOLD
						+ author + ChatColor.AQUA + " has been closed!");

				if (plugin.getServer().getPlayer(author) != null) {
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
