package me.staartvin.bookticket.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import me.staartvin.bookticket.BookTicket;
import net.md_5.bungee.api.ChatColor;

public class PlayerDropItemListener implements Listener {

	private BookTicket plugin;
	
	public PlayerDropItemListener(BookTicket plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onItemDrop(PlayerDropItemEvent event) {

		Item item = event.getItemDrop();
		
		if (item == null) return;
		
		ItemStack itemStack = item.getItemStack();
		
		if (itemStack != null
				&& itemStack.getType()
						.equals(Material.WRITTEN_BOOK)) {
			//Signed book
			return;
		}

		if (itemStack == null
				|| !itemStack.getType()
						.equals(Material.BOOK_AND_QUILL)) {
			// ItemStack == not book
			return;
		}

		if (!itemStack.hasItemMeta()) {
			// Book is empty
			return;
		}

		if (!itemStack.getItemMeta().hasDisplayName()) {
			// Not a ticket
			return;
		}

		if (!itemStack.getItemMeta().getDisplayName()
				.contains("Ticket ")
				|| !itemStack.getItemMeta().getDisplayName()
						.contains(" of ")) {
			// Not a valid ticket
			return;
		}
		
		event.getPlayer().sendMessage(ChatColor.RED + "You may not drop this ticket!");
		
		event.setCancelled(true);
	}
}
