package net.mako.hcf.timer.timers;

import java.sql.Time;

import org.bukkit.entity.Player;

import net.mako.hcf.HCF;
import net.mako.hcf.timer.PlayerTimer;
import net.mako.hcf.timer.Timer;

public class PvPTimer extends PlayerTimer {
	public PvPTimer(Player player) {
		super("Invincibility", "&a", new Time(0, 0, HCF.getInstance().getConfig().getInt("timers.pvp-timer")), player);
	}

	@Override
	public void tick() {
	
	}
}
