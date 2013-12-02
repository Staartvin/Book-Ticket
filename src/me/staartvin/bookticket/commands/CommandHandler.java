package me.staartvin.bookticket.commands;

import me.staartvin.bookticket.BookTicket;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {

	private BookTicket plugin;

	public CommandHandler(BookTicket instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (args.length == 0) {
			sender.sendMessage(ChatColor.BLUE
					+ "-----------------------------------------------------");
			sender.sendMessage(ChatColor.GOLD + "Developed by: "
					+ ChatColor.GRAY + "Staartvin");
			sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.GRAY
					+ plugin.getDescription().getVersion());
			sender.sendMessage(ChatColor.YELLOW
					+ "Type /ticket help for a list of commands.");
			return true;
		}

		if (args[0].equalsIgnoreCase("send")) {
			return new SendCommand(plugin).onCommand(sender, cmd, label, args);
		} else if (args[0].equalsIgnoreCase("open")) {
			return new OpenCommand(plugin).onCommand(sender, cmd, label, args);
		} else if (args[0].equalsIgnoreCase("new")) {
			return new NewCommand(plugin).onCommand(sender, cmd, label, args);
		} else if (args[0].equalsIgnoreCase("list")) {
			return new ListCommand(plugin).onCommand(sender, cmd, label, args);
		} else if (args[0].equalsIgnoreCase("mylist")) {
			return new MyListCommand(plugin)
					.onCommand(sender, cmd, label, args);
		} else if (args[0].equalsIgnoreCase("reply")) {
			return new ReplyCommand(plugin).onCommand(sender, cmd, label, args);
		} else if (args[0].equalsIgnoreCase("tp")
				|| args[0].equalsIgnoreCase("teleport")) {
			return new TpCommand(plugin).onCommand(sender, cmd, label, args);
		} else if (args[0].equalsIgnoreCase("close")) {
			return new CloseCommand(plugin).onCommand(sender, cmd, label, args);
		} else if (args[0].equalsIgnoreCase("help")) {
			return new HelpCommand().onCommand(sender, cmd, label, args);
		} else if (args[0].equalsIgnoreCase("back")) {
			return new TpBackCommand(plugin).onCommand(sender, cmd, label, args);
		} else {
			sender.sendMessage(ChatColor.RED + "Command not recognised!");
			sender.sendMessage(ChatColor.YELLOW
					+ "Type '/ticket help' for a list of commands");
			return true;
		}
	}
}
