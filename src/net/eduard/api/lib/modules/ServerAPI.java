package net.eduard.api.lib.modules;

import java.io.ByteArrayOutputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.bukkit.event.EventHandler;

/**
 * Sistema de controle dos servidores pelo bungee
 * @author Eduard
 * @version 1.5
 *
 */
public final class ServerAPI
{
  private static ServerControl control;
  private static boolean registred;
  
  public static enum ServerState
  {
    RESTARTING(3),  IN_GAME(2),  ONLINE(1),  OFFLINE(0),  DISABLED(-1);
    
    private int value;
    
    public int getValue()
    {
      return this.value;
    }
    
    private ServerState(int value)
    {
      this.value = value;
      ServerAPI.serverStates.put(name(), Integer.valueOf(value));
    }
  }
  
  public static enum ServerType
  {
    DEFAULT(0),  LOBBY(1),  SKYWARS_LOBBY(2),  SKYWARS_ROOM(3);
    
    private int value;
    
    public int getValue()
    {
      return this.value;
    }
    
    private ServerType(int value)
    {
      this.value = value;
      ServerAPI.serverStates.put(name(), Integer.valueOf(value));
    }
  }
  
  private static Map<String, Integer> serverStates = new HashMap<>();
  private static Map<String, Integer> serverTypes = new HashMap<>();
  private static Map<String, Server> servers = new HashMap<>();
  private static List<ServerPlayer> allPlayers = new ArrayList<>();
  private static List<ServerReceiveMessage> receivers = new ArrayList<>();
  private static boolean bungee = false;
  private static String channel = "ServerAPI";
  
  public static Map<String, Server> getServers()
  {
    return servers;
  }
  
  public static void setServers(Map<String, Server> servers)
  {
    ServerAPI.servers = servers;
  }
  
  public static Map<String, Integer> getServerStates()
  {
    return serverStates;
  }
  
  public static void setServerStates(Map<String, Integer> serverStates)
  {
	  ServerAPI.serverStates = serverStates;
  }
  
  public static Map<String, Integer> getServerTypes()
  {
    return serverTypes;
  }
  
  public static void setServerTypes(Map<String, Integer> serverTypes)
  {
	  ServerAPI.serverTypes = serverTypes;
  }
  
  public static ServerPlayer getServerPlayer(String name)
  {
	  
	  
	  for (Server server : servers.values()) {
		  for (ServerPlayer player : server.getPlayersNames()) {
			  if (player.getName().equals(name))
			  {
				  return player;
			  }
		  }
	  }
	  
    return new ServerPlayer(name);
  }
  
  public static Server getServer(String name)
  {
    return (Server)servers.getOrDefault(name, new Server(name));
  }
  
  public static Server getServer(String host, int port)
  {
    for (Map.Entry<String, Server> entry : servers.entrySet())
    {
      Server Server = (Server)entry.getValue();
      if ((Server.getHost().equals(host)) && (Server.getPort() == port)) {
        return Server;
      }
    }
    return new Server(host, port);
  }
  
  public static class ServerMessage
  {
    private String playerName;
    private String serverName;
    private String tag;
    private List<Object> messages = new ArrayList<>();
    
    public byte[] write()
    {
      return ServerAPI.write(this.playerName + ";;" + this.serverName + ";;" + this.tag, false, this.messages);
    }
    
    public ServerMessage() {}
    
    public ServerMessage(ServerMessage message)
    {
      this.playerName = message.getPlayerName();
      this.serverName = message.getServerName();
      this.tag = message.getTag();
      this.messages.addAll(message.getMessages());
    }
    
    public static ServerMessage read(byte[] data)
    {
      List<Object> lines = ServerAPI.read(data, false);
      String inicial = (String)lines.get(0);
      String[] split = inicial.split(";;");
      lines.remove(inicial);
      ServerMessage serverMessage = new ServerMessage();
      serverMessage.setPlayerName(split[0]);
      serverMessage.setServerName(split[1]);
      serverMessage.setTag(split[2]);
      serverMessage.setMessages(lines);
      return serverMessage;
    }
    
    public ServerMessage(String playerName, String serverName, String tag, Object... messages)
    {
      this(playerName, serverName, tag, Arrays.asList(messages));
    }
    
