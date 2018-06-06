package net.eduard.api.test.bungee_messager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class BukkitMessageListener implements PluginMessageListener{


	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		if (channel.equals(bukkitMessager.getChannel())) {
			BukkitReceiveMessageEvent bukkitReceiveMessageEvent = new BukkitReceiveMessageEvent();
			bukkitReceiveMessageEvent.setPlayer(player);
			ByteArrayDataInput byteArrayReader = ByteStreams.newDataInput(data);
			String message = byteArrayReader.readUTF();
			if (message.contains(" ")) {
				bukkitReceiveMessageEvent.setArgs(message.split(" "));
			}else {
				bukkitReceiveMessageEvent.setArgs(message);
			}
			Bukkit.getPluginManager().callEvent(bukkitReceiveMessageEvent);
			
		}
	}
	public BukkitMessager getBukkitMessager() {
		return bukkitMessager;
	}
	public void setBukkitMessager(BukkitMessager bukkitMessager) {
		this.bukkitMessager = bukkitMessager;
	}
	private BukkitMessager  bukkitMessager;
	public BukkitMessageListener(BukkitMessager bukkitMessager) {
		super();
		this.bukkitMessager = bukkitMessager;
	}

}
