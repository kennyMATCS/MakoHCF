package net.mako.hcf.timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import net.mako.hcf.HCF;
import net.mako.hcf.timer.timers.CombatTagTimer;
import net.mako.hcf.timer.timers.PvPTimer;

public class TimerHandler implements Listener {
	private List<Timer> timers = new ArrayList<>();
	
	public TimerHandler() {
		//runs every seconds and checks through all timers and decrements them if they are running
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.scheduleSyncRepeatingTask(HCF.getInstance(), new Runnable() {
			//used to check if a timer was just at zero seconds so it can be removed.
			List<Timer> hitZero = new ArrayList<>();
			Map<Timer, Integer> timerTimes = new HashMap();
			@Override
			public void run() {
				for (Iterator<Timer> iterator = timers.iterator(); iterator.hasNext();) {
					Timer timer = iterator.next();
					//checks if its been 20 ticks and decrements time by a second
					
					if (timer.isRunning()) {
						if (timer.getTime().getTime() == (timer.getOriginalTime())) {
							timerTimes.put(timer, 0);
							timer.getTime().setTime(timer.getOriginalTime() + 999L);
							continue;
						}
						
						timerTimes.put(timer, timerTimes.get(timer) + 1);
						
						//adds the timer to hit zero which means they will be removed on the next time decrement.
						if (timer.getTime().getHours() == 0 && timer.getTime().getMinutes() == 0 && timer.getTime().getSeconds() == 0) {
							if (!hitZero.contains(timer)) {
								hitZero.add(timer);	
							} 
						}	
						
						if (timerTimes.get(timer) >= 20) {
							timer.decrementTime();	
							timerTimes.put(timer, 0);
							
							if (hitZero.contains(timer)) {
								iterator.remove();
								timer.disable();
								timerTimes.remove(timer);
								continue;
							}
						}
						
						timer.tick();
						timerTimes.put(timer, timerTimes.get(timer) + 1);
					}
				}
			}
		}, 0L, 2L);
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
		List<PlayerTimer> playerTimers = new ArrayList<>();
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
		List<PlayerTimer> playerTimers = new ArrayList<>();
		for (PlayerTimer playerTimer : getPlayerTimers()) {
			if (playerTimer.getPlayer().equals(player)) {
				playerTimers.add(playerTimer);
			}
		}
		return playerTimers;
	}

	//checks if player has timer that is the class as the timer argument
	public Timer getTimer(Player player, Class<?> t) {
		for (Timer timer : getTimersFromPlayer(player)) {
			if (timer.getClass().equals(t)) {
				return timer;
			}
		}
		return null;
	}
	
	public boolean hasTimer(Timer timer) {
		if (timers.contains(timer))
			return true;
		return false;
	}
	
	public void addTimer(Timer timer) {
		if (!timers.contains(timer)) {
			timers.add(timer);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)  {
		PvPTimer timer = new PvPTimer(event.getPlayer());
		CombatTagTimer timer2 = new CombatTagTimer(event.getPlayer());
		timers.add(timer2);
		timers.add(timer);
	}
	
	//loops through timers, checks if its a player timer and if true removes it
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		for (PlayerTimer playerTimer : getPlayerTimers()) {
			if (playerTimer.getPlayer().equals(event.getPlayer())) {
				timers.remove(playerTimer);
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
