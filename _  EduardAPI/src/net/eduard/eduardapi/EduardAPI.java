package net.eduard.eduardapi;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.eduard.api.API;
import net.eduard.api.command.AplicateCommand;
import net.eduard.api.command.admin.AdminCommand;
import net.eduard.api.command.staff.CheckIpCommand;
import net.eduard.api.config.Config;
import net.eduard.api.config.ConfigSection;
import net.eduard.api.event.BungeeMessageEvent;
import net.eduard.api.event.ChatMessageEvent;
import net.eduard.api.game.Ability;
import net.eduard.api.game.ChatChannel;
import net.eduard.api.game.Drop;
import net.eduard.api.kits.Achilles;
import net.eduard.api.manager.CMD;
import net.eduard.api.manager.TimeManager;
import net.eduard.api.server.Arena;
import net.eduard.api.setup.ExtraAPI;
import net.eduard.api.setup.ExtraAPI.Replacer;
import net.eduard.api.setup.RexAPI;
import net.eduard.api.setup.SpigotAPI;
import net.eduard.api.setup.StorageAPI;
import net.eduard.api.setup.VaultAPI;
import net.eduard.eduardapi.command.ApiCommand;
import net.eduard.eduardapi.command.EnchantCommand;
import net.eduard.eduardapi.command.GotoCommand;
import net.eduard.eduardapi.command.SoundCommand;
import net.eduard.eduardapi.command.antihack.AntiHackCommand;
import net.eduard.eduardapi.command.config.ConfigCommand;
import net.eduard.eduardapi.command.lag.LagCommand;
import net.eduard.eduardapi.command.map.MapCommand;
import net.eduard.eduardapi.command.permission.PermissionCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
/**
 * // Nomes para os Meus Plugins // MasterPlugin (Minigames , Eventos, Grandes)
 * // ePlugin (Economia, Loja, Dinheiro, GUI) // EduPlugin (Geral) // EMPlugin
 * (Plugins Gratis) Posso usar _ ou - entre as Palavras ou não opcional Os
 * preços que colocar já é incluido tudo configuravel
 * 
 * Meus nicks de Jogo EduardKillerPro EduardMaster
 * 
 * Diferença entre apenas um & em vez de dois && O if quando tem apenas 1 é
 * simultaneo pra todos O if quanto tem 2 é sequencial do primeiro boolean até o
 * ultimo O mesmo vale para o | e para o ||
 * 
 * @author Eduard-PC
 *
 */
