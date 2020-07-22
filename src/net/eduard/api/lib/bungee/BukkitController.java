package net.eduard.api.lib.bungee;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BukkitController implements ServerController{
	
	private Plugin plugin;
	// na versão 1.15 do minecraft precisa ter : e precisa ser minusculo
	private String channel="bukkit:bungee";
	private BukkitMessageListener listener = new BukkitMessageListener(this);

	@Override
	public void sendMessage(String server, String tag, String line) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(server);
		out.writeUTF(tag);
		out.writeUTF(line);
		Bukkit.getServer().sendPluginMessage(plugin, channel, out.toByteArray());
		
	}

	@Override
	public void sendMessage(String tag, String line) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(channel);
		out.writeUTF(tag);
		out.writeUTF(line);
		Bukkit.getServer().sendPluginMessage(plugin, channel, out.toByteArray());

		
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public void register() {
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, getChannel());
		Bukkit.getMessenger().registerIncomingPluginChannel(plugin, getChannel(), listener );

	}

	@Override
	public void unregister() {
		Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin, getChannel(),listener);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, getChannel());

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
