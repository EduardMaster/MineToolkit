package net.eduard.api.test.bungee_messager;


import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeMessageListener implements Listener {
	private BungeeMessager bungeeMessager;

	public BungeeMessageListener(BungeeMessager messager) {
		setBungeeMessager(messager);
	}

	@EventHandler
	public void event(PluginMessageEvent event) {
		if (event.getTag().equals(bungeeMessager.getChannel())) {
			byte[] data = event.getData();
			ByteArrayDataInput byteArrayReader = ByteStreams.newDataInput(data);
			BungeeReceiveMessageEvent bungeeReceiveMessageEvent = new BungeeReceiveMessageEvent();
			if (event.getReceiver() instanceof ServerConnection) {
				ServerConnection serverConnection = (ServerConnection) event.getReceiver();
				bungeeReceiveMessageEvent.setServer(serverConnection.getInfo());
				String message = byteArrayReader.readUTF();
				if (message.contains(" ")) {
					bungeeReceiveMessageEvent.setArgs(message.split(" "));
				} else {
					bungeeReceiveMessageEvent.setArgs(message);
				}
			}else if (event.getReceiver() instanceof UserConnection) {
				UserConnection userConnection = (UserConnection) event.getReceiver();
				bungeeReceiveMessageEvent.setServer(userConnection.getReconnectServer());
				String message = byteArrayReader.readUTF();
				if (message.contains(" ")) {
					bungeeReceiveMessageEvent.setArgs(message.split(" "));
				} else {
					bungeeReceiveMessageEvent.setArgs(message);
				}
			}
			BungeeCord.getInstance().getPluginManager().callEvent(bungeeReceiveMessageEvent);
		}
	}

	public BungeeMessager getBungeeMessager() {
		return bungeeMessager;
	}

	public void setBungeeMessager(BungeeMessager bungeeMessager) {
		this.bungeeMessager = bungeeMessager;
	}
}
