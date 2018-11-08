package net.eduard.api.server;

import java.util.List;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.modules.BukkitTimeHandler;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.StorageAPI;

/**
 * Representa os plugins feitos pelo Eduard
 * 
 * @version 1.0
 * @since 4.0
 * @author Eduard
 *
 */
public abstract class EduardPlugin extends JavaPlugin implements BukkitTimeHandler {

	protected Config config;
	protected Config messages;
	protected Config storage;
	protected boolean free;

	public boolean isEditable() {
		return !config.getKeys().isEmpty();
	}

	public boolean hasMessages() {
		return !messages.getKeys().isEmpty();
	}

	public boolean hasStorage() {
		return !storage.getKeys().isEmpty();
	}
	public Config getStorage() {
		return storage;
	}

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
		storage = new Config(this, "storage.yml");
	}

	public void registerPackage(String packname) {
		StorageAPI.registerPackage(getClass(), packname);
	}

	public List<Class<?>> getClasses(String pack) {
		return Mine.getClasses(this, pack);
	}

	public double getPrice() {

		double valor = 0;
		List<Class<?>> classes = Mine.getClasses(this, getClass());
		for (Class<?> claz : classes) {
			valor += Extra.calculateClassValue(claz);
		}
		if (hasMessages()) {
			valor += 5;
		}
		if (isEditable()) {
			valor += 5;
		}
		if (hasStorage()) {
			valor += 10;
		}
		valor += 5;
		return valor;

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
