package net.eduard.api.lib.modules;

import java.io.ByteArrayOutputStream;

import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Iterables;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

/**
 * Sistema de controle dos servidores pelo bungee
 *
 * @author Eduard
 * @version 1.6
 */
public final class ServerAPI {
    private static ServerControl control;
    private static boolean registred;
    private static String channel = "bungeecord:serverapi";

    public enum ServerState {
        RESTARTING(3), IN_GAME(2), ONLINE(1), OFFLINE(0), DISABLED(-1);

        private int value;

        public int getValue() {
            return this.value;
        }

        ServerState(int value) {
            this.value = value;

        }
    }

    public enum ServerType {
        DEFAULT(0), LOBBY(1), SKYWARS_LOBBY(2), SKYWARS_ROOM(3);

        private int value;

        public int getValue() {
            return this.value;
        }

        ServerType(int value) {
            this.value = value;

        }
    }


    private static Map<String, Server> servers = new HashMap<>();
    private static List<ServerPlayer> allPlayers = new ArrayList<>();
    private static List<ServerReceiveMessage> receivers = new ArrayList<>();
    private static boolean bungee = false;

    // Na versão 1.16 do minecraft tem que colocar tudo minusculo


    public static ServerPlayer getServerPlayer(String name) {


        for (Server server : servers.values()) {
            for (ServerPlayer player : server.getPlayersNames()) {
                if (player.getName().equals(name)) {
                    return player;
                }
            }
        }

        return new ServerPlayer(name);
    }

    public static Server getServer(String name) {
        return servers.getOrDefault(name, new Server(name));
    }

    public static Server getServer(String host, int port) {
        for (Map.Entry<String, Server> entry : servers.entrySet()) {
            Server Server = entry.getValue();
            if ((Server.getHost().equals(host)) && (Server.getPort() == port)) {
                return Server;
            }
        }
        return new Server(host, port);
    }

    public static class ServerMessage {
        private String playerName;
        private String serverName;
        private String tag;
        private String subTag = "";
        private List<String> messages = new ArrayList<>();

        public byte[] serialize() {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(stream);
                out.writeUTF(playerName);
                out.writeUTF(serverName);
                out.writeUTF(tag);
                out.writeUTF(subTag);
                out.writeShort(messages.size());
                for (Object value : messages) {
                    out.writeUTF(value.toString());
                }


                return stream.toByteArray();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;

        }

        public ServerMessage() {
        }


