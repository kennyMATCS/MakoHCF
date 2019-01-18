package net.mako.hcf;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.mako.hcf.scoreboard.ScoreboardHandler;
import net.mako.hcf.spawn.Spawn;
import net.mako.hcf.timer.TimerHandler;

public class HCF extends JavaPlugin {
	private static HCF instance;
	private TimerHandler timerHandler;
	private ScoreboardHandler scoreboardHandler;
	private Spawn spawn;
	
	@Override
	public void onEnable() {
		this.instance = this;
		
		//creates config
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			this.getConfig().options().copyDefaults(true);
			this.saveConfig();
		}
		
		this.timerHandler = new TimerHandler();
		this.scoreboardHandler = new ScoreboardHandler();
		this.spawn = new Spawn();
		
		Bukkit.getServer().getPluginManager().registerEvents(scoreboardHandler, this);
		Bukkit.getServer().getPluginManager().registerEvents(timerHandler, this);
		Bukkit.getServer().getPluginManager().registerEvents(spawn, this);
	}
	
	@Override
	public void onDisable() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			player.kickPlayer("Server restarting.");
		}
	}
	
	public String getScoreboardTitle() {
		return this.getConfig().getString("scoreboard-title");
	}
	
	public int getHCFMap() {
		return this.getConfig().getInt("map");
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
