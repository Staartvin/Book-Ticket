package me.staartvin.bookticket.mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import me.staartvin.bookticket.BookTicket;
import me.staartvin.bookticket.files.MainConfig.MySQLOption;

import org.bukkit.inventory.ItemStack;

/**
 * @author Staartvin
 * 
 */
public class MySQLHandler {

	BookTicket plugin;
	private MySQL mysql;
	private String host, user, password, database, table;

	public MySQLHandler(BookTicket instance) {
		plugin = instance;
	}

	private void getConfigData() {
		host = plugin.getMainConfig().getMySQLOption(MySQLOption.HOSTNAME);
		user = plugin.getMainConfig().getMySQLOption(MySQLOption.USERNAME);
		password = plugin.getMainConfig().getMySQLOption(MySQLOption.PASSWORD);
		database = plugin.getMainConfig().getMySQLOption(MySQLOption.DATABASE);
		table = plugin.getMainConfig().getMySQLOption(MySQLOption.TABLE);
	}

	public void setupSQL() {
		if (!plugin.getMainConfig().useMySQL())
			return;

		getConfigData();

		mysql = new MySQL(host, user, password, database);

		if (mysql.connect()) {
			plugin.getLogger().info("MySQL database connected!");
		} else {
			plugin.getLogger().info("Could not connect to MySQL database!");
		}
	}

	public void constructTables() {
		// Fixed MySQL connection not staying alive
		// If the MySQL Connection is somehow closed, open it again.
		if (mysql.isClosed()) {
			mysql.connect();
		}

		final String statement = "CREATE TABLE IF NOT EXISTS "
				+ table
				+ " "
				+ "(book_id INT PRIMARY KEY AUTO_INCREMENT, ticket_id INT UNIQUE NOT NULL,"
				+ " creator VARCHAR(32)," + " page1 TEXT," + " page2 TEXT,"
				+ " page3 TEXT," + " page4 TEXT," + " page5 TEXT,"
				+ " page6 TEXT," + " page7 TEXT," + " page8 TEXT,"
				+ " page9 TEXT," + " page10 TEXT," + " page11 TEXT,"
				+ " page12 TEXT," + " page13 TEXT," + " page14 TEXT,"
				+ " page15 TEXT," + " page16 TEXT," + " page17 TEXT,"
				+ " page18 TEXT," + " page19 TEXT," + " page20 TEXT,"
				+ " page21 TEXT," + " page22 TEXT," + " page23 TEXT,"
				+ " page24 TEXT," + " page25 TEXT," + " page26 TEXT,"
				+ " page27 TEXT," + " page28 TEXT," + " page29 TEXT,"
				+ " page30 TEXT," + " page31 TEXT," + " page32 TEXT,"
				+ " page33 TEXT," + " page34 TEXT," + " page35 TEXT,"
				+ " page36 TEXT," + " page37 TEXT," + " page38 TEXT,"
				+ " page39 TEXT," + " page40 TEXT," + " page41 TEXT,"
				+ " page42 TEXT," + " page43 TEXT," + " page44 TEXT,"
				+ " page45 TEXT," + " page46 TEXT," + " page47 TEXT,"
				+ " page48 TEXT," + " page49 TEXT," + " page50 TEXT);";

		plugin.getServer().getScheduler()
				.runTaskAsynchronously(plugin, new Runnable() {
					public void run() {
						mysql.execute(statement);
					}
				});
	}

	public void readBookAndSave(ItemStack book, final String creator, final int ticketId) {
		final List<String> pages = plugin.getBookHandler().getPages(book);

		plugin.getServer().getScheduler()
				.runTaskAsynchronously(plugin, new Runnable() {
					public void run() {

						// Run starting statement
						String fStatement = "INSERT INTO " + table
								+ " (creator, ticket_id) VALUES ('" + creator + "', " + ticketId + ")" +
										"ON DUPLICATE KEY UPDATE ticket_id=" + ticketId;

						mysql.execute(fStatement);

						// Set pages
						for (int i = 0; i < pages.size(); i++) {

							String page = pages.get(i);

							String pageName = "page" + (i + 1);

							String statement = "UPDATE " + table
									+ " SET " + pageName + "=? WHERE ticket_id=" + ticketId + "";
							PreparedStatement pStatement = null;
							try {
								pStatement = mysql.getConnection()
										.prepareStatement(statement);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							try {
								pStatement.setString(1, page);

								pStatement.executeUpdate();

								// Update book
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});

	}
}