        public static ServerMessage desirialize(byte[] data) {
            try {
                ServerMessage serverMessage = new ServerMessage();
                List<String> lista = new ArrayList<>();
                DataInput in = ByteStreams.newDataInput(data);



                serverMessage.setPlayerName(in.readUTF());
                serverMessage.setServerName(in.readUTF());
                serverMessage.setTag(in.readUTF());
                serverMessage.setSubTag(in.readUTF());

                short size = in.readShort();
                for (int id = 0; id < size; id++) {
                    lista.add(in.readUTF());
                }
                serverMessage.setMessages(lista);


                return serverMessage;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        public ServerMessage(String playerName, String serverName, String tag, Object... messages) {
            this(playerName, serverName, tag, Arrays.asList(messages));
        }

        public ServerMessage(String playerName, String serverName, String tag, List<Object> messages) {
            if (playerName == null)
                playerName = "";
            if (serverName == null) serverName = "";
            if (tag == null) tag = null;
            this.playerName = playerName;

            this.serverName = serverName;
            this.tag = tag;
            for (Object object : messages) {
                this.messages.add(object.toString());
            }
        }

        public List<String> getLines() {

            return messages;
        }

        public String getPlayerName() {
            return this.playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public String getServerName() {
            return this.serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public String getTag() {
            return this.tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public List<String> getMessages() {
            return this.messages;
        }

        public void setMessages(List<String> messages) {
            this.messages = messages;
        }

        public String getSubTag() {
            return subTag;
        }

        public void setSubTag(String subTag) {
            this.subTag = subTag;
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

    public static boolean isBukkit() {
        return !isBungeeCord();
    }

    public static boolean isBungeeCord() {
        return bungee;
    }

    public static String getChannel() {
        return channel;
    }

    public static void setChannel(String channel) {
        ServerAPI.channel = channel;
    }


    public static class BungeeControl
            implements ServerAPI.ServerReceiveMessage, Listener, ServerAPI.ServerControl, Runnable {
        private static ScheduledTask task;
        private static Plugin plugin;

        public BungeeControl() {
        }

        public void run() {
        }

        public static ScheduledTask getTask() {
            return task;
        }

        public static void setTask(ScheduledTask task) {
            BungeeControl.task = task;
        }

        @EventHandler
        public void event(PluginMessageEvent e) {
            if (e.getTag().equalsIgnoreCase(ServerAPI.getChannel())) {
                //Connection receiver = e.getReceiver();
                //Connection sender = e.getSender();

                ServerAPI.ServerMessage message = ServerAPI.ServerMessage.desirialize(e.getData());
                log("§7TAG: " + message.getTag() + " SERVER: " + message.getServerName() +
                        " DATA-AMOUNT " + message.getMessages().size());
                for (ServerAPI.ServerReceiveMessage messageReceiver : ServerAPI.receivers) {
                    messageReceiver.onReceiveMessage(message);
                }
            }
        }

        public static BungeeControl getControl() {

            return (BungeeControl) ServerAPI.getControl();
        }

        public static void register(Plugin plugin) {
            ServerAPI.registred = true;
            setPlugin(plugin);
            BungeeCord.getInstance().registerChannel(ServerAPI.getChannel());

            BungeeCord.getInstance().getPluginManager().registerListener(plugin, getControl());
            ServerAPI.register(getControl());
            for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
                ServerAPI.Server servidor = ServerAPI.getServer(server.getName());
                servidor.setHost(server.getAddress().getHostName());
                servidor.setPort(server.getAddress().getPort());
                ServerAPI.servers.put(server.getName(), servidor);
            }
            task = BungeeCord.getInstance().getScheduler().schedule(getPlugin(), getControl(), 1L, 1L, TimeUnit.SECONDS);
            BungeeCord.getInstance().getConsole().sendMessage(
                    new TextComponent("§b[ServerAPI] §aAtivando sistema de comunicacao com Bukkit!"));
        }

        public static void unregister() {
            if (ServerAPI.registred) {
                BungeeCord.getInstance().getPluginManager().unregisterListener(getControl());
                BungeeCord.getInstance().unregisterChannel(ServerAPI.getChannel());
                BungeeCord.getInstance().getScheduler().cancel(task);
                ServerAPI.registred = false;
            }
        }

        public static void updateServers() {
            for (ServerAPI.Server server : ServerAPI.servers.values()) {
                ServerInfo servidor = getServer(server);
                if ((!server.isDisabled()) && (
                        (server.isOffline()) || ((servidor.getPlayers().size() == 0) && (server.isRestarting())))) {
                    servidor.ping(new Callback<ServerPing>() {
                        public void done(ServerPing result, Throwable error) {
                            if (result == null) {
                                if (!server.isRestarting()) {
                                    server.setState(ServerAPI.ServerState.OFFLINE);
                                }
                            } else {
                                server.setMax(result.getPlayers().getMax());
                                server.setCount(result.getPlayers().getOnline());
                                if ((server.isOffline()) || (server.isRestarting())) {
                                    server.setState(ServerAPI.ServerState.ONLINE);
                                }
                            }
                        }


                    });
                }
            }
        }


        public static ServerInfo getServer(ServerAPI.Server Server) {
            return BungeeCord.getInstance().getServerInfo(Server.getName());
        }

        public static Plugin getPlugin() {
            return plugin;
        }

        public static void setPlugin(Plugin plugin) {
            BungeeControl.plugin = plugin;
        }

        public void log(String message) {
            BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerAPI] §f" + message));
        }

        public void onReceiveMessage(ServerAPI.ServerMessage message) {
            if (message.getTag().equalsIgnoreCase("send-to-bungee")) {
                sendToBungee(message);
            }
            if (message.getTag().equalsIgnoreCase("send-to-server")) {
                sendToServer(message);
            }
            if (message.getTag().equalsIgnoreCase("send-to-servers")) {
                sendToServers(message);
            }

        }

        public void sendToServer(ServerAPI.ServerMessage message) {
            ServerInfo server = BungeeCord.getInstance().getServerInfo(message.getServerName());
            if (server != null) {
                server.sendData(ServerAPI.getChannel(), message.serialize());
            }
        }

        public void sendToServers(ServerAPI.ServerMessage message) {
            for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
                message.setServerName(server.getName());
                sendToServer(message);
            }
        }

        public void sendToBungee(ServerAPI.ServerMessage message) {
            if (message.getSubTag().equalsIgnoreCase("connect-to-server")) {
                int serverType = Integer.parseInt(message.getMessages().get(0));
                int serverState = Integer.parseInt(message.getMessages().get(1));
                connectToServer(message.getPlayerName(), serverType, serverType);
            }
            if (message.getSubTag().equalsIgnoreCase("set-state")) {
                int serverType = Integer.parseInt(message.getMessages().get(0));
                setServerStatus(message.getServerName(), serverType);
            }
        }


        public void sendMessage(byte[] data) {
            for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
                Collection<ProxiedPlayer> players = server.getPlayers();
                if (players.size() > 0) {
                    ProxiedPlayer player = players.iterator().next();
                    player.sendData(ServerAPI.getChannel(), data);
                }
            }
        }

        @Override
        public void connectToServer(String playerName, int serverType, int serverState) {
            ProxiedPlayer player = BungeeCord.getInstance().getPlayer(playerName);

            for (ServerAPI.Server server : ServerAPI.servers.values()) {
                if ((server.getType() == serverType) && serverState == server.getState()
                ) {
                    player.connect(BungeeCord.getInstance().getServerInfo(server.getName()));
                }
            }
        }

        @Override
        public void setServerStatus(String serverName, int state) {

            ServerAPI.Server server = ServerAPI.getServer(serverName);
            server.setState(state);
        }

    }

    public static class BukkitControl
            implements ServerAPI.ServerControl, PluginMessageListener, ServerAPI.ServerReceiveMessage {
        private static JavaPlugin plugin;

        public static BukkitControl getControl() {
            return (BukkitControl) ServerAPI.getControl();
        }

        public static JavaPlugin getInstance() {
            return plugin;
        }

        private BukkitControl() {
        }

        public static void register(JavaPlugin plugin) {
            BukkitControl.plugin = plugin;
            Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, ServerAPI.getChannel());
            Bukkit.getMessenger().registerIncomingPluginChannel(plugin, ServerAPI.getChannel(), getControl());
            ServerAPI.registred = true;
            ServerAPI.register(getControl());
            Bukkit.getConsoleSender().sendMessage("§b[ServerAPI] §aAtivando sistema de comunicacao com Bungeecord!");
        }

        public static void unregister() {
            if (ServerAPI.registred) {
                Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin, ServerAPI.getChannel());
                Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, ServerAPI.getChannel());
                ServerAPI.registred = false;
            }
        }

