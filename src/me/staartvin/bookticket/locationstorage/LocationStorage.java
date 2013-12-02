package me.staartvin.bookticket.locationstorage;

import java.util.HashMap;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.Location;

public class LocationStorage {

	@SuppressWarnings("unused")
	private BookTicket plugin;
	private HashMap<String, Location> lastLocation = new HashMap<String, Location>();
	
	public LocationStorage(BookTicket instance) {
		plugin = instance;
	}
	
	public void setLastLocation(String playerName, Location loc) {
		lastLocation.put(playerName, loc);
	}
	
	public Location getLastLocation(String playerName) {
		if (lastLocation.containsKey(playerName)) {
			return lastLocation.get(playerName);
		}
		
		return null;
	}
	
	
}
