package me.staartvin.bookticket.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

import me.staartvin.bookticket.BookTicket;

public class PlayerEditBookListener implements Listener {

	private BookTicket plugin;
	
	public PlayerEditBookListener(BookTicket plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEdit(PlayerEditBookEvent event) {
		Player player = event.getPlayer();
		
		BookMeta oldMeta = event.getPreviousBookMeta();
		BookMeta newMeta = event.getNewBookMeta();
		
		String title = plugin.getBookHandler().getTitleOfBook(
				player.getItemInHand());
		
		int ticket = plugin.getMainConfig().getTicketIdByTitle(null, title);
		
		// This is a valid, open ticket so change the displayname
		if (plugin.getTicketHandler().doesTicketExist(ticket)) {
			
			// Change new meta with correct displayname
			newMeta.setDisplayName(oldMeta.getDisplayName());
			
			event.setNewBookMeta(newMeta);
		}
	}
}
