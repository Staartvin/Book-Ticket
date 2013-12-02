package me.staartvin.bookticket.commands;

import java.util.List;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ListCommand {

	private BookTicket plugin;

	public ListCommand(BookTicket instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (args[0].equalsIgnoreCase("list")) {

			if (!sender.hasPermission("bookticket.list")) {
				sender.sendMessage(ChatColor.RED
						+ "You are not allowed to view a list of tickets!");
				return true;
			}

			List<String> tickets = plugin.getMainConfig().getOpenTickets();

			sender.sendMessage(ChatColor.GOLD + "----------------------------");
			
			if (tickets.size() == 0) {
				sender.sendMessage(ChatColor.AQUA + "There are no tickets.");
				return true;
			}
			sender.sendMessage(ChatColor.AQUA + "Tickets: ");
			
			
			for (int i = 0; i < tickets.size(); i++) {
				String ticket = tickets.get(i);
				StringBuilder ticketList = new StringBuilder();
				
				ticketList = ticketList.append(ChatColor.AQUA + "#" + ChatColor.GOLD + ticket + ChatColor.AQUA + " by: " + ChatColor.GOLD + plugin.getMainConfig().getAuthor(Integer.parseInt(ticket)));
				
				if (!plugin.getMainConfig().getSubTitle(Integer.parseInt(ticket)).equalsIgnoreCase("") && !plugin.getMainConfig().getSubTitle(Integer.parseInt(ticket)).equalsIgnoreCase(" ")) {
					ticketList = ticketList.append(ChatColor.AQUA + " title: " + ChatColor.GRAY + plugin.getMainConfig().getSubTitle(Integer.parseInt(ticket)));
				}
				sender.sendMessage(ticketList.toString());
			}
		}

		return true;
	}
}
