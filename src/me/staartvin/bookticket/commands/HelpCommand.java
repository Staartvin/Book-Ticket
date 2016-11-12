package me.staartvin.bookticket.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class HelpCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (args[0].equalsIgnoreCase("help")) {

			if (args.length == 1) {
				showHelpPage(1, sender);
				return true;
			} else {
				Integer id = -1;

				try {
					id = Integer.parseInt(args[1]);
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED
							+ (args[1] + " is not a valid page number!"));
					return true;
				}

				showHelpPage(id, sender);
				return true;
			}
		}
		return true;
	}
	
	private void showHelpPage(int page, CommandSender sender) {
		int maximumPages = 2;
		if (page == 2) {
			sender.sendMessage(ChatColor.BLUE + "--------------["
					+ ChatColor.GOLD + "Book Ticket" + ChatColor.BLUE
					+ "]------------------");
			sender.sendMessage(ChatColor.GOLD + "/ticket tp <ticketID>"
					+ ChatColor.BLUE + " --- Teleport to ticket");
			sender.sendMessage(ChatColor.GOLD + "/ticket reply"
					+ ChatColor.BLUE + " --- Reply to a ticket");
			sender.sendMessage(ChatColor.GOLD + "/ticket back"
					+ ChatColor.BLUE + " --- Teleport back to your old location");
			sender.sendMessage(ChatColor.GOLD + "Page " + ChatColor.BLUE + "2 "
					+ ChatColor.GOLD + "of " + ChatColor.BLUE + maximumPages);
		} else {
			sender.sendMessage(ChatColor.BLUE + "--------------["
					+ ChatColor.GOLD + "Book Ticket" + ChatColor.BLUE
					+ "]------------------");
			sender.sendMessage(ChatColor.GOLD + "/ticket"
					+ ChatColor.BLUE + " --- Shows basic information");
			sender.sendMessage(ChatColor.GOLD + "/ticket list"
					+ ChatColor.BLUE + " --- Shows all open tickets");
			sender.sendMessage(ChatColor.GOLD + "/ticket mylist"
					+ ChatColor.BLUE + " --- Shows your open tickets");
			sender.sendMessage(ChatColor.GOLD + "/ticket new"
					+ ChatColor.BLUE + " --- Create a new ticket");
			sender.sendMessage(ChatColor.GOLD + "/ticket send"
					+ ChatColor.BLUE + " --- Send a new ticket");
			sender.sendMessage(ChatColor.GOLD + "/ticket open <ticketID>"
					+ ChatColor.BLUE + " --- Open a ticket");
			sender.sendMessage(ChatColor.GOLD + "/ticket close <ticketID>"
					+ ChatColor.BLUE + " --- Close a ticket");
			sender.sendMessage(ChatColor.GOLD + "/ticket help <page>"
					+ ChatColor.BLUE + " --- Show help page");
			sender.sendMessage(ChatColor.GOLD + "Page " + ChatColor.BLUE + "1 "
					+ ChatColor.GOLD + "of " + ChatColor.BLUE + maximumPages);
		}
	}
}
