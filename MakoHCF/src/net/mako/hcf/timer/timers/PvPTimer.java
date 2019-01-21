package net.mako.hcf.timer.timers;

import java.sql.Time;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.mako.hcf.HCF;
import net.mako.hcf.spawn.SpawnEnterEvent;
import net.mako.hcf.spawn.SpawnLeaveEvent;
import net.mako.hcf.timer.PlayerTimer;
import net.md_5.bungee.api.ChatColor;

public class PvPTimer extends PlayerTimer implements Listener {
	//TODO: register events per object opposed to per class
	public PvPTimer(Player player) {
		super("PvP Timer", "&a", new Time(0, 0, HCF.getInstance().getConfig().getInt("timers.pvp-timer")), player);
		this.setDisplayWhilePaused(true);
		Bukkit.getServer().getPluginManager().registerEvents(this, HCF.getInstance());
	}

	@Override
	public void tick() {
	
	}

	@Override
	public void onEnable() {
		this.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lPvP Timer &r&ahas been activated! You have &c" + this.getFormattedTime() + "&a remaining!"));
		System.out.println(this.getPlayer().getName());
	}

	@Override
	public void onDisable() {
		this.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lPvP Timer &r&chas been deactivated!"));
	}
	
	@EventHandler
	public void onSpawnEnter(SpawnEnterEvent event) {
		Player player = event.getPlayer();
		//checking if the timer is equal to this to see if this was the pvp timer that we are calling for
		if (this.getTimerHandler().hasTimer(this) && this.isRunning() && event.getTimer().equals(this))
			this.disable();
	}
	
	@EventHandler
	public void onSpawnLeave(SpawnLeaveEvent event) {
		Player player = event.getPlayer();
		//checking if the timer is equal to this to see if this was the pvp timer that we are calling for
		if (this.getTimerHandler().hasTimer(this) && !this.isRunning() && event.getTimer().equals(this))
			this.enable();
	}
}
