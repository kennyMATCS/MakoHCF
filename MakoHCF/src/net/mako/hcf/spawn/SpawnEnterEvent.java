package net.mako.hcf.spawn;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.mako.hcf.timer.Timer;
import net.mako.hcf.timer.timers.PvPTimer;

public class SpawnEnterEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();
	
	private final Player player;
	private final PvPTimer timer;
	
	public SpawnEnterEvent(Player player) {
		this.player = player;
		this.timer = null;
	}
	
	//made for pvp timer
	public SpawnEnterEvent(Player player, PvPTimer timer) {
		this.player = player;
		this.timer = timer;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Timer getTimer() {
		return timer;
	}
	
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