    public ServerMessage(String playerName, String serverName, String tag, List<Object> messages)
    {
      this.playerName = playerName;
      this.serverName = serverName;
      this.tag = tag;
      this.messages = messages;
    }
    
    public List<String> getLines()
    {
      List<String> lista = new ArrayList<>();
      for (Object item : this.messages) {
        lista.add(item.toString());
      }
      return lista;
    }
    
    public String getPlayerName()
    {
      return this.playerName;
    }
    
    public void setPlayerName(String playerName)
    {
      this.playerName = playerName;
    }
    
    public String getServerName()
    {
      return this.serverName;
    }
    
    public void setServerName(String serverName)
    {
      this.serverName = serverName;
    }
    
    public String getTag()
    {
      return this.tag;
    }
    
    public void setTag(String tag)
    {
      this.tag = tag;
    }
    
    public List<Object> getMessages()
    {
      return this.messages;
    }
    
    public void setMessages(List<Object> messages)
    {
      this.messages = messages;
    }
  }
  
  public static void register(ServerReceiveMessage receiver)
  {
    receivers.add(receiver);
  }
  
  public static void unregister(ServerReceiveMessage receiver)
  {
    receivers.remove(receiver);
  }
  
  public static boolean isRegistred(ServerReceiveMessage receiver)
  {
    return receivers.contains(receiver);
  }
  
  public static ServerControl getControl()
  {
    return control;
  }
  
  static
  {
    try
    {
      Class.forName("org.bukkit.Bukkit");
      control = new BukkitControl();
    }
    catch (Exception e)
    {
      bungee = true;
      control = new BungeeControl();
    }
  }
  
  public static boolean isBukkit()
  {
    return !isBungeeCord();
  }
  
  public static boolean isBungeeCord()
  {
    return bungee;
  }
  
  public static String getChannel()
  {
    return channel;
  }
  
  public static void setChannel(String channel)
  {
	  ServerAPI. channel = channel;
  }
  
  public static List<Object> read(byte[] message, boolean oneLine)
  {
    List<Object> lista = new ArrayList<>();
    ByteArrayDataInput in = ByteStreams.newDataInput(message);
    if (oneLine)
    {
      String text = in.readUTF();
      if (text.contains(";"))
      {
        String[] arrayOfString;
        int j = (arrayOfString = text.split(";")).length;
        for (int i = 0; i < j; i++)
        {
          String line = arrayOfString[i];
          lista.add(line);
        }
      }
      else
      {
        lista.add(text);
      }
    }
    else
    {
      String text = in.readUTF();
      lista.add(text);
      short size = in.readShort();
      for (int id = 1; id < size + 1; id++) {
        lista.add(in.readUTF());
      }
    }
    return lista;
  }
  
  public static byte[] write(String tag, boolean oneLine, Object... objects)
  {
    return write(tag, oneLine, Arrays.asList(objects));
  }
  
