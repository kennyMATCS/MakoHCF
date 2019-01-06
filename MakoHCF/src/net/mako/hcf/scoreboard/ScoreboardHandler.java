package net.mako.hcf.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import net.mako.hcf.HCF;
import net.mako.hcf.timer.Timer;
import net.mako.hcf.utils.SimpleScoreboard;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

public class ScoreboardHandler implements Listener {
	private Map<Player, SimpleScoreboard> scoreboards = new HashMap();
	
	public ScoreboardHandler() {
		BukkitTask runnable = Bukkit.getScheduler().runTaskTimer(HCF.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (Player player : scoreboards.keySet()) {
					updateScoreboard(player);
				}
			}
		}, 0L, 20L);
	}
	
	public void updateScoreboard(Player player) {
		SimpleScoreboard scoreboard = scoreboards.get(player);
		int i = 15;
		
		scoreboard.add("&7&m" + StringUtils.repeat("-", 21) + " ", i);
		i--;
		
		//checks if player has timer and if they do loops through all their timers and adds them to the scoreboard.
		if (HCF.getInstance().getTimerHandler().getPlayerTimers(player) != null) {
			for (Timer timer : HCF.getInstance().getTimerHandler().getPlayerTimers(player)) {
				scoreboard.add(timer.getColor() + "&l" + timer.getName(), i);
				i--;
				scoreboard.add("  &c" + timer.getFormattedTime(), i);
				i--;
			}
		}
		
		scoreboard.add("&7&m " + StringUtils.repeat("-", 21), i);
		i--;
		
		//if nothing on scoreboard return null
		if (i == 13) {
			return;
		}
			
		scoreboard.update();
	}
	
	public Map<Player, SimpleScoreboard> getScoreboards() {
		return scoreboards;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)  {
		SimpleScoreboard scoreboard = new SimpleScoreboard("&6&lMakoHCF");
		scoreboard.send(event.getPlayer());
		scoreboards.put(event.getPlayer(), scoreboard);
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		if (scoreboards.containsKey(event.getPlayer())) {
			scoreboards.remove(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (scoreboards.containsKey(event.getPlayer())) {
			scoreboards.remove(event.getPlayer());
		}
	}
}
