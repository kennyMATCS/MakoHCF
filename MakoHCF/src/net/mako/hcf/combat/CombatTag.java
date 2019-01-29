package net.mako.hcf.combat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import net.mako.hcf.HCF;
import net.mako.hcf.spawn.SpawnEnterEvent;
import net.mako.hcf.timer.Timer;
import net.mako.hcf.timer.timers.CombatTagTimer;
import net.mako.hcf.timer.timers.PvPTimer;
import net.md_5.bungee.api.ChatColor;

public class CombatTag implements Listener {
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && !HCF.getInstance().getSpawn().insideSpawn(event.getEntity())) {
			Player attacker = null;
			//checks if damager is a projectile and gets the player and from it.
			if (event.getDamager() instanceof Projectile) {
				Projectile projectile = (Projectile) event.getDamager();
				if (projectile.getShooter() instanceof Player) {
					attacker = (Player) projectile.getShooter();
				}
			} else if (event.getDamager() instanceof Player) {
				attacker = (Player) event.getDamager();
			} else {
				return;
			}
			
			Player target = (Player) event.getEntity();
			
			//checking if player has pvp timer
			if (HCF.getInstance().getTimerHandler().getTimer(attacker, PvPTimer.class) != null) {
				attacker.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have PvP Timer enabled! Do &l/pvp enable &r&cto remove it!"));
				event.setCancelled(true);
				return;
			}
			
			if (HCF.getInstance().getTimerHandler().getTimer(attacker, PvPTimer.class) != null) {
				attacker.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + target.getName() + " still has &lPvP Timer &r&cenabled!"));
				event.setCancelled(true);
				return;
			}
			
			CombatTagEvent combatTagEventAttacker = new CombatTagEvent(attacker);
			CombatTagEvent combatTagEventTarget = new CombatTagEvent(target);
			Bukkit.getPluginManager().callEvent(combatTagEventAttacker);
			Bukkit.getPluginManager().callEvent(combatTagEventTarget);
			
			//checking if player has combat tag already, if not creates a new one, else resets the old one
			if (HCF.getInstance().getTimerHandler().getTimer(attacker, CombatTagTimer.class) == null) {
				CombatTagTimer combatTagTimerAttacker = new CombatTagTimer(attacker);
				HCF.getInstance().getTimerHandler().addTimer(combatTagTimerAttacker);
			} else {
				HCF.getInstance().getTimerHandler().getTimer(attacker, CombatTagTimer.class).reset();
			}
			
			if (HCF.getInstance().getTimerHandler().getTimer(target, CombatTagTimer.class) == null) {
				CombatTagTimer combatTagTimerTarget = new CombatTagTimer(target);
				HCF.getInstance().getTimerHandler().addTimer(combatTagTimerTarget);
			} else {
				HCF.getInstance().getTimerHandler().getTimer(target, CombatTagTimer.class).reset();
			}
		}
	}
	
	//checks if player is in spawn with combat timer and if true moves them back to their last location
	@EventHandler
	public void onSpawnEnter(SpawnEnterEvent event) { 
		boolean combatTagTimer = false;
		if (HCF.getInstance().getTimerHandler().getTimer(event.getPlayer(), CombatTagTimer.class) != null) {
			if (HCF.getInstance().getSpawn().insideSpawn(event.getPlayer()))  {
				
			}
		}
	}
}
