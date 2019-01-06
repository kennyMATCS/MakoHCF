package net.mako.hcf;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.mako.hcf.scoreboard.ScoreboardHandler;
import net.mako.hcf.timer.TimerHandler;

public class HCF extends JavaPlugin {
	private static HCF instance;
	private TimerHandler timerHandler;
	private ScoreboardHandler scoreboardHandler;
	
	@Override
	public void onEnable() {
		this.instance = this;
		
		this.timerHandler = new TimerHandler();
		this.scoreboardHandler = new ScoreboardHandler();
		
		Bukkit.getServer().getPluginManager().registerEvents(scoreboardHandler, this);
		Bukkit.getServer().getPluginManager().registerEvents(timerHandler, this);
	}
	
	@Override
	public void onDisable() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			player.kickPlayer("Server restarting.");
		}
	}
	
	public static HCF getInstance() {
		return instance;
	}
	
	public TimerHandler getTimerHandler() {
		return timerHandler;
	}
	
	public ScoreboardHandler getScoreboardHandler() {
		return scoreboardHandler;
	}
}
