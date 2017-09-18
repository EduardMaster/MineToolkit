package net.eduard.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.eduard.api.config.Config;
import net.eduard.api.config.ConfigSection;
import net.eduard.api.event.PlayerTargetEvent;
import net.eduard.api.event.ScoreUpdateEvent;
import net.eduard.api.event.TagUpdateEvent;
import net.eduard.api.game.ChatChannel;
import net.eduard.api.game.Sounds;
import net.eduard.api.game.Tag;
import net.eduard.api.manager.CMD;
import net.eduard.api.manager.TimeManager;
import net.eduard.api.server.Arena;
import net.eduard.api.setup.ExtraAPI;
import net.eduard.api.setup.GameAPI;
import net.eduard.api.setup.ItemAPI;
import net.eduard.api.setup.RefAPI;
import net.eduard.api.setup.ScoreAPI.DisplayBoard;
import net.eduard.api.setup.ScoreAPI.FakeOfflinePlayer;
import net.eduard.api.setup.StorageAPI;
import net.eduard.api.setup.VaultAPI;
import net.eduard.api.setup.WorldAPI;
import net.eduard.api.setup.WorldAPI.EmptyWorldGenerator;

/**
 * API principal da EduardAPI contendo muitos codigos bons e utilitarios Boolean
 * = Teste
 * 
 * @author Eduard
 *
 */
@SuppressWarnings("unchecked")
public class API {
	/**
	 * Mapa de Arenas dos Jogadores
	 */
	public static Map<Player, Arena> MAPS = new HashMap<>();
	private final static API INSTANCE = new API();
	/**
	 * Mapa de Arenas registradas
	 */
	public static Map<String, Arena> SCHEMATICS = new HashMap<>();
	/**
	 * Mapa das posições 1 dos jogadores
	 */
	public static Map<Player, Location> POSITION1 = new HashMap<>();
	/**
	 * Mapa das posições 2 dos jogadores
	 */
	public static Map<Player, Location> POSITION2 = new HashMap<>();
	/**
	 * Som do rosnar do gato
	 */
	public static final Sounds ROSNAR = Sounds.create(Sound.CAT_PURR);

	/**
	 * Mapa contendo todos os Canais de Chat
	 */
	public static Map<String, ChatChannel> CHATS = new HashMap<>();
	/**
	 * Mapa contendo todas Scoreboards dos Jogadores
	 */
	public static Map<Player, DisplayBoard> SCORES = new HashMap<>();
	public static List<String> GROUPS_TAGS = new ArrayList<>();
	public static Map<Player, Tag> TAGS = new HashMap<>();

