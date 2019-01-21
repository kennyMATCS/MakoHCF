package net.mako.hcf.timer;

import java.util.ArrayList;
import java.util.List;
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
			int i = 0;
			@Override
			public void run() {
				for (Timer timer : timers) {
					//checks if its been 20 ticks and decrements time by a second
					if (timer.isRunning()) {
						if (i >= 20) {
							timer.decrementTime();
						}	
						timer.tick();
					}

				}
				//sets i back to 0 if a second has been decremented.
				if (i >= 20) {
					i = 0;
				} else {	
					i++;	
				}
			}
		}, 0L, 1L);
	}
	
	public List<Timer> getTimers() {
		return timers;
	}
	
	public void remove(Timer timer) {
		if (timers.contains(timer)) {
			timers.remove(timer);
		}
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
	
	public boolean hasTimer(Timer timer) {
		if (timers.contains(timer))
			return true;
		return false;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)  {
		PvPTimer timer = new PvPTimer(event.getPlayer());
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
