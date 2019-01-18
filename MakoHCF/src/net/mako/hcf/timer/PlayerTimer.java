package net.mako.hcf.timer;

import java.sql.Time;

import org.bukkit.entity.Player;

public class PlayerTimer extends Timer {
	private Player player;
	
	public PlayerTimer(String name, String color, Time time, Player player) {
		super(name, color, time);
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
}
