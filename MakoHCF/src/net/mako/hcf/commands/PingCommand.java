package net.mako.hcf.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class PingCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			//checks if there are no args and gets the sender's ping if true
			if (args.length == 0) {
				int ping = ((CraftPlayer) player).getHandle().ping;
				player.sendMessage(ChatColor.GOLD + "Your ping: " + ChatColor.GREEN + ping);
			//if there are args then checks if the player is online and sends their ping to the sender
			} else if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) != null) {
					Player target = Bukkit.getPlayer(args[0]);
					int ping = ((CraftPlayer) target).getHandle().ping;
					player.sendMessage(ChatColor.GOLD + target.getName() + "'s ping: " + ChatColor.GREEN + ping);
				} else {
					player.sendMessage(ChatColor.RED + "That player is not online!");
				}
			}
		} else {
			sender.sendMessage("You must be a player to run this command!");
		}
		return false;
	}
}
