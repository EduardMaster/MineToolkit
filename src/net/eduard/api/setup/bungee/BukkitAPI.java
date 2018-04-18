package net.eduard.api.setup.bungee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public final class BukkitAPI {
	private static List<Server> servers=new ArrayList<>();
	private static List<ServerPlayer> players = new ArrayList<>();
	private static Plugin plugin;
	private static PluginMessageListener listener = new BungeeServerMessageListener();
	private static boolean registered;
	static {
		plugin = JavaPlugin.getProvidingPlugin(BukkitAPI.class);
		register(plugin);
		
	}
	public static ServerPlayer getPlayer(String name) {
		for (ServerPlayer player : players) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}
	public static boolean hasPlayer(String name) {
		return getPlayer(name)!= null;
	}
	public static ServerPlayer newPlayer(String name) {
		ServerPlayer player = new ServerPlayer()	;
		player.setName(name);
		players.add(player);
		return player;
		
		
	}
	public static void remove(String name) {
		players.remove(getPlayer(name));
	}
	public static boolean hasServer(String name) {
		return getServer(name) != null;
	}
	public static Server newServer(String name) {
		Server server = new Server();
		server.setName(name);
		servers.add(server);
		return server;
	}
	public static void removeServer(String name) {
		servers.remove(getServer(name));
	}
	public static Server getServer(String name) {
		for (Server server : servers) {
			if (server.name.equals(name)) {
				return server;
			}
		}
//		Server server = new Server();
//		server.name = name;
//		servers.add(server);
		return null;
	}
	
	public static String[] readBungeeMessage(ByteArrayDataInput data) {
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
	public static void register(Plugin plugin) {
		if (registered) {
			unregister();
		}
		registered = true;
		Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", listener);
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
	}
	public static void unregister() {
		if (registered) {
			Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord",listener);
			Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord");
			registered = false;
		}
	}
	public static void send(ByteArrayDataOutput message) {
		Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", message.toByteArray());
	}

	public static void send(Player player, ByteArrayDataOutput message) {
		player.sendPluginMessage(plugin, "BungeeCord", message.toByteArray());
	}

	
	public static void log(String message) {

		Bukkit.getConsoleSender().sendMessage("§b[BungeeCord] §f" + message);
	}

	public static boolean isServersRequest(String request) {
		return (request.equals("GetServers"));
	}

	public static boolean isServerRequest(String request) {
		return request.equals("GetServer");
	}

	public static String[] getServers(ByteArrayDataInput data) {
		String[] serversNames = data.readUTF().split(", ");
		log("§aSERVERS: §F" + Arrays.asList(serversNames));
		return serversNames;
	}

	// public static Plugin getInstance() {
	// return JavaPlugin.getProvidingPlugin(BungeeMine.class);
	// }
	public static void connectServer(Player player, String server) {
		log("CONNECT TO §E" + server + "§F PLAYER §E" + player.getName());
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);

		send(player, out);
	}

	public static void connectServer(String playerName, String server) {
		log("CONNECT TO §E" + server + "§F PLAYER §E" + playerName);
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ConnectOther");
		out.writeUTF(playerName);
		out.writeUTF(server);
		send(out);
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
	public static void sendForwardToServer(String subChannel, String server, Object... data) {
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

		send(out);
	}

	public static void sendForwardToPlayer(String playerName, String subChannel, Object... data) {
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

		send(out);
	}
	
	public static boolean isPlayerListRequest(String request) {
		return request.equals("GetServer");
	}

	public static String[] getPlayerList(ByteArrayDataInput data) {
		String server = data.readUTF();
		String[] players = data.readUTF().split(", ");
		log("§ASERVER PLAYERS: §F" + server + " | " + Arrays.asList(players));
		return players;
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

	public static UUID getPlayerId(Player p, ByteArrayDataInput data) {
		UUID id = UUID.nameUUIDFromBytes(data.readUTF().getBytes());
		log("§aPLAYER UUID §F" + p.getName() + " | " + id);
		return id;
	}

	public static UUID getPlayerId(ByteArrayDataInput data) {
		String playerName = data.readUTF();
		UUID id = UUID.nameUUIDFromBytes(data.readUTF().getBytes());
		log("§aPLAYER NAME ID §F" + playerName + " | " + id);
		return id;
	}

	public static boolean isPlayerIpRequest(String request) {

		return request.equals("IP");
	}

	public static String getPlayerIp(Player p, ByteArrayDataInput data) {
		String host = data.readUTF();
		int port = data.readInt();
		log("§aPLAYER IP §F" + p.getName() + " | " + host + ":" + port);
		return host + ":" + port;
	}

	public static boolean isServerIpRequest(String request) {

		return request.equals("ServerIP");
	}

	public static void updatePlayerCount(String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF(server);
		send(out);
	}

	public static void updateServersNames() {
		log("? SERVERS");
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServers");
		send(out);
	}

	public static void updateServerIp(String server) {
		log("? SERVER IP §e" + server);
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ServerIP");
		out.writeUTF(server);
		send(out);
	}

	public static void updatePlayerId(Player player) {
		log("? PLAYER ID §e" + player.getName());
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("UUID");

		send(player, out);
	}

	public static void updatePlayerId(String playerName) {
		log("? PLAYER NAME ID §e" + playerName);
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("UUIDOther");
		out.writeUTF(playerName);
		send(out);
	}

	public static void updatePlayerIp(Player player) {
		log("? PLAYER IP §e" + player.getName());
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("IP");
		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}

	public static void updatePlayerList(String server) {
		log("? SERVER PLAYERS §E" + server);
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerList");
		out.writeUTF(server);
		send(out);
	}

	public static void updatePlayerServer(Player player) {
		log("? PLAYER SERVER §E" + player.getName());
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServer");
		send(player, out);
	}

	public static void sendMessageToPlayer(String playerName, String message) {
		log("CHAT §E" + playerName + "§F MESSAGE §E" + message);
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Message");
		out.writeUTF(playerName);
		out.writeUTF(message);
		send(out);
	}

	public static void kickPlayer(String playerName, String reason) {
		log("KICK §e" + playerName + "'§E REASON §F" + reason);
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("KickPlayer");
		out.writeUTF(playerName);
		out.writeUTF(reason);

		send(out);
	}
	public static class BungeeCordMessageEvent extends Event {
		private static final HandlerList handlers = new HandlerList();

		@Override
		public HandlerList getHandlers() {
			return handlers;
		}

		public static HandlerList getHandlerList() {
			return handlers;
		}

		public BungeeCordMessageEvent(Player player, ByteArrayDataInput data) {
			this.player = player;
			this.data = data;
			this.request = data.readUTF();
		}

		public ByteArrayDataInput getData() {
			return data;
		}

		public Player getPlayer() {
			return player;
		}

		public String getRequest() {
			return request;
		}

		private ByteArrayDataInput data;
		private Player player;
		private String request;

	}
	public static class ServerPlayer{
		private String name;
		private UUID uuid;
		private int port;
		private String host;
		private String server;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public UUID getUuid() {
			return uuid;
		}
		public void setUuid(UUID uuid) {
			this.uuid = uuid;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public String getServer() {
			return server;
		}
		public void setServer(String server) {
			this.server = server;
		}
		
	}
	
	public static class Server{
		private String name;
		private String host;
		private int port;
		private int playersAmount;
		private List<String> players = new ArrayList<>();
		public int getPlayersAmount() {
			return playersAmount;
		}
		public void setPlayersAmount(int playersAmount) {
			this.playersAmount = playersAmount;
		}
		public List<String> getPlayers() {
			return players;
		}
		public void setPlayers(List<String> players) {
			this.players = players;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
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
	private static class BungeeServerMessageListener implements PluginMessageListener{

		@Override
		public void onPluginMessageReceived(String channel, Player player, byte[] message) {
			System.out.println("Channel "+channel+" sent a message.");
			if (!channel.equals("BungeeCord")) {
				return;
			}
			System.out.println("BungeeCord sent a message.");
			ByteArrayDataInput in = ByteStreams.newDataInput(message);
			BungeeCordMessageEvent event = new BungeeCordMessageEvent(player, in);
			Bukkit.getPluginManager().callEvent(event);
			ByteArrayDataInput msg = ByteStreams.newDataInput(message);
			try {
				String request = msg.readUTF();
				if (isPlayerCountRequest(request)) {
					String server = msg.readUTF();
					int count = msg.readInt();
					getServer(server).setPlayersAmount(count);
				} else if (isServerIpRequest(request)) {
					String name = msg.readUTF();
					Server servidor = getServer(name);
					servidor.setHost(msg.readUTF());
					servidor.setPort(msg.readUnsignedShort());
					log("§aSERVER IP §F" + servidor.getName() + " | " + servidor.getHost() + ":"
							+ servidor.getPort());

				} else if (isServerRequest(request)) {
					String name = msg.readUTF();
					Server server = getServer(name);
					ServerPlayer jogador = getPlayer(player.getName());
					jogador.setServer(name);
					if (!server.getPlayers().contains(player.getName()))
						server.getPlayers().add(player.getName());
					log("§APLAYER §F" + player.getName() + "§A CONNECTED §F" + server);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
	}
}
