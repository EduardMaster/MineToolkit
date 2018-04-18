package net.eduard.api.setup.bungee;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;

public final class ServerAPI {

	public enum ServerState {
		RESTARTING(3), IN_GAME(2), ONLINE(1), OFFLINE(0), DISABLED(-1);
		;
		private int value;

		public int getValue() {
			return value;
		}

		private ServerState(int value) {
			this.value = value;
			serverStates.put(name(), value);
		}

	}

	public enum ServerType {
		DEFAULT(0), LOBBY(1), SKYWARS_LOBBY(2), SKYWARS_ROOM(3);
		private int value;

		public int getValue() {
			return value;
		}

		private ServerType(int value) {
			this.value = value;
			serverStates.put(name(), value);
		}
	}

	private static ServerControl control;
	private static boolean registred;
	private static Map<String, Integer> serverStates = new HashMap<>();
	private static Map<String, Integer> serverTypes = new HashMap<>();
	private static Map<String, Server> servers = new HashMap<>();
	private static List<ServerPlayer> allPlayers = new ArrayList<>();
	private static List<ServerReceiveMessage> receivers = new ArrayList<>();
	private static boolean bungee = false;
	private static String channel = "ServerAPI";

	public static Map<String, Server> getServers() {
		return servers;
	}

	public static void setServers(Map<String, Server> servers) {
		ServerAPI.servers = servers;
	}

	public static Map<String, Integer> getServerStates() {
		return serverStates;
	}

	public static void setServerStates(Map<String, Integer> serverStates) {
		ServerAPI.serverStates = serverStates;
	}

	public static Map<String, Integer> getServerTypes() {
		return serverTypes;
	}

	public static void setServerTypes(Map<String, Integer> serverTypes) {
		ServerAPI.serverTypes = serverTypes;
	}

	public static ServerPlayer getServerPlayer(String name) {
		for (Server server : servers.values()) {
			for (ServerPlayer ServerPlayer : server.getPlayersNames()) {
				if (ServerPlayer.getName().equals(name)) {
					return ServerPlayer;
				}
			}
		}
		return new ServerPlayer(name);
	}

	public static Server getServer(String name) {
		return servers.getOrDefault(name, new Server(name));
	}

	public static Server getServer(String host, int port) {
		for (Entry<String, Server> entry : servers.entrySet()) {
			Server Server = entry.getValue();
			if (Server.getHost().equals(host) && Server.getPort() == port)
				return Server;
		}
		return new Server(host, port);
	}

	public static class ServerMessage {
		private String playerName;
		private String serverName;
		private String tag;
		private List<Object> messages = new ArrayList<>();

		public byte[] write() {
			return ServerAPI.write(playerName + ";;" + serverName + ";;" + tag, false, messages);
		}

		public ServerMessage() {
			// TODO Auto-generated constructor stub
		}

		public ServerMessage(ServerMessage message) {
			this.playerName = message.getPlayerName();
			this.serverName = message.getServerName();
			this.tag = message.getTag();
			messages.addAll(message.getMessages());
		}

		public static ServerMessage read(byte[] data) {
			List<Object> lines = ServerAPI.read(data, false);
			String inicial = (String) lines.get(0);
			String[] split = inicial.split(";;");
			lines.remove(inicial);
			ServerMessage serverMessage = new ServerMessage();
			serverMessage.setPlayerName(split[0]);
			serverMessage.setServerName(split[1]);
			serverMessage.setTag(split[2]);
			serverMessage.setMessages(lines);
			return serverMessage;
		}

		public ServerMessage(String playerName, String serverName, String tag, Object... messages) {
			this(playerName, serverName, tag, Arrays.asList(messages));
		}

		public ServerMessage(String playerName, String serverName, String tag, List<Object> messages) {
			super();
			this.playerName = playerName;
			this.serverName = serverName;
			this.tag = tag;
			this.messages = messages;
		}

		public List<String> getLines() {
			List<String> lista = new ArrayList<>();
			for (Object item : messages) {
				lista.add("" + item);
			}
			return lista;
		}

		public String getPlayerName() {
			return playerName;
		}

		public void setPlayerName(String playerName) {
			this.playerName = playerName;
		}

		public String getServerName() {
			return serverName;
		}

		public void setServerName(String serverName) {
			this.serverName = serverName;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public List<Object> getMessages() {
			return messages;
		}

		public void setMessages(List<Object> messages) {
			this.messages = messages;
		}

	}

