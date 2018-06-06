package net.eduard.api.test.bungee_messager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMessager {

	private JavaPlugin plugin;
	private String channel;
	private BukkitMessageListener bukkitMessageListener;

	public void sendMessage(Player player, String... args) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String arg : args)
			stringBuilder.append(arg);
		sendMessage(player, stringBuilder.toString());
	}

	public void sendMessage(Player player, String message) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(stream);
		try {
			out.writeUTF(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.sendPluginMessage(plugin, channel, stream.toByteArray());
	}

	public void enable() {
		Bukkit.getMessenger().registerIncomingPluginChannel(plugin, channel, bukkitMessageListener);
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, channel);
	}

	public void disable() {
		Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin, channel, bukkitMessageListener);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, channel);
	}

	public BukkitMessager(JavaPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public void setPlugin(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public BukkitMessageListener getBukkitMessageListener() {
		return bukkitMessageListener;
	}

	public void setBukkitMessageListener(BukkitMessageListener bukkitMessageListener) {
		this.bukkitMessageListener = bukkitMessageListener;
	}

}
