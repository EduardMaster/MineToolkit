package net.eduard.api;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import net.eduard.api.command.EnchantCommand;
import net.eduard.api.command.GotoCommand;
import net.eduard.api.command.SoundCommand;
import net.eduard.api.command.api.ApiCommand;
import net.eduard.api.command.config.ConfigCommand;
import net.eduard.api.command.map.MapCommand;
import net.eduard.api.config.Config;
import net.eduard.api.config.ConfigSection;
import net.eduard.api.lib.ConfigAPI;
import net.eduard.api.lib.Mine;
import net.eduard.api.lib.Mine.Replacer;
import net.eduard.api.lib.bungee.BukkitController;
import net.eduard.api.lib.bungee.BungeeAPI;
import net.eduard.api.lib.game.Schematic;
import net.eduard.api.lib.manager.CommandManager;
import net.eduard.api.lib.manager.DBManager;
import net.eduard.api.lib.manager.DropManager;
import net.eduard.api.lib.manager.PlayersManager;
import net.eduard.api.lib.manager.TimeManager;
import net.eduard.api.lib.modules.BukkitBungeeAPI;
import net.eduard.api.lib.modules.VaultAPI;
import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.lib.storage.StorageBase;
import net.eduard.api.lib.storage.bukkit_storables.BukkitStorables;
import net.eduard.api.server.EduardPlugin;

/**
 * Classe Principal do Plugin EduardAPI herda todas propriedades de um
 * JavaPlugin e tamb§m implementa Listener para alterar eventos. <br>
 * Padroes que v§o existir na nomeclatura dos plugins<br>
 * Possiveis Prefixos: e, Edu, Eduard, Master, EduMaster, EM, <br>
 * Padr§o para Plugins iniciado no dia 02/03/2018<br>
 * Prefixo+Nome exemplo MasterFactions<br>
 * <br>
 * A qualquer momento posso mudar a nomeclatura porem os plugins j§ nomeados
 * anteriormente continuam com mesmo nome.<br>
 * 
 * @author Eduard
 * @version 1.0
 * @since 1.0
 * 
 * 
 */
public class EduardAPI extends JavaPlugin implements Listener {

	private static JavaPlugin plugin;
	private TimeManager time;

	private Config messages;

	private Config config;

	public static JavaPlugin getInstance() {
		return plugin;
	}

	public boolean getBoolean(String path) {
		return config.getBoolean(path);
	}

	public String message(String path) {
		return messages.message(path);
	}

	public List<String> getMessages(String path) {
		return messages.getMessages(path);
	}

	public TimeManager getTime() {
		return time;
	}

	public Config getMessages() {
		return messages;
	}

	public Config getConfigs() {
		return config;
	}

	public void onLoad() {

	}


	public void onEnable() {
		plugin = this;
		// BukkitControl.register(this);
		// BukkitAPI.register(this);
		config = new Config(this, "config.yml");
		messages = new Config(this, "messages.yml");
		time = new TimeManager(this);
		BukkitBungeeAPI.requestCurrentServer();
		BukkitController bukkit = BungeeAPI.getBukkit();
		bukkit.setPlugin(plugin);
		bukkit.register();
		StorageBase.setDebug(config.getBoolean("debug-storage"));
		DBManager.setDebug(config.getBoolean("debug-db"));
		StorageAPI.registerPackage(getClass(), "net.eduard.api.command");
		StorageAPI.registerPackage(getClass(), "net.eduard.api.lib.game");
		StorageAPI.registerPackage(getClass(), "net.eduard.api.lib.manager");
		StorageAPI.registerPackage(getClass(), "net.eduard.api.server");
		StorageAPI.registerClasses(Mine.class);

		BukkitStorables.load();
		Mine.resetScoreboards();
		Mine.console("§bEduardAPI §fScoreboards resetadas!");
		replacers();
		time.timers(20, new Runnable() {

			@Override
			public void run() {
				Mine.updateTargets();
			}
		});
		new ApiCommand().register();
		new MapCommand().register();
		new ConfigCommand().register();
		new EnchantCommand().register();
		new GotoCommand().register();
		new SoundCommand().register();

		Mine.console("§bEduardAPI §fCustom Tag e Scoreboard ativado!");
		saveObjects();
		Mine.console("§bEduardAPI §fDataBase §agerada!");
		new DropManager().register(this);
		Mine.console("§bEduardAPI §fCustom drops ativado!");
		Mine.registerEvents(this, this);
		Mine.console("§bEduardAPI §fBase ativado!");

		Mine.loadMaps();
		Mine.console("§bEduardAPI §fMapas §acarregados!");

		config.add("sound-teleport", Mine.OPT_SOUND_TELEPORT);
		config.add("sound-error", Mine.OPT_SOUND_ERROR);
		config.add("sound-success", Mine.OPT_SOUND_SUCCESS);
		config.saveConfig();
		Mine.OPT_AUTO_RESPAWN = config.getBoolean("auto-respawn");
		Mine.OPT_NO_JOIN_MESSAGE = config.getBoolean("no-join-message");
		Mine.OPT_NO_QUIT_MESSAGE = config.getBoolean("no-quit-message");
		Mine.OPT_NO_DEATH_MESSAGE = config.getBoolean("no-death-message");

		Mine.MSG_ON_JOIN = config.message("on-join-message");
		Mine.MSG_ON_QUIT = config.message("on-quit-message");
		Mine.OPT_SOUND_TELEPORT = config.getSound("sound-teleport");
		Mine.OPT_SOUND_ERROR = config.getSound("sound-error");
		Mine.OPT_SOUND_SUCCESS = config.getSound("sound-success");
		if (config.getBoolean("auto-rejoin")) {
			for (Player p : Mine.getPlayers()) {
				Mine.callEvent(new PlayerJoinEvent(p, null));
			}
		}

		Mine.setPlayerManager(new PlayersManager());
		Mine.getPlayerManager().register(this);
		Mine.console("§bEduardAPI §acarregado!");
//		teste();
	}
	

