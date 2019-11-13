package net.eduard.api.server;

import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.manager.DBManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Representa os Plugins meus feitos para o Bungeecord
 * 
 * @author Eduard
 *
 */
public class EduardBungeePlugin extends Plugin implements Listener {
	private boolean logEnabled = true;
	private boolean errorLogEnabled = true;
	protected Config config;
	protected Config messages;
	protected DBManager db;

	public DBManager getDB() {
		return db;
	}

	public void reloadDBManager() {
		db = (DBManager) config.get("database");
		
	}

	public void onLoad() {
		config = new Config(this,"bungee-config.yml");
		messages = new Config(this,"bungee-messages.yml");
		db = new DBManager();
		config.add("database", db);
		config.saveConfig();
		reloadDBManager();

	}

	public String message(String path) {
		return messages.message(path);
	}

	public void registerEvents(Listener events) {
		BungeeCord.getInstance().getPluginManager().registerListener(this, events);
	}

	public void registerCommand(Command comand) {
		BungeeCord.getInstance().getPluginManager().registerCommand(this, comand);
	}

	public void log(String msg) {
		if (isLogEnabled()) {
			console(getPrefix() + "§f" + msg);
		}
	}

	public void error(String msg) {
		if (isErrorLogEnabled()) {
			console(getPrefix() + "§c" + msg);
		}
	}

	public void console(String msg) {
		BungeeCord.getInstance().getConsole().sendMessage(new TextComponent(msg));
	}

	public String getPrefix() {
		return "§b[" + getDescription().getName() + "] ";
	}

	public boolean isLogEnabled() {
		return logEnabled;
	}

	public void setLogEnabled(boolean logEnabled) {
		this.logEnabled = logEnabled;
	}

	public boolean isErrorLogEnabled() {
		return errorLogEnabled;
	}

	public void setErrorLogEnabled(boolean errorLogEnabled) {
		this.errorLogEnabled = errorLogEnabled;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getMessages() {
		return messages;
	}

	public void setMessages(Config messages) {
		this.messages = messages;
	}

}
