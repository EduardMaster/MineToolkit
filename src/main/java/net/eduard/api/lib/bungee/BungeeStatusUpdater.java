package net.eduard.api.lib.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.stream.Collectors;

public class BungeeStatusUpdater implements Runnable {

    public static void updateServers() {
        for (ServerSpigot server : BungeeAPI.getServers().values()) {
            int quantidadeDePlayers = server.getCount();
            ServerInfo servidor = ProxyServer.getInstance().getServerInfo(server.getName());
            if (servidor == null) continue;
            if (((server.isOffline()) || ((servidor.getPlayers().size() == 0)
                    && (server.isRestarting())))) {
                servidor.ping((result, error) -> {
                    if (result == null) {
                        if (!server.isRestarting()) {
                            server.setState(ServerState.OFFLINE);
                        }
                    } else {
                        if (server.getMax() == 1) {
                            server.setMax(result.getPlayers().getMax());
                        }

                        if ((server.isOffline()) || (server.isRestarting())) {
                            server.setState(ServerState.ONLINE);
                        }
                    }
                });
            }
            server.setCount(servidor.getPlayers().size());
            server.setPlayers(servidor.getPlayers()
                    .stream().map(CommandSender::getName).collect(Collectors.toList()));

            if (quantidadeDePlayers != server.getCount()) {
                try {
                    ProxyServer.getInstance().getConsole().sendMessage(
                            new TextComponent("§aEnviando updates do Servidor "+server.getName() +" para todos servidores")
                    );
                    ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
                    ObjectOutputStream out = new ObjectOutputStream(arrayOut);
                    out.writeUTF(server.getName());
                    out.writeUTF("server-update");
                    out.writeObject(server);
                    for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) {
                        serverInfo.sendData(BungeeAPI.getChannel(),
                                arrayOut.toByteArray());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void run() {
        updateServers();
    }
}
