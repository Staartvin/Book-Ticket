package me.staartvin.bookticket.commands;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand {

	private BookTicket plugin;

	public ReplyCommand(BookTicket instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (args[0].equalsIgnoreCase("reply")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED
						+ "Only players can reply to tickets.");
				return true;
			}

			Player player = (Player) sender;

			if (player.getItemInHand() != null
					&& player.getItemInHand().getType()
							.equals(Material.WRITTEN_BOOK)) {
				player.sendMessage(ChatColor.RED
						+ "You do not need to sign the book!");
				return true;
			}

			if (player.getItemInHand() == null
					|| !player.getItemInHand().getType()
							.equals(Material.BOOK_AND_QUILL)) {
				player.sendMessage(ChatColor.RED
						+ "You are not holding a book in your hand!");
				return true;
			}

			if (!player.getItemInHand().hasItemMeta()) {
				player.sendMessage(ChatColor.RED
						+ "You haven't written anything in this book!");
				return true;
			}

			if (!player.getItemInHand().getItemMeta().hasDisplayName()) {
				player.sendMessage(ChatColor.RED + "This book is not a ticket!");
				return true;
			}

			if (!player.getItemInHand().getItemMeta().getDisplayName()
					.contains("Ticket ")
					|| !player.getItemInHand().getItemMeta().getDisplayName()
							.contains(" of ")) {
				player.sendMessage(ChatColor.RED
						+ "This book is not a valid ticket.");
				return true;
			}
			String title = plugin.getBookHandler().getTitleOfBook(
					player.getItemInHand());
			
			int ticket = plugin.getMainConfig().getTicketIdByTitle(null, title);
			
			// Check for closed ticket
			if (!plugin.getTicketHandler().doesTicketExist(ticket)) {
				player.sendMessage(ChatColor.RED + "This ticket doesn't exist (anymore)!");
				return true;
			}
			
			// Reply to self made ticket
			if (plugin.getMainConfig().getAuthor(ticket).equalsIgnoreCase(player.getName())) {
				if (!sender.hasPermission("bookticket.reply.self")) {
					sender.sendMessage(ChatColor.RED
							+ "You are not allowed to reply!");
					return true;
				}
			} else {
				if (!sender.hasPermission("bookticket.reply.other")) {
					sender.sendMessage(ChatColor.RED
							+ "You are not allowed to reply!");
					return true;
				}
			}
			
			String author = plugin.getBookHandler().replyBook(player, 
					player.getItemInHand());

			if (author == null) {
				player.sendMessage(ChatColor.RED
						+ "ERROR: Could not get author of ticket!");
				return true;
			}

			if (plugin.getServer().getPlayer(author) != null) {
				plugin.getServer()
						.getPlayer(author)
						.sendMessage(
								ChatColor.GOLD
										+ player.getName()
										+ ChatColor.AQUA
										+ " has replied to your ticket #"
										+ ChatColor.GOLD
										+ plugin.getMainConfig()
												.getTicketIdByTitle(author,
														title));
			}

			player.sendMessage(ChatColor.AQUA + "You replied to "
					+ ChatColor.GOLD + author + ChatColor.AQUA + "'s ticket.");
			player.setItemInHand(null);

			return true;
		}

		return true;
	}
}
