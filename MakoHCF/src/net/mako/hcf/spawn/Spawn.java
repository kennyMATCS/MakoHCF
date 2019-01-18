package net.mako.hcf.spawn;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import net.mako.hcf.HCF;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

public class Spawn implements Listener {
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
	public boolean insideSpawn(Player player) { 
		double x = player.getLocation().getX();
		double z = player.getLocation().getZ();
		
		
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
	
	public boolean insideSpawn(Block block) { 
		double x = block.getLocation().getX();
		double z = block.getLocation().getZ();
		
		
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
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 40));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "                       &eWelcome to " + HCF.getInstance().getScoreboardTitle() + "&r&e!"));
		player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 40));
		player.sendMessage("");
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
}
