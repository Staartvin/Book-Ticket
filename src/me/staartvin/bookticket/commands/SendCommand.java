package me.staartvin.bookticket.commands;

import java.util.ArrayList;
import java.util.List;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class SendCommand implements CommandExecutor {

	private BookTicket plugin;

	public SendCommand(BookTicket instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (args[0].equalsIgnoreCase("send")) {

			if (!sender.hasPermission("bookticket.send")) {
				sender.sendMessage(ChatColor.RED
						+ "You are not allowed to send books!");
				return true;
			}

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED
						+ "Only players can send books.");
				return true;
			}

			Player player = (Player) sender;

			// It is a written book
			if (player.getItemInHand() != null
					&& player.getItemInHand().getType()
							.equals(Material.WRITTEN_BOOK)) {
				
				
				// Change it to a not-written book
				
				BookMeta bMeta = (BookMeta) player.getItemInHand().getItemMeta();
				ItemStack newItem = new ItemStack(Material.BOOK_AND_QUILL, player.getItemInHand().getAmount());
				
				newItem.setItemMeta(bMeta);
				player.setItemInHand(newItem);
				
				/*player.sendMessage(ChatColor.RED
						+ "You do not need to sign the book!");
				return true;*/
			}

			if (player.getItemInHand() == null
					|| !player.getItemInHand().getType()
							.equals(Material.BOOK_AND_QUILL)) {
				player.sendMessage(ChatColor.RED
						+ "You are not holding a book in your hand!");
				return true;
			}

			String title = plugin.getBookHandler().saveBook(player,
					player.getItemInHand());
			String subTitle = "";

			if (args.length != 1) {
				List<String> arguments = new ArrayList<String>();

				// Fill result list
				for (int i = 0; i < args.length; i++) {
					String argument = args[i];

					// Don't include 'set', 'variable' and 'scrollname'
					if (i == 0)
						continue;

					arguments.add(argument);
				}
				
				subTitle = plugin.getBookHandler().convertToStringForArguments(arguments);
			}
			

			if (title != null) {
					plugin.getTicketHandler()
							.createNewTicket(player, title, subTitle);
					
					plugin.getBookHandler().setNewTicket(player.getName(), true);

				return true;
			} else {
				player.sendMessage(ChatColor.RED
						+ "Something went wrong with your book! Try again.");
				return true;
			}

		}

		return true;
	}
}
