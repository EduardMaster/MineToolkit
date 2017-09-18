package net.eduard.eduardapi;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.config.Config;
import net.eduard.api.manager.TimeManager;

public abstract class EduardPlugin extends JavaPlugin {

	protected TimeManager time;
	protected Config config;
	protected Config messages;
	public void onLoad() {
		config = new Config(this);
		messages = new Config(this,"messages.yml");
		time = new TimeManager(this);
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
