package net.eduard.api.server;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.manager.TimeManager;
import net.eduard.api.lib.modules.BukkitTimeHandler;
import net.eduard.api.lib.storage.StorageAPI;

/**
 * Representa os plugins feitos pelo Eduard
 * 
 * @version 1.0
 * @since 4.0
 * @author Eduard
 *
 */
public abstract class EduardPlugin extends JavaPlugin implements BukkitTimeHandler{

	protected Config config;
	protected Config messages;
	protected boolean free;

	public boolean isFree() {

		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}
	public Plugin getPlugin() {
		return this;
	}

	public void onLoad() {
		config = new Config(this);
		messages = new Config(this, "messages.yml");
	}

	public void registerPackage(String packname) {
		StorageAPI.registerPackage(getClass(), packname);
	}

	public List<Class<?>> getClasses(String pack) {
		return Mine.getClasses(this, pack);
	}

	public void save() {

	}

	public void reload() {
	}

	public void configDefault() {

	}
	


	public boolean getBoolean(String path) {
		return config.getBoolean(path);
	}

	public String message(String path) {
		return messages.message(path);
	}

	public List<String> getMessages(String path) {
		return messages.getMessages(path);
	}


	public Config getMessages() {
		return messages;
	}

	public Config getConfigs() {
		return config;
	}
}
