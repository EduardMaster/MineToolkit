package net.eduard.api.lib.modules;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.*;

/**
 * API de controlar o BungeeCord pelo Servidor Spigot
 * @author Eduard
 * @version 2.0
 * @since 2.3
 *
 */
@SuppressWarnings("unused")
public final class BukkitBungeeAPI {
	private static boolean debug = true;

	public static void log(String message) {
		if (debug)
			Bukkit.getConsoleSender().sendMessage("§b[BukkitBungeeAPI] §e" + message);
	}

	private static String currentServer = "lobby";
	private static final Map<String, SimpleServer> servers = new HashMap<>();
	private static final Map<String, SimplePlayer> players = new HashMap<>();
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

	private static class BukkitBungeeListener implements PluginMessageListener, Listener {

		@EventHandler
		public void event(PlayerJoinEvent e){
			sendMissingMessages(e.getPlayer());
		}


		@Override
		public void onPluginMessageReceived(String channel, Player player, byte[] message) {

			if (!channel.equals("BungeeCord")) {
				return;
			}
			try {
				ByteArrayInputStream stream = new ByteArrayInputStream(message);
				DataInputStream dataReader = new DataInputStream(stream);
				String request = dataReader.readUTF();
				if (isPlayerCountRequest(request)) {
					String serverName = dataReader.readUTF();
					SimpleServer server = getServer(serverName);
					server.playerCount = dataReader.readInt();
					log("RESPONSE §aPLAYER-AMOUNT §fFROM SERVER "+server.name +" = " +server.playerCount );
				} else if (isServersRequest(request)) {
					String[] servers = dataReader.readUTF().split(", ");
					log("RESPONSE §aSERVERS-NAMES = §f" + Arrays.asList(servers));
					for (String server : servers) {
						getServer(server);
					}
				} else if (isPlayerListRequest(request)) {
					String serverName = dataReader.readUTF();
					SimpleServer server = getServer(serverName);
					String[] players = dataReader.readUTF().split(", ");
					List<String> list = Arrays.asList(players);
					server.players = list;
					log("RESPONSE §aPLAYER-NAMES §fFROM SERVER "+server.name +" = " +list );
				} else if (isServerRequest(request)) {
					currentServer = dataReader.readUTF();
					log("§aRESPONSE §aSERVER-NAME §f= " + currentServer);
				} else if (isServerIpRequest(request)) {
					String serverName = dataReader.readUTF();
					String ip = dataReader.readUTF();
					int port = dataReader.readUnsignedShort();
					SimpleServer server = getServer(serverName);
					server.host = ip;
					server.port = port;
					log("§aRESPONSE §aSERVER-IP §fFROM SERVER "+serverName+" = " + server.host+":"+server.port);
				} else if (isUUIDRequest(request)) {
					SimplePlayer user = getPlayer(player.getName());
					user.uuid = dataReader.readUTF();
					log("§aRESPONSE §aPLAYER-UUID§f FROM"+player.getName()+" = " + user.uuid);
				} else if (isUUIDOtherRequest(request)) {
					String playerName = dataReader.readUTF();
					SimplePlayer user = getPlayer(playerName);
					user.uuid = dataReader.readUTF();
					log("§aRESPONSE §aPLAYER-UUID§f FROM"+playerName+" = " + user.uuid);
				} else if (isPlayerIpRequest(request)) {
					String ip = dataReader.readUTF();
					int port = dataReader.readInt();
					SimplePlayer user = getPlayer(player.getName());
					user.host = ip;
					user.port = port;
					log("§aRESPONSE §aPLAYER-IP§f FROM"+player.getName()+" = " + user.host+":"+user.port);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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

	private static  BukkitBungeeListener listener;
	private static Queue<ByteArrayOutputStream> needSend = new ArrayDeque<>();
	public static void sendMissingMessages(Player player){
		log("TRYING SEND MISSING MESSAGES");
		if (needSend.isEmpty())return;
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			log("SENDING "+needSend.size()+ " MISSING MESSAGES" );
			while(!needSend.isEmpty()){
				ByteArrayOutputStream message = needSend.remove();
				sendMessage(player, message);
			}
		},1);

	}
	public static Plugin getInstance() {
		return plugin;
	}

	public static void sendMessage(ByteArrayOutputStream message) {

		if (Bukkit.getOnlinePlayers().size() == 0){
			log("PUTTING THIS MESSAGE IN THE QUEUE");
			needSend.add(message);
		}else{
			sendMessage(Bukkit.getOnlinePlayers().iterator().next() , message);
		}



	}

	public static void sendMessage(Player player, ByteArrayOutputStream message) {
		player.sendPluginMessage(getInstance(), "BungeeCord", message.toByteArray());
	}

	/**
	 * String server to send to, ALL to send to every server (except the one sending
	 * the plugin message), or ONLINE to send to every server that's online (except
	 * the one sending the plugin message)
	 *
	 * @param subChannel Nome do subchannel
	 * @param server Servidor que vai ler estes dados
	 * @param data Lista de objetos
	 */
	public static void forwardToServer(String subChannel, String server, Object... data) {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dataWriter = new DataOutputStream(stream);

			dataWriter.writeUTF("Forward"); // So BungeeCord knows to forward it
			dataWriter.writeUTF(server);
			dataWriter.writeUTF(subChannel); // The channel name to check if this your data

			ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			DataOutputStream msgout = new DataOutputStream(msgbytes);
			try {
				msgout.writeInt(data.length);
				for (Object datum : data) {
					msgout.writeUTF(datum.toString());
				}

			} catch (IOException e) {
				e.printStackTrace();
			} // You can do anything you want with msgout

			dataWriter.writeShort(msgbytes.toByteArray().length);
			dataWriter.write(msgbytes.toByteArray());

			sendMessage(stream);
		}catch (Exception ignored){
		}
	}

