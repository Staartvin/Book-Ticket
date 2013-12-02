package me.staartvin.bookticket.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {

	private String hostname;
	private String username;
	private String password;
	private String database;

	private Connection conn = null;

	/**
	 * Create a new MySQL Connection
	 * 
	 * @param hostname Hostname (127.0.0.1:3306)
	 * @param username Username
	 * @param password Password
	 * @param database Database
	 */
	public MySQL(String hostname, String username, String password,
			String database) {
		this.hostname = hostname;
		this.username = username;
		this.password = password;
		this.database = database;
	}

	/**
	 * Execute a query. Query cannot be null.
	 * This query doesn't return anything. (Good for updating tables)
	 * 
	 * @param query Query to execute
	 */
	public void execute(String query) {
		
		if (isClosed()) connect();
		
		Statement stmt = null;

		if (conn != null) {
			try {

				stmt = conn.createStatement();
				stmt.execute(query);

			} catch (SQLException ex) {
				// Error because we tried to check database on lost columns
				if (ex.getSQLState().equalsIgnoreCase("42S21")
						&& ex.getErrorCode() == 1060) {
					// Do nothing
					//System.out.print("[FoundOres Revisited] Database is up to date!");
				} else {
					//System.out.println("SQLDataStorage.execute");
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
			} finally {

				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException sqlEx) {
					}

					stmt = null;
				}
			}
		}
	}

	/**
	 * Execute a query and returns a ResultSet. Query cannot be null.
	 * 
	 * @param query Query to execute
	 * @return ResultSet if successfully performed, null if an error occured.
	 */
	public ResultSet executeQuery(String query) {
		if (isClosed()) connect();
		
		Statement stmt = null;
		ResultSet rs = null;

		if (conn != null) {
			try {

				stmt = conn.createStatement();
				rs = stmt.executeQuery(query);

			} catch (SQLException ex) {
				System.out.println("SQLDataStorage.executeQuery");
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
		}

		return rs;
	}

	/**
	 * Open a new MySQL connection
	 * 
	 * @return true if connection was successfully set up.
	 */
	public boolean connect() {
		
		try {
			if (conn != null) {
				if (!conn.isClosed()) return false;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			//System.out.println("jdbc:mysql://" + hostname + "/" + database
			//	+ "?" + "user=" + username + "&password=" + password);
			String url = "jdbc:mysql://" + hostname + "/" + database;
			conn = DriverManager.getConnection(url, username, password);

		} catch (SQLException ex) {
			System.out.println("SQLDataStorage.connect");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception e) {

			e.printStackTrace();
		}
		return conn != null;
	}
	
	public boolean isClosed() {
		try {
			return conn.isClosed();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}
	
	public Connection getConnection() {
		return conn;
	}

}
