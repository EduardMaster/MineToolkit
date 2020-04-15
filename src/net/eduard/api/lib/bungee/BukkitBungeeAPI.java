package net.eduard.api.lib.bungee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
/**
 * API de controlar o BungeeCord pelo Servidor Spigot
 * @author Eduard
 * @version 2.0
 * @since 2.3
 *
 */
public final class BukkitBungeeAPI {
	public static String getCurrentServer() {
		return currentServer;
	}

	public static int getPlayerCount(String server) {
		return getServer(server).playerCount;
	}

	public static List<String> getPlayers(String server) {
		return getServer(server).players;
	}

	public static String getPlayerServer(String player) {
		return getPlayer(player).server;
	}

	private static class BukkitListener implements PluginMessageListener {

		@Override
		public void onPluginMessageReceived(String channel, Player player, byte[] message) {

			if (!channel.equals("BungeeCord")) {
				return;
			}
			try {
				ByteArrayDataInput data = ByteStreams.newDataInput(message);
				String request = data.readUTF();
				if (isPlayerCountRequest(request)) {
					String server = data.readUTF();
					int playercount = data.readInt();
					getServer(server).playerCount = playercount;
				} else if (isServersRequest(request)) {
					String[] servers = data.readUTF().split(", ");
					log("§aRESPONSE SERVERS: §F" + Arrays.asList(servers));
					for (String server : servers) {
						getServer(server);
					}
				} else if (isPlayerListRequest(request)) {
					String server = data.readUTF();
					String[] players = data.readUTF().split(", ");
					List<String> list = Arrays.asList(players);
					getServer(server).players = list;
					log("§aRESPONSE PLAYERS FROM SERVER §F" + server + " : " + list);
				} else if (isServerRequest(request)) {
					String server = data.readUTF();
					currentServer = server;
				} else if (isServerIpRequest(request)) {
					String serverName = data.readUTF();
					String ip = data.readUTF();
					int port = data.readUnsignedShort();
					SimpleServer server = getServer(serverName);
					server.host = ip;
					server.port = port;
				} else if (isUUIDRequest(request)) {
					getPlayer(player.getName()).uuid = data.readUTF();
				} else if (isUUIDOtherRequest(request)) {
					String playerName = data.readUTF();
					String uuid = data.readUTF();
					getPlayer(playerName).uuid = uuid;
				} else if (isPlayerIpRequest(request)) {
					String ip = data.readUTF();
					int port = data.readInt();
					SimplePlayer fake = getPlayer(player.getName());
					fake.host = ip;
					fake.port = port;
				}

			} catch (Exception e) {
		//		e.printStackTrace();
				log("Deu erro na linha "+e.getLocalizedMessage());
			}
		}

	}

	private static boolean debug = true;

	public static void log(String message) {

		if (debug)
			Bukkit.getConsoleSender().sendMessage("§7[§8BukkitBungeeAPI§7] §f" + message);
	}

	public static class SimpleServer {
		private String name;
		private List<String> players = new ArrayList<>();
		private int playerCount = 0;
		private String host;
		private int port;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<String> getPlayers() {
			return players;
		}

		public void setPlayers(List<String> players) {
			this.players = players;
		}

		public int getPlayerCount() {
			return playerCount;
		}

		public void setPlayerCount(int playerCount) {
			this.playerCount = playerCount;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

	}

	public static class SimplePlayer {

		private String server;
		private String name;
		private String uuid;
		private String host;
		private int port;

		public String getServer() {
			return server;
		}

		public void setServer(String server) {
			this.server = server;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

	}

	private static String currentServer = "lobby";
	private static Map<String, SimpleServer> servers = new HashMap<>();
	private static Map<String, SimplePlayer> players = new HashMap<>();

	public static SimpleServer getServer(String serverName) {
		SimpleServer server = servers.get(serverName);
		if (server == null) {
			server = new SimpleServer();
			server.name = serverName;
			servers.put(serverName, server);
		}
		return server;
	}

	public static SimplePlayer getPlayer(String playerName) {
		SimplePlayer player = players.get(playerName);
		if (player == null) {
			player = new SimplePlayer();
			player.name = playerName;
			players.put(playerName, player);
		}
		return player;
	} 

	static Player getFirstPlayer() {
		return Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
	}

	private static PluginMessageListener listener;

	public static Plugin getInstance() {
		return JavaPlugin.getProvidingPlugin(BukkitBungeeAPI.class);
	}

	static void sendMessage(ByteArrayDataOutput message) {
		Bukkit.getServer().sendPluginMessage(getInstance(), "BungeeCord", message.toByteArray());
//		sendMessage(getFirstPlayer(), message);
	}

	static void sendMessage(Player player, ByteArrayDataOutput message) {
		player.sendPluginMessage(getInstance(), "BungeeCord", message.toByteArray());
	}

