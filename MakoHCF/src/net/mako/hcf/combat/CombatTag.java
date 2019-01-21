package net.mako.hcf.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.mako.hcf.HCF;
import net.mako.hcf.timer.Timer;
import net.mako.hcf.timer.timers.PvPTimer;
import net.md_5.bungee.api.ChatColor;

public class CombatTag implements Listener {
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player && !HCF.getInstance().getSpawn().insideSpawn(event.getEntity())) {
			Player attacker = (Player) event.getDamager();
			Player target = (Player) event.getEntity();
			
			for (Timer timer : HCF.getInstance().getTimerHandler().getTimersFromPlayer(attacker)) {
				if (timer instanceof PvPTimer) {
					attacker.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have PvP Timer enabled! Do &l/pvp enable &r&cto remove it!"));
					event.setCancelled(true);
					return;
				}
			}
			
			for (Timer timer : HCF.getInstance().getTimerHandler().getTimersFromPlayer(target)) {
				if (timer instanceof PvPTimer) {
					attacker.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + target.getName() + " still has &lPvP Timer &r&cenabled!"));
					event.setCancelled(true);
				}
			}
		}
	}
}