        public void connectToServer(String playerName, int serverType, int serverState) {
            ServerAPI.ServerMessage message = new ServerAPI.ServerMessage(playerName, null, "connect-to-server", serverType, serverState);
            getControl().sendToBungee(message);
        }

        public void setServerStatus(int status) {
            setServerStatus(getServerName(), status);
        }

        public void setServerStatus(String serverName, int status) {
            ServerAPI.ServerMessage message = new ServerAPI.ServerMessage(serverName, null, "set-state", status);
            getControl().sendToBungee(message);
        }

        public void onReceiveMessage(ServerAPI.ServerMessage message) {
        }

        public void onPluginMessageReceived(String channel, Player player, byte[] data) {
            if (!channel.equals(ServerAPI.getChannel())) {
                return;
            }
            ServerAPI.ServerMessage message = ServerAPI.ServerMessage.desirialize(data);


            log("§7TAG: " + message.getTag() + " SUB-TAG: " + message.getSubTag() +
                    " PLAYER-NAME: " + message.getPlayerName()
                    + " SERVER-NAME: " + message.getServerName());
            for (ServerAPI.ServerReceiveMessage messageReceiver : ServerAPI.receivers) {
                messageReceiver.onReceiveMessage(message);
            }
        }

        public void log(String message) {
            Bukkit.getConsoleSender().sendMessage("§a[ServerAPI] §f" + message);
        }

