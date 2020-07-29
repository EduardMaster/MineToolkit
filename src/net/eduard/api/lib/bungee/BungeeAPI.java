package net.eduard.api.lib.bungee;

import java.util.ArrayList;
import java.util.List;

public class BungeeAPI {
	private static String channel="bukkit:bungee";
	public static String getChannel() {
		return channel;
	}
	private static List<ServerSpigot> servers = new ArrayList<>();
	private static ServerController controller;
	public static ServerSpigot getServer(String serverName) {
		for (ServerSpigot server : BungeeAPI.getServers()) {
			if (server.getName().equals(serverName)) {
				return server;
			}
		}
		ServerSpigot server = new ServerSpigot(serverName);
		BungeeAPI.getServers().add(server);
		return server;
	}

	static {
		try {
			Class.forName("org.bukkit.Bukkit");
			controller = new BukkitController();
		} catch (Exception e) {
			controller = new BungeeController();
		}
	}
	public static BukkitController getBukkit() {
		return (BukkitController)controller;
	}
	public static BungeeController getBungee() {
		return (BungeeController)controller;
	}
	private static List<ServerMessageHandler> handlers = new ArrayList<>();
	public static void register(ServerMessageHandler receiver) {
		handlers.add(receiver);
	}
	public static List<ServerMessageHandler> getHandlers() {
		return handlers;
	}
	public static void setHandlers(List<ServerMessageHandler> handlers) {
		BungeeAPI.handlers = handlers;
	}
	public static ServerController getController() {
		return controller;
	}
	public static void setController(ServerController controller) {
		BungeeAPI.controller = controller;
	}
	public static List<ServerSpigot> getServers() {
		return servers;
	}
	public static void setServers(List<ServerSpigot> servers) {
		BungeeAPI.servers = servers;
	}
	

}
