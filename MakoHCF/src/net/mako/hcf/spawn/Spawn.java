package net.mako.hcf.spawn;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import net.mako.hcf.HCF;
import net.mako.hcf.timer.Timer;
import net.mako.hcf.timer.timers.PvPTimer;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

public class Spawn implements Listener {
	private Map<Player, Boolean> inSpawn = new HashMap();
	
	public Spawn() {
		BukkitTask runnable = Bukkit.getScheduler().runTaskTimer(HCF.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (insideSpawn(player)) {
						//used to get player health; player.getHealth() is ambigious
						Damageable damageable = (Damageable) player;
						if ((double) damageable.getHealth() < 20)
							player.setHealth(20);
						if (player.getFoodLevel() < 20) 
							player.setFoodLevel(20);
					}
					
					if (!inSpawn.containsKey(player)) {
						inSpawn.put(player, insideSpawn(player));
						continue;
					}
					
					//gets the previous in spawn value then the new one and compares them.
					boolean wasInSpawn = inSpawn.get(player);
					inSpawn.put(player, insideSpawn(player));
					boolean isInSpawn = inSpawn.get(player);
					//calls event based on if the player was in spawn or not before.
					boolean hasPvPTimer = false;
					if (wasInSpawn == false && isInSpawn == true) {
						//checks if the player has pvp timer and stores it inside the event
						for (Timer timer : HCF.getInstance().getTimerHandler().getTimersFromPlayer(player)) {
							if (timer instanceof PvPTimer) {
								SpawnEnterEvent spawnEnterEvent = new SpawnEnterEvent(player, (PvPTimer) timer);
								Bukkit.getPluginManager().callEvent(spawnEnterEvent);
								hasPvPTimer = true;
								break;
							} 
						}
						
						if (!hasPvPTimer) {		
							SpawnEnterEvent spawnEnterEvent = new SpawnEnterEvent(player);
							Bukkit.getPluginManager().callEvent(spawnEnterEvent);
						}
						
					} else if (wasInSpawn == true && isInSpawn == false) {
						//checks if the player has pvp timer and stores it inside the event
						for (Timer timer : HCF.getInstance().getTimerHandler().getTimersFromPlayer(player)) {
							if (timer instanceof PvPTimer) {
								SpawnLeaveEvent spawnLeaveEvent = new SpawnLeaveEvent(player, (PvPTimer) timer);
								Bukkit.getPluginManager().callEvent(spawnLeaveEvent);
								hasPvPTimer = true;
								break;
							} 
						}
						
						if (!hasPvPTimer) {
							SpawnLeaveEvent spawnLeaveEvent = new SpawnLeaveEvent(player);
							Bukkit.getPluginManager().callEvent(spawnLeaveEvent);
						}
					} 
				}
			}
		}, 0L, 1L);
	}
	
	//gets the corner one from the config
	public int[] getCornerOne() {
		int x = HCF.getInstance().getConfig().getInt("spawn.corner-1.x");	
		int z = HCF.getInstance().getConfig().getInt("spawn.corner-1.z");
		int[] corner = {x, z};
		return corner;
	}
	
	//gets the corner two from the config
	public int[] getCornerTwo() {
		int x = HCF.getInstance().getConfig().getInt("spawn.corner-2.x");
		int z = HCF.getInstance().getConfig().getInt("spawn.corner-2.z");
		int[] corner = {x, z};
		return corner;
	}
	
	/*
	 * checks if the player is inside spawn by comparing
	 * the edges of the corners to the player's location
	 */
	public boolean insideSpawn(double x, double z) { 
		int corner1[] = getCornerOne();
		int corner2[] = getCornerTwo();
		
		//increases corner values.
		//adds or subtracts one based on if the corner value is pos or neg.
		//this is so the method checks based on the outside corner of the block opposed to the inside
		corner1[0] = corner1[0] < 0 ? corner1[0] - 1 : corner1[0] + 1;
		corner1[1] = corner1[1] < 0 ? corner1[1] - 1 : corner1[1] + 1;
		corner2[0] = corner2[0] < 0 ? corner2[0] - 1 : corner2[0] + 1;
		corner2[1] = corner2[1] < 0 ? corner2[1] - 1 : corner2[1] + 1;
		
		if (x >= corner1[0]
			|| x <= corner2[0]
			|| z >= corner1[1]
			|| z <= corner2[1]) {
			
			return false;
		} else {
			return true;
		}
	}
	
	public boolean insideSpawn(Player player) {
		return insideSpawn(player.getLocation().getX(), player.getLocation().getZ());
	}
	
	public boolean insideSpawn(Block block) {
		return insideSpawn(block.getLocation().getX(), block.getLocation().getZ());
	}
	
	public boolean insideSpawn(Entity entity) {
		return insideSpawn(entity.getLocation().getX(), entity.getLocation().getZ());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 40));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eWelcome to " + HCF.getInstance().getScoreboardTitle() + "&r&e!"));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThe server is currently on &4&lMap 1&e."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThe map kit is Protection &c&l" + HCF.getInstance().getConfig().getInt("map-kit.protection") + "&r&e, Sharpness &c&l" + HCF.getInstance().getConfig().getInt("map-kit.sharpness") + "&r&e."));
		player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 40));
		player.sendMessage("");
		
		//adds or subtracts one based on if the corner value is pos or neg so the player spawns in the middle of the block.
		double x = HCF.getInstance().getConfig().getDouble("spawn.spawn-location.x") < 0 ? HCF.getInstance().getConfig().getDouble("spawn.spawn-location.x") - 0.5 : HCF.getInstance().getConfig().getDouble("spawn.spawn-location.x") + 0.5;
		double z = HCF.getInstance().getConfig().getDouble("spawn.spawn-location.x") < 0 ? HCF.getInstance().getConfig().getDouble("spawn.spawn-location.x") - 0.5 : HCF.getInstance().getConfig().getDouble("spawn.spawn-location.x") + 0.5;
		
		player.teleport(new Location(Bukkit.getWorld("world"), x, HCF.getInstance().getConfig().getInt("spawn.spawn-location.y"), z, 0, 0));
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (insideSpawn(player)) {
				event.setCancelled(true);
			}
 		}
	}
	
	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if (insideSpawn(player) || insideSpawn(event.getEntity())) {
				event.setCancelled(true);
			}
 		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) { 
		Player player = event.getPlayer();
		if (insideSpawn(player) || insideSpawn(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) { 
		Player player = event.getPlayer();
		if (insideSpawn(player) || insideSpawn(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHungerDeplete(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (insideSpawn(player))  {
				event.setCancelled(true);
			}
 		}
	}
	
	@EventHandler
	public void onPlayerEnterSpawn(SpawnEnterEvent event) {
		Player player = event.getPlayer();
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have entered &lSpawn&r&a and gained spawn protection!"));
	}
	
	@EventHandler
	public void onPlayerLeaveSpawn(SpawnLeaveEvent event) {
		Player player = event.getPlayer();
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have left &lSpawn&r&c and lost your spawn protection!"));
	}
}
