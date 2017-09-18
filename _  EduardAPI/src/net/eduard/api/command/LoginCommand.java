package net.eduard.api.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

import net.eduard.api.API;
import net.eduard.api.config.Config;
import net.eduard.api.game.Title;
import net.eduard.api.manager.CMD;
import net.eduard.api.setup.GameAPI;
import net.eduard.api.setup.WorldAPI;

public class LoginCommand extends CMD {
	public String message = "§aVoce foi logado com sucesso!";
	public String messageError = "§cVoce ja esta logado!";
	public String messageRegister = "§cVoce não esta registrado!";
	public String messagePassword = "§cSenha incorreta";
	public String messageKickOnFails = "§cVoce errou muitas vezes a senha!";
	public int maxFailsLogin = 2;

	public Title title = new Title(20, 20 * 60, 20, "§cAutenticação",
			"§c/login <senha>");
	public Title titleSuccess = new Title(20, 20 * 3, 20, "§a§lAutenticado!",
			"§6§lSeja bem vindo novamente!");
	public Config config = new Config("auth.yml");
	public boolean kickOnMaxFails = true;
	public boolean banOnMaxFails = false;
	public boolean banIpOnMaxFails = false;
	public static final Map<Player, String> PLAYERS_LOGGED = new HashMap<>();
	public static final Map<Player, Integer> FAILS_LOGINS = new HashMap<>();
	public LoginCommand() {
		super("login");

	}
	public boolean isRegistered(Player p) {
		return config.contains(p.getUniqueId().toString() + ".password");
	}
	public String getPassword(Player p) {
		return config.getString(p.getUniqueId().toString() + ".password");
	}
	@EventHandler
	public void event(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (!PLAYERS_LOGGED.containsKey(p)) {
			if (!WorldAPI.equals2(e.getFrom(), e.getTo())) {
				e.setTo(e.getFrom().setDirection(e.getTo().getDirection()));
				API.SOUND_ERROR.create(p);
			}

		}
	}
	@EventHandler
	public void event(PluginDisableEvent e) {
		if (e.getPlugin().equals(getPlugin())) {
			config.saveConfig();
		}
	}
	@EventHandler
	public void event(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (!PLAYERS_LOGGED.containsKey(p)) {
			e.setCancelled(true);
			API.SOUND_ERROR.create(p);
		}
	}
	@EventHandler
	public void event(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (!PLAYERS_LOGGED.containsKey(p)) {
			e.setCancelled(true);
			API.SOUND_ERROR.create(p);
		}
	}
	@EventHandler
	public void event(PlayerQuitEvent e) {
		PLAYERS_LOGGED.remove(e.getPlayer());

	}
	@EventHandler
	public void event(PlayerKickEvent e) {
		PLAYERS_LOGGED.remove(e.getPlayer());

	}
	@EventHandler
	public void event(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if (!PLAYERS_LOGGED.containsKey(p)) {
			e.setCancelled(true);
			API.SOUND_ERROR.create(p);
		}
	}
	@EventHandler
	public void event(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();

			if (!PLAYERS_LOGGED.containsKey(p)) {
				e.setCancelled(true);
				API.SOUND_ERROR.create(p);
			}
		}
	}
	@EventHandler
	public void event(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (isRegistered(p)) {
			if (!PLAYERS_LOGGED.containsKey(p)) {
				title.create(p);
			}

		}
	}

	@EventHandler
	public void event(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (!PLAYERS_LOGGED.containsKey(p)) {
			e.setCancelled(true);
			API.SOUND_ERROR.create(p);
		}
	}

	@EventHandler
	public void event(PlayerCommandPreprocessEvent e) {
		String msg = e.getMessage();
		Player p = e.getPlayer();
		if (!PLAYERS_LOGGED.containsKey(p)) {
			e.setCancelled(true);
			if (msg.startsWith("/login") | msg.startsWith("/register")) {
				e.setCancelled(false);
			} else {
				for (String cmd : getAliases()) {
					if (msg.startsWith("/" + cmd)) {
						e.setCancelled(false);
					}
				}
				for (String cmd : Bukkit.getPluginCommand("register")
						.getAliases()) {
					if (msg.startsWith("/" + cmd)) {
						e.setCancelled(false);
					}
				}
			}
		}

	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if (API.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (args.length == 0) {
				sendUsage(sender);
			} else {
				String pass = args[0];
				if (!isRegistered(p)) {
					API.chat(sender, messageRegister);
					API.SOUND_ERROR.create(p);
				} else {
					if (PLAYERS_LOGGED.containsKey(p)) {
						API.chat(sender, messageError);
						API.SOUND_ERROR.create(p);
					} else {
						String password = getPassword(p);
						if (pass.equals(password)) {
							API.chat(p, message);
							if (p.getGameMode() != GameMode.CREATIVE) {

								p.setFlying(false);
								p.setAllowFlight(false);
							}
							PLAYERS_LOGGED.put(p,
									"" + System.currentTimeMillis());
							titleSuccess.create(p);
							API.SOUND_SUCCESS.create(p);
							config.set(p.getUniqueId().toString() + ".last-ip",
									GameAPI.getIp(p));
						} else {
							int fails = 0;
							if (FAILS_LOGINS.containsKey(p))
								fails = FAILS_LOGINS.get(p);;
							fails++;
							FAILS_LOGINS.put(p, fails);
							if (fails == maxFailsLogin) {
								if (kickOnMaxFails) {
									p.kickPlayer(messageKickOnFails);
								}
								if (banOnMaxFails) {
									p.setBanned(true);
								}
								if (banIpOnMaxFails) {
									Bukkit.banIP(GameAPI.getIp(p));
								}
								FAILS_LOGINS.remove(p);
							}
							API.chat(p, messagePassword);
							API.SOUND_ERROR.create(p);
						}
					}
				}

			}
		}
		return true;
	}

}
