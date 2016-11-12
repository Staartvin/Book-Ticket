package me.staartvin.bookticket.commands;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.staartvin.bookticket.BookTicket;

public class CommandHandler implements CommandExecutor {

	private BookTicket plugin;

	public CommandHandler(BookTicket instance) {
		plugin = instance;

		// Register command classes
		registeredCommands.put(Arrays.asList("send"), new SendCommand(plugin));
		registeredCommands.put(Arrays.asList("open"), new OpenCommand(plugin));
		registeredCommands.put(Arrays.asList("new"), new NewCommand(plugin));
		registeredCommands.put(Arrays.asList("list"), new ListCommand(plugin));
		registeredCommands.put(Arrays.asList("mylist"), new MyListCommand(plugin));
		registeredCommands.put(Arrays.asList("reply"), new ReplyCommand(plugin));
		registeredCommands.put(Arrays.asList("tp", "teleport"), new TeleportCommand(plugin));
		registeredCommands.put(Arrays.asList("close"), new CloseCommand(plugin));
		registeredCommands.put(Arrays.asList("help"), new HelpCommand());
		registeredCommands.put(Arrays.asList("back"), new TeleportBackCommand(plugin));
	}

	// Use linked hashmap so that input order is kept
	private final LinkedHashMap<List<String>, CommandExecutor> registeredCommands = new LinkedHashMap<List<String>, CommandExecutor>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args.length == 0) {
			sender.sendMessage(ChatColor.BLUE + "-----------------------------------------------------");
			sender.sendMessage(ChatColor.GOLD + "Developed by: " + ChatColor.GRAY + "Staartvin");
			sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.GRAY + plugin.getDescription().getVersion());
			sender.sendMessage(ChatColor.YELLOW + "Type /ticket help for a list of commands.");
			return true;
		}

		for (Entry<List<String>, CommandExecutor> entry : registeredCommands.entrySet()) {
			for (String argument : entry.getKey()) {
				if (args[0].trim().equalsIgnoreCase(argument)) {
					entry.getValue().onCommand(sender, cmd, argument, args);
					return true;
				}
			}
		}

		sender.sendMessage(ChatColor.RED + "Command not recognised!");
		sender.sendMessage(ChatColor.YELLOW + "Type '/ticket help' for a list of commands");
		return true;
	}
}
