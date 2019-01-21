package net.mako.hcf.timer;

import java.sql.Time;

import net.mako.hcf.HCF;

public abstract class Timer {
	private String name;
	private String color;
	private Time time;
	private boolean running;
	private boolean displayWhilePaused;
	
	public Timer(String name, String color, Time time) {
		this.name = name;
		this.color = color;
		this.time = time;
		this.running = false;
		this.displayWhilePaused = false;
	}
	
	public abstract void tick();
	public abstract void onEnable();
	public abstract void onDisable();

	public Time getTime() {
		return time;
	}
	
	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}
	
	public boolean getDisplayWhilePaused() {
		return displayWhilePaused;
	}
	
	public String getFormattedTime() {
		String formatted = this.time.toString();
		
		if (time.getHours() == 0)
			formatted = formatted.substring(3, formatted.length());
		
		return formatted;
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setTime(Time time) {
		this.time = time;
	}
	
	public void setDisplayWhilePaused(boolean bool) {
		displayWhilePaused = bool;
	}
	
	public void enable() {
		this.running = true;
		this.onEnable();
	}
	
	public void disable() {
		this.running = false;
		this.onDisable();
	}
	
	public TimerHandler getTimerHandler() {
		return HCF.getInstance().getTimerHandler();
	}
	
	//decreases timer by one second
	public void decrementTime() {
		time.setSeconds(time.getSeconds() - 1);
	}
}