	/**
	 * String server to send to, ALL to send to every server (except the one sending
	 * the plugin message), or ONLINE to send to every server that's online (except
	 * the one sending the plugin message)
	 * 
	 * @param subChannel
	 * @param server
	 * @param data
	 */
	public static void forwardToServer(String subChannel, String server, Object... data) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Forward"); // So BungeeCord knows to forward it
		out.writeUTF(server);
		out.writeUTF(subChannel); // The channel name to check if this your data

		ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
		DataOutputStream msgout = new DataOutputStream(msgbytes);
		try {
			msgout.writeInt(data.length);
			for (int i = 0; i < data.length; i++) {
				msgout.writeUTF(data[i].toString());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // You can do anything you want with msgout

		out.writeShort(msgbytes.toByteArray().length);
		out.write(msgbytes.toByteArray());

		sendMessage(out);
	}

	public static void forwardToPlayer(String playerName, String subChannel, Object... data) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ForwardToPlayer"); // So BungeeCord knows to forward it
		out.writeUTF(playerName);
		out.writeUTF(subChannel); // The channel name to check if this your data

		ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
		DataOutputStream msgout = new DataOutputStream(msgbytes);
		try {
			msgout.writeInt(data.length);
			for (int i = 0; i < data.length; i++) {
				msgout.writeUTF(data[i].toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} // You can do anything you want with msgout

		out.writeShort(msgbytes.toByteArray().length);
		out.write(msgbytes.toByteArray());

		sendMessage(out);
	}

	static {
		Plugin plugin = getInstance();
		listener = new BukkitListener();
		Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", listener);
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
	}

	public static void unregister() {
		Plugin plugin = getInstance();
		Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord", listener);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord");
	}

	public static void connectToServer(Player player, String server) {
		log("CONNECT TO §E" + server + "§F PLAYER §E" + player.getName());
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);

		sendMessage(player, out);
	}

	public static void connectToServer(String playerName, String server) {
		log("CONNECT TO §E" + server + "§F PLAYER §E" + playerName);
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ConnectOther");
		out.writeUTF(playerName);
		out.writeUTF(server);
		sendMessage(out);
	}

	static String[] readMessage(ByteArrayDataInput data) {
		short len = data.readShort();
		byte[] msgbytes = new byte[len];

		data.readFully(msgbytes);
		String[] result = null;
		DataInputStream msgIn = new DataInputStream(new ByteArrayInputStream(msgbytes));
		try {
			result = new String[msgIn.readInt()];
			for (int i = 0; i < result.length; i++) {
				String somedata = msgIn.readUTF();
				result[i] = somedata;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = new String[0];
		}
		return result;
	}

	public static void kickPlayer(String playerName, String reason) {
		log("KICK §e" + playerName + "'§E REASON §F" + reason);
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("KickPlayer");
		out.writeUTF(playerName);
		out.writeUTF(reason);

		sendMessage(out);
	}

	public static void sendMessage(String playerName, String message) {
		log("CHAT §E" + playerName + "§F MESSAGE §E" + message);
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Message");
		out.writeUTF(playerName);
		out.writeUTF(message);
		sendMessage(out);
	}

	public static boolean isServersRequest(String request) {
		return (request.equals("GetServers"));
	}

	public static boolean isPlayerIpRequest(String request) {

		return request.equals("IP");
	}

	public static boolean isServerRequest(String request) {
		return request.equals("GetServer");
	}

	public static boolean isPlayerListRequest(String request) {
		return request.equals("PlayerList");
	}

	public static boolean isPlayerCountRequest(String request) {
		return request.equals("PlayerCount");
	}

	public static boolean isUUIDRequest(String request) {

		return request.equals("UUID");
	}

	public static boolean isUUIDOtherRequest(String request) {

		return request.equals("UUIDOther");
	}

	public static boolean isServerIpRequest(String request) {

		return request.equals("ServerIP");
	}

	public static void requestServersNames() {
		log("REQUEST SERVERS NAMES");
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServers");
		sendMessage(out);
	}

	public static void requestServerIp(String server) {
		log("REQUEST IP FROM SERVER §e" + server);
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ServerIP");
		out.writeUTF(server);
		sendMessage(out);
	}

	public static void requestPlayerCount(String server) {
		log("REQUEST PLAYER COUNT FROM SERVER §e" + server);
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF(server);
		sendMessage(out);
	}

	public static void requestPlayerId(Player player) {
		log("REQUEST ID FROM PLAYER §e" + player.getName());
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("UUID");

		sendMessage(player, out);
	}

	public static void requestPlayerId(String playerName) {
		log("REQUEST ID FROM TARGET PLAYER §e" + playerName);
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("UUIDOther");
		out.writeUTF(playerName);
		sendMessage(out);
	}

	public static void requestPlayerIp(Player player) {
		log("REQUEST IP FROM PLAYER §e" + player.getName());
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("IP");
		player.sendPluginMessage(getInstance(), "BungeeCord", out.toByteArray());
	}

	public static void requestPlayerList(String server) {
		log("REQUEST PLAYERS FROM SERVER §E" + server);
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerList");
		out.writeUTF(server);
		sendMessage(out);
	}

	public static void requestCurrentServer() {
		log("REQUEST CURRENT SERVER");
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServer");
		sendMessage(out);
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		BukkitBungeeAPI.debug = debug;
	}

}
