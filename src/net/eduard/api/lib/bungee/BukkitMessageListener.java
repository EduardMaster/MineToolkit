package net.eduard.api.lib.bungee;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class BukkitMessageListener implements PluginMessageListener{

	public BukkitMessageListener(BukkitController bukkitController) {
		setController(bukkitController);
	}

	private BukkitController controller;
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (channel.equals(BungeeAPI.getChannel())) {
			ByteArrayDataInput data = ByteStreams.newDataInput(message);
			String server = data.readUTF();
			String tag = data.readUTF();
			String line = data.readUTF();
			controller.receiveMessage(server, tag, line);
		}
	}

	public BukkitController getController() {
		return controller;
	}

	public void setController(BukkitController controller) {
		this.controller = controller;
	}

}
