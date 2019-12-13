package net.eduard.api.lib.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeMessageListener  implements Listener{
	private BungeeController controller;
	public BungeeMessageListener(BungeeController bungeeController) {
		setController(bungeeController);
	}
	@EventHandler
	public void onMessage(PluginMessageEvent event) {
		if (event.getTag().equals(controller.getChannel())) {
			ByteArrayDataInput data = ByteStreams.newDataInput(event.getData());
			String server = data.readUTF();
			String tag = data.readUTF();
			String line = data.readUTF();
			controller.receiveMessage(server, tag, line);
		}
	}
	public BungeeController getController() {
		return controller;
	}
	public void setController(BungeeController controller) {
		this.controller = controller;
	}
}
