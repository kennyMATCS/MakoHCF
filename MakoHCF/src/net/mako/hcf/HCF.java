package net.mako.hcf;

import org.bukkit.plugin.java.JavaPlugin;

public class HCF extends JavaPlugin {
	private static HCF instance;
	
	@Override
	public void onEnable() {
		this.instance = this;
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static HCF getInstance() {
		return instance;
	}
}