	public static void register(ServerReceiveMessage receiver) {
		receivers.add(receiver);
	}

	public static void unregister(ServerReceiveMessage receiver) {
		receivers.remove(receiver);
	}

	public static boolean isRegistred(ServerReceiveMessage receiver) {
		return receivers.contains(receiver);
	}

	public static ServerControl getControl() {
		return control;
	}

	static {
		try {
			Class.forName("org.bukkit.Bukkit");
			control = new BukkitControl();
		} catch (Exception e) {
			bungee = true;
			control = new BungeeControl();
		}
	}

	public static interface ServerReceiveMessage {

		public void onReceiveMessage(ServerMessage message);
	}

	public static interface ServerControl {

		public void log(String message);

		public void sendToServer(ServerMessage message);

		public void sendToServers(ServerMessage message);

		public void sendToPlayer(ServerMessage message);

		public void sendToNetwork(ServerMessage message);

		public void sendToPlayers(ServerMessage message);

		public void sendToBungee(ServerMessage message);

		public void sendOnceTime(byte[] data);

		public void sendForEveryone(byte[] data);
	}

	public static boolean isBukkit() {
		return !isBungeeCord();
	}

	public static boolean isBungeeCord() {
		return bungee;
	}

	private ServerAPI() {

	}

	public static String getChannel() {
		return channel;
	}

	public static void setChannel(String channel) {
		ServerAPI.channel = channel;
	}

	public static List<Object> read(byte[] message, boolean oneLine) {
		List<Object> lista = new ArrayList<>();
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		if (oneLine) {
			String text = in.readUTF();
			if (text.contains(";")) {
				for (String line : text.split(";")) {
					lista.add(line);
				}
			} else {
				lista.add(text);
			}
		} else {
			String text = in.readUTF();
			lista.add(text);
			short size = in.readShort();
			for (int id = 1; id < size + 1; id++) {
				lista.add(in.readUTF());
			}
		}
		return lista;
	}

	public static byte[] write(String tag, boolean oneLine, Object... objects) {
		return write(tag, oneLine, Arrays.asList(objects));
	}

