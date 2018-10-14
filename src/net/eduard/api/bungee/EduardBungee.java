package net.eduard.api.bungee;

import java.util.UUID;

import net.eduard.api.lib.BungeeConfig;
import net.eduard.api.lib.bungee.BungeeAPI;
import net.eduard.api.lib.bungee.BungeeController;
import net.eduard.api.lib.bungee.ServerSpigot;
import net.eduard.api.lib.bungee.ServerState;
import net.eduard.api.lib.manager.DBManager;
import net.eduard.api.lib.storage.StorageAPI;
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
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
public class EduardBungee extends Plugin implements Listener {
	private static EduardBungee instance;
	private DBBungee bungeeManager;

	private BungeeConfig config;

	private BungeeConfig getConfig() {
		return config;
	}

	public static EduardBungee getInstance() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		BungeeCord.getInstance().getPluginManager().registerListener(this, this);
		StorageAPI.register(DBManager.class);
		StorageAPI.register(DBBungee.class);

		config = new BungeeConfig("control.yml", this);
		reload();
		BungeeController bungee = BungeeAPI.getBungee();
		bungee.setPlugin(this);
		bungee.register();
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new BungeeConfigReloadCommand());
	}

	
	public void reload() {
		config.reloadConfig();
		config.add("debug-plugin-messages", true);
		config.add("database-debug", false);
		config.saveConfig();
		DBManager.setDebug(config.getBoolean("database-debug"));
		if (bungeeManager != null) {
			bungeeManager.closeConnection();
		}
		if (config.contains("Database")) {
			setBungeeManager((DBBungee) config.get("Database"));
		} else {
			setBungeeManager(new DBBungee("root", "", "localhost"));
			config.set("Database", bungeeManager);
			config.saveConfig();

		}
		
		

		bungeeManager.openConnection();
		if (bungeeManager.hasConnection()) {
			bungeeManager.createBungeeTables();

			for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
				if (!bungeeManager.serversContains(server.getName())) {
					bungeeManager.insert("servers", server.getName(), server.getAddress().getAddress().getHostAddress(),
							server.getAddress().getPort(), 0, 0);
				} else {
					bungeeManager.change("servers", "host = ? , port = ?", "name = ?",
							server.getAddress().getAddress().getHostAddress(), server.getAddress().getPort(),
							server.getName());
				}
			}

		} else {
			BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("�cFalha ao conectar com a Database"));
		}
		for (ServerInfo server : BungeeCord.getInstance().getServers().values()) {
			config.add("servers." + server.getName() + ".enabled", true);
			config.add("servers." + server.getName() + ".type", 0);
		}
		config.saveConfig();
		BungeeController bungee = BungeeAPI.getBungee();
		bungee.setPlugin(this);
		bungee.register();
		for (String serverName : config.getSection("servers").getKeys()) {
			boolean enabled = config.getBoolean("servers." + serverName + ".enabled");
			int type = config.getInt("servers." + serverName + ".type");
			
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
			EduardBungee.getInstance().getConfig().reloadConfig();
			sender.sendMessage(new TextComponent("�aToda configuracao foi recarregada!"));
			EduardBungee.getInstance().reload();
		}

	}

	public void onDisable() {
		bungeeManager.closeConnection();
		BungeeAPI.getController().unregister();
	}

	public DBManager getBungeeManager() {
		return bungeeManager;
	}

	public void setBungeeManager(DBBungee bungeeManager) {
		this.bungeeManager = bungeeManager;
	}

	@EventHandler
	public void onJoin(PreLoginEvent e) {
		// PendingConnection p = e.getConnection();
		// info("�aPreLoginEvent", p);
	}

	@EventHandler
	public void onJoin(LoginEvent e) {
		// PendingConnection p = e.getConnection();
		// info("�aLoginEvent", p);
	}

	@EventHandler
	public void onJoin(PostLoginEvent e) {
		ProxiedPlayer player = e.getPlayer();
		// info("�aPostLoginEvent", player.getPendingConnection());
		if (bungeeManager.hasConnection()) {
			if (!bungeeManager.playersContains(player.getName())) {
				bungeeManager.insert("players", player.getName(), player.getUniqueId(), "");
			}
		}
	}

	@EventHandler
	public void event(ServerDisconnectEvent e) {
		String serverName = e.getTarget().getName();
		UUID playerUUID = e.getPlayer().getUniqueId();
		int playerAmount = e.getTarget().getPlayers().size();
		if (bungeeManager.hasConnection()) {
			BungeeCord.getInstance().getScheduler().runAsync(getInstance(), () -> {
				bungeeManager.setPlayersAmount(serverName, playerAmount);
				bungeeManager.setPlayerServer(playerUUID, "");
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

		if (bungeeManager.hasConnection()) {
			BungeeCord.getInstance().getScheduler().runAsync(getInstance(), () -> {
				bungeeManager.setPlayersAmount(serverName, playerAmount);
				bungeeManager.setPlayerServer(playerUUID, serverName);
			});

		}
	}

	/**
	 * � possivel alterar o sistema de permiss�o do bungee
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
