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
	private List<Timer> timers = new ArrayList();
	
	public TimerHandler() {
		//runs every seconds and checks through all timers and decrements them if they are running
		BukkitTask runnable = Bukkit.getScheduler().runTaskTimer(HCF.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (Timer timer : timers) {
					if (timer.isRunning()) {
						timer.decrementTime();
					}
				}
			}
		}, 0L, 20L);
	}
	
	public List<Timer> getTimers() {
		return timers;
	}
	
	//loops through all timers and returns the player timers
	public List<PlayerTimer> getPlayerTimers() {
		List<PlayerTimer> playerTimers = new ArrayList();
		for (Timer timer : timers) {
			if (timer instanceof PlayerTimer) {
				PlayerTimer playerTimer = (PlayerTimer) timer;
				playerTimers.add(playerTimer);
			}
		}
		return playerTimers;
	}
	
	//loops through all player timers and gets the timers from a specific player
	public List<PlayerTimer> getTimersFromPlayer(Player player) {
		List<PlayerTimer> playerTimers = new ArrayList();
		for (PlayerTimer playerTimer : getPlayerTimers()) {
			if (playerTimer.getPlayer().equals(player)) {
				playerTimers.add(playerTimer);
			}
		}
		return playerTimers;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)  {
		PvPTimer timer = new PvPTimer(event.getPlayer());
		timer.setRunning(true);
		timers.add(timer);
	}
	
	//loops through timers, checks if its a player timer and if true removes it
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		for (Timer timer : timers) {
			if (timer instanceof PlayerTimer) {
				timers.remove(timer);
			}
		}
	}
	
	//loops through timers, checks if its a player timer and if true removes it
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		for (PlayerTimer playerTimer : getPlayerTimers()) {
			if (playerTimer.getPlayer().equals(event.getPlayer())) {
				timers.remove(playerTimer);
			}
		}
	}
}