public class EduardAPI extends JavaPlugin
		implements
			Listener,
			PluginMessageListener {
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
	@Override
	public void onPluginMessageReceived(String channel, Player player,
			byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		API.callEvent(new BungeeMessageEvent(player, in));
	}
	@EventHandler
	public void event(AsyncPlayerChatEvent e) {
		// String message = ChatColor.translateAlternateColorCodes('&',
		// e.getPlayer().getName()
		// +
		// "&aoiaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
		// " + e.getMessage());
		String message = ChatColor.translateAlternateColorCodes('&',
				e.getMessage());
		String format = "§b" + e.getPlayer().getName() + ": ";
		e.setCancelled(true);
		// e.getPlayer().spigot().sendMessage(new TextComponent(
		// ChatColor.translateAlternateColorCodes('&', message)));

		TextComponent text = SpigotAPI.getTextCorrect(format);
		text.addExtra(SpigotAPI
				.getTextCorrect(ChatColor.getLastColors(format) + message));
		text.setHoverEvent(new HoverEvent(
				net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("aaaaaaaaaaaaaaaaaaaaa").create()));
		// e.getPlayer().sendRawMessage(message);

		e.getPlayer().spigot().sendMessage(text);
		// ChatColor.translateAlternateColorCodes('&', message))
		// .create());
	}

	@Override
	public void onEnable() {
		plugin = this;
		StorageAPI.init();
		config = new Config(this, "config.yml");
		messages = new Config(this, "messages.yml");
		time = new TimeManager(this);
		this.getServer().getMessenger().registerOutgoingPluginChannel(this,
				"BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this,
				"BungeeCord", this);

		StorageAPI.registerPackage(AplicateCommand.class);
		StorageAPI.registerPackage(AdminCommand.class);
		StorageAPI.registerPackage(CheckIpCommand.class);
		StorageAPI.registerPackage(Achilles.class);
		StorageAPI.registerPackage(Ability.class);
		StorageAPI.registerPackage(CMD.class);
		StorageAPI.registerPackage(Arena.class);

		ExtraAPI.resetScoreboards();
		ExtraAPI.consoleMessage("§bEduardAPI §fScoreboard resetadas!");
		replacers();
		time.timer(20, new Runnable() {

			@Override
			public void run() {
				API.updateTagsScores();
				API.updateTargets();
			}
		});
		new GotoCommand().register();
		new ApiCommand().register();
		new SoundCommand().register();
		new EnchantCommand().register();
		new PermissionCommand().register();
		new MapCommand().register();
		new ConfigCommand().register();
		new AntiHackCommand().register();
		new LagCommand().register();

		ExtraAPI.consoleMessage("§bEduardAPI §fCustom Tag e Score ativado!");
		saveObjects();
		ExtraAPI.consoleMessage("§bEduardAPI §fDataBase §agerada!");
		new Drop().register(this);
		ExtraAPI.consoleMessage("§bEduardAPI §fCustom drops ativado!");
		API.event(this);
		ExtraAPI.consoleMessage("§bEduardAPI §fBase ativado!");
		API.loadMaps();
		ExtraAPI.consoleMessage("§bEduardAPI §fMapas §acarregados!");
		config.add("chat-default", "local");

		config.add("sound-teleport", API.SOUND_TELEPORT);
		config.add("sound-error", API.SOUND_ERROR);
		config.add("sound-success", API.SOUND_SUCCESS);
		ChatChannel local = new ChatChannel("local",
				"$chat_prefix $player $chat_suffix: $message", "&e(L)&f", "",
				"l");
		ChatChannel global = new ChatChannel("global",
				"$chat_prefix $player $chat_suffix: $message", "&e(L)&f", "",
				"g");
		config.add("chats.local", local);
		config.add("chats.global", global);
		config.saveConfig();
		API.AUTO_RESPAWN = config.getBoolean("auto-respawn");
		API.NO_JOIN_MESSAGE = config.getBoolean("no-join-message");
		API.NO_QUIT_MESSAGE = config.getBoolean("no-quit-message");
		API.NO_DEATH_MESSAGE = config.getBoolean("no-death-message");
		API.CUSTOM_CHAT = config.getBoolean("custom-chat");
		API.ON_JOIN = config.message("on-join-message");
		API.ON_QUIT = config.message("on-quit-message");
		API.SERVER_TAG = config.message("server-tag");
		API.SOUND_TELEPORT = config.getSound("sound-teleport");
		API.SOUND_ERROR = config.getSound("sound-error");
		API.SOUND_SUCCESS = config.getSound("sound-success");
		API.CHAT_SPIGOT = config.getBoolean("chat-clicable");
		API.TAG_ENABLED = config.getBoolean("auto-tag");
		API.GROUPS_TAGS = config.getStringList("tags-rank");
		for (ConfigSection sec : config.getValues("chats")) {
			ChatChannel chat = (ChatChannel) sec.getValue();
			API.CHATS.put(chat.getName(), chat);
		}

		API.CHAT = API.CHATS.getOrDefault(config.getString("chat-default"),
				local);
		if (config.getBoolean("auto-rejoin")) {
			for (Player p : API.getPlayers()) {
				API.callEvent(new PlayerJoinEvent(p, null));
			}
		}

		ExtraAPI.consoleMessage("§bEduardAPI §acarregado!");
	}
	public static void sendMessage(Player player, String message,
			ChatChannel channel) {
		ChatMessageEvent chat = new ChatMessageEvent(player, channel, message);
		API.callEvent(chat);
		if (!chat.isCancelled()) {
			String permission = "chat." + chat.getChannel().getName();
			message = chat.getFormat();
			for (Entry<String, String> map : chat.getTags().entrySet()) {
				message = message.replaceAll(map.getKey(), map.getValue());
			}
			message = chat.getFormat()
					.replace("$chat_prefix", chat.getChannel().getPrefix())
					.replace("$chat_suffix", chat.getChannel().getSuffix())
					.replace("$message", chat.getMessage())
					.replace("$player", player.getName());
			if (API.CHAT_SPIGOT) {
				TextComponent text = new TextComponent(message);
				if (chat.getOnClickCommand() != null) {
					text.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND,
							chat.getOnClickCommand()));
				}
				if (chat.getOnHoverText() != null) {
					ComponentBuilder builder = new ComponentBuilder("");
					for (String line : chat.getOnHoverText()) {
						builder.append(line + "\n");
					}
					text.setHoverEvent(new HoverEvent(
							HoverEvent.Action.SHOW_TEXT, builder.create()));
				}

				for (Player p : API.getPlayers()) {
					if (p.hasPermission(permission)) {
						p.spigot().sendMessage(text);
					}
				}
			} else {
				API.broadcast(message, permission);
			}
		}
	}
	@Override
	public void onDisable() {
		API.saveMaps();
		ExtraAPI.consoleMessage("§bEduardAPI §aMapas salvados!");
		ExtraAPI.consoleMessage("§bEduardAPI §cdesativado!");
	}
	@EventHandler
	public void onEnable(PluginEnableEvent e) {
		if (e.getPlugin() instanceof EduardPlugin) {
			Plugin pl = e.getPlugin();
			ExtraAPI.sendMessage("§aPlugin " + pl.getName()
					+ " criado por §bEduard §aversão "
					+ pl.getDescription().getVersion());
			ExtraAPI.sendMessage(
					"§bCanal: §fhttps://www.youtube.com/user/EduTutoriaisHD");
			ExtraAPI.sendMessage("§bSite: §fhttp://www.eduarddev.tk/");
			ExtraAPI.consoleMessage("§aSkype: §flive:eduardkiller");

		}
		for (Config config : Config.CONFIGS) {
			if (e.getPlugin().equals(config.getPlugin())) {
				config.reloadConfig();
			}
		}
	}
	@EventHandler
	public void onDisable(PluginDisableEvent e) {
		for (Config config : Config.CONFIGS) {
			if (config.isAutoSave()) {
				config.saveConfig();
			}
		}
	}
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (API.CUSTOM_CHAT) {

			for (ChatChannel channel : API.CHATS.values()) {
				if (ExtraAPI.commandEquals(e.getMessage(),
						"/" + channel.getName())) {
					e.setCancelled(true);
					sendMessage(p, e.getMessage().replaceFirst("/", ""),
							channel);
					break;
				}
				if (ExtraAPI.commandEquals(e.getMessage(),
						"/" + channel.getCommand())) {
					e.setCancelled(true);
					sendMessage(p, e.getMessage().replaceFirst("/", ""),
							channel);
					break;
				}
			}

		}

	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (API.CUSTOM_CHAT) {
			Player player = e.getPlayer();
			e.setCancelled(true);
			sendMessage(player, e.getMessage(), API.CHAT);
		}

	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (API.AUTO_RESPAWN) {
			if (p.hasPermission("eduardapi.autorespawn")) {
				API.TIME.delay(1L, new Runnable() {

					@Override
					public void run() {
						if (p.isDead()) {
							p.setFireTicks(0);
							try {
								RexAPI.makeRespawn(p);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				});
			}

		}
		if (API.NO_DEATH_MESSAGE) {
			e.setDeathMessage("");
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
			e.setQuitMessage(API.ON_QUIT.replace("$player", p.getName()));
		if (API.NO_QUIT_MESSAGE) {
			e.setQuitMessage("");
		}
		API.removeScore(e.getPlayer());
		API.removeTag(e.getPlayer());
	}
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		API.removeScore(e.getPlayer());
		API.removeTag(e.getPlayer());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (API.SCORE_ENABLED) {
			API.setScore(e.getPlayer(), API.SCORE.copy());
		}
		if (API.TAG_ENABLED) {
			API.updateTagByRank(p);
		}

		if (config.getBoolean("save-players")) {
			saveObject("Players/" + p.getName() + " " + p.getUniqueId(), p);
		}
		if (config.getBoolean("custom-join-message")) {
			e.setJoinMessage(API.ON_JOIN.replace("$player", p.getName()));
		}

		if (API.NO_JOIN_MESSAGE) {
			e.setJoinMessage(null);
			return;
		}

	}

	public static void saveEnum(Class<?> value) {
		saveEnum(value, false);
	}
	public static void saveClassLikeEnum(Class<?> value) {
		try {
			Config config = new Config(
					"DataBase/" + value.getSimpleName() + ".yml");
			for (Field field : value.getFields()) {
				if (field.getType().equals(value)) {
					Object obj = field.get(value);
					ConfigSection section = config.getSection(field.getName());
					for (Method method : obj.getClass().getDeclaredMethods()) {
						String name = method.getName();
						if ((method.getParameterCount() == 0)
								&& name.startsWith("get")
										| name.startsWith("is")
										| name.startsWith("can")) {
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
				if ((method.getParameterCount() == 0) && name.startsWith("get")
						| name.startsWith("is") | name.startsWith("can")) {
					method.setAccessible(true);
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
						Config config = new Config(
								"DataBase/" + value.getSimpleName() + "/"
										+ obj.name() + ".yml");
						ConfigSection section = config.getConfig();
						section.set("number", obj.ordinal());
						for (Method method : obj.getClass()
								.getDeclaredMethods()) {
							String name = method.getName();
							if ((method.getParameterCount() == 0)
									&& name.startsWith("get")
											| name.startsWith("is")
											| name.startsWith("can")) {
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
				Config config = new Config(
						"DataBase/" + value.getSimpleName() + ".yml");
				boolean used = false;
				for (Object part : value.getEnumConstants()) {
					try {
						Enum<?> obj = (Enum<?>) part;
						ConfigSection section = config.add(obj.name(),
								obj.ordinal());

						for (Method method : obj.getClass()
								.getDeclaredMethods()) {
							String name = method.getName();
							if ((method.getParameterCount() == 0)
									&& name.startsWith("get")
											| name.startsWith("is")
											| name.startsWith("can")) {
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
									ExtraAPI.consoleMessage(
											"§bDataBase §fO metodo §c" + name
													+ "§f causou erro!");
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
		ExtraAPI.newWrapper("<br>");

		ExtraAPI.newWrapper("\\n");
		ExtraAPI.newWrapper("$br");
		ExtraAPI.addReplacer("$player_group", new Replacer() {

			@Override
			public Object getText(Player p) {
				return VaultAPI.getPermission().getPrimaryGroup(p);
			}
		});
		ExtraAPI.addReplacer("$player_prefix", new Replacer() {

			@Override
			public Object getText(Player p) {
				return VaultAPI.getChat().getPlayerPrefix(p);
			}
		});
		ExtraAPI.addReplacer("$player_suffix", new Replacer() {

			@Override
			public Object getText(Player p) {
				return VaultAPI.getChat().getPlayerPrefix(p);
			}
		});
		ExtraAPI.addReplacer("$group_prefix", new Replacer() {

			@Override
			public Object getText(Player p) {
				return VaultAPI.getChat().getGroupPrefix("null",
						VaultAPI.getPermission().getPrimaryGroup(p));
			}
		});
		ExtraAPI.addReplacer("$group_suffix", new Replacer() {

			@Override
			public Object getText(Player p) {
				return VaultAPI.getChat().getGroupSuffix("null",
						VaultAPI.getPermission().getPrimaryGroup(p));
			}
		});
		ExtraAPI.addReplacer("$players_online", new Replacer() {

			@Override
			public Object getText(Player p) {
				return API.getPlayers().size();
			}
		});
		ExtraAPI.addReplacer("$player_world", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getWorld().getName();
			}
		});
		ExtraAPI.addReplacer("$player_displayname", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getDisplayName();
			}
		});
		ExtraAPI.addReplacer("$player_name", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getName();
			}
		});
		ExtraAPI.addReplacer("$player_health", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getHealth();
			}
		});
		ExtraAPI.addReplacer("$player_maxhealth", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getMaxHealth();
			}
		});
		ExtraAPI.addReplacer("$player_kills", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getStatistic(Statistic.PLAYER_KILLS);
			}
		});
		ExtraAPI.addReplacer("$player_deaths", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getStatistic(Statistic.DEATHS);
			}
		});
		ExtraAPI.addReplacer("$player_kdr", new Replacer() {

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
		ExtraAPI.addReplacer("$player_kill/death", new Replacer() {

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

		ExtraAPI.addReplacer("$player_money", new Replacer() {

			@Override
			public Object getText(Player p) {
				if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {

					DecimalFormat decimal = new DecimalFormat("#,##0.00");
					return decimal.format(VaultAPI.getEconomy().getBalance(p));

				}
				return "0.00";
			}
		});
		ExtraAPI.addReplacer("$player_x", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLocation().getX();
			}
		});
		ExtraAPI.addReplacer("$player_y", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLocation().getY();
			}
		});
		ExtraAPI.addReplacer("$player_z", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLocation().getZ();
			}
		});

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
				saveObject("Players/" + name + " " + player.getUniqueId(),
						player);
			}
		}
		saveObject("Server", Bukkit.getServer());
	}

}
