package net.eduard.api.lib.bungee;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.eduard.api.lib.modules.Extra;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BungeeController implements ServerController {

    private Plugin plugin;
    private BungeeMessageListener listener = new BungeeMessageListener(this);
    private BungeeStatusUpdater updater = new BungeeStatusUpdater();
    private ScheduledTask task;

    @Override
    public void sendMessage(String server, String tag, String line) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(server);
        out.writeUTF(tag);
        out.writeUTF(line);
        ServerInfo sv = BungeeCord.getInstance().getServerInfo(server);
        if (sv != null) {
            sv.sendData(BungeeAPI.getChannel(), out.toByteArray(), true);
        }

    }

    @Override
    public void sendMessage(String tag, String line) {
        for (ServerInfo sv : BungeeCord.getInstance().getServers().values()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("bungeecord");
            out.writeUTF(tag);
            out.writeUTF(line);
            sv.sendData(BungeeAPI.getChannel(), out.toByteArray(), true);
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
        BungeeCord.getInstance().registerChannel(BungeeAPI.getChannel());
        BungeeCord.getInstance().getPluginManager().registerListener(plugin, listener);
        for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
            ServerSpigot servidor = BungeeAPI.getServer(server.getName());
            servidor.setHost(server.getAddress().getHostName());
            servidor.setPort(server.getAddress().getPort());
            servidor.setPlayers(server.getPlayers().stream().map(ProxiedPlayer::getName).collect(Collectors.toList()));
            servidor.setCount(server.getPlayers().size());
        }
        task = BungeeCord.getInstance().getScheduler().schedule(plugin, updater, 1, 1, TimeUnit.SECONDS);
        BungeeCord.getInstance().getConsole().sendMessage("§aRegistrando sistema de Conexão com servidores Spigot via PluginMeessaging");

    }

    @Override
    public void unregister() {
        BungeeCord.getInstance().getPluginManager().unregisterListener(listener);
        BungeeCord.getInstance().unregisterChannel(BungeeAPI.getChannel());
        BungeeCord.getInstance().getScheduler().cancel(task);
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public void connect(String player, int serverType, int serverState) {
        ProxiedPlayer p = BungeeCord.getInstance().getPlayer(player);
        if (p != null) {
            for (ServerSpigot server : BungeeAPI.getServers()) {
                if (server.getState() == serverState && server.getType() == serverType
                && server.getCount() < server.getMax()) {
                    ServerInfo sv = BungeeCord.getInstance().getServerInfo(server.getName());
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
