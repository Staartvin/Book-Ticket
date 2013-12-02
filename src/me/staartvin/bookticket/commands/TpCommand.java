package me.staartvin.bookticket.commands;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class TpCommand {

	private BookTicket plugin;

	public TpCommand(BookTicket instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {

			if (!sender.hasPermission("bookticket.tp")) {
				sender.sendMessage(ChatColor.RED
						+ "You are not allowed to teleport to tickets!");
				return true;
			}

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED
						+ "Only players can teleport to tickets.");
				return true;
			}

			Player player = (Player) sender;
			String ticketTitle = null;
			int ticket = 1;
			Location location = null;

			// /suppport tp (Use book)
			if (args.length == 1) {

				if (player.getItemInHand() == null
						|| !player.getItemInHand().getType()
								.equals(Material.BOOK_AND_QUILL)) {
					player.sendMessage(ChatColor.RED
							+ "You are not holding a book in your hand!");
					return true;
				}

				if (!player.getItemInHand().hasItemMeta()) {
					player.sendMessage(ChatColor.RED
							+ "This book is not a ticket!");
					return true;
				}

				if (!player.getItemInHand().getItemMeta().hasDisplayName()) {
					player.sendMessage(ChatColor.RED
							+ "This book is not a ticket!");
					return true;
				}

				if (!player.getItemInHand().getItemMeta().getDisplayName()
						.contains("Ticket ")
						|| !player.getItemInHand().getItemMeta()
								.getDisplayName().contains(" of ")) {
					player.sendMessage(ChatColor.RED
							+ "This book is not a valid ticket.");
					return true;
				}

				ticketTitle = plugin.getBookHandler().getTitleOfBook(
						player.getItemInHand());

				ticket = plugin.getMainConfig().getTicketIdByTitle(null,
						ticketTitle);

			} else if (args.length == 2) {
				// Use a ticket id;

				try {
					ticket = Integer.parseInt(args[1]);
				} catch (Exception e) {
					player.sendMessage(ChatColor.RED + args[1]
							+ " is not a valid ticket number!");
					return true;
				}

				if (!plugin.getTicketHandler().validateTicket(ticket, sender))
					return true;
			} else {
				sender.sendMessage(ChatColor.RED + "Invalid command usage!");
				sender.sendMessage(ChatColor.YELLOW + "Usage: /ticket tp (id)");
				return true;
			}

			location = plugin.getMainConfig().getLocation(ticket);
			
			// Store old location for player
			plugin.getLocStorage().setLastLocation(player.getName(), player.getLocation());

			player.teleport(location);
			
			player.sendMessage(ChatColor.GOLD + "----------------------------");
			player.sendMessage(ChatColor.AQUA + "You are at the location of "
					+ ChatColor.GOLD + plugin.getMainConfig().getAuthor(ticket)
					+ ChatColor.AQUA + "'s ticket #" + ChatColor.GOLD + ticket);
		}
		return true;
	}
}