  public static byte[] write(String tag, boolean oneLine, List<Object> objects)
  {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(stream);
    try
    {
      int id;
      if (oneLine)
      {
        StringBuilder sb = new StringBuilder();
        for (id = 0; id < objects.size(); id++)
        {
          if (id != 0) {
            sb.append(";");
          }
          sb.append(objects.get(id));
        }
        out.writeUTF(sb.toString());
      }
      else
      {
        out.writeUTF(tag);
        out.writeShort(objects.size());
        for (Object value : objects) {
          out.writeUTF(value.toString());
        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return stream.toByteArray();
  }
  
  public static class BungeeControl
    implements ServerAPI.ServerReceiveMessage, Listener, ServerAPI.ServerControl, Runnable
  {
    private static ScheduledTask task;
    private static Plugin plugin;
    
    public BungeeControl() {}
    
    public void run() {}
    
    public static ScheduledTask getTask()
    {
      return task;
    }
    
    public static void setTask(ScheduledTask task)
    {
BungeeControl. task = task;
    }
    
    @EventHandler
    public void event(PluginMessageEvent e)
    {
      if (e.getTag().equalsIgnoreCase(ServerAPI.getChannel()))
      {
        Connection receiver = e.getReceiver();
        Connection sender = e.getSender();
        String senderName = "";String receiverName = "";String serverName = "";String playerName = "";
        if ((receiver instanceof UserConnection))
        {
          UserConnection userConnection = (UserConnection)receiver;
          receiverName = userConnection.getName();
          playerName = receiverName;
        }
        else if ((receiver instanceof ServerConnection))
        {
          ServerConnection serverConnection = (ServerConnection)receiver;
          receiverName = serverConnection.getInfo().getName();
        }
        if ((sender instanceof UserConnection))
        {
          UserConnection userConnection = (UserConnection)sender;
          senderName = userConnection.getName();
          serverName = userConnection.getServer().getInfo().getName();
        }
        else if ((sender instanceof ServerConnection))
        {
          ServerConnection serverConnection = (ServerConnection)sender;
          senderName = serverConnection.getInfo().getName();
          serverName = senderName;
        }
        ServerAPI.ServerMessage message = ServerAPI.ServerMessage.read(e.getData());
        String tag = message.getTag();
        List<String> lista = message.getLines();
        if (message.getPlayerName().equals("null")) {
          message.setPlayerName(playerName);
        }
        if (message.getServerName().equals("null")) {
          message.setServerName(serverName);
        }
        log("§7" + receiverName + " << " + senderName + " " + tag + ":" + lista);
        for (ServerAPI.ServerReceiveMessage messageReceiver : ServerAPI.receivers) {
          messageReceiver.onReceiveMessage(message);
        }
      }
    }
    
    public static BungeeControl getControl()
    {
      return (BungeeControl)ServerAPI.getControl();
    }
    
    public static void register(Plugin plugin)
    {
      ServerAPI.registred = true;
      setPlugin(plugin);
      BungeeCord.getInstance().registerChannel(ServerAPI.getChannel());
      
      BungeeCord.getInstance().getPluginManager().registerListener(plugin, getControl());
      ServerAPI.register(getControl());
      for (ServerInfo server : BungeeCord.getInstance().getServers().values())
      {
        ServerAPI.Server servidor = ServerAPI.getServer(server.getName());
        servidor.setHost(server.getAddress().getHostName());
        servidor.setPort(server.getAddress().getPort());
        ServerAPI.servers.put(server.getName(), servidor);
      }
      task = BungeeCord.getInstance().getScheduler().schedule(getPlugin(), getControl(), 1L, 1L, TimeUnit.SECONDS);
      BungeeCord.getInstance().getConsole()
        .sendMessage(new TextComponent("§b[ServerAPI] §aAtivando sistema de comunicacao com Bukkit!"));
    }
    
    public static void unregister()
    {
      if (ServerAPI.registred)
      {
        BungeeCord.getInstance().getPluginManager().unregisterListener(getControl());
        BungeeCord.getInstance().unregisterChannel(ServerAPI.getChannel());
        BungeeCord.getInstance().getScheduler().cancel(task);
        ServerAPI.registred = false;
      }
    }
    
    public static void updateServers()
    {
      for (ServerAPI.Server server : ServerAPI.servers.values())
      {
        ServerInfo servidor = getServer(server);
        if ((!server.isDisabled()) && (
          (server.isOffline()) || ((servidor.getPlayers().size() == 0) && (server.isRestarting())))) {
          servidor.ping(new Callback<ServerPing>()
          {
            public void done(ServerPing result, Throwable error)
            {
              if (result == null)
              {
                if (!server.isRestarting()) {
                	server.setState(ServerAPI.ServerState.OFFLINE);
                }
              }
              else
              {
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
    
    public void onReceiveMessage(String serverName, String playerName, String tag, List<String> values) {}
    
    public static ServerInfo getServer(ServerAPI.Server Server)
    {
      return BungeeCord.getInstance().getServerInfo(Server.getName());
    }
    
    public static Plugin getPlugin()
    {
      return plugin;
    }
    
    public static void setPlugin(Plugin plugin)
    {
      BungeeControl.plugin = plugin;
    }
    
    public void log(String message)
    {
      BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerAPI] §f" + message));
    }
    
    public void onReceiveMessage(ServerAPI.ServerMessage message)
    {
      String tag = message.getTag();
      String playerName = message.getPlayerName();
      String serverName = message.getServerName();
      List<String> lista = message.getLines();
      if (tag.equals("connect-to-server"))
      {
        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(playerName);
        Integer type = Integer.valueOf((String)lista.get(0));
        for (ServerAPI.Server server : ServerAPI.servers.values()) {
          if ((server.getType() == type.intValue()) && 
            (server.canReallyConnect())) {
            player.connect(BungeeCord.getInstance().getServerInfo(server.getName()));
          }
        }
      }
      else if (tag.equals("set-state"))
      {
        int state = Integer.parseInt((String)lista.get(0));
        ServerAPI.Server server = ServerAPI.getServer(serverName);
        server.setState(state);
      }
      else if (tag.contains("::"))
      {
        String[] split = tag.split("::");
        String sub = split[0];
        tag = split[1];
        ServerAPI.ServerMessage newMessage = new ServerAPI.ServerMessage(message);
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
    
    public void sendToServer(ServerAPI.ServerMessage message)
    {
      ServerInfo server = BungeeCord.getInstance().getServerInfo(message.getServerName());
      if (server != null) {
        server.sendData(ServerAPI.getChannel(), message.write());
      }
    }
    
    public void sendToServers(ServerAPI.ServerMessage message)
    {
      for (ServerInfo server : BungeeCord.getInstance().getServers().values())
      {
        message.setServerName(server.getName());
        sendToServer(message);
      }
    }
    
    public void sendToBungee(ServerAPI.ServerMessage message)
    {
      sendToServers(message);
    }
    
    public void sendToPlayer(ServerAPI.ServerMessage message)
    {
      ProxiedPlayer player = BungeeCord.getInstance().getPlayer(message.getPlayerName());
      if (player != null) {
        player.getServer().sendData(ServerAPI.getChannel(), message.write());
      } else {
        sendToServer(message);
      }
    }
    
    public void sendToNetwork(ServerAPI.ServerMessage message)
    {
      for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
        if (!server.getName().equals(message.getServerName())) {
          sendToServer(message);
        }
      }
    }
    
    public void sendToPlayers(ServerAPI.ServerMessage message)
    {
      for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers())
      {
        message.setPlayerName(player.getName());
        message.setServerName(player.getServer().getInfo().getName());
        player.getServer().sendData(ServerAPI.getChannel(), message.write());
      }
    }
    
    public void sendOnceTime(byte[] data)
    {
      for (ServerInfo server : BungeeCord.getInstance().getServers().values())
      {
        Collection<ProxiedPlayer> players = server.getPlayers();
        if (players.size() > 0)
        {
          ProxiedPlayer player = (ProxiedPlayer)players.iterator().next();
          player.sendData(ServerAPI.getChannel(), data);
        }
      }
    }
    
    public void sendForEveryone(byte[] data)
    {
      for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
        server.sendData(ServerAPI.getChannel(), data);
      }
    }
    
    @Deprecated
    public void sendData(ProxiedPlayer player, ServerAPI.ServerMessage message)
    {
      player.sendData(ServerAPI.getChannel(), message.write());
    }
  }
  
  public static class BukkitControl
    implements ServerAPI.ServerControl, PluginMessageListener, ServerAPI.ServerReceiveMessage
  {
    private static JavaPlugin plugin;
    
    public static BukkitControl getControl()
    {
      return (BukkitControl)ServerAPI.getControl();
    }
    
    public static JavaPlugin getInstance()
    {
      return plugin;
    }
    
    private BukkitControl() {}
    
    public static void register(JavaPlugin plugin)
    {
      BukkitControl.plugin = plugin;
      Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, ServerAPI.getChannel());
      Bukkit.getMessenger().registerIncomingPluginChannel(plugin, ServerAPI.getChannel(), getControl());
      ServerAPI.registred = true;
      ServerAPI.register(getControl());
      Bukkit.getConsoleSender().sendMessage("§b[ServerAPI] §aAtivando sistema de comunicacao com Bungeecord!");
    }
    
    public static void unregister()
    {
      if (ServerAPI.registred)
      {
        Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin, ServerAPI.getChannel());
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, ServerAPI.getChannel());
        ServerAPI.registred = false;
      }
    }
    
    public static void connectToServer(Player p, int type)
    {
      ServerAPI.ServerMessage message = new ServerAPI.ServerMessage(p.getName(), null, "connect-to-server", new Object[] { Integer.valueOf(type) });
      getControl().sendToBungee(message);
    }
    
    public static void setServerStatus(int status)
    {
      ServerAPI.ServerMessage message = new ServerAPI.ServerMessage(null, null, "set-state", new Object[] { Integer.valueOf(status) });
      getControl().sendToBungee(message);
    }
    
    public void onReceiveMessage(ServerAPI.ServerMessage message) {}
    
    public void onPluginMessageReceived(String channel, Player player, byte[] data)
    {
      if (!channel.equals(ServerAPI.getChannel())) {
        return;
      }
      ServerAPI.ServerMessage message = ServerAPI.ServerMessage.read(data);
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
      for (ServerAPI.ServerReceiveMessage messageReceiver : ServerAPI.receivers) {
        messageReceiver.onReceiveMessage(message);
      }
    }
    
    public void log(String message)
    {
      Bukkit.getConsoleSender().sendMessage("§a[ServerAPI] §f" + message);
    }
    
    public void sendToServer(ServerAPI.ServerMessage message)
    {
      ServerAPI.ServerMessage newMessage = new ServerAPI.ServerMessage(message);
      newMessage.setTag("sendToServer::" + newMessage.getTag());
      sendOnceTime(newMessage.write());
    }
    
    public void sendToServers(ServerAPI.ServerMessage message)
    {
      ServerAPI.ServerMessage newMessage = new ServerAPI.ServerMessage(message);
      newMessage.setTag("sendToServers::" + newMessage.getTag());
      sendOnceTime(newMessage.write());
    }
    
    public void sendToPlayer(ServerAPI.ServerMessage message)
    {
      ServerAPI.ServerMessage newMessage = new ServerAPI.ServerMessage(message);
      newMessage.setTag("sendToPlayer::" + newMessage.getTag());
      sendOnceTime(newMessage.write());
    }
    
    public void sendToNetwork(ServerAPI.ServerMessage message)
    {
      ServerAPI.ServerMessage newMessage = new ServerAPI.ServerMessage(message);
      newMessage.setTag("sendToNetwork::" + newMessage.getTag());
      sendOnceTime(newMessage.write());
    }
    
    public void sendToPlayers(ServerAPI.ServerMessage message)
    {
      ServerAPI.ServerMessage newMessage = new ServerAPI.ServerMessage(message);
      newMessage.setTag("sendToPlayers::" + newMessage.getTag());
      sendOnceTime(newMessage.write());
    }
    
    public void sendOnceTime(byte[] data)
    {
      Player player = (Player)Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
      if (player != null) {
        player.sendPluginMessage(getInstance(), ServerAPI.getChannel(), data);
      }
    }
    
    public void sendForEveryone(byte[] data)
    {
      Bukkit.getServer().sendPluginMessage(getInstance(), ServerAPI.getChannel(), data);
    }
    
    public void sendToBungee(ServerAPI.ServerMessage message)
    {
      sendOnceTime(message.write());
    }
  }
  
  public static class ServerPlayer
  {
    private String name;
    private UUID uuid;
    private ServerAPI.Server server;
    private int port;
    private String host;
    
    public ServerPlayer(String name)
    {
      this.name = name;
    }
    
    public ServerPlayer() {}
    
    public String getName()
    {
      return this.name;
    }
    
    public void setName(String name)
    {
      this.name = name;
    }
    
    public UUID getUuid()
    {
      return this.uuid;
    }
    
    public void setUuid(UUID uuid)
    {
      this.uuid = uuid;
    }
    
    public ServerAPI.Server getServer()
    {
      return this.server;
    }
    
    public void setServer(ServerAPI.Server server)
    {
      this.server = server;
    }
    
    public int getPort()
    {
      return this.port;
    }
    
    public void setPort(int port)
    {
      this.port = port;
    }
    
    public String getHost()
    {
      return this.host;
    }
    
    public void setHost(String host)
    {
      this.host = host;
    }
  }
  
  public static List<ServerPlayer> getAllPlayers()
  {
    return allPlayers;
  }
  
  public static void setAllPlayers(List<ServerPlayer> allPlayers)
  {
    ServerAPI.allPlayers = allPlayers;
  }
  
  private ServerAPI() {}
  
  public static class Server
  {
    private int max;
    private String name;
    private int type;
    private int count;
    private int state;
    private String host;
    private int port;
    
    public Server(String name)
    {
      this.name = name;
    }
    
    public void setState(ServerAPI.ServerState state)
    {
      this.state = state.getValue();
    }
    
    public Server(String host, int port)
    {
      this.host = host;
      this.port = port;
    }
    
    public boolean isState(int state)
    {
      return this.state == state;
    }
    
    public boolean isType(int type)
    {
      return this.type == type;
    }
    
    public boolean isState(ServerAPI.ServerState state)
    {
      return this.state == state.getValue();
    }
    
    public boolean isType(ServerAPI.ServerType type)
    {
      return this.type == type.getValue();
    }
    
    private List<String> players = new ArrayList<>();
    
    public int getCount()
    {
      return this.count;
    }
    
    public void setCount(int count)
    {
      this.count = count;
    }
    
    public List<ServerAPI.ServerPlayer> getPlayersNames()
    {
      List<ServerAPI.ServerPlayer> lista = new ArrayList<ServerPlayer>();
      for (ServerAPI.ServerPlayer player : ServerAPI.allPlayers) {
        if (player.getServer() == this) {
          lista.add(player);
        }
      }
      return lista;
    }
    
    public boolean canConnect()
    {
      return (isReallyOnline()) && (!isFull());
    }
    
    public boolean canReallyConnect()
    {
      return (canConnect()) && (!isRestarting()) && (!isGameStarted());
    }
    
    public boolean isRestarting()
    {
      return isState(ServerAPI.ServerState.RESTARTING);
    }
    
    public boolean isPlaying()
    {
      return isState(ServerAPI.ServerState.IN_GAME);
    }
    
    public boolean isGameStarted()
    {
      return isPlaying();
    }
    
    public boolean isReallyOnline()
    {
      return (!isOffline()) && (!isDisabled());
    }
    
    public boolean isOffline()
    {
      return isState(ServerAPI.ServerState.OFFLINE);
    }
    
    public boolean isDisabled()
    {
      return isState(ServerAPI.ServerState.DISABLED);
    }
    
    public boolean isOnline()
    {
      return isState(ServerAPI.ServerState.ONLINE);
    }
    
    public boolean isFull()
    {
      return this.players.size() == this.max;
    }
    
    public boolean isEmpty()
    {
      return this.players.isEmpty();
    }
    
    public int getMax()
    {
      return this.max;
    }
    
    public void setMax(int max)
    {
      this.max = max;
    }
    
    public String getName()
    {
      return this.name;
    }
    
    public void setName(String name)
    {
      this.name = name;
    }
    
    public int getType()
    {
      return this.type;
    }
    
    public void setType(int type)
    {
      this.type = type;
    }
    
    public int getState()
    {
      return this.state;
    }
    
    public void setState(int state)
    {
      this.state = state;
    }
    
    public String getHost()
    {
      return this.host;
    }
    
    public void setHost(String host)
    {
      this.host = host;
    }
    
    public int getPort()
    {
      return this.port;
    }
    
    public void setPort(int port)
    {
      this.port = port;
    }
  }
  
  public static abstract interface ServerControl
  {
    public abstract void log(String paramString);
    
    public abstract void sendToServer(ServerAPI.ServerMessage paramServerMessage);
    
    public abstract void sendToServers(ServerAPI.ServerMessage paramServerMessage);
    
    public abstract void sendToPlayer(ServerAPI.ServerMessage paramServerMessage);
    
    public abstract void sendToNetwork(ServerAPI.ServerMessage paramServerMessage);
    
    public abstract void sendToPlayers(ServerAPI.ServerMessage paramServerMessage);
    
    public abstract void sendToBungee(ServerAPI.ServerMessage paramServerMessage);
    
    public abstract void sendOnceTime(byte[] paramArrayOfByte);
    
    public abstract void sendForEveryone(byte[] paramArrayOfByte);
  }
  
  public static abstract interface ServerReceiveMessage
  {
    public abstract void onReceiveMessage(ServerAPI.ServerMessage paramServerMessage);
  }
}
