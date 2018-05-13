package net.eduard.api;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.config.Config;
import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.lib.storage.manager.TimeManager;
/**
 * Representa os plugins feitos pelo Eduard
 * @version 1.0
 * @since 4.0
 * @author Eduard
 *
 */
public abstract class EduardPlugin extends JavaPlugin {

	protected TimeManager time;
	protected Config config;
	protected Config messages;

	public void onLoad() {
		config = new Config(this);
		messages = new Config(this, "messages.yml");
		time = new TimeManager(this);
	}

	public void registerPackage(String packname) {
		StorageAPI.registerPackage(getClass(), packname);
	}

	public List<Class<?>> getClasses(String clazzName) {
		return Mine.getClasses(this, clazzName);
	}

	public List<Class<?>> getClasses(JavaPlugin plugin, Class<?> clazz) {
		return Mine.getClasses(plugin, clazz);
	}

	public void save() {

	}

	public void reload() {
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

	public TimeManager getTime() {
		return time;
	}

	public Config getMessages() {
		return messages;
	}

	public Config getConfigs() {
		return config;
	}
}
