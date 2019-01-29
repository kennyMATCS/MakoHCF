package net.mako.hcf.timer;

import java.sql.Time;

import net.mako.hcf.HCF;

public abstract class Timer {
	private String name;
	private String color;
	private Time time;
	private long originalTime;
	private boolean running;
	
	public Timer(String name, String color, Time time) {
		this.name = name;
		this.color = color;	
		this.time = time;
		this.originalTime = time.getTime();
		this.running = false;
		
		//TODO: timer has reset
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
	
	public long getOriginalTime() {
		return originalTime;
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
	
	public void enable() {
		this.running = true;
		this.onEnable();
	}
	
	public void disable() {
		this.running = false;
		this.onDisable();
	}
	
	//resets the time to the original time
	public void reset() {
		time.setTime(originalTime);
	}
	 
	public TimerHandler getTimerHandler() {
		return HCF.getInstance().getTimerHandler();
	}
	
	//decreases timer by one second
	public void decrementTime() {
		time.setSeconds(time.getSeconds() - 1);
	}
}
