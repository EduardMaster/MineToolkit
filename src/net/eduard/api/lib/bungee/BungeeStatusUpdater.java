package net.eduard.api.lib.bungee;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;

public class BungeeStatusUpdater implements Runnable{

	
	
	public static void updateServers() {
		for (ServerSpigot server : BungeeAPI.getServers()) {
			ServerInfo servidor = BungeeCord.getInstance().getServerInfo(server.getName());
			if ((!server.isDisabled())
					&& ((server.isOffline()) || ((servidor.getPlayers().size() == 0) && (server.isRestarting())))) {
				servidor.ping(new Callback<ServerPing>() {
					public void done(ServerPing result, Throwable error) {
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
					}

				});
			}
		}
	}

	@Override
	public void run() {
		updateServers();
		// TODO Auto-generated method stub
		
	}
}