	@Override
	public void onDisable() {
		Mine.saveMaps();
		Mine.console("§bEduardAPI §aMapas salvados!");
		Mine.console("§bEduardAPI §cdesativado!");
		BungeeAPI.getController().unregister();
	}

	@EventHandler
	public void onEnable(PluginEnableEvent e) {
		if (e.getPlugin() instanceof EduardPlugin) {

			EduardPlugin plugin = (EduardPlugin) e.getPlugin();
			String msg = "§b[Eduard-Dev] §f" + plugin.getName() + " §fv" + plugin.getDescription().getVersion()
					+ "§a foi ativado com sucesso.";
			if (plugin.isFree()) {
				Mine.broadcast(msg);
			} else {

				Mine.console(msg);
			}
		}
		for (Config config : Config.CONFIGS) {
			if (e.getPlugin().equals(config.getPlugin())) {
				config.reloadConfig();
			}
		}
	}

	@EventHandler
	public void marketing(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		if (p.hasPermission("eduard.plugins")) {
			for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
				if (plugin instanceof EduardPlugin) {
					if (plugin.isEnabled()) {
						p.sendMessage("§b[Eduard-Dev] §f" + plugin.getName() + " §fv"
								+ plugin.getDescription().getVersion() + "§a esta ativado.");
					} else {
						p.sendMessage("§b[Eduard-Dev] §f" + plugin.getName() + " §fv"
								+ plugin.getDescription().getVersion() + "§c esta desativado.");
					}

				}
			}
			p.sendMessage("§aCaso deseje comprar mais plugins entre em contato ou no site §bwww.eduarddev.tk");
		}

	}

	@EventHandler
	public void onDisable(PluginDisableEvent e) {
		if (e.getPlugin() instanceof EduardPlugin) {

			EduardPlugin plugin = (EduardPlugin) e.getPlugin();
			String msg = "§b[Eduard-Dev] §f" + plugin.getName() + " §fv" + plugin.getDescription().getVersion()
					+ " foi desativado com sucesso.";
			if (plugin.isFree()) {
				Mine.broadcast(msg);
			} else {
				Mine.console(msg);
			}

		}
		for (Config config : Config.CONFIGS) {
			if (e.getPlugin().equals(config.getPlugin())) {
				if (config.isAutoSave()) {
					config.saveConfig();
				}
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (Mine.OPT_AUTO_RESPAWN) {
			if (p.hasPermission("eduardAPI.autorespawn")) {
				Mine.TIME.delay(1L, new Runnable() {

					@Override
					public void run() {
						if (p.isDead()) {
							p.setFireTicks(0);
							try {
								Mine.makeRespawn(p);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				});
			}

		}
		if (Mine.OPT_NO_DEATH_MESSAGE) {
			e.setDeathMessage(null);
		}
	}

	@EventHandler
	public void onPingServer(ServerListPingEvent e) {
		Integer amount = config.getInt("custom-motd-amount");
		if (amount > -1) {
			e.setMaxPlayers(amount);
		}

		if (config.getBoolean("custom-motd")) {
			StringBuilder builder = new StringBuilder();
			for (String line : config.getMessages("motd")) {
				builder.append(line + "\n");
			}
			e.setMotd(builder.toString());
		}

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (config.getBoolean("custom-quit-message"))
			e.setQuitMessage(Mine.MSG_ON_QUIT.replace("$player", p.getName()));
		if (Mine.OPT_NO_QUIT_MESSAGE) {
			e.setQuitMessage("");
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (config.getBoolean("save-players")) {
			saveObject("Players/" + p.getName() + " " + p.getUniqueId(), p);
		}
		if (config.getBoolean("custom-join-message")) {
			e.setJoinMessage(Mine.MSG_ON_JOIN.replace("$player", p.getName()));
		}

		if (Mine.OPT_NO_JOIN_MESSAGE) {
			e.setJoinMessage(null);
			return;
		}

	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.CREATIVE) {
			if (e.getItem() == null)
				return;
			if (e.getItem().getType() == Material.WOOD_AXE) {
				Schematic mapa = Mine.getSchematic(p);
				if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
					mapa.setHigh(e.getClickedBlock().getLocation().toVector());
					p.sendMessage("§bEduardAPI §6Posi§§o 1 setada!");
				} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					mapa.setLow(e.getClickedBlock().getLocation().toVector());
					p.sendMessage("§bEduardAPI §6Posi§§o 2 setada!");
				}

			}
		}
	}

	public static void saveEnum(Class<?> value) {
		saveEnum(value, false);
	}

	public static void saveClassLikeEnum(Class<?> value) {
		try {
			Config config = new Config("DataBase/" + value.getSimpleName() + ".yml");
			for (Field field : value.getFields()) {
				if (field.getType().equals(value)) {
					Object obj = field.get(value);
					ConfigSection section = config.getSection(field.getName());
					for (Method method : obj.getClass().getDeclaredMethods()) {
						String name = method.getName();
						if ((method.getParameterCount() == 0)
								&& name.startsWith("get") | name.startsWith("is") | name.startsWith("can")) {
							method.setAccessible(true);
							Object test = method.invoke(obj);
							if (test instanceof Class)
								continue;
							section.add(method.getName(), test);
						}
					}
				}
			}
			config.saveConfig();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void saveObject(String local, Object value) {
		try {
			Config config = new Config("DataBase/" + local + ".yml");
			ConfigSection section = config.getConfig();
			for (Method method : value.getClass().getDeclaredMethods()) {
				String name = method.getName();
				if ((method.getParameterCount() == 0)
						&& name.startsWith("get") | name.startsWith("is") | name.startsWith("can")) {
					method.setAccessible(true);
					if (name.equals("getLoadedChunks")) {
						continue;
					}
					if (name.equals("getOfflinePlayers")) {
						continue;
					}
					Object test = method.invoke(value);
					if (test == null)
						continue;
					if (test instanceof Class)
						continue;
					section.set(method.getName(), test);
				}
			}
			config.saveConfig();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void saveEnum(Class<?> value, boolean perConfig) {
		try {
			if (perConfig) {

				for (Object part : value.getEnumConstants()) {

					try {
						Enum<?> obj = (Enum<?>) part;
						Config config = new Config("DataBase/" + value.getSimpleName() + "/" + obj.name() + ".yml");
						ConfigSection section = config.getConfig();
						section.set("number", obj.ordinal());
						for (Method method : obj.getClass().getDeclaredMethods()) {
							String name = method.getName();
							if ((method.getParameterCount() == 0)
									&& name.startsWith("get") | name.startsWith("is") | name.startsWith("can")) {
								method.setAccessible(true);
								Object test = method.invoke(obj);
								if (test instanceof Class)
									continue;
								section.add(method.getName(), test);
							}
						}
						config.saveConfig();
					} catch (Exception ex) {
						ex.printStackTrace();
						continue;
					}
				}

			} else {
				Config config = new Config("DataBase/" + value.getSimpleName() + ".yml");
				boolean used = false;
				for (Object part : value.getEnumConstants()) {
					try {
						Enum<?> obj = (Enum<?>) part;
						ConfigSection section = config.add(obj.name(), obj.ordinal());

						for (Method method : obj.getClass().getDeclaredMethods()) {
							String name = method.getName();
							if ((method.getParameterCount() == 0)
									&& name.startsWith("get") | name.startsWith("is") | name.startsWith("can")) {
								try {
									method.setAccessible(true);
									Object test = method.invoke(obj);
									if (test == null)
										continue;
									if (test instanceof Class)
										continue;
									section.add(method.getName(), test);
									used = true;
								} catch (Exception ex) {
									Mine.console("§bDataBase §fO metodo §c" + name + "§f causou erro!");
									ex.printStackTrace();
									continue;
								}

							}
						}

					} catch (Exception ex) {
						ex.printStackTrace();
						continue;
					}
				}
				if (!used)
					config.setIndent(0);
				config.saveConfig();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void replacers() {
		if (Mine.hasPlugin("Vault")) {
			Mine.addReplacer("$player_group", new Replacer() {

				@Override
				public Object getText(Player p) {
					return VaultAPI.getPermission().getPrimaryGroup(p);
				}
			});
			Mine.addReplacer("$player_prefix", new Replacer() {

				@Override
				public Object getText(Player p) {
					return Mine.toChatMessage(VaultAPI.getChat().getPlayerPrefix(p));
				}
			});
			Mine.addReplacer("$player_suffix", new Replacer() {

				@Override
				public Object getText(Player p) {
					return Mine.toChatMessage(Mine.toChatMessage(VaultAPI.getChat().getPlayerPrefix(p)));
				}
			});
			Mine.addReplacer("$group_prefix", new Replacer() {

				@Override
				public Object getText(Player p) {
					return Mine.toChatMessage(
							VaultAPI.getChat().getGroupPrefix("null", VaultAPI.getPermission().getPrimaryGroup(p)));
				}
			});
			Mine.addReplacer("$group_suffix", new Replacer() {

				@Override
				public Object getText(Player p) {
					return Mine.toChatMessage(
							VaultAPI.getChat().getGroupSuffix("null", VaultAPI.getPermission().getPrimaryGroup(p)));
				}
			});
			Mine.addReplacer("$player_money", new Replacer() {

				@Override
				public Object getText(Player p) {
					if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {

						DecimalFormat decimal = new DecimalFormat("#,##0.00");
						return decimal.format(VaultAPI.getEconomy().getBalance(p));

					}
					return "0.00";
				}
			});
		}

		Mine.addReplacer("$players_online", new Replacer() {

			@Override
			public Object getText(Player p) {
				return Mine.getPlayers().size();
			}
		});
		Mine.addReplacer("$player_world", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getWorld().getName();
			}
		});
		Mine.addReplacer("$player_displayname", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getDisplayName();
			}
		});
		Mine.addReplacer("$player_name", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getName();
			}
		});
		Mine.addReplacer("$player_health", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getHealth();
			}
		});
		Mine.addReplacer("$player_maxhealth", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getMaxHealth();
			}
		});
		Mine.addReplacer("$player_level", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLevel();
			}
		});
		Mine.addReplacer("$player_xp", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getTotalExperience();
			}
		});
		Mine.addReplacer("$player_kills", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getStatistic(Statistic.PLAYER_KILLS);
			}
		});
		Mine.addReplacer("$player_deaths", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getStatistic(Statistic.DEATHS);
			}
		});
		Mine.addReplacer("$player_kdr", new Replacer() {

			@Override
			public Object getText(Player p) {
				int kill = p.getStatistic(Statistic.PLAYER_KILLS);
				int death = p.getStatistic(Statistic.DEATHS);
				if (kill == 0)
					return 0;
				if (death == 0)
					return 0;
				return kill / death;
			}
		});
		Mine.addReplacer("$player_kill/death", new Replacer() {

			@Override
			public Object getText(Player p) {
				int kill = p.getStatistic(Statistic.PLAYER_KILLS);
				int death = p.getStatistic(Statistic.DEATHS);
				if (kill == 0)
					return 0;
				if (death == 0)
					return 0;
				return kill / death;
			}
		});

		Mine.addReplacer("$player_x", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLocation().getX();
			}
		});
		Mine.addReplacer("$player_y", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLocation().getY();
			}
		});
		Mine.addReplacer("$player_z", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLocation().getZ();
			}
		});
		// if (Mine.hasPlugin("mcMMO")) {
		// Mine.addReplacer("$mcmmo_level", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		// McMMOPlayer usuario = UserManager.getPlayer(p);
		// int nivel = usuario.getPowerLevel();
		// return nivel;
		// }
		// });
		//
		// }

	}

	private void saveObjects() {

		if (!new File(getDataFolder(), "DataBase").exists()) {
			saveEnum(DamageCause.class);
			saveEnum(Material.class);
			saveEnum(Effect.class);
			saveEnum(EntityType.class);
			saveEnum(TargetReason.class);
			saveEnum(Sound.class);
			saveEnum(EntityEffect.class);
			saveEnum(Environment.class);
			saveEnum(PotionType.class);
			saveClassLikeEnum(PotionEffectType.class);
		}

		if (getConfigs().getBoolean("save-worlds")) {
			for (World world : Bukkit.getWorlds()) {
				saveObject("Worlds/" + world.getName(), world);
			}
		}
		if (getConfigs().getBoolean("save-players")) {
			for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
				String name = player.getName();
				saveObject("Players/" + name + " " + player.getUniqueId(), player);
			}
		}
		saveObject("Server", Bukkit.getServer());
	}

}
