package net.eduard.api.test.server;

import org.bukkit.plugin.java.JavaPlugin;

public class UsandoCliente extends JavaPlugin{
	private HTTPServerThread serverThread;
	public void onEnable() {
		try {
			serverThread = new HTTPServerThread(this);
			serverThread.start();
		} catch (Exception e2) {
		}
	}
}
