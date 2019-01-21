package net.mako.hcf.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mako.hcf.HCF;
import net.mako.hcf.timer.Timer;
import net.mako.hcf.timer.timers.PvPTimer;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

public class PvPCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			//checks if there are no args and sends the player a help message if true
			if (args.length == 0) {
				helpMessage(player);
			// if there args, checks what the args are and if they match the commands, runs them, otherwise does help again.
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("enable")) {
					//checks if the player has a pvp timer and removes it if they do
					for (Timer timer : HCF.getInstance().getTimerHandler().getTimersFromPlayer(player)) {
						if (timer instanceof PvPTimer) {
							HCF.getInstance().getTimerHandler().remove(timer);
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYour PvP Timer has been removed! &a&lGood luck!"));
							return true;
						}
					}
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have a PvP Timer!"));
				} else if (args[0].equalsIgnoreCase("time")) {
					//checks if the player has a pvp timer and if they do messages them how much time remains
					for (Timer timer : HCF.getInstance().getTimerHandler().getTimersFromPlayer(player)) {
						if (timer instanceof PvPTimer) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYour PvP Timer has &c" + timer.getFormattedTime() + "&e remaining."));
						}
					}
				} else {
					helpMessage(player);
				}
			}
		} else {
			sender.sendMessage("You must be a player to run this command!");
		}
		return false;
	}
	
	public void helpMessage(Player player) {
		player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 40));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&lPvP Timer"));
		player.sendMessage(" ");
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/pvp enable &eRemoves your PvP Timer."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/pvp time &eDisplays your remaining PvP Timer time."));
		player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 40));
	}
}
