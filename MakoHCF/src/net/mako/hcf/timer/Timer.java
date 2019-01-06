package net.mako.hcf.timer;

import java.sql.Time;

public class Timer {
	private String name;
	private String color;
	private Time time;
	private boolean running;
	
	public Timer(String name, String color, Time time) {
		this.name = name;
		this.color = color;
		this.time = time;
		this.running = false;
	}

	public Time getTime() {
		return time;
	}
	
	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
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
	
	public void setRunning(boolean bool) {
		this.running = bool;
	}
	
	//decreases timer by one second
	public void decrementTime() {
		time.setSeconds(time.getSeconds() - 1);
	}
}