	public static Config MAPS_CONFIG;
	/**
	 * Ligar Sistema de Scoreboard
	 */
	public static boolean SCORE_ENABLED = false;
	/**
	 * Ligar Sistema de Tag
	 */
	public static boolean TAG_ENABLED = false;
	/**
	 * Score base
	 */
	public static DisplayBoard SCORE;
	/**
	 * Chat local
	 */
	public static ChatChannel CHAT;
	/**
	 * Ligar Sistema de Chat
	 */
	public static boolean CUSTOM_CHAT = false;
	/**
	 * Mensagem de quando console digita um comando
	 */
	public static String ONLY_PLAYER = "§cApenas jogadores pode fazer este comando!";
	/**
	 * Mensagem de quando o Mundo é invalido
	 */
	public static String WORLD_NOT_EXISTS = "§cEste mundo $world não existe!";
	/**
	 * Mensagem de quando o jogador é invalido
	 */
	public static String PLAYER_NOT_EXISTS = "§cEste jogador $player não existe!";
	/**
	 * Mensagem de quando plugin é invalido
	 */
	public static String PLUGIN_NOT_EXITS = "§cEste plugin $plugin não exite!";
	/**
	 * Mensagem de quando não tem permissão
	 */
	public static String NO_PERMISSION = "§cVoce não tem permissão para usar este comando!";
	/**
	 * Mensagem de quando Entrar no Servidor
	 */
	public static String ON_JOIN = "§6O jogador $player entrou no Jogo!";
	/**
	 * Mensagem de quando Sair do Servidor
	 */
	public static String ON_QUIT = "§6O jogador $player saiu no Jogo!";
	/**
	 * Prefixo de Ajuda dos Comandos
	 */
	public static String USAGE = "§FDigite: §c";
	/**
	 * Tag do Servidor
	 */
	public static String SERVER_TAG = "§b§lEduardAPI ";
	/**
	 * Lista de Comandos para efeito Positivo
	 */
	public static List<String> COMMANDS_ON = new ArrayList<>(
			Arrays.asList("on", "ativar"));
	/**
	 * Lista de Comandos para efeito Negativo
	 */
	public static List<String> COMMANDS_OFF = new ArrayList<>(
			Arrays.asList("off", "desativar"));
	/**
	 * Som para o Teleporte
	 */
	public static Sounds SOUND_TELEPORT = Sounds
			.create(Sound.ENDERMAN_TELEPORT);
	/**
	 * Som para algum sucesso
	 */
	public static Sounds SOUND_SUCCESS = Sounds.create(Sound.LEVEL_UP);
	/**
	 * Som para algum erro
	 */
	public static Sounds SOUND_ERROR = Sounds.create(Sound.NOTE_BASS_DRUM);
	/**
	 * Desativar mensagem de morte
	 */
	public static boolean NO_DEATH_MESSAGE = true;
	/**
	 * Desativar mensagem de entrada
	 */
	public static boolean NO_JOIN_MESSAGE = true;
	/**
	 * Desativar mensagem de saida
	 */
	public static boolean NO_QUIT_MESSAGE = true;

	/**
	 * Velocidade minima de corrida
	 */
	public static double MIN_WALK_SPEED = 0.2;
	/**
	 * Velocidade minima de voo
	 */
	public static double MIN_FLY_SPEED = 0.1;
	/**
	 * Ligar sistema de Respawn Automatico
	 */
	public static boolean AUTO_RESPAWN = true;
	/**
	 * Ligar sistema de Chat para o Spigot
	 */
	public static boolean CHAT_SPIGOT = false;

	/**
	 * Controlador de Tempo da API
	 */
	public static TimeManager TIME;
	/**
	 * Plugin da API
	 */
	public static JavaPlugin PLUGIN;
	/**
	 * Mapa dos Comandos do Servidor
	 */
	private static Map<String, Command> commands = new HashMap<>();

