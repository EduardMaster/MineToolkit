package net.eduard.api.lib.bungee;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import net.eduard.api.lib.modules.Extra;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BungeeController implements ServerController {

    private Plugin plugin;
    private final BungeeMessageListener listener = new BungeeMessageListener(this);
    private final BungeeStatusUpdater updater = new BungeeStatusUpdater();
    private ScheduledTask task;

    @Override
    public void sendMessage(String server, String tag, String line) {
        ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(arrayOut);
        try {
            out.writeUTF(server);
            out.writeUTF(tag);
            out.writeUTF(line);
            ServerInfo sv = ProxyServer.getInstance().getServerInfo(server);
            if (sv != null) {
                sv.sendData(BungeeAPI.getChannel(), arrayOut.toByteArray(), true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void sendMessage(String tag, String line) {
        for (ServerInfo sv : ProxyServer.getInstance().getServers().values()) {
            ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(arrayOut);
            try {
                out.writeUTF("bungeecord");
                out.writeUTF(tag);
                out.writeUTF(line);
                sv.sendData(BungeeAPI.getChannel(), arrayOut.toByteArray(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public void receiveMessage(String server, String tag, String line) {
        if (server.equals("all")) {
            sendMessage(tag, line);
            for (ServerMessageHandler handler : BungeeAPI.getHandlers()) {
                handler.onMessage(server, tag, line);
            }
        } else if (server.equals("bungeecord")) {
            if (tag.equals("connect")) {
                String[] split = line.split(" ");
                connect(split[0], Extra.toInt(split[1]), Extra.toInt(split[2]));
            } else {
                for (ServerMessageHandler handler : BungeeAPI.getHandlers()) {
                    handler.onMessage(server, tag, line);
                }
            }

        } else {
            sendMessage(server, tag, line);
        }

    }


    @Override
    public void register() {
        ProxyServer.getInstance().registerChannel(BungeeAPI.getChannel());
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, listener);
        for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
            ServerSpigot servidor = BungeeAPI.getServer(server.getName());
            servidor.setHost(server.getAddress().getHostName());
            servidor.setPort(server.getAddress().getPort());
            servidor.setPlayers(server.getPlayers().stream().map(ProxiedPlayer::getName).collect(Collectors.toList()));
            servidor.setCount(server.getPlayers().size());
        }
        task = ProxyServer.getInstance().getScheduler().schedule(plugin, updater, 1, 1, TimeUnit.SECONDS);
        ProxyServer.getInstance().getConsole().
                sendMessage(new TextComponent(
                        "§aRegistrando sistema de Conexão com servidores Spigot via Plugin Messager"));

    }

    @Override
    public void unregister() {
        ProxyServer.getInstance().getPluginManager().unregisterListener(listener);
        ProxyServer.getInstance().unregisterChannel(BungeeAPI.getChannel());
        ProxyServer.getInstance().getScheduler().cancel(task);
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public void connect(String player, int serverType, int serverState) {
        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(player);
        if (p != null) {
            for (ServerSpigot server : BungeeAPI.getServers().values()) {
                if (server.getState() == serverState
                        && server.getType() == serverType
                        && server.getCount() < server.getMax()) {
                    ServerInfo sv = ProxyServer.getInstance().getServerInfo(server.getName());
                    p.connect(sv);
                    break;
                }
            }
        }

    }

    @Override
    public void setState(String serverName, int state) {
        ServerSpigot server = BungeeAPI.getServer(serverName);
        server.setState(state);
    }

}