	public static byte[] write(String tag, boolean oneLine, List<Object> objects) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(stream);
		try {
			if (oneLine) {
				StringBuilder sb = new StringBuilder();
				for (int id = 0; id < objects.size(); id++) {
					if (id != 0) {
						sb.append(";");
					}
					sb.append(objects.get(id));
				}
				out.writeUTF(sb.toString());
			} else {
				out.writeUTF(tag);
				out.writeShort(objects.size());
				for (Object value : objects) {
					out.writeUTF("" + value);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream.toByteArray();
	}

	public static class BungeeControl implements ServerReceiveMessage, Listener, ServerControl, Runnable {

		public void run() {
			updateServers();
		}

		private static ScheduledTask task;

		public static ScheduledTask getTask() {
			return task;
		}

		public static void setTask(ScheduledTask task) {
			BungeeControl.task = task;
		}

		@EventHandler
		public void event(PluginMessageEvent e) {
			if (e.getTag().equalsIgnoreCase(getChannel())) {
				Connection receiver = e.getReceiver();
				Connection sender = e.getSender();
				String senderName = "", receiverName = "", serverName = "", playerName = "";
				if (receiver instanceof UserConnection) {
					UserConnection userConnection = (UserConnection) receiver;
					receiverName = userConnection.getName();
					playerName = receiverName;
				} else if (receiver instanceof ServerConnection) {
					ServerConnection serverConnection = (ServerConnection) receiver;
					receiverName = serverConnection.getInfo().getName();
				}
				if (sender instanceof UserConnection) {
					UserConnection userConnection = (UserConnection) sender;
					senderName = userConnection.getName();
					serverName = userConnection.getServer().getInfo().getName();
				} else if (sender instanceof ServerConnection) {
					ServerConnection serverConnection = (ServerConnection) sender;
					senderName = serverConnection.getInfo().getName();
					serverName = senderName;
				}
				ServerMessage message = ServerMessage.read(e.getData());
				String tag = message.getTag();
				List<String> lista = message.getLines();
				if (message.getPlayerName().equals("null")) {
					message.setPlayerName(playerName);
				}
				if (message.getServerName().equals("null")) {
					message.setServerName(serverName);
				}
				// receiverName+"|"+senderName + " " +
				log("§7" + receiverName + " << " + senderName + " " + tag + ":" + lista);
				for (ServerReceiveMessage messageReceiver : receivers) {
					messageReceiver.onReceiveMessage(message);
				}
			}
		}

		private static Plugin plugin;

		public static BungeeControl getControl() {
			return (BungeeControl) ServerAPI.getControl();
		}

		public static void register(Plugin plugin) {
			registred = true;
			BungeeControl.setPlugin(plugin);
			BungeeCord.getInstance().registerChannel(getChannel());
			// BungeeCord.getInstance().getPlayers().iterator().next().getServer().getInfo().sen
			BungeeCord.getInstance().getPluginManager().registerListener(plugin, getControl());
			ServerAPI.register(BungeeControl.getControl());

			for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
				Server servidor = ServerAPI.getServer(server.getName());
				servidor.setHost(server.getAddress().getHostName());
				servidor.setPort(server.getAddress().getPort());
				servers.put(server.getName(), servidor);
			}

			task = BungeeCord.getInstance().getScheduler().schedule(getPlugin(), getControl(), 1, 1, TimeUnit.SECONDS);
			BungeeCord.getInstance().getConsole()
					.sendMessage(new TextComponent("§b[ServerAPI] §aAtivando sistema de comunicacao com Bukkit!"));
		}

		public static void unregister() {
			if (registred) {
				BungeeCord.getInstance().getPluginManager().unregisterListener(getControl());
				BungeeCord.getInstance().unregisterChannel(getChannel());
				BungeeCord.getInstance().getScheduler().cancel(task);
				registred = false;

			}
		}

		public static void updateServers() {
			
			for (Server server : servers.values()) {
				ServerInfo servidor = getServer(server);
				if (!server.isDisabled()) {
					if (server.isOffline() || (servidor.getPlayers().size() == 0 && server.isRestarting())) {
						servidor.ping(new Callback<ServerPing>() {

							@Override
							public void done(ServerPing result, Throwable error) {
								if (result == null) {
									if (!server.isRestarting()) {
										server.setState(ServerState.OFFLINE);
									}

								} else {
									server.setMax(result.getPlayers().getMax());
									server.setCount(result.getPlayers().getOnline());
									if (server.isOffline()||server.isRestarting()) {
										server.setState(ServerState.ONLINE);
									}
								}

							}
						});
					}

				}

			}
			// ServerInfo a = BungeeCord.getInstance().getServerInfo("a");
		}

		public void onReceiveMessage(String serverName, String playerName, String tag, List<String> values) {

		}

		public static ServerInfo getServer(Server Server) {
			return BungeeCord.getInstance().getServerInfo(Server.getName());
		}

		public static Plugin getPlugin() {
			return plugin;
		}

		public static void setPlugin(Plugin plugin) {
			BungeeControl.plugin = plugin;
		}

		@Override
		public void log(String message) {
			BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerAPI] §f" + message));
		}

		@Override
		public void onReceiveMessage(ServerMessage message) {
			String tag = message.getTag();
			String playerName = message.getPlayerName();
			String serverName = message.getServerName();
			List<String> lista = message.getLines();
			 if (tag.equals("connect-to-server")) {
				ProxiedPlayer player = BungeeCord.getInstance().getPlayer(playerName);
				Integer type = Integer.valueOf(lista.get(0));
				for (Server server : servers.values()) {
					if (server.getType() == type) {
						if (server.canReallyConnect()) {
							player.connect(BungeeCord.getInstance().getServerInfo(server.getName()));
						}

					}
				}
			} else if (tag.equals("set-state")) {
				int state = Integer.parseInt(lista.get(0));
				Server server = ServerAPI.getServer(serverName);
				server.setState(state);
			} else if (tag.contains("::")) {
				String[] split = tag.split("::");
				String sub = split[0];
				tag = split[1];
				ServerMessage newMessage = new ServerMessage(message);
				newMessage.setTag(tag);
				if (sub.equals("sendToServer")) {
					sendToServer(newMessage);
				} else if (sub.equals("sendToPlayer")) {
					sendToPlayer(newMessage);
				} else if (sub.equals("sendToPlayers")) {
					sendToPlayers(newMessage);
				} else if (sub.equals("sendToServers")) {
					sendToServers(newMessage);
				} else if (sub.equals("sendToNetwork")) {
					sendToNetwork(newMessage);
				}
			}

		}

		@Override
		public void sendToServer(ServerMessage message) {
			ServerInfo server = BungeeCord.getInstance().getServerInfo(message.getServerName());
			if (server != null) {
				server.sendData(getChannel(), message.write());
			}
		}

		@Override
		public void sendToServers(ServerMessage message) {
			for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
				message.setServerName(server.getName());
				sendToServer(message);
			}

		}

