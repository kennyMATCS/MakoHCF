package net.mako.hcf.timer;

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
import net.mako.hcf.timer.timers.PvPTimer;

public class TimerHandler implements Listener {
	private Map<Player, List<Timer>> playerTimers = new HashMap();
	private List<Timer> timers = new ArrayList();
	
	public TimerHandler() {
		BukkitTask runnable = Bukkit.getScheduler().runTaskTimer(HCF.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (List<Timer> timers : playerTimers.values()) {
					for (Timer timer : timers) {
						if (timer.isRunning()) {
							timer.decrementTime();
						}
					}
				}
			}
		}, 0L, 20L);
	}
	
	public List<Timer> getTimers() {
		return timers;
	}
	
	public Map<Player, List<Timer>> getPlayerTimers() {
		return playerTimers;
	}
	
	public List<Timer> getPlayerTimers(Player player) {
		if (playerTimers.containsKey(player)) {
			return playerTimers.get(player);
		}
		return null;
	}
	
	public void addPlayer(Player player, List<Timer> timers) {
		playerTimers.put(player, timers);
	}
	
	public boolean removePlayer(Player player) {
		if (playerTimers.containsKey(player)) {
			playerTimers.remove(player);
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)  {
		List<Timer> timers = new ArrayList();
		PvPTimer timer = new PvPTimer();
		timer.setRunning(true);
		timers.add(timer);
		playerTimers.put(event.getPlayer(), timers);
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		if (playerTimers.containsKey(event.getPlayer())) {
			playerTimers.remove(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (playerTimers.containsKey(event.getPlayer())) {
			playerTimers.remove(event.getPlayer());
		}
	}
}
