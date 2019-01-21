package net.mako.hcf.scoreboard;

import java.util.HashMap;
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
		}, 0L, 1L);
	}
	
	public void updateScoreboard(Player player) {
		SimpleScoreboard scoreboard = scoreboards.get(player);
		int i = 15;
		
		//subtracting i by 1 for each text added to get the score.
		scoreboard.add("&7&m" + StringUtils.repeat("-", 21) + " ", i);
		i--;
		
		//checks if player has timers and if they do loops through all their timers and adds them to the scoreboard.
		if (HCF.getInstance().getTimerHandler().getTimersFromPlayer(player) != null) {
			for (Timer timer : HCF.getInstance().getTimerHandler().getTimersFromPlayer(player)) {
				if (timer.isRunning() || timer.getDisplayWhilePaused()) {
					scoreboard.add(timer.getColor() + timer.getName(), i);
					i--;
					scoreboard.add(" &f" + timer.getFormattedTime(), i);
					i--;
				}		
			}	
		}

		scoreboard.add(" ", i);
		i--;
		scoreboard.add("&c&omako.net", i);
		i--;
		scoreboard.add("&7&m " + StringUtils.repeat("-", 21), i);
		i--;

		
		//if nothing on scoreboard, set the player to the main scoreboard and return
		if (i == 11) {
			if (!player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard()))
				player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			return;
		}
			
		//sets the player scoreboard and updates it 
		scoreboard.send(player);
		scoreboard.update();
	}
	
	public Map<Player, SimpleScoreboard> getScoreboards() {
		return scoreboards;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)  {
		SimpleScoreboard scoreboard = new SimpleScoreboard(HCF.getInstance().getScoreboardTitle() + " &7[&4Map " + HCF.getInstance().getHCFMap() + "&7]");
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
