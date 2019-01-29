package net.mako.hcf.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import net.mako.hcf.HCF;
import net.mako.hcf.timer.timers.CombatTagTimer;

public class CombatTagWall implements Listener {
	List<Location> storedBlocks = new ArrayList<>();
	
	public CombatTagWall() {
		//loads border blocks from a height of 70 to 120
		loadBorderBlocks(70, 150);
		
		//TODO: https://bukkit.org/threads/sending-several-block-changes-to-one-client.132248/#post-1570250
		
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.scheduleSyncRepeatingTask(HCF.getInstance(), new Runnable() {
			Map<Player, List<Location>> viewable = new HashMap<>();
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					//checking if player has combat timer to continue
					if (HCF.getInstance().getTimerHandler().getTimer(player, CombatTagTimer.class) != null) {
						for (Location location : storedBlocks) {
							//checks if the player is at least 6 blocks from the looped combat tag wall block
							if (player.getLocation().distanceSquared(location) <= 36) {
								//if the player hasn't already had combat tag wall rendered to them, creates a new key and value for them
								if (!viewable.containsKey(player)) {
									List<Location> locations = new ArrayList<>();
									locations.add(location);
									viewable.put(player, locations);
									player.sendBlockChange(location, Material.STAINED_GLASS, (byte) 14);
								//places a new block if it can be rendered to the player
								} else if (!viewable.get(player).contains(location)) {
									viewable.get(player).add(location);
									player.sendBlockChange(location, Material.STAINED_GLASS, (byte) 14);
								}
							//if the player isnt near a rendered block anymore, clears it
							} else if (viewable.containsKey(player)) {
								if (viewable.get(player).contains(location)) {
									player.sendBlockChange(location, Material.AIR, (byte) 1);
									viewable.get(player).remove(location);
								}
							}
						}
					//clears all players combat tag wall blocks if their combat tag has expired
					} else if (HCF.getInstance().getTimerHandler().getTimer(player, CombatTagTimer.class) == null && viewable.get(player) != null) {
						for (Location location : viewable.get(player)) {
							player.sendBlockChange(location, Material.AIR, (byte) 1);
						}
						viewable.get(player).clear();
					}
				}	
			}
		}, 0L, 1L);
	}
	
	public List<Location> getStoredBlocks() {
		return storedBlocks;
	}
	
	public void loadBorderBlocks(int minY, int maxY) {
		int corner1[] = HCF.getInstance().getSpawn().getCornerOne();
		int corner2[] = HCF.getInstance().getSpawn().getCornerTwo();;
		constructWallX(Bukkit.getWorld("world"), corner2[0], corner1[0], minY, maxY, corner1[1], storedBlocks);
		constructWallX(Bukkit.getWorld("world"), corner2[0], corner1[0], minY, maxY, corner2[1], storedBlocks);
		constructWallZ(Bukkit.getWorld("world"), corner2[1], corner1[1], minY, maxY, corner1[0], storedBlocks);
		constructWallZ(Bukkit.getWorld("world"), corner2[1], corner1[1], minY, maxY, corner2[0], storedBlocks);
		
		//TODO: Make compatible with nether and end
	}
	
	//creates invisible wall that will be shown when the player passes it
	public void constructWallX(World world, int x, int x2, int y, int y2, int z, List<Location> blocks) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(HCF.getInstance(), new Runnable() {
			@Override
			public void run() {
				//layer is the block y, x is the block x value. loops through the border dimensions on the x and places a fake block
				for (int layer = y; layer < y2; layer++) {
					for (int i = x; i <= x2; i++) {
						Location location = new Location(world, i, layer, z);
						blocks.add(location);						
					}
				}
			}
		}, 10L);
	}
	
	//creates invisible wall that will be shown when the player passes it
	public void constructWallZ(World world, int z, int z2, int y, int y2, int x, List<Location> blocks) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(HCF.getInstance(), new Runnable() {
			@Override
			public void run() {
				//layer is the block y, z is the block z value. loops through the border dimensions on the z and places a fake block
				for (int layer = y; layer < y2; layer++) {
					for (int i = z; i <= z2; i++) {
						Location location = new Location(world, x, layer, i);
						blocks.add(location);
					}
				}
			}
		}, 10L);
	}
	
	//checks if the player breaks a combat tag wall and cancels it by sending another wall in place
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) { 
		Player player = event.getPlayer();
		for (Location location : getStoredBlocks()) {
			if (location.equals(event.getBlock().getLocation())) {
				player.sendBlockChange(location, Material.STAINED_GLASS, (byte) 14);
			}
		}
	}
}
