package net.eduard.api.lib;

import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.lib.storage.bukkit_storables.BukkitStorables;

public class EduardLib extends JavaPlugin{

	@Override
	public void onEnable() {
		BukkitStorables.load();
	}
}
