package net.eduard.api.lib;

import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.lib.storage.Mine;

public class BukkitMain extends JavaPlugin{

	@Override
	public void onEnable() {
		Mine.registerDefaults();
	}
}
