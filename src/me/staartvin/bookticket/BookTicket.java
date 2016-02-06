package me.staartvin.bookticket;

import org.bukkit.plugin.java.JavaPlugin;

import me.staartvin.bookticket.bookstorage.BookStorageHandler;
import me.staartvin.bookticket.commands.CommandHandler;
import me.staartvin.bookticket.files.MainConfig;
import me.staartvin.bookticket.listeners.PlayerEditBookListener;
import me.staartvin.bookticket.locationstorage.LocationStorage;
import me.staartvin.bookticket.mysql.MySQLHandler;
import me.staartvin.bookticket.tasks.NoticeTask;
import me.staartvin.bookticket.tickethandler.TicketHandler;

public class BookTicket extends JavaPlugin {

	private BookStorageHandler bookHandler = new BookStorageHandler(this);
	private MainConfig config = new MainConfig(this);
	private TicketHandler ticketHandler = new TicketHandler(this);
	private MySQLHandler mysqlHandler = new MySQLHandler(this);
	private LocationStorage locStorage = new LocationStorage(this);

	public void onEnable() {

		// Register commands
		getCommand("support").setExecutor(new CommandHandler(this));

		// Load config
		config.loadConfiguration();

		// Setup MYSQL
		if (config.useMySQL()) {
			mysqlHandler.setupSQL();

			mysqlHandler.constructTables();
		}

		// Run notice timer
		getServer().getScheduler().runTaskTimer(this, new NoticeTask(this),
				100L, config.getIntervalNoticeTime() * 1200);

		// Load edit book listener
		this.getServer().getPluginManager().registerEvents(new PlayerEditBookListener(this), this);
		
		getLogger().info(
				"Book Ticket v" + getDescription().getVersion()
						+ " has been enabled!");
	}

	public void onDisable() {
		// Close all tasks
		getServer().getScheduler().cancelTasks(this);

		getLogger().info(
				"Book Ticket v" + getDescription().getVersion()
						+ " has been disabled!");
	}

	public BookStorageHandler getBookHandler() {
		return bookHandler;
	}

	public void setBookHandler(BookStorageHandler bookHandler) {
		this.bookHandler = bookHandler;
	}

	public MainConfig getMainConfig() {
		return config;
	}

	public TicketHandler getTicketHandler() {
		return ticketHandler;
	}

	public void setTicketHandler(TicketHandler ticketHandler) {
		this.ticketHandler = ticketHandler;
	}

	/**
	 * The displayname of an item has colours in it.
	 * Those need to be removed before comparing it.
	 * 
	 * @param oldDisplayName Displayname to fix.
	 * @return A string without colours.
	 */
	public static String fixName(String oldDisplayName) {
		if (oldDisplayName == null)
			return null;
		return oldDisplayName.replace("§0", "").replace("§1", "")
				.replace("§2", "").replace("§3", "").replace("§4", "")
				.replace("§5", "").replace("§6", "").replace("§7", "")
				.replace("§8", "").replace("§9", "").replace("§a", "")
				.replace("§b", "").replace("§c", "").replace("§d", "")
				.replace("§e", "").replace("§f", "");
	}

	public MySQLHandler getMysqlHandler() {
		return mysqlHandler;
	}

	public void setMysqlHandler(MySQLHandler mysqlHandler) {
		this.mysqlHandler = mysqlHandler;
	}

	public LocationStorage getLocStorage() {
		return locStorage;
	}

	public void setLocStorage(LocationStorage locStorage) {
		this.locStorage = locStorage;
	}
}
