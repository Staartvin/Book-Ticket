package me.staartvin.bookticket.files;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MainConfig {

	private BookTicket plugin;
	private FileConfiguration config;

	public MainConfig(BookTicket instance) {
		plugin = instance;
	}

	public void loadConfiguration() {
		config = plugin.getConfig();

		config.options().header(
				"Book Ticket v" + plugin.getDescription().getVersion()
						+ " Config" + "\n\nTickets is a list of created tickets."
								+ "\n\nThe 'interval notice time' is the amount of minutes that the 'there are x open tickets' message gets repeated."
								+ "\nIf 'add first info page' is true, when a ticket is opened, the first page of the book will show general info about"
								+ "\nthe player that created the ticket."
								+ "\nThe 'allow closing own tickets' option can be set to true or false. When you want players to be able to close their own tickets,"
								+ "\nset this to true. If you only want your moderators to close any tickets, set this to false."
								+ "\nThe 'cooldown period submitting tickets' is the time in minutes that a player has to wait before sending a new ticket.");

		// Messages
		//config.addDefault("Messages.",
		//		"§6Teleporting in %time% seconds..");
		//config.addDefault("Messages.move warning",
		//		"§4Don't move or teleportation is cancelled.");
		//config.addDefault("Messages.teleport message", "§6Commencing teleport!");

		// General information
		
		config.addDefault("General.interval notice time", 5);
		
		config.addDefault("General.Add first info page", true);
		
		config.addDefault("General.allow closing own tickets", true);
		
		config.addDefault("General.cooldown period submitting tickets", 5);
		
		config.addDefault("MySQL.enabled", false);
		config.addDefault("MySQL.hostname", "localhost:3306");
		config.addDefault("MySQL.username", "root");
		config.addDefault("MySQL.password", "");
		config.addDefault("MySQL.database", "bookticket");
		config.addDefault("MySQL.table", "bookticket");
		
		// Ticket information later
		config.addDefault("Tickets.0.Author", "Staartvin");

		config.addDefault("Tickets.0.Book", "Ticket 0");

		config.addDefault("Tickets.0.Location", "1, 1, 1, world");

		config.addDefault("Tickets.0.Time", getTimeAndDateAsString());
		
		config.addDefault("Tickets.0.SubTitle", "");
		
		config.addDefault("Tickets.0.Status", "closed");
		

		config.options().copyDefaults(true);
		plugin.saveConfig();
	}
	
	public enum MySQLOption {HOSTNAME, USERNAME, PASSWORD, DATABASE, TABLE}
	
	public String getStatus(int ticket) {
		return config.getString("Tickets." + ticket + ".Status");
	}
	
	public void setStatus(String status, int ticket) {
		config.set("Tickets." + ticket + ".Status", status);

		plugin.saveConfig();
	}
	
	public boolean isOpen(int ticket) {
		return getStatus(ticket).equalsIgnoreCase("open");
	}
	
	public String getMySQLOption(MySQLOption option) {
		switch (option) {
			case HOSTNAME:
				return config.getString("MySQL.hostname");
			case USERNAME:
				return config.getString("MySQL.username");
			case PASSWORD:
				return config.getString("MySQL.password");
			case DATABASE:
				return config.getString("MySQL.database");
			case TABLE:
				return config.getString("MySQL.table");
			default:
				return null;
		}
	}
	
	public boolean useMySQL() {
		return config.getBoolean("MySQL.enabled");
	}

	public List<String> getOpenTickets() {
		List<String> tickets = new ArrayList<String>();

		for (String ticket : config.getConfigurationSection("Tickets").getKeys(
				false)) {
			// First ticket is an example ticket
			if (ticket.equalsIgnoreCase("0"))
				continue;
			
			// If ticket is closed, skip it.
			if (!isOpen(Integer.parseInt(ticket))) 
				continue;

			tickets.add(ticket);
		}

		return tickets;
	}
	
	public List<String> getAllTickets() {
		List<String> tickets = new ArrayList<String>();

		for (String ticket : config.getConfigurationSection("Tickets").getKeys(
				false)) {
			// First ticket is an example ticket
			if (ticket.equalsIgnoreCase("0"))
				continue;

			tickets.add(ticket);
		}

		return tickets;
	}

	public int createNewTicket(Player player, String bookName, String subTitle) {

		int lastTicket;

		if (getLastTicketCount() < 0) {
			lastTicket = 1;
		} else {
			lastTicket = getLastTicketCount() + 1;
		}

		// Set author
		setAuthor(player.getName(), lastTicket);

		// Set book
		setBookName(bookName, lastTicket);

		// Set location
		setLocation(player.getLocation(), lastTicket);

		// Set time
		setTime(new Date(), lastTicket);
		
		// Set subTitle
		setSubTitle(subTitle, lastTicket);
		
		// Set status open
		setStatus("open", lastTicket);

		return lastTicket;
	}

	public boolean doesTicketExist(int ticket) {
		return (config.getString("Tickets." + ticket + ".Author", null) != null);
	}

	public String getAuthor(int ticket) {
		return config.getString("Tickets." + ticket + ".Author");
	}

	public void setAuthor(String playerName, int ticket) {
		config.set("Tickets." + ticket + ".Author", playerName);
		plugin.saveConfig();
	}

	public void setBookName(String bookName, int ticket) {
		config.set("Tickets." + ticket + ".Book", bookName);
	}

	public void setTime(Date date, int ticket) {
		if (date != null) {
			String dateString = getTimeAndDateAsString();
			config.set("Tickets." + ticket + ".Time", dateString);
		} else {
			// To allow for quick removal
			config.set("Tickets." + ticket + ".Time", null);
		}
		plugin.saveConfig();
	}

	public void setLocation(Location location, int ticket) {
		if (location != null) {
			int x = location.getBlockX(), y = location.getBlockY(), z = location
					.getBlockZ();
			String world = location.getWorld().getName();

			config.set("Tickets." + ticket + ".Location", x + ", " + y + ", "
					+ z + ", " + world);

		} else {
			config.set("Tickets." + ticket + ".Location", null);
		}
		plugin.saveConfig();
	}

	public String getTime(int ticket) {
		return config.getString("Tickets." + ticket + ".Time");
	}

	public String getTimeAndDateAsString() {
		// In ISO 8601 format
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd");
		
		return dateFormat.format(cal.getTime());
	}
	
	public String getTimeAsString() {
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		
		return dateFormat.format(cal.getTime());
	}
	
	public String getDateAsString() {
		// In ISO 8601 format
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		
		return dateFormat.format(cal.getTime());
	}

	/**
	 * Get the most recent ticket
	 * 
	 * @return most recent ticket
	 */
	public int getLastTicketCount() {
		List<String> tickets = getAllTickets();

		if (tickets.size() == 0)
			return -1;
		String ticketName = tickets.get(tickets.size() - 1);
		int number = 1;

		try {
			number = Integer.parseInt(ticketName);
		} catch (Exception e) {
			return number;
		}

		return number;
	}

	/**
	 * Get the name of the book from a ticket id
	 * 
	 * @param ticket ticket id to get the book of
	 * @return name of the book
	 */
	public String getBookName(int ticket) {
		return config.getString("Tickets." + ticket + ".Book");
	}

	/**
	 * Get the ticket id by title
	 * 
	 * @param author Author (can be null, but ticket cannot include author)
	 * @param title Title of the book
	 * @return ticket id of the book
	 */
	public int getTicketIdByTitle(String author, String title) {

		if (title == null) return -1;
		
		// Title = "Ticket # of 'player"
		title = title.replace("Ticket ", "").replace("of " + author, "");

		int number = Integer.parseInt(title.trim());

		return number;
	}

	/**
	 * Get all tickets created by an author
	 * 
	 * @param author Player to find tickets of
	 * @return A list of tickets
	 */
	public List<String> getTicketsOfAuthor(String author) {
		List<String> tickets = new ArrayList<String>();

		for (String ticket : config.getConfigurationSection("Tickets").getKeys(
				false)) {
			// First ticket is an example ticket
			if (ticket.equalsIgnoreCase("0"))
				continue;
			
			// Closed tickets
			if (!isOpen(Integer.parseInt(ticket))) 
				continue;
			
			if (getAuthor(Integer.parseInt(ticket)).equalsIgnoreCase(author)) {
				tickets.add(ticket);
			}
		}

		return tickets;
	}

	/**
	 * Get the location of a ticket (World, x, y, z)
	 * 
	 * @param ticket Ticket id
	 * @return A bukkit location
	 */
	public org.bukkit.Location getLocation(int ticket) {
		String location = config.getString("Tickets." + ticket + ".Location");

		String world, x, y, z;

		String[] args = location.split(",");

		world = args[3].trim();
		x = args[0].trim();
		y = args[1].trim();
		z = args[2].trim();

		return new Location(plugin.getServer().getWorld(world),
				Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));
	}

	public boolean closeTicket(int ticket) {
		if (!doesTicketExist(ticket))
			return false;

		// Set author
		//setAuthor(null, ticket);

		// Set book
		//setBookName(null, ticket);

		// Set location
		//setLocation(null, ticket);

		// Set time
		//setTime(null, ticket);
		
		// Remove last thing
		//config.set("Tickets." + ticket, null);
		
		// Set status to closed
		setStatus("closed", ticket);
		
		plugin.saveConfig();
		return true;
	}
	
	public int getIntervalNoticeTime() {
		return config.getInt("General.interval notice time");
	}
	
	public String getSubTitle(int ticket) {
		return config.getString("Tickets." + ticket + ".SubTitle", "");
	}
	
	public void setSubTitle(String subTitle, int ticket) {
		config.set("Tickets." + ticket + ".SubTitle", subTitle);
		plugin.saveConfig();
	}
	
	public boolean doAddFirstInfoPage() {
		return config.getBoolean("General.Add first info page", true);
	}
	
	public boolean allowClosingOwnTickets() {
		return config.getBoolean("General.allow closing own tickets", true);
	}
	
	public int getCooldownPeriod() {
		return config.getInt("General.cooldown period submitting tickets", 5);
	}
}