        public void sendToServer(ServerAPI.ServerMessage message) {
            message.setSubTag(message.getTag());
            message.setTag("send-to-server");
            sendMessage(message.serialize());
        }

        public void sendToServers(ServerAPI.ServerMessage message) {
            message.setSubTag(message.getTag());
            message.setTag("send-to-servers");
            sendMessage(message.serialize());
        }

        private String getServerName() {
            return Bukkit.getServer().getName();
        }

        public void sendToBungee(ServerAPI.ServerMessage message) {
            message.setSubTag(message.getTag());
            message.setServerName(getServerName());
            message.setTag("send-to-bungee");
            sendMessage(message.serialize());
        }


        public void sendMessage(byte[] data) {
            Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player != null) {
                player.sendPluginMessage(getInstance(), ServerAPI.getChannel(), data);
            } else {
                Bukkit.getServer().sendPluginMessage(getInstance(), ServerAPI.getChannel(), data);
            }
        }

    }

    public static class ServerPlayer {
        private String name;
        private UUID uuid;
        private ServerAPI.Server server;
        private int port;
        private String host;

        public ServerPlayer(String name) {
            this.name = name;
        }

        public ServerPlayer() {
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public UUID getUuid() {
            return this.uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public ServerAPI.Server getServer() {
            return this.server;
        }

        public void setServer(ServerAPI.Server server) {
            this.server = server;
        }

        public int getPort() {
            return this.port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getHost() {
            return this.host;
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

    private ServerAPI() {
    }

    public static class Server {
        private int max;
        private String name;
        private int type;
        private int count;
        private int state;
        private String host;
        private int port;

        public Server(String name) {
            this.name = name;
        }

        public void setState(ServerAPI.ServerState state) {
            this.state = state.getValue();
        }

        public Server(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public boolean isState(int state) {
            return this.state == state;
        }

        public boolean isType(int type) {
            return this.type == type;
        }

        public boolean isState(ServerAPI.ServerState state) {
            return this.state == state.getValue();
        }

        public boolean isType(ServerAPI.ServerType type) {
            return this.type == type.getValue();
        }

        private List<String> players = new ArrayList<>();

        public int getCount() {
            return this.count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ServerAPI.ServerPlayer> getPlayersNames() {
            List<ServerAPI.ServerPlayer> lista = new ArrayList<ServerPlayer>();
            for (ServerAPI.ServerPlayer player : ServerAPI.allPlayers) {
                if (player.getServer() == this) {
                    lista.add(player);
                }
            }
            return lista;
        }

        public boolean canConnect() {
            return (isReallyOnline()) && (!isFull());
        }

        public boolean canReallyConnect() {
            return (canConnect()) && (!isRestarting()) && (!isGameStarted());
        }

        public boolean isRestarting() {
            return isState(ServerAPI.ServerState.RESTARTING);
        }

        public boolean isPlaying() {
            return isState(ServerAPI.ServerState.IN_GAME);
        }

        public boolean isGameStarted() {
            return isPlaying();
        }

        public boolean isReallyOnline() {
            return (!isOffline()) && (!isDisabled());
        }

        public boolean isOffline() {
            return isState(ServerAPI.ServerState.OFFLINE);
        }

        public boolean isDisabled() {
            return isState(ServerAPI.ServerState.DISABLED);
        }

        public boolean isOnline() {
            return isState(ServerAPI.ServerState.ONLINE);
        }

        public boolean isFull() {
            return this.players.size() == this.max;
        }

        public boolean isEmpty() {
            return this.players.isEmpty();
        }

        public int getMax() {
            return this.max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return this.type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getState() {
            return this.state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getHost() {
            return this.host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return this.port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public interface ServerControl {
        void log(String message);

        void sendToServer(ServerMessage message);

        void sendToServers(ServerMessage message);

        void sendToBungee(ServerMessage message);

        void sendMessage(byte[] data);

        void connectToServer(String playerName, int serverType, int serverState);

        void setServerStatus(String serverName, int state);

    }

    public interface ServerReceiveMessage {
        void onReceiveMessage(ServerMessage message);
    }
}