	public static void forwardToPlayer(String playerName, String subChannel, Object... data) {

		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dataWriter = new DataOutputStream(stream);

			dataWriter.writeUTF("ForwardToPlayer"); // So BungeeCord knows to forward it
			dataWriter.writeUTF(playerName);
			dataWriter.writeUTF(subChannel); // The channel name to check if this your data


			ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			DataOutputStream msgout = new DataOutputStream(msgbytes);
			try {
				msgout.writeInt(data.length);
				for (Object dataLoop : data) {
					msgout.writeUTF(dataLoop.toString());
				}

			} catch (IOException e) {
				e.printStackTrace();
			} // You can do anything you want with msgout

			dataWriter.writeShort(msgbytes.toByteArray().length);
			dataWriter.write(msgbytes.toByteArray());

			sendMessage(stream);
		}catch (Exception ignored){
		}

	}

	public static void register(Plugin plugin){
		BukkitBungeeAPI.plugin = plugin;
		listener = new BukkitBungeeListener();
		Bukkit.getPluginManager().registerEvents(listener , plugin);
		Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", listener);
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
		log("Registrando Sistema");

	}
	private static Plugin plugin;
	public static void unregister() {
		HandlerList.unregisterAll(listener);
		Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord", listener);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord");
		needSend.clear();
		log("Desregistrando Sistema");
	}

	public static void connectToServer(Player player, String server) {
		log("CONNECT TO §E" + server + "§F PLAYER §E" + player.getName());
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dataWriter = new DataOutputStream(stream);

			dataWriter.writeUTF("Connect");
			dataWriter.writeUTF(server);

			sendMessage(player,stream);
		}catch (Exception ignored){
		}


	}

	public static void connectToServer(String playerName, String server) {
		log("CONNECT TO §E" + server + "§F PLAYER §E" + playerName);
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dataWriter = new DataOutputStream(stream);

			dataWriter.writeUTF("ConnectOther");
			dataWriter.writeUTF(playerName);
			dataWriter.writeUTF(server);
			sendMessage(stream);
		}catch (Exception ignored){
		}

	}



	public static void kickPlayer(String playerName, String reason) {
		log("KICK §e" + playerName + "'§E REASON §F" + reason);
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dataWriter = new DataOutputStream(stream);

			dataWriter.writeUTF("KickPlayer");
			dataWriter.writeUTF(playerName);
			dataWriter.writeUTF(reason);
			sendMessage(stream);
		}catch (Exception ignored){
		}



	}

	public static void sendMessage(String playerName, String message) {
		log("SENDING MESSAGE TO §a" + playerName + " §fMESSAGE §a" + message);
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dataWriter = new DataOutputStream(stream);

			dataWriter.writeUTF("Message");
			dataWriter.writeUTF(playerName);
			dataWriter.writeUTF(message);
			sendMessage(stream);
		}catch (Exception ignored){
		}

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


	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		BukkitBungeeAPI.debug = debug;
	}


	public static void requestServersNames() {
		log("REQUEST SERVERS NAMES");
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dataWriter = new DataOutputStream(stream);

			dataWriter.writeUTF("GetServers");
			sendMessage(stream);
		}catch (Exception ignored){

		}
	}

	public static void requestServerIp(String server) {
		log("REQUEST IP FROM SERVER §a" + server);
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dataWriter = new DataOutputStream(stream);

			dataWriter.writeUTF("ServerIP");
			dataWriter.writeUTF(server);
			sendMessage(stream);
		}catch (Exception ignored){

		}


	}

	public static void requestPlayerCount(String server) {
		log("REQUEST PLAYER COUNT FROM SERVER §a" + server);
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dataWriter = new DataOutputStream(stream);

			dataWriter.writeUTF("PlayerCount");
			dataWriter.writeUTF(server);
			sendMessage(stream);
		}catch (Exception ignored){

		}
	}

	public static void requestPlayerId(Player player) {
		log("REQUEST ID FROM PLAYER §a" + player.getName());
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dataWriter = new DataOutputStream(stream);

			dataWriter.writeUTF("UUID");

			sendMessage(player,stream);
		}catch (Exception ignored){
		}

	}

	public static void requestPlayerId(String playerName) {
		log("REQUEST ID FROM TARGET PLAYER §a" + playerName);
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dataWriter = new DataOutputStream(stream);

			dataWriter.writeUTF("UUIDOther");
			dataWriter.writeUTF(playerName);
			sendMessage(stream);
		}catch (Exception ignored){
		}

	}

	public static void requestPlayerIp(Player player) {
		log("REQUEST IP FROM PLAYER §a" + player.getName());
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dataWriter = new DataOutputStream(stream);

			dataWriter.writeUTF("IP");

			sendMessage(player,stream);
		}catch (Exception ignored){
		}

	}

	public static void requestPlayerList(String server) {
		log("REQUEST PLAYERS FROM SERVER §a" + server);
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dataWriter = new DataOutputStream(stream);

			dataWriter.writeUTF("PlayerList");
			dataWriter.writeUTF(server);

			sendMessage(stream);
		}catch (Exception ignored){
		}

	}

	public static void requestCurrentServer() {
		log("REQUEST CURRENT SERVER");
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dataWriter = new DataOutputStream(stream);
			dataWriter.writeUTF("GetServer");

			sendMessage(stream);
		}catch (Exception ignored){
		}

	}

}
