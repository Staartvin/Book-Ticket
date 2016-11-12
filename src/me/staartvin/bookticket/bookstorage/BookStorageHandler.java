package me.staartvin.bookticket.bookstorage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

/**
 * All methods in this class have been written by Staartvin and cannot be copied
 * without his permission.
 * 
 * @author Staartvin
 * 
 */
public class BookStorageHandler {

	private BookTicket plugin;

	public BookStorageHandler(BookTicket instance) {
		plugin = instance;
	}

	// Stores who did /ticket new. When a player does /ticket send, the newTicket is true again
	private HashMap<String, Boolean> newTicket = new HashMap<String, Boolean>();
	
	// Store when a player has last sent a ticket, so it cannot spam the ticket system.
	private HashMap<String, Long> lastSentTicket = new HashMap<>();

	public boolean isNewTicket(String playerName) {
		if (!newTicket.containsKey(playerName)
				|| newTicket.get(playerName) == null) {
			return true;
		}

		return newTicket.get(playerName);
	}

	public void setNewTicket(String playerName, boolean bln) {
		newTicket.put(playerName, bln);
	}
	
	public void setLastSentTicket(String playerName) {
		this.lastSentTicket.put(playerName, System.currentTimeMillis());
	}
	
	/**
	 * Get how many minutes ago a ticket was sent by the user
	 * @param playerName Name of the player
	 * @return how many minutes ago a ticket was submitted or -1 if none was submitted since the restarting of the server.
	 */
	public long getLastSentTicket(String playerName) {
		Long time = this.lastSentTicket.get(playerName);
		
		if (time == null) return -1;
		
		return (System.currentTimeMillis() - time) / 60000;
	}

