package me.staartvin.bookticket.tasks;

import java.util.List;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NoticeTask implements Runnable {

	private BookTicket plugin;
	
	public NoticeTask(BookTicket instance) {
		plugin = instance;
	}
	
	@Override
	public void run() {
		// Notice players on tickets
		
		for (Player player: plugin.getServer().getOnlinePlayers()) {
			if (player.hasPermission("bookticket.notice")) {
				List<String> tickets = plugin.getMainConfig().getTickets();
				
				if (tickets.size() == 0) return;
				player.sendMessage(ChatColor.AQUA + "There are " + ChatColor.GOLD + tickets.size() + ChatColor.AQUA + " open tickets!");
			}
		}
		
	}

}
