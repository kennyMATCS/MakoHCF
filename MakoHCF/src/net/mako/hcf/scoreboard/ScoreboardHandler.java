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
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import net.mako.hcf.HCF;
import net.mako.hcf.timer.Timer;
import net.mako.hcf.utils.SimpleScoreboard;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

public class ScoreboardHandler implements Listener {
	private Map<Player, SimpleScoreboard> scoreboards = new HashMap<>();
	private Map<Player, List<Integer>> scores = new HashMap<>();
	private int i;

	public ScoreboardHandler() {
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.scheduleSyncRepeatingTask(HCF.getInstance(), new Runnable() {
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
		i = 15;
		
		//if nothing on scoreboard, set the player to the main scoreboard and return
		if (HCF.getInstance().getTimerHandler().getTimersFromPlayer(player) == null || HCF.getInstance().getTimerHandler().getTimersFromPlayer(player).isEmpty()) {
			player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			return;
		}
		
		if (!scores.containsKey(player)) {
			List<Integer> list = new ArrayList<>();
			scores.put(player, list);
		}

		add(player, scoreboard, "&7&m" + StringUtils.repeat("-", 21) + " ");
		
		//checks if player has timers and if they do loops through all their timers and adds them to the scoreboard.
		if (HCF.getInstance().getTimerHandler().getTimersFromPlayer(player) != null) {
			for (Timer timer : HCF.getInstance().getTimerHandler().getTimersFromPlayer(player)) {
				add(player, scoreboard, timer.getColor() + timer.getName());
				add(player, scoreboard, " &f" + timer.getFormattedTime());
			}	
		}

		blank(player, scoreboard);
		add(player, scoreboard, "&c&omako.net");
		add(player, scoreboard, "&7&m " + StringUtils.repeat("-", 21));
		
		Objective obj = scoreboard.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
		scores.get(player).stream().forEach(s -> {
			for (String string : scoreboard.getScoreboard().getEntries()) {
				Score score = obj.getScore(string);

                if (score == null)
                    continue;

                if (score.getScore() > i)
                    continue;
                
                scoreboard.getScoreboard().resetScores(string);
			}
		});
		
		//builds and sends the scoreboard to the player
		scoreboard.send(player);
		scoreboard.update();
	}
	 
	//adds text to scoreboard and removes and adds needed values
	private void add(Player player, SimpleScoreboard scoreboard, String string) {
		scoreboard.add(string, i);
		if (!scores.get(player).contains(i))
			scores.get(player).add(i);
		i--;
	}
	
	private void blank(Player player, SimpleScoreboard scoreboard) {
		scoreboard.add(" ", i);
		if (!scores.get(player).contains(i))
			scores.get(player).add(i);
		i--;
	}
	
	public Map<Player, SimpleScoreboard> getScoreboards() {
		return scoreboards;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)  {
		SimpleScoreboard scoreboard = new SimpleScoreboard(HCF.getInstance().getScoreboardTitle() + " &7[&4Map " + HCF.getInstance().getHCFMap() + "&7]");
		updateScoreboard(event.getPlayer());
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
