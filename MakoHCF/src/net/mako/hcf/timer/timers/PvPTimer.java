package net.mako.hcf.timer.timers;

import java.sql.Time;

import net.mako.hcf.timer.Timer;

public class PvPTimer extends Timer {
	public PvPTimer() {
		super("Invincibility", "&a", new Time(0, 30, 0));
	}
}
