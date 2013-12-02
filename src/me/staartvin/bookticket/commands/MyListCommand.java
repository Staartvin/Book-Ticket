package me.staartvin.bookticket.commands;

import java.util.List;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MyListCommand {

	private BookTicket plugin;

	public MyListCommand(BookTicket instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (args[0].equalsIgnoreCase("mylist")) {

			if (!sender.hasPermission("bookticket.mylist")) {
				sender.sendMessage(ChatColor.RED
						+ "You are not allowed to view a list of your tickets!");
				return true;
			}

			List<String> myTickets = plugin.getMainConfig().getTicketsOfAuthor(sender.getName());

			StringBuilder ticketList = new StringBuilder();
			for (int i = 0; i < myTickets.size(); i++) {
				String ticket = myTickets.get(i);

				if (i == (myTickets.size() - 1)) {
					ticketList.append(ticket);
				} else {
					ticketList.append(ticket + ", ");
				}
			}

			sender.sendMessage(ChatColor.GOLD + "----------------------------");
			if (myTickets.size() == 0) {
				sender.sendMessage(ChatColor.AQUA + "You have no tickets.");
				return true;
			}
			sender.sendMessage(ChatColor.AQUA + "Your tickets: ");
			sender.sendMessage(ChatColor.DARK_GREEN + ticketList.toString());
		}

		return true;
	}
}