	/**
	 * Ligando algumas coisas
	 */
	static {
		try {
			PLUGIN = JavaPlugin.getProvidingPlugin(API.class);
			TIME = new TimeManager(PLUGIN);
			MAPS_CONFIG = new Config(PLUGIN,"maps.yml");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Object map = RefAPI.getValue(Bukkit.getServer().getPluginManager(),
					"commandMap");

			commands = (Map<String, Command>) RefAPI.getValue(map,
					"knownCommands");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	public static void setScore(Player player, DisplayBoard score) {
		SCORES.put(player, score);
		score.apply(player);
	}
	public static DisplayBoard getScore(Player player) {
		return SCORES.get(player);

	}

	public static Tag getTag(Player player) {
		return TAGS.get(player);
	}

	public static void resetTag(Player player) {
		setTag(player, "");
	}

	public static void setTag(Player player, String prefix) {
		setTag(player, prefix, "");
	}

	public static void setTag(Player player, String prefix, String suffix) {
		setTag(player, new Tag(prefix, suffix));
	}

	public static void setTag(Player player, Tag tag) {
		TAGS.put(player, tag);

	}

	public static void updateTagsScores() {
		if (SCORE_ENABLED) {

			try {
				for (Entry<Player, DisplayBoard> map : SCORES.entrySet()) {
					DisplayBoard score = map.getValue();
					Player player = map.getKey();
					ScoreUpdateEvent event = new ScoreUpdateEvent(player, score);
					if (!event.isCancelled()) {
						score.update(player);	
					}
				}
			} catch (Exception e) {
				Bukkit.getLogger().info(
						"Falha ao dar update ocorreu uma Troca de Scoreboard no meio do FOR");
			}
		}
		if (TAG_ENABLED) {
			Scoreboard main = Bukkit.getScoreboardManager().getMainScoreboard();

			for (Player p : API.getPlayers()) {
				Scoreboard score = p.getScoreboard();
				if (score == null) {
					p.setScoreboard(main);
					score = main;
					continue;
				}
				updateTags(score);

			}
			updateTags(main);
		}
	}
	public static void updateTargets() {
		for (Player p : API.getPlayers()) {

			PlayerTargetEvent event = new PlayerTargetEvent(p,
					GameAPI.getTarget(p,
							GameAPI.getPlayerAtRange(p.getLocation(), 100)));
			API.callEvent(event);

		}
	}
	@SuppressWarnings("deprecation")
	public static void updateTags(Scoreboard score) {
		for (Entry<Player, Tag> map : TAGS.entrySet()) {
			Tag tag = map.getValue();
			Player player = map.getKey();
			if (player == null)
				continue;
			String name = ExtraAPI.getText(tag.getRank() + player.getName());
			Team team = score.getTeam(name);
			if (team == null)
				team = score.registerNewTeam(name);
			TagUpdateEvent event = new TagUpdateEvent(tag,player);
			API.callEvent(event);
			if (!event.isCancelled())continue;
			team.setPrefix(
					ExtraAPI.toText(ExtraAPI.toChatMessage(tag.getPrefix())));
			team.setSuffix(
					ExtraAPI.toText(ExtraAPI.toChatMessage(tag.getSuffix())));
			if (!team.hasPlayer(player))
				team.addPlayer(player);

		}
	}
	public static void updateScoreboard(Player player) {
		getScore(player).update(player);
	}
	public static void updateTagByRank(Player player) {
		String group = VaultAPI.getPermission().getPrimaryGroup(player);
		String prefix = VaultAPI.getChat().getGroupPrefix("null", group);
		String suffix = VaultAPI.getChat().getGroupSuffix("null", group);
		int id = 0;
		for (String rank : GROUPS_TAGS) {
			if (rank.equalsIgnoreCase(group)) {
				Tag tag = new Tag(prefix, suffix);
				tag.setName(player.getName());
				tag.setRank(id);
				setTag(player, tag);
				break;
			}
			id++;
		}

	}

	public static void removeScore(Player player) {
		player.setScoreboard(API.getMainScoreboard());
		TAGS.remove(player);
	}
	public static void removeTag(Player player) {
		TAGS.remove(player);
	}

	public static Map<String, Command> getCommands() {
		return commands;
	}

	public static PluginCommand command(String commandName,
			CommandExecutor command) {
		PluginCommand cmd = Bukkit.getPluginCommand(commandName);
		cmd.setExecutor(command);
		cmd.setPermissionMessage(
				API.NO_PERMISSION.replace("$permission", cmd.getPermission()));
		return cmd;
	}

	public static PluginCommand command(String commandName,
			CommandExecutor command, String permission) {

		PluginCommand cmd = Bukkit.getPluginCommand(commandName);
		cmd.setExecutor(command);
		cmd.setPermission(permission);
		cmd.setPermissionMessage(
				API.NO_PERMISSION.replace("$permission", cmd.getPermission()));
		return cmd;
	}

	public static boolean isIpProxy(String ip) {
		return ExtraAPI.isIpProxy(ip);
	}

	public static BukkitTask delay(Plugin plugin, long ticks, Runnable run) {
		return ExtraAPI.delay(plugin, ticks, run);
	}
	public static BukkitTask delays(Plugin plugin, long ticks, Runnable run) {
		return ExtraAPI.delays(plugin, ticks, run);
	}

	public static void event(Listener event) {

		event(event, getAPI());
	}

	public static void event(Listener event, Plugin plugin) {
		ExtraAPI.event(event, plugin);
	}

	public static boolean existsPlayer(CommandSender sender, String player) {

		Player p = Bukkit.getPlayer(player);
		if (p == null) {
			sender.sendMessage(
					API.PLAYER_NOT_EXISTS.replace("$player", player));
			return false;
		}
		return true;
	}
	public static boolean existsPlugin(CommandSender sender, String plugin) {

		Plugin p = getPlugin(plugin);
		if (p == null) {
			sender.sendMessage(API.PLUGIN_NOT_EXITS.replace("$plugin", plugin));
			return false;
		}
		return true;
	}
	public static Plugin getPlugin(String plugin) {
		return Bukkit.getPluginManager().getPlugin(plugin);
	}
	public static boolean existsWorld(CommandSender sender, String name) {
		World world = Bukkit.getWorld(name);
		if (world == null) {
			sender.sendMessage(API.WORLD_NOT_EXISTS.replace("$world", name));
			return false;
		}
		return true;
	}

	public static JavaPlugin getAPI() {
		if (hasAPI()) {
			PLUGIN = (JavaPlugin) Bukkit.getPluginManager()
					.getPlugin("EduardAPI");
		}
		return PLUGIN;
	}

	public static boolean hasAPI() {

		return hasPlugin("EduardAPI");
	}

	public static boolean hasPlugin(String plugin) {
		return ExtraAPI.hasPlugin(plugin);
	}

	public static boolean getChance(double chance) {

		return ExtraAPI.getChance(chance);
	}
	public static Player getPlayer(String name) {
		return Bukkit.getPlayerExact(name);
	}
	public static World getWorld(String name) {
		return Bukkit.getWorld(name);
	}

	public static List<Player> getPlayers() {
		return GameAPI.getPlayers();
	}

	public static boolean hasPerm(CommandSender sender, String permission) {
		if (!sender.hasPermission(permission)) {
			sender.sendMessage(
					API.NO_PERMISSION.replace("$permission", permission));
			return false;
		}
		return true;

	}
	@SafeVarargs
	public static void commands(ConfigSection section, CMD... cmds) {
		for (CMD cmd : cmds) {
			try {

				String name = cmd.getName();
				if (section != null) {
					if (section.contains(name)) {
						cmd = (CMD) section.get(name);

					}

				}
				cmd.register();
				if (section != null) {
					section.add(name, cmd);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static World newEmptyWorld(String worldName) {
		World world = Bukkit.createWorld(
				new WorldCreator(worldName).generateStructures(false)
						.generator(new EmptyWorldGenerator()));
		world.getBlockAt(100, 100, 100).setType(Material.GLASS);
		world.setSpawnLocation(100, 101, 100);
		return world;
	}

	public static ItemStack newItem(Material material, String name) {
		ItemStack item = new ItemStack(material);
		ItemAPI.setName(item, name);
		return item;
	}

	public static ItemStack newItem(Material material, String name,
			int amount) {
		return newItem(material, name, amount, 0);
	}

	public static ItemStack newItem(Material material, String name, int amount,
			int data, String... lore) {

		ItemStack item = newItem(material, name);
		ItemAPI.setLore(item, lore);
		item.setAmount(amount);
		item.setDurability((short) data);
		return item;
	}

	public static ItemStack newItem(String name, Material material) {
		ItemStack item = new ItemStack(material);
		ItemAPI.setName(item, name);
		return item;
	}

	public static ItemStack newItem(String name, Material material, int data) {
		return newItem(material, name, 1, data);
	}

	public static ItemStack newItem(String name, Material material, int amount,
			int data, String... lore) {
		return newItem(material, name, amount, data, lore);
	}

	public static Scoreboard newScoreboard() {
		return Bukkit.getScoreboardManager().getNewScoreboard();

	}

	public static World newWorld(String world, Environment environment,
			WorldType worldType) {
		return new WorldCreator(world).environment(environment).type(worldType)
				.createWorld();
	}

	public static boolean noConsole(CommandSender sender) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(API.ONLY_PLAYER);
			return false;
		}
		return true;
	}

	public static boolean onlyPlayer(CommandSender sender) {
		return noConsole(sender);
	}

	public static void removeAliaseFromCommand(PluginCommand cmd,
			String aliase) {
		String cmdName = cmd.getName().toLowerCase();
		if (getCommands().containsKey(aliase)) {
			getCommands().remove(aliase);
			console("§bCommandAPI §fremovendo aliase §a" + aliase
					+ "§f do comando §b" + cmdName);
		} else {
			console("§bCommandAPI §fnao foi encontrado a aliase §a" + aliase
					+ "§f do comando §b" + cmdName);
		}
	}

	public static void removeCommand(String name) {
		if (getCommands().containsKey(name)) {
			PluginCommand cmd = Bukkit.getPluginCommand(name);
			String pluginName = cmd.getPlugin().getName();
			String cmdName = cmd.getName();
			for (String aliase : cmd.getAliases()) {
				removeAliaseFromCommand(cmd, aliase);
				removeAliaseFromCommand(cmd,
						pluginName.toLowerCase() + ":" + aliase);
			}
			try {
				getCommands().remove(cmd.getName());
			} catch (Exception e) {
			}
			console("§bCommandAPI §fremovendo o comando §a" + cmdName
					+ "§f do Plugin §b" + pluginName);
		} else {
			console("§bCommandAPI §fnao foi encontrado a commando §a" + name);
		}

	}


	public static void addPermission(Player p, String permission) {
		p.addAttachment(API.PLUGIN, permission, true);
	}
	public static void removePermission(Player p, String permission) {
		p.addAttachment(API.PLUGIN, permission, false);
	}

	@SuppressWarnings("deprecation")
	public static Scoreboard applyScoreboard(Player player, String title,
			String... lines) {
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("score", "dummy");
		obj.setDisplayName(title);
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		int id = 15;
		for (String line : lines) {
			String empty = ChatColor.values()[id - 1].toString();
			obj.getScore(new FakeOfflinePlayer(line.isEmpty() ? empty : line))
					.setScore(id);;
			id--;
			if (id == 0) {
				break;
			}
		}

		player.setScoreboard(board);
		return board;
	}
	public static Scoreboard newScoreboard(Player player, String title,
			String... lines) {
		return applyScoreboard(player, title, lines);
	}

	public static int getRandomInt(int minValue, int maxValue) {
		return ExtraAPI.getRandomInt(minValue, maxValue);
	}
	public static void chat(CommandSender sender, Object... objects) {
		sender.sendMessage(API.SERVER_TAG + ExtraAPI.getMessage(objects));
	}
	public static void all(Object... objects) {

		broadcast(objects);
		console(objects);
	}

	public static void broadcast(Object... objects) {

		for (Player p : API.getPlayers()) {
			chat(p, objects);
		}
	}

	public static void broadcast(String message, String permision) {
		for (Player p : API.getPlayers()) {
			if (p.hasPermission(permision)) {
				chat(p, message);
			}
		}
	}

	public static void console(Object... objects) {

		chat(Bukkit.getConsoleSender(), objects);
	}
	public static void send(Collection<Player> players, Object... objects) {
		for (Player player : players) {
			chat(player, objects);
		}

	}

	public static long getNow() {
		return ExtraAPI.getNow();
	}

	public static void callEvent(Event event) {
		ExtraAPI.callEvent(event);
	}

	public static Scoreboard getMainScoreboard() {
		return ExtraAPI.getMainScoreboard();
	}

	public static BukkitTask timer(Plugin plugin, long ticks, Runnable run) {
		return ExtraAPI.timer(plugin, ticks, run);
	}

	public static Inventory newInventory(String title, int size) {
		return ItemAPI.newInventory(title, size);
	}
	public static Inventory createInventory(String title, int size) {
		return ItemAPI.newInventory(title, size);
	}
	public static Location getSpawn() {
		return WorldAPI.getSpawn();
	}
	public static void runCommand(String command) {
		ExtraAPI.runCommand(command);
	}
	
	public static void loadMaps() {
		if (MAPS_CONFIG.contains("MAPS")) {

			try {
				StorageAPI.restoreField(INSTANCE,
						MAPS_CONFIG.getSection("MAPS").toMap(),
						RefAPI.getField(INSTANCE, "MAPS"));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	public static void saveMaps() {

		try {
			Object value = StorageAPI.storeField(INSTANCE,
					RefAPI.getField(INSTANCE, "MAPS"));
			MAPS_CONFIG.set("MAPS", value);

		} catch (Exception e) {
			e.printStackTrace();
		}
		MAPS_CONFIG.saveConfig();
	}

}
