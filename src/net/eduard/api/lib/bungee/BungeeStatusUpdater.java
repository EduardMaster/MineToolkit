package net.eduard.api.lib.bungee;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class BungeeStatusUpdater implements Runnable {

    public static void updateServers() {
        for (ServerSpigot server : BungeeAPI.getServers().values()) {
            ServerInfo servidor = BungeeCord.getInstance().getServerInfo(server.getName());
            if (servidor == null) continue;
            if ((!server.isDisabled())
                    && ((server.isOffline()) || ((servidor.getPlayers().size() == 0)
                    && (server.isRestarting())))) {
                servidor.ping((result, error) -> {

                    if (result == null) {
                        if (!server.isRestarting()) {
                            server.setState(ServerState.OFFLINE);
                        }
                    } else {
                        server.setMax(result.getPlayers().getMax());
                        server.setCount(result.getPlayers().getOnline());
                        if ((server.isOffline()) || (server.isRestarting())) {
                            server.setState(ServerState.ONLINE);
                        }
                    }
                });
            }
            if (!servidor.getPlayers().isEmpty()) {
                try {
                    ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
                    ObjectOutputStream out = new ObjectOutputStream(arrayOut);
                    out.writeUTF(server.getName());
                    out.writeUTF("server-update");
                    out.writeObject(server);
                    for (ServerInfo sv : BungeeCord.getInstance().getServers().values()) {
                        sv.sendData(BungeeAPI.getChannel(), arrayOut.toByteArray());
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
