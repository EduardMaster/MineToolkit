package net.eduard.api.test.bungee_messager;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMessager {
	private Plugin plugin;
	private BungeeMessageListener bungeeMessageListener;
	private String channel;
	public void sendMessage(ServerInfo server,String args) {
		
	}
	public void enable() {
		BungeeCord.getInstance().registerChannel(channel);
		setBungeeMessageListener(new BungeeMessageListener(this));
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, bungeeMessageListener);
	}
	public void disable() {
		BungeeCord.getInstance().unregisterChannel(channel);
	}
	
	public BungeeMessager(Plugin plugin) {
		setPlugin(plugin);
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}
	public BungeeMessageListener getBungeeMessageListener() {
		return bungeeMessageListener;
	}
	public void setBungeeMessageListener(BungeeMessageListener bungeeMessageListener) {
		this.bungeeMessageListener = bungeeMessageListener;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}

}