	/**
	 * Call this when you want to save a new book.
	 * Not an already existing one. (To reply, use the {@link #replyBook(Player, ItemStack)} method.
	 * @param player Player that created the book
	 * @param book Book that was created
	 * @return Title of the book.
	 */
	public String saveBook(Player player, ItemStack book) {
		//test if book
		if (!book.getType().equals(Material.BOOK_AND_QUILL))
			return null;

		//get book meta
		if (!book.hasItemMeta())
			return null;

		BookMeta bm = (BookMeta) book.getItemMeta();

		//getting actual information!
		//String author, title;
		//author = bookData.getString("author");
		int ticket = 1;

		if (plugin.getMainConfig().getLastTicketCount() < 0)
			ticket = 1;
		else
			ticket = plugin.getMainConfig().getLastTicketCount() + 1;
		
		String title = "Ticket " + (ticket);

		// Save book on MYSQL if enabled
		if (plugin.getMainConfig().useMySQL()) {
			plugin.getMysqlHandler().readBookAndSave(book, player.getName(), ticket);
		}

		//we'll store our pages here
		List<String> pages = new ArrayList<String>();
		
		// Add page with info (if needed)
		if (plugin.getMainConfig().doAddFirstInfoPage()) {
			
			String date = plugin.getMainConfig().getDateAsString();
			String time = plugin.getMainConfig().getTimeAsString();
			String location = player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ();
			
			String firstInfo = "Ticket: " + ticket
					+ "\nName: " + player.getName()
					+ "\nDate: " + date
					+ "\nTime: " + time
					+ "\nLocation: " + location
					+ "\nWorld: " + player.getWorld().getName();
			pages.add(firstInfo);
			
		}

		if (!bm.hasPages())
			return null;
		
		//grabbing pages
		for (String page : bm.getPages()) {
			pages.add(page);
		}

		//creates a new file named by the title of the book. It will override any file that already exists there by that name though. careful.
		File txt = new File(plugin.getDataFolder() + "/books", title + ".txt");
		try {
			txt.getParentFile().mkdirs();
			txt.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		//create our writer
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(txt));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		//write stuff
		try {
			out.write("+Title: " + title);
			out.newLine();
			out.write("+Author: " + player.getName());
			out.newLine();
			out.write("");
			out.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				out.close();
				return null;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		//write pages
		for (int i = 0; i < pages.size(); i++) {
			String page = pages.get(i);
			
			try {
				out.write("+Page " + (i + 1));
				out.newLine();
				out.write(page);
				out.newLine();
				out.write("");
				
				out.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					out.close();
					return null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		}

		//close
		try {
			out.close();
			return title;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String replyBook(Player replier, ItemStack book) {
		//test if book
		if (!book.getType().equals(Material.BOOK_AND_QUILL))
			return null;

		//get book meta
		if (!book.hasItemMeta())
			return null;

		BookMeta bm = (BookMeta) book.getItemMeta();
		//getting actual information!
		String title = getTitleOfBook(book);

		if (title == null)
			return null;

		int ticket = 1;

		ticket = Integer.parseInt(title.replace("Ticket", "").trim());

		//we'll store our pages here
		List<String> pages = new ArrayList<String>();

		if (!bm.hasPages())
			return null;

		//grabbing pages
		for (String page : bm.getPages()) {
			pages.add(page);
		}

		//creates a new file named by the title of the book. It will override any file that already exists there by that name though. careful.
		File txt = new File(plugin.getDataFolder() + "/books", title + ".txt");
		try {
			txt.getParentFile().mkdirs();
			txt.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		//create our writer
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(txt));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		//write stuff
		try {
			out.write("+Title: " + title);
			out.newLine();
			out.write("+Author: " + plugin.getMainConfig().getAuthor(ticket));
			out.newLine();
			out.write("");
			out.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				out.close();
				return null;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		//write pages
		for (int i = 0; i < pages.size(); i++) {
			String page = pages.get(i);
			try {
				out.write("+Page " + (i + 1));
				out.newLine();
				out.write(page);
				out.newLine();
				out.write("");
				out.newLine();

				if (i == (pages.size() - 1)) {
					if (replier.hasPermission("bookticket.open.other")) {
						out.write(" -Staff");
					} else {
						out.write(" -" + replier.getName());
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}

		//close
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		// Save book on MYSQL if enabled
		if (plugin.getMainConfig().useMySQL()) {
			plugin.getMysqlHandler().readBookAndSave(book,
					plugin.getMainConfig().getAuthor(ticket), ticket);
		}

		return plugin.getMainConfig().getAuthor(ticket);
	}

	public String loadBook(Player player, File bookFile) {
		BufferedReader br = null;
		List<String> pages = new ArrayList<String>();
		Boolean saveNow = false;
		int pageNumber = 1;
		Boolean emptyList = false;

		String author = null, title = null;

		List<String> pagesEntries = new ArrayList<String>();
		
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(bookFile));
			
			while ((sCurrentLine = br.readLine()) != null) {

				if (emptyList) {
					emptyList = false;
					pagesEntries.clear();
				}

				if (sCurrentLine.contains("+Author:")) {
					author = sCurrentLine.replace("+Author:", "").trim();
				}

				if (sCurrentLine.contains("+Title:")) {
					title = sCurrentLine.replace("+Title:", "").trim();
				}

				if (saveNow && !sCurrentLine.contains("+Page")) {

					//if (sCurrentLine.equalsIgnoreCase(" ")
						//	|| sCurrentLine.equalsIgnoreCase(""))
						//continue;

					pagesEntries.add(sCurrentLine);
				} else if (sCurrentLine.contains("+Page")) {

					String stringNumber = sCurrentLine.replace("+Page", "");
					int number = Integer.parseInt(stringNumber.trim());

					if (number == 1) {
						saveNow = true;
						pageNumber = 0;
					} else {

						pages.add(convertToStringForBooks(pagesEntries));
						//pages.put(pageNumber, pagesEntries);

						emptyList = true;
						//pagesEntries.clear();

						pageNumber = pageNumber + 1;
					}
				}
			}
			pages.add(convertToStringForBooks(pagesEntries));

			saveNow = false;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		// Create a new book
		ItemStack book = new ItemStack(Material.BOOK_AND_QUILL, 1);

		BookMeta bm = (BookMeta) book.getItemMeta();

		bm.setPages(pages);
		bm.setDisplayName(title + " of " + ChatColor.GOLD + author);
		book.setItemMeta(bm);

		// Give book
		player.getInventory().addItem(book);

		return title;
	}

	public String convertToStringForBooks(List<String> strings) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < strings.size(); i++) {
			String string = strings.get(i);

			if (i == (strings.size() - 1)) {
				builder.append(string);
			} else {
				builder.append(string + "\n");
			}
		}

		return builder.toString().trim();
	}
	
	public String convertToStringForArguments(List<String> strings) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < strings.size(); i++) {
			String string = strings.get(i);

			if (i == (strings.size() - 1)) {
				builder.append(string);
			} else {
				builder.append(string + " ");
			}
		}

		return builder.toString().trim();
	}

	/**
	 * Get the title of the book
	 * 
	 * @param book To get the title from
	 * @return Title. This is in the form of 'ticket #'
	 */
	public String getTitleOfBook(ItemStack book) {

		if (!book.hasItemMeta())
			return null;

		if (!book.getItemMeta().hasDisplayName())
			return null;

		// Ticket # of <player>
		String displayName = BookTicket.fixName(book.getItemMeta()
				.getDisplayName());

		if (!displayName.contains("Ticket"))
			return null;

		String[] words = displayName.split(" ");

		if (words.length < 4)
			return null;

		// Ticket #
		String title = "Ticket " + words[1];

		return title;
	}

	public List<String> getPages(ItemStack book) {
		List<String> pages = new ArrayList<String>();

		if (!book.hasItemMeta())
			return pages;

		BookMeta bm = (BookMeta) book.getItemMeta();

		if (!bm.hasPages())
			return pages;

		pages = bm.getPages();

		return pages;
	}
}