		@Override
		public void sendToBungee(ServerMessage message) {
			sendToServers(message);
		}

		@Override
		public void sendToPlayer(ServerMessage message) {
			ProxiedPlayer player = BungeeCord.getInstance().getPlayer(message.getPlayerName());
			if (player != null) {
				player.getServer().sendData(getChannel(), message.write());
			} else {
				sendToServer(message);
			}

		}

		@Override
		public void sendToNetwork(ServerMessage message) {
			for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
				if (!server.getName().equals(message.getServerName())) {
					sendToServer(message);
				}
			}

		}

		@Override
		public void sendToPlayers(ServerMessage message) {
			for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
				message.setPlayerName(player.getName());
				message.setServerName(player.getServer().getInfo().getName());
				player.getServer().sendData(getChannel(), message.write());
			}

		}

		@Override
		public void sendOnceTime(byte[] data) {
			for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
				Collection<ProxiedPlayer> players = server.getPlayers();
				if (players.size() > 0) {
					ProxiedPlayer player = players.iterator().next();
					player.sendData(getChannel(), data);
				}

			}
		}

		@Override
		public void sendForEveryone(byte[] data) {
			for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
				server.sendData(getChannel(), data);
			}
		}

		/**
		 * Por algum motivo não funciona
		 * 
		 * @param player
		 * @param message
		 */
		@Deprecated
		public void sendData(ProxiedPlayer player, ServerMessage message) {
			player.sendData(getChannel(), message.write());
		}

	}

	public static class BukkitControl implements ServerControl, PluginMessageListener, ServerReceiveMessage {
		public static BukkitControl getControl() {
			return (BukkitControl) ServerAPI.getControl();
		}

		private static JavaPlugin plugin;

		public static JavaPlugin getInstance() {
			return plugin;
		}

		private BukkitControl() {
		}

		public static void register(JavaPlugin plugin) {
			BukkitControl.plugin = plugin;
			Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, getChannel());
			Bukkit.getMessenger().registerIncomingPluginChannel(plugin, getChannel(), getControl());
			registred = true;
			ServerAPI.register(BukkitControl.getControl());
			Bukkit.getConsoleSender().sendMessage("§b[ServerAPI] §aAtivando sistema de comunicacao com Bungeecord!");
		}

		public static void unregister() {
			if (registred) {
				Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin, getChannel());
				Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, getChannel());
				registred = false;
			}
		}

		public static void connectToServer(Player p, int type) {
			ServerMessage message = new ServerMessage(p.getName(), null, "connect-to-server", type);
			getControl().sendToBungee(message);
		}

		public static void setServerStatus(int status) {
			ServerMessage message = new ServerMessage(null, null, "set-state", status);
			getControl().sendToBungee(message);
		}

		@Override
		public void onReceiveMessage(ServerMessage message) {
		}

		@Override
		public void onPluginMessageReceived(String channel, Player player, byte[] data) {
			if (!channel.equals(getChannel())) {
				return;
			}
			ServerMessage message = ServerMessage.read(data);
			String playerName = message.getPlayerName();
			String serverName = message.getServerName();
			String tag = message.getTag();
			List<String> lista = message.getLines();
			if (message.getPlayerName().equals("null")) {
				message.setPlayerName(playerName);
			}
			if (message.getServerName().equals("null")) {
				message.setServerName(serverName);
			}
			log("§7" + playerName + " " + serverName + " " + tag + " : " + lista);
			for (ServerReceiveMessage messageReceiver : receivers) {
				messageReceiver.onReceiveMessage(message);
			}
		}

		@Override
		public void log(String message) {
			Bukkit.getConsoleSender().sendMessage("§a[ServerAPI] §f" + message);
		}

		@Override
		public void sendToServer(ServerMessage message) {
			ServerMessage newMessage = new ServerMessage(message);
			newMessage.setTag("sendToServer::" + newMessage.getTag());
			sendOnceTime(newMessage.write());
			// Bukkit.getServer().sendPluginMessage(getInstance(), getChannel(),
			// newMessage.write());

		}

		@Override
		public void sendToServers(ServerMessage message) {
			ServerMessage newMessage = new ServerMessage(message);
			newMessage.setTag("sendToServers::" + newMessage.getTag());
			sendOnceTime(newMessage.write());
			// Bukkit.getServer().sendPluginMessage(getInstance(), getChannel(),
			// newMessage.write());

		}

		@Override
		public void sendToPlayer(ServerMessage message) {
			ServerMessage newMessage = new ServerMessage(message);
			newMessage.setTag("sendToPlayer::" + newMessage.getTag());
			sendOnceTime(newMessage.write());
			// Player player = Bukkit.getPlayer(message.getPlayerName());
			// if (player != null) {
			// player.sendPluginMessage(getInstance(), getChannel(), newMessage.write());
			// } else {
			// Bukkit.getServer().sendPluginMessage(getInstance(), getChannel(),
			// newMessage.write());
			// }
		}

		@Override
		public void sendToNetwork(ServerMessage message) {
			ServerMessage newMessage = new ServerMessage(message);
			newMessage.setTag("sendToNetwork::" + newMessage.getTag());
			sendOnceTime(newMessage.write());
			// Bukkit.getServer().sendPluginMessage(getInstance(), getChannel(),
			// newMessage.write());

		}

		@Override
		public void sendToPlayers(ServerMessage message) {
			ServerMessage newMessage = new ServerMessage(message);
			newMessage.setTag("sendToPlayers::" + newMessage.getTag());
			sendOnceTime(newMessage.write());
			// sendForEveryone(message.write());
			// Bukkit.getServer().sendPluginMessage(getInstance(), getChannel(),
			// message.write());
		}

		@Override
		public void sendOnceTime(byte[] data) {
			Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
			if (player != null) {
				player.sendPluginMessage(getInstance(), getChannel(), data);
			}
		}

		@Override
		public void sendForEveryone(byte[] data) {
			Bukkit.getServer().sendPluginMessage(getInstance(), getChannel(), data);
		}

		@Override
		public void sendToBungee(ServerMessage message) {
			sendOnceTime(message.write());
		}

	}

	public static class ServerPlayer {
		private String name;
		private UUID uuid;
		private Server server;
		private int port;
		private String host;

		public ServerPlayer(String name) {
			this.name = name;
		}

		public ServerPlayer() {
		}

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

		public Server getServer() {
			return server;
		}

		public void setServer(Server server) {
			this.server = server;
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

	}

	public static List<ServerPlayer> getAllPlayers() {
		return allPlayers;
	}

	public static void setAllPlayers(List<ServerPlayer> allPlayers) {
		ServerAPI.allPlayers = allPlayers;
	}

	public static class Server {

		public Server(String name) {
			this.name = name;
		}

		public void setState(ServerState state) {
			this.state = state.getValue();

		}

		public Server(String host, int port) {
			this.host = host;
			this.port = port;
		}

		private int max;
		private String name;
		private int type;
		private int count;
		private int state;

		public boolean isState(int state) {
			return this.state == state;

		}

		public boolean isType(int type) {
			return this.type == type;

		}

		public boolean isState(ServerState state) {
			return this.state == state.getValue();
		}

		public boolean isType(ServerType type) {
			return this.type == type.getValue();
		}

		private String host;
		private int port;
		private List<String> players = new ArrayList<>();

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public List<ServerPlayer> getPlayersNames() {
			List<ServerPlayer> lista = new ArrayList<>();
			for (ServerPlayer player : allPlayers) {
				if (player.getServer() == this) {
					lista.add(player);
				}
			}
			return lista;
		}
		// public getState() {
		//
		// }

		public boolean canConnect() {
			return isReallyOnline() && !isFull();
		}

		public boolean canReallyConnect() {
			return canConnect() && !isRestarting() && !isGameStarted();
		}

		public boolean isRestarting() {
			return isState(ServerState.RESTARTING);
		}

		public boolean isPlaying() {
			return isState(ServerState.IN_GAME);
		}

		public boolean isGameStarted() {
			return isPlaying();
		}

		public boolean isReallyOnline() {
			return !isOffline() && !isDisabled();
		}

		public boolean isOffline() {
			return isState(ServerState.OFFLINE);
		}

		public boolean isDisabled() {
			return isState(ServerState.DISABLED);
		}

		public boolean isOnline() {
			return isState(ServerState.ONLINE);
		}

		public boolean isFull() {
			return players.size() == max;
		}

		public boolean isEmpty() {
			return players.isEmpty();
		}

		public int getMax() {
			return max;
		}

		public void setMax(int max) {
			this.max = max;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
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

}
