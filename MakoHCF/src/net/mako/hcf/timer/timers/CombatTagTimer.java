package net.mako.hcf.timer.timers;

import java.sql.Time;

import org.bukkit.entity.Player;

import net.mako.hcf.HCF;
import net.mako.hcf.timer.PlayerTimer;
import net.md_5.bungee.api.ChatColor;

public class CombatTagTimer extends PlayerTimer {
	public CombatTagTimer(Player player) {
		super("Combat Tag", "&4", new Time(0, 0, HCF.getInstance().getConfig().getInt("timers.combat-tag")), player);
		this.enable();
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void onEnable() {
		this.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have been combat tagged for &4&l" + HCF.getInstance().getConfig().getInt("timers.combat-tag") + " seconds&r&c!"));
	}

	@Override
	public void onDisable() {
		this.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lYour combat tag has expired!"));
	}
}
