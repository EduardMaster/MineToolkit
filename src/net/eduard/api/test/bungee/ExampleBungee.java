package net.eduard.api.test.bungee;

import org.bukkit.event.EventHandler;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class ExampleBungee implements Listener{
	public ExampleBungee(Plugin plugin) {
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
		bungeeMessager = new BungeeMessager(plugin);
		bungeeMessager.enable();
	}
	private BungeeMessager bungeeMessager;
	
	@EventHandler
	public void event(BungeeReceiveMessageEvent e) {
		String[] args = e.getArgs();
		
	}
	public BungeeMessager getBungeeMessager() {
		return bungeeMessager;
	}
	public void setBungeeMessager(BungeeMessager bungeeMessager) {
		this.bungeeMessager = bungeeMessager;
	}

}
