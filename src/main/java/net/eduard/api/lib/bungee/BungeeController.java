package net.eduard.api.lib.bungee;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.eduard.api.lib.modules.Extra;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BungeeController implements ServerController<Plugin> {


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
                String[] args = line.split(" ");
                connect(args[0], args[1], args[2] , Extra.toInt(args[3]));
            } else {
                for (ServerMessageHandler handler : BungeeAPI.getHandlers()) {
                    handler.onMessage(server, tag, line);
                }
            }

        } else {
            sendMessage(server, tag, line);
        }

    }

    private Plugin plugin;
    @Override
    public void register(Plugin pl) {
        setPlugin(pl);
        ProxyServer.getInstance().registerChannel(BungeeAPI.getChannel());
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, listener);

        task = ProxyServer.getInstance().getScheduler().schedule(plugin, updater, 1, 1, TimeUnit.SECONDS);
        ProxyServer.getInstance().getConsole().
                sendMessage(new TextComponent(
                        "§aRegistrando sistema de Conexao com servidores Spigot via Plugin Messager"));

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
    public void connect(String playerName, String serverType, String subType,int teamSize) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        if (player != null) {
            for (ServerSpigot spigot : BungeeAPI.getServers().values()) {
                if (spigot.getTeamSize() == teamSize&&spigot.getSubType().equalsIgnoreCase(subType)
                        && spigot.getType().equalsIgnoreCase(serverType)
                        && spigot.getCount() < spigot.getMax()) {
                    ServerInfo server = ProxyServer.getInstance()
                            .getServerInfo(spigot.getName());
                    player.connect(server);
                    break;
                }
            }
        }

    }

    @Override
    public void setState(String serverName, ServerState state) {
        ServerSpigot server = BungeeAPI.getServer(serverName);
        server.setState(state);
    }

}
