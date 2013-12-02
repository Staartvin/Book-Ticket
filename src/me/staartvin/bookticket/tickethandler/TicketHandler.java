package me.staartvin.bookticket.tickethandler;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TicketHandler {

	private BookTicket plugin;

	public TicketHandler(BookTicket instance) {
		plugin = instance;
	}

	public int createNewTicket(Player player, String title, String subTitle) {
		int ticketNumber = plugin.getMainConfig()
				.createNewTicket(player, title, subTitle);

		player.sendMessage(ChatColor.GOLD + "Ticket #" + ticketNumber
				+ ChatColor.GREEN + " has been created.");

		player.setItemInHand(new ItemStack(Material.AIR, 1));

		noticeAdmins(ticketNumber);

		return ticketNumber;
	}

	public void noticeAdmins(int ticket) {
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (player.hasPermission("bookticket.notice")) {
				player.sendMessage(ChatColor.GOLD
						+ plugin.getMainConfig().getAuthor(ticket)
						+ ChatColor.AQUA + " has filed ticket #"
						+ ChatColor.GOLD + ticket + "" + ChatColor.AQUA + "!");
			}
		}
	}

	public boolean doesTicketExist(int ticket) {
		return plugin.getMainConfig().doesTicketExist(ticket);
	}

	public boolean validateTicket(int ticket, CommandSender sender) {
		if (ticket <= 0 || !plugin.getTicketHandler().doesTicketExist(ticket)) {
			sender.sendMessage(ChatColor.RED + "There is no such ticket!");
			if (plugin.getMainConfig().getLastTicketCount() > 0) {
				sender.sendMessage(ChatColor.YELLOW
						+ "The most recent ticket is " + ChatColor.GOLD
						+ "ticket "
						+ plugin.getMainConfig().getLastTicketCount());
			} else {
				sender.sendMessage(ChatColor.YELLOW + "There are no tickets.");
			}
			return false;
		} else return true;
	}
	
	public boolean closeTicket(int ticket) {
		//File bookFile = new File(plugin.getDataFolder() + "/books", plugin.getMainConfig().getBookName(ticket) + ".txt");
		boolean deleted = plugin.getMainConfig().closeTicket(ticket);
		
		/*try {
			bookFile.delete();
			return deleted;
		} catch (Exception e) {
			return false;
		} */
		return deleted;
	}
}
