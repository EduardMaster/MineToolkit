package net.eduard.api;

import java.util.UUID;


import net.eduard.api.lib.bungee.BungeeAPI;
import net.eduard.api.lib.bungee.BungeeController;
import net.eduard.api.lib.bungee.ServerSpigot;
import net.eduard.api.lib.bungee.ServerState;
import net.eduard.api.lib.database.DBManager;
import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.server.BungeeDB;
import net.eduard.api.server.EduardBungeePlugin;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ProxyReloadEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.event.TabCompleteResponseEvent;
import net.md_5.bungee.api.event.TargetedEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.event.EventHandler;

/**
 * Para fazer plugins usando esta dependencia , lembre-se de colocar depends: [EduardAPI] 
 * em vez de depend: [EduardAPI na bungee.yml
 * @author Eduard
 *
 */
public class EduardBungeeAPI extends EduardBungeePlugin{
	private static EduardBungeeAPI instance;
	

	public static EduardBungeeAPI getInstance() {
		return instance;
	}
	private BungeeDB bungee;
	public void onLoad() {
		StorageAPI.register(DBManager.class);
		super.onLoad();
	}
	public void onEnable() {
		instance = this;
		
		reload();
		
		
		
		registerEvents(this);
	
		BungeeController bungee = BungeeAPI.getBungee();
		bungee.setPlugin(this);
		bungee.register();
		
	
		
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new BungeeConfigReloadCommand());
	}

	public void reload() {
		
		getConfig().reloadConfig();
		getConfig().add("debug-plugin-messages", true);
		getConfig().add("database-debug", false);
		getConfig().saveConfig();
		bungee = new BungeeDB(getDb());
		DBManager.setDebug(getConfig().getBoolean("database-debug"));
			
		if (getDb().isEnabled()) {
			log("MySQL Ativado iniciando conexao");
			getDb().openConnection();
			if (getDb().hasConnection()) {
				bungee.createBungeeTables();

				for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
					if (!bungee.serversContains(server.getName())) {
						getDb().insert("servers", server.getName(),
								server.getAddress().getAddress().getHostAddress(), server.getAddress().getPort(), 0, 0);
					} else {
						getDb().change("servers", "host = ? , port = ?", "name = ?",
								server.getAddress().getAddress().getHostAddress(), server.getAddress().getPort(),
								server.getName());
					}
				}

			} else {
				error("§cFalha ao conectar com a Database");
			}
		} else {
			log("MySQL destivado algumas coisas da EduardBungeeAPI estarao desativado");
		} 
		for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
			getConfig().add("servers." + server.getName() + ".enabled", true);
			getConfig().add("servers." + server.getName() + ".type", 0);
		}
		getConfig().saveConfig();
		BungeeController bungee = BungeeAPI.getBungee();
		bungee.setPlugin(this);
		bungee.register();
		for (String serverName : getConfig().getSection("servers").getKeys()) {
			boolean enabled = getConfig().getBoolean("servers." + serverName + ".enabled");
			int type = getConfig().getInt("servers." + serverName + ".type");

			ServerSpigot server = BungeeAPI.getServer(serverName);
			server.setType(type);
			if (enabled) {
				server.setState(ServerState.OFFLINE);
			} else {
				server.setState(ServerState.DISABLED);
			}

		}

	}

	public static class BungeeConfigReloadCommand extends Command {

		public BungeeConfigReloadCommand() {
			super("bungeereload");
		}

		@Override
		public void execute(CommandSender sender, String[] args) {
			EduardBungeeAPI.getInstance().getConfig().reloadConfig();
			sender.sendMessage(new TextComponent("§aToda configuracao foi recarregada!"));
			EduardBungeeAPI.getInstance().reload();
		}

	}

	public void onDisable() {
		getDb().closeConnection();
		BungeeAPI.getController().unregister();
	}

	

	@EventHandler
	public void onJoin(PreLoginEvent e) {
		// PendingConnection p = e.getConnection();
		// info("§aPreLoginEvent", p);
	}

	@EventHandler
	public void onJoin(LoginEvent e) {
		// PendingConnection p = e.getConnection();
		// info("§aLoginEvent", p);
	}

	@EventHandler
	public void onJoin(PostLoginEvent e) {
		ProxiedPlayer player = e.getPlayer();
		// info("§aPostLoginEvent", player.getPendingConnection());
		if (getDb().hasConnection()) {
			if (!bungee.playersContains(player.getName())) {
				getDb().insert("players", player.getName(), player.getUniqueId(), "");
			}
		}
	}

	@EventHandler
	public void event(ServerDisconnectEvent e) {
		String serverName = e.getTarget().getName();
		UUID playerUUID = e.getPlayer().getUniqueId();
		int playerAmount = e.getTarget().getPlayers().size();
		if (getDb().hasConnection()) {
			BungeeCord.getInstance().getScheduler().runAsync(getInstance(), () -> {
				bungee.setPlayersAmount(serverName, playerAmount);
				bungee.setPlayerServer(playerUUID, "");
			});
		}

	}

	@EventHandler()
	public void event(ServerConnectEvent e) {
	}

	@EventHandler
	public void event(ServerConnectedEvent e) {
		String serverName = e.getServer().getInfo().getName();
		UUID playerUUID = e.getPlayer().getUniqueId();
		int playerAmount = e.getServer().getInfo().getPlayers().size();

		if (getDb().hasConnection()) {
			BungeeCord.getInstance().getScheduler().runAsync(getInstance(), () -> {
				bungee.setPlayersAmount(serverName, playerAmount);
				bungee.setPlayerServer(playerUUID, serverName);
			});

		}
	}

	/**
	 * § possivel alterar o sistema de permiss§o do bungee
	 * 
	 * @param e
	 */
	@EventHandler
	public void event(PermissionCheckEvent e) {

	}

	@EventHandler
	public void event(PlayerHandshakeEvent e) {
	}

	@EventHandler
	public void event(PlayerDisconnectEvent e) {

	}

	@EventHandler
	public void event(PluginMessageEvent e) {

	}

	@EventHandler
	public void event(ProxyPingEvent e) {

	}

	@EventHandler
	public void event(ServerKickEvent e) {

	}

	@EventHandler
	public void event(ServerSwitchEvent e) {

	}

	@EventHandler
	public void event(TargetedEvent e) {

	}

	@EventHandler
	public void event(ProxyReloadEvent e) {

	}

	@EventHandler
	public void event(TabCompleteResponseEvent e) {

	}

	@EventHandler
	public void event(TabCompleteEvent e) {

	}

}
