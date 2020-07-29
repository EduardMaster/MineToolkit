package net.eduard.api.lib.bungee;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BukkitController implements ServerController{
	
	private Plugin plugin;

	private BukkitMessageListener listener = new BukkitMessageListener(this);

	@Override
	public void sendMessage(String server, String tag, String line) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(server);
		out.writeUTF(tag);
		out.writeUTF(line);
		Bukkit.getServer().sendPluginMessage(plugin, BungeeAPI.getChannel(), out.toByteArray());
		
	}

	@Override
	public void sendMessage(String tag, String line) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("bungeecord");
		out.writeUTF(tag);
		out.writeUTF(line);
		Bukkit.getServer().sendPluginMessage(plugin, BungeeAPI.getChannel(), out.toByteArray());

		
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}


	@Override
	public void register() {
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, BungeeAPI.getChannel());
		Bukkit.getMessenger().registerIncomingPluginChannel(plugin, BungeeAPI.getChannel(), listener );
		Bukkit.getConsoleSender().sendMessage("§aRegistrando sistema de conexão com Bungeecoard via Plugin Messaging");

	}

	@Override
	public void unregister() {
		Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin, BungeeAPI.getChannel(),listener);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, BungeeAPI.getChannel());

	}

	@Override
	public void receiveMessage(String server, String tag, String line) {
		for (ServerMessageHandler handler : BungeeAPI.getHandlers()) {
			handler.onMessage(server, tag, line);
		}

	}

	@Override
	public void connect(String player, int serverType, int serverState) {
		sendMessage("connect", player+" "+serverType+" "+serverState);
		
	}

	@Override
	public void setState(String server, int state) {
		sendMessage("set-state", server+" "+state);
	}

}
