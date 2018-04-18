package net.eduard.api.setup;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import net.eduard.api.config.Config;
import net.eduard.api.config.ConfigSection;
import net.eduard.api.server.event.PlayerTargetEvent;
import net.eduard.api.setup.StorageAPI.Storable;
import net.eduard.api.setup.StorageAPI.Variable;
import net.eduard.api.setup.game.FakePlayer;
import net.eduard.api.setup.game.Schematic;
import net.eduard.api.setup.game.Sounds;
import net.eduard.api.setup.manager.CommandManager;
import net.eduard.api.setup.manager.PlayerManager;
import net.eduard.api.setup.manager.TimeManager;

/**
 * API principal da Lib contendo muitos codigos bons e utilitarios
 * 
 * @author Eduard
 * @version 3.0
 */
public final class Mine {

	
	private static PlayerManager playerManager;
	

	public static List<Class<?>> getClasses(JavaPlugin plugin, String pkgname) {
		List<Class<?>> lista = new ArrayList<>();
		try {
			Method fileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
			fileMethod.setAccessible(true);
			File file = (File) fileMethod.invoke(plugin);
			return Extra.getClasses(new JarFile(file), pkgname);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lista;
	}
	public static List<Class<?>> getClasses(JavaPlugin plugin, Class<?> clazzName) {
		return getClasses(plugin, clazzName.getPackage().getName());
	}
	

	/***
	 * ----------------------------------------------------------
	 * 
	 */

	

	public static List<Class<?>> getListeners(JavaPlugin plugin,String packname) {

		return getClasses(plugin, packname).stream().filter(classe -> classe != null)
				.filter(classe -> Listener.class.isAssignableFrom(classe)).collect(Collectors.toList());
	}
	
	
	/*  
	 * 
	 * 
	 * 
	 * 
	 */

	/**
	 * Mapa de Arenas registradas
	 */
	public static Map<String, Schematic> SCHEMATICS = new HashMap<>();
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
	public static final Sounds ROSNAR = Sounds.create("CAT_PURR");

	public static Config MAPS_CONFIG;

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
	 * Lista de Comandos para efeito Positivo
	 */
	public static List<String> COMMANDS_ON = new ArrayList<>(Arrays.asList("on", "ativar"));
	/**
	 * Lista de Comandos para efeito Negativo
	 */
	public static List<String> COMMANDS_OFF = new ArrayList<>(Arrays.asList("off", "desativar"));
	/**
	 * Som para o Teleporte
	 */
	public static Sounds SOUND_TELEPORT = Sounds.create("ENDERMAN_TELEPORT");
	/**
	 * Som para algum sucesso
	 */
	public static Sounds SOUND_SUCCESS = Sounds.create("LEVEL_UP");
	/**
	 * Som para algum erro
	 */
	public static Sounds SOUND_ERROR = Sounds.create("NOTE_BASS_DRUM");
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
	 * Controlador de Tempo da Mine
	 */
	public static TimeManager TIME;

	/**
	 * Mapa dos Comandos do Servidor
	 */
	// private static Map<String, Command> commands = new HashMap<>();

	public static boolean hasPos1(Player p) {
		return POSITION1.get(p) != null;
	}

	public static boolean hasPos2(Player p) {
		return POSITION2.get(p) != null;
	}

	public static Schematic getSchematic(Player player) {
		// return new Schematic(player.getLocation(), POSITION1.get(player),
		// POSITION2.get(player));
		return null;
	}

	/**
	 * Ligando algumas coisas
	 */
	static {
		try {
			TIME = new TimeManager(getMainPlugin());
			MAPS_CONFIG = new Config(getMainPlugin(), "maps.yml");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void updateTargets() {
		for (Player p : getPlayers()) {

			PlayerTargetEvent event = new PlayerTargetEvent(p,
					Mine.getTarget(p, Mine.getPlayerAtRange(p.getLocation(), 100)));
			Mine.callEvent(event);

		}
	}

	public static void createCommand(JavaPlugin plugin, Command... cmds) {
		try {
			Class<?> serverClass = Extra.get(Bukkit.getServer());
			Field field = serverClass.getDeclaredField("commandMap");
			field.setAccessible(true);
			CommandMap map = (CommandMap) field.get(Bukkit.getServer());
			for (Command cmd : cmds) {
				map.register(plugin.getName(), cmd);
			}
			// }
		} catch (Exception ex) {
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Command> getCommands() {
		try {
			Object map = Extra.getValue(Bukkit.getServer().getPluginManager(), "commandMap");

			return (Map<String, Command>) Extra.getValue(map, "knownCommands");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static PluginCommand command(String commandName, CommandExecutor command) {
		PluginCommand cmd = Bukkit.getPluginCommand(commandName);
		cmd.setExecutor(command);
		cmd.setPermissionMessage(Mine.NO_PERMISSION.replace("$permission", cmd.getPermission()));
		return cmd;
	}

	public static PluginCommand command(String commandName, CommandExecutor command, String permission) {

		PluginCommand cmd = Bukkit.getPluginCommand(commandName);
		cmd.setExecutor(command);
		cmd.setPermission(permission);
		cmd.setPermissionMessage(Mine.NO_PERMISSION.replace("$permission", cmd.getPermission()));
		return cmd;
	}

	public static boolean existsPlayer(CommandSender sender, String player) {

		Player p = Bukkit.getPlayer(player);
		if (p == null) {
			sender.sendMessage(Mine.PLAYER_NOT_EXISTS.replace("$player", player));
			return false;
		}
		return true;
	}

	public static boolean existsPlugin(CommandSender sender, String plugin) {

		Plugin p = getPlugin(plugin);
		if (p == null) {
			sender.sendMessage(Mine.PLUGIN_NOT_EXITS.replace("$plugin", plugin));
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
			sender.sendMessage(Mine.WORLD_NOT_EXISTS.replace("$world", name));
			return false;
		}
		return true;
	}

	public static boolean hasPerm(CommandSender sender, String permission) {
		if (!sender.hasPermission(permission)) {
			sender.sendMessage(Mine.NO_PERMISSION.replace("$permission", permission));
			return false;
		}
		return true;

	}

	@SafeVarargs
	public static void commands(ConfigSection section, CommandManager... cmds) {
		for (CommandManager cmd : cmds) {
			try {

				String name = cmd.getName();
				if (section != null) {
					if (section.contains(name)) {
						cmd = (CommandManager) section.get(name);

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

	public static boolean noConsole(CommandSender sender) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(Mine.ONLY_PLAYER);
			return false;
		}
		return true;
	}

	public static boolean onlyPlayer(CommandSender sender) {
		return noConsole(sender);
	}

	public static void console(String message) {
		Bukkit.getConsoleSender().sendMessage(message);
	}

	public static void broadcast(String message) {
		Bukkit.broadcastMessage(message);
	}

	public static void removeAliaseFromCommand(PluginCommand cmd, String aliase) {
		String cmdName = cmd.getName().toLowerCase();
		if (getCommands().containsKey(aliase)) {
			getCommands().remove(aliase);
			console("§bCommandAPI §fremovendo aliase §a" + aliase + "§f do comando §b" + cmdName);
		} else {
			console("§bCommandAPI §fnao foi encontrado a aliase §a" + aliase + "§f do comando §b" + cmdName);
		}
	}

	public static void removeCommand(String name) {
		if (getCommands().containsKey(name)) {
			PluginCommand cmd = Bukkit.getPluginCommand(name);
			String pluginName = cmd.getPlugin().getName();
			String cmdName = cmd.getName();
			for (String aliase : cmd.getAliases()) {
				removeAliaseFromCommand(cmd, aliase);
				removeAliaseFromCommand(cmd, pluginName.toLowerCase() + ":" + aliase);
			}
			try {
				getCommands().remove(cmd.getName());
			} catch (Exception e) {
			}
			console("§bCommandAPI §fremovendo o comando §a" + cmdName + "§f do Plugin §b" + pluginName);
		} else {
			console("§bCommandAPI §fnao foi encontrado a commando §a" + name);
		}

	}

	public static void addPermission(Player p, String permission) {
		p.addAttachment(getMainPlugin(), permission, true);
	}

	public static void removePermission(Player p, String permission) {
		p.addAttachment(getMainPlugin(), permission, false);
	}

	public static Scoreboard newScoreboard(Player player, String title, String... lines) {
		return applyScoreboard(player, title, lines);
	}

	public static JavaPlugin getMainPlugin() {
		return JavaPlugin.getProvidingPlugin(Mine.class);
	}

	public static void chat(CommandSender sender, String message) {
		Mine.send(sender, message);

	}
	@SuppressWarnings("deprecation")
	public static Scoreboard applyScoreboard(Player player, String title, String... lines) {
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("score", "dummy");
		obj.setDisplayName(title);
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		int id = 15;
		for (String line : lines) {
			String empty = ChatColor.values()[id - 1].toString();
			obj.getScore(new FakePlayer(line.isEmpty() ? empty : line)).setScore(id);
			;
			id--;
			if (id == 0) {
				break;
			}
		}

		player.setScoreboard(board);
		return board;
	}
	/*
	 * 
	 * 
	 * 
	 * 
	 */

	public static boolean equals(Chunk chunk1, Chunk chunk2) {
		return chunk1.getX() == chunk2.getX() && chunk1.getZ() == chunk2.getZ();
	}

	/*
	 * Abrir um Menu Gui paginado
	 * 
	 * 
	 */
	public static void openGui(Player player, List<ItemStack> lista, int pagina, int divisao, String nome, int linhas,
			int index, int voltarSlot, int avancarSlot) {
		int quantidadeDePaginas = lista.size() / divisao;
		int inicial = (pagina - 1) * divisao;
		if (inicial > lista.size()) {
			return;
		}
		List<ItemStack> subLista = lista.subList(inicial, lista.size());
		Inventory inv = Bukkit.createInventory(null, linhas * 9, nome);
		int current = 1;
		for (ItemStack item : subLista) {
			while (Mine.isColumn(index, 1) || Mine.isColumn(index, 9)) {
				index++;
			}
			inv.setItem(index, item);
			current++;
			index++;
			if (current == divisao) {
				break;
			}
		}
		if (pagina > 1) {
			inv.setItem(voltarSlot, Mine.newItem(Material.ARROW, "§aVoltar"));
		}
		if (pagina < quantidadeDePaginas) {
			inv.setItem(avancarSlot, Mine.newItem(Material.ARROW, "§aAvançar"));
		}
		player.openInventory(inv);

	}

	public static List<Chunk> getChunks(Location location, int amount, int size) {
		List<Chunk> lista = new ArrayList<>();
		Chunk chunkInicial = location.getChunk();
		World world = location.getWorld();
		int xInicial = chunkInicial.getX();
		int zInicial = chunkInicial.getZ();

		for (int x = xInicial - size; x < xInicial + size; x++) {
			for (int z = zInicial - size; z < zInicial + size; z++) {
				Chunk chunk = world.getChunkAt(x, z);
				lista.add(chunk);
				if (lista.size() == amount) {
					break;
				}
			}
		}

		return lista;
	}

	public static void registerDefaults() {
		StorageAPI.register(Item.class, new Item());

		StorageAPI.register(MaterialData.class, new Variable() {

			@SuppressWarnings("deprecation")
			@Override
			public Object save(Object object) {
				if (object instanceof MaterialData) {
					MaterialData materialData = (MaterialData) object;
					return materialData.getItemTypeId() + ":" + materialData.getData();

				}
				return null;
			}

			@SuppressWarnings("deprecation")
			@Override
			public Object get(Object object) {
				if (object instanceof String) {
					String string = (String) object;
					try {
						String[] split = string.split(":");
						return new MaterialData(Material.getMaterial(Mine.toInt(split[0])), Mine.toByte(split[1]));
					} catch (Exception e) {
						new MaterialData(Material.STONE);
					}

				}
				return null;
			}
		});
		StorageAPI.register(Vector.class, new Storable() {

			@Override
			public Object restore(Map<String, Object> map) {
				return new Vector();
			}

			@Override
			public void store(Map<String, Object> map, Object object) {

			}

			@Override
			public String alias() {
				return "Vector";
			}
		});
		StorageAPI.register(Enchantment.class, new Variable() {

			@SuppressWarnings("deprecation")
			@Override
			public Object save(Object object) {
				if (object instanceof Enchantment) {
					Enchantment enchantment = (Enchantment) object;
					return enchantment.getId();

				}
				return null;
			}

			@SuppressWarnings("deprecation")
			@Override
			public Object get(Object object) {
				if (object instanceof String) {
					String string = (String) object;
					return Enchantment.getById(Extra.toInt(string));

				}
				return null;
			}
		});
		StorageAPI.register(PotionEffectType.class, new Variable() {

			@Override
			public Object get(Object object) {
				if (object instanceof String) {

					String string = (String) object;
					String[] split = string.split(";");
					return PotionEffectType.getByName(split[1]);
				}
				return null;
			}

			@SuppressWarnings("deprecation")
			@Override
			public Object save(Object object) {
				if (object instanceof PotionEffectType) {
					PotionEffectType potionEffectType = (PotionEffectType) object;
					return potionEffectType.getName() + ";" + potionEffectType.getId();

				}
				return null;
			}

		});
		StorageAPI.register(PotionEffect.class, new Variable() {

			@Override
			public Object save(Object object) {
				return null;
			}

			@Override
			public Object get(Object object) {
				return new PotionEffect(PotionEffectType.SPEED, 20, 0);
			}
		});
		StorageAPI.register(OfflinePlayer.class, new Variable() {

			@Override
			public Object get(Object object) {
				if (object instanceof String) {
					String id = (String) object;
					String[] split = id.split(";");
					return new FakePlayer(split[0], UUID.fromString(split[1]));

				}
				return null;
			}

			@Override
			public Object save(Object object) {
				if (object instanceof OfflinePlayer) {
					OfflinePlayer p = (OfflinePlayer) object;
					return p.getName() + ";" + p.getUniqueId().toString();

				}
				return null;
			}

		});
		StorageAPI.register(Location.class, new Storable() {

			@Override
			public Object restore(Map<String, Object> map) {
				return new Location(Bukkit.getWorlds().get(0), 1, 1, 1);
			}

			@Override
			public void store(Map<String, Object> map, Object object) {
			}

			@Override
			public String alias() {
				return "Location";
			}

		});

		StorageAPI.register(Chunk.class, new Variable() {

			@Override
			public Object get(Object object) {
				if (object instanceof String) {
					String string = (String) object;
					String[] split = string.split(";");
					return Bukkit.getWorld(split[0]).getChunkAt(Extra.toInt(split[1]), Extra.toInt(split[2]));

				}
				return null;
			}

			@Override
			public Object save(Object object) {
				if (object instanceof Chunk) {
					Chunk chunk = (Chunk) object;
					return chunk.getWorld().getName() + ";" + chunk.getX() + ";" + chunk.getZ();
				}

				return null;
			}

		});

		StorageAPI.register(World.class, new Variable() {
			@Override
			public Object get(Object object) {
				if (object instanceof String) {
					String world = (String) object;
					return Bukkit.getWorld(world);

				}
				return null;
			}

			@Override
			public Object save(Object object) {
				if (object instanceof World) {
					World world = (World) object;
					return world.getName();

				}
				return null;
			}
		});
		StorageAPI.register(ItemStack.class, new Storable() {
			@Override
			public Object restore(Map<String, Object> map) {
				int id = Extra.toInt(map.get("id"));
				int amount = Extra.toInt(map.get("amount"));
				int data = Extra.toInt(map.get("data"));
				@SuppressWarnings("deprecation")
				ItemStack item = new ItemStack(id, amount, (short) data);
				String name = Extra.toChatMessage((String) map.get("name"));
				if (!name.isEmpty()) {
					Mine.setName(item, name);
				}
				@SuppressWarnings("unchecked")
				List<String> lore = Extra.toMessages((List<Object>) map.get("lore"));
				if (!lore.isEmpty()) {
					Mine.setLore(item, lore);
				}
				String enchants = (String) map.get("enchants");
				if (!enchants.isEmpty()) {
					if (enchants.contains(", ")) {
						String[] split = enchants.split(", ");
						for (String enchs : split) {
							String[] sub = enchs.split("-");
							@SuppressWarnings("deprecation")
							Enchantment ench = Enchantment.getById(Extra.toInt(sub[0]));
							Integer level = Extra.toInt(sub[1]);
							item.addUnsafeEnchantment(ench, level);

						}
					} else {
						String[] split = enchants.split("-");
						@SuppressWarnings("deprecation")
						Enchantment ench = Enchantment.getById(Extra.toInt(split[0]));
						Integer level = Extra.toInt(split[1]);
						item.addUnsafeEnchantment(ench, level);

					}
				}
				return item;
			}

			@Override
			public String alias() {
				return "Item";
			}

			@SuppressWarnings("deprecation")
			@Override
			public void store(Map<String, Object> map, Object object) {
				if (object instanceof ItemStack) {
					ItemStack item = (ItemStack) object;
					map.remove("durability");
					map.remove("meta");
					map.remove("type");
					map.put("id", item.getTypeId());
					map.put("data", item.getDurability());
					map.put("amount", item.getAmount());
					map.put("name", Mine.getName(item));
					map.put("lore", Mine.getLore(item));
					String enchants = "";
					if (item.getItemMeta().hasEnchants()) {
						StringBuilder str = new StringBuilder();
						int id = 0;
						for (Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
							if (id > 0)
								str.append(", ");
							Enchantment enchantment = entry.getKey();
							str.append(enchantment.getId() + "-" + entry.getValue());
							id++;
						}
						enchants = str.toString();
					}
					map.put("enchants", enchants);
				}

			};
		});
	}

	public static String getRedHeart() {
		return ChatColor.RED + "♥";
	}

	public static String cutText(String text, int lenght) {
		return text.length() > lenght ? text.substring(0, lenght) : text;
	}

	public static class Item extends ItemStack implements Variable {

		@SuppressWarnings("deprecation")
		public Item(int id) {
			super(id);
		}

		@SuppressWarnings("deprecation")
		public Item() {
			super(1);
		}

		@SuppressWarnings("deprecation")
		public Item(int id, int amount, int data, String name, String... lore) {
			setTypeId(id);
			setAmount(amount);
			setDurability((short) data);
			ItemMeta meta = getItemMeta();
			meta.setDisplayName(name);
			;
			meta.setLore(Arrays.asList(lore));
			;
			this.setItemMeta(meta);
		}

		/**
		 * id:data-qnt;enchId-enchData,enchId-enchData;nome;descriao1,descricao2
		 */
		@SuppressWarnings("deprecation")
		@Override
		public Object get(Object object) {
			if (object instanceof String) {
				String text = (String) object;

				try {
					String[] split = text.split(";");
					String[] splitData = split[0].split("-");
					Integer qnt = Mine.toInt(splitData[1]);
					String[] splitInfo = splitData[0].split(":");
					Integer id = Mine.toInt(splitInfo[0]);
					short data = Mine.toShort(splitInfo[1]);
					ItemStack item = new Item();
					item.setTypeId(id);
					item.setDurability(data);
					item.setAmount(qnt);
					if (split.length > 0) {
						if (split[1].contains(",")) {
							String[] enchs = split[1].split(",");
							for (String enchant : enchs) {
								String[] ench = enchant.split("-");
								Integer ench_id = Mine.toInt(ench[0]);
								Integer ench_level = Mine.toInt(ench[1]);
								item.addUnsafeEnchantment(Enchantment.getById(ench_id), ench_level);
							}
						} else {
							if (!split[1].equals(" ")) {
								String[] ench = split[1].split("-");
								Integer ench_id = Mine.toInt(ench[0]);
								Integer ench_level = Mine.toInt(ench[1]);
								item.addUnsafeEnchantment(Enchantment.getById(ench_id), ench_level);
							}

						}
					}
					ItemMeta meta = item.getItemMeta();
					if (split.length > 1) {
						String nome = split[2];
						if (!nome.equals(" ")) {
							meta.setDisplayName(Extra.toChatMessage(nome));
						}
					}
					if (split.length > 2) {
						List<String> lista = new ArrayList<>();
						String descricao = split[3];
						if (descricao.contains(",")) {
							String[] lore = descricao.split(",");
							for (String line : lore) {
								lista.add(Extra.toChatMessage(line));
							}
						} else {
							if (!descricao.equals(" ")) {
								lista.add(descricao);
							}

						}
						meta.setLore(lista);
					}
					item.setItemMeta(meta);

					return item;

				} catch (Exception e) {
					e.printStackTrace();
					return new Item(1);
				}

			}
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		public Object save(Object object) {
			if (object instanceof ItemStack) {
				ItemStack item = (ItemStack) object;
				StringBuilder textao = new StringBuilder();
				textao.append(item.getTypeId() + ":" + item.getDurability() + "-" + item.getAmount() + ";");
				ItemMeta meta = item.getItemMeta();
				if (meta != null) {

					if (meta.hasEnchants()) {
						boolean first = true;
						for (Entry<Enchantment, Integer> enchant : item.getItemMeta().getEnchants().entrySet()) {
							if (!first) {
								textao.append(",");
							} else
								first = false;
							textao.append(enchant.getKey().getId());
							textao.append("-");
							textao.append(enchant.getValue());
						}
					} else {
						textao.append(" ");
					}
					textao.append(";");
					if (item.getItemMeta().hasDisplayName()) {
						textao.append(item.getItemMeta().getDisplayName());
					} else {
						textao.append(" ");
					}
					textao.append(";");
					if (meta.hasLore()) {
						boolean first = true;
						for (String line : meta.getLore()) {
							if (!first) {
								textao.append(",");
							} else
								first = false;
							textao.append(line);
						}

					} else {
						textao.append(" ");
					}
					textao.append(";");
				}
				return textao.toString();
			}
			return null;
		}

	}

	public static ItemStack reloadItem(String text) {
		return (ItemStack) new Item().get(text);
	}

	public static String saveItem(ItemStack item) {
		return (String) new Item().save(item);
	}

	public static ItemStack newBanner() {
		ItemStack banner = new ItemStack(Material.BANNER);
		BannerMeta meta = (BannerMeta) banner.getItemMeta();
		meta.setBaseColor(DyeColor.BLACK);
		meta.setDisplayName("§aBaner");
		meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.SKULL));
		banner.setItemMeta(meta);
		// meta.set
		return banner;
	}

	public static String getProgressBar(double money, double price, String concluidoCor, String faltandoCor,
			String symbol) {
		StringBuilder result = new StringBuilder();
		double div = money / price;
		// 10 5 2
		// long redonde = Math.round(div * 100);
		// long con = redonde / 10;
		if (div > 1) {
			div = 1;
		}
		double rest = 1D - div;
		result.append(concluidoCor);
		while (div > 0) {
			result.append(symbol);
			div -= 0.1;
		}
		result.append(faltandoCor);
		while (rest > 0) {
			result.append(symbol);
			rest -= 0.1;
		}
		return result.toString();
	}

	public static Team getTeam(Scoreboard scoreboard, String name) {
		Team team = scoreboard.getTeam(Mine.cutText(name, 16));
		if (team == null) {
			team = scoreboard.registerNewTeam(cutText(name, 16));
		}
		return team;
	}

	/**
	 * Seta o nivel do jogador
	 * 
	 * @param jogador
	 * @param novoNivel
	 */
	public static void setarNivelJogador(Player jogador, int novoNivel) {
		jogador.setLevel(novoNivel);
	}

	/**
	 * Seta o nivel da barra de fome do jogador
	 * 
	 * @param jogador
	 * @param quantidade
	 */
	public static void setarNivelFomeJogador(Player jogador, int quantidade) {
		jogador.setFoodLevel(quantidade);
	}

	/**
	 * Seta a barra de Xp do jogador <br>
	 * 100 = Barra cheia <br>
	 * 0 = Barra vazia <br>
	 * 
	 * @param jogador
	 * @param porcentagem
	 */
	public static void setarBarraXpJogador(Player jogador, int porcentagem) {
		jogador.setExp(porcentagem == 0 ? 0F : porcentagem / 100);
	}

	public static void sendTo(Collection<Player> players, String message) {
		for (Player player : players) {
			player.sendMessage(message);
		}

	}

	private static Map<String, Replacer> replacers = new HashMap<>();

	public static interface Replacer {

		Object getText(Player p);
	}

	public static void resetScoreboards() {

		for (Team teams : getMainScoreboard().getTeams()) {
			teams.unregister();
		}
		for (Objective objective : getMainScoreboard().getObjectives()) {
			objective.unregister();
		}
		for (Player player : Mine.getPlayers()) {
			player.setScoreboard(getMainScoreboard());
			player.setMaxHealth(20);
			player.setHealth(20);
			player.setHealthScaled(false);
		}
	}

	/**
	 * Pega um Som baseado num Objeto (Texto)
	 * 
	 * @param object
	 *            Objeto
	 * @return Som
	 */
	public static Sound getSound(Object object) {
		String str = object.toString().replace("_", "").trim();
		for (Sound sound : Sound.values()) {
			if (str.equals("" + sound.ordinal())) {
				return sound;
			}
			if (str.equalsIgnoreCase(sound.name().replace("_", ""))) {
				return sound;
			}

		}
		return null;
	}

	public static String formatTime(long time) {
		return Extra.formatTime(time);
	}

	public static String formatDiference(long timestamp) {
		return formatTime(timestamp - System.currentTimeMillis());
	}

	public static long parseDateDiff(String time, boolean future) throws Exception {
		return Extra.parseDateDiff(time, future);
	}

	/**
	 * Pega uma lista de Entidades baseada em um Argumento (Texto)
	 * 
	 * @param argument
	 *            Texto
	 * @return Lista de Entidades
	 */
	public static List<String> getLivingEntities(String argument) {
		List<String> list = new ArrayList<>();
		argument = argument.trim().replace("_", "");
		for (EntityType type : EntityType.values()) {
			if (type == EntityType.PLAYER)
				continue;
			if (type.isAlive() & type.isSpawnable()) {
				String text = Mine.toTitle(type.name(), "");
				String line = type.name().trim().replace("_", "");
				if (startWith(line, argument)) {
					list.add(text);
				}
			}

		}
		return list;
	}

	@SuppressWarnings("deprecation")
	public static EntityType getEntity(Object object) {
		String str = object.toString().replace("_", "").trim();
		for (EntityType type : EntityType.values()) {
			if (str.equals("" + type.getTypeId())) {
				return type;
			}
			if (str.equalsIgnoreCase("" + type.getName())) {
				return type;
			}
			if (str.equalsIgnoreCase(type.name().replace("_", ""))) {
				return type;
			}

		}
		return null;
	}

	public static List<String> getEnchants(String argument) {
		if (argument == null) {
			argument = "";
		}
		argument = argument.trim().replace("_", "");
		List<String> list = new ArrayList<>();

		for (Enchantment enchant : Enchantment.values()) {
			String text = Mine.toTitle(enchant.getName(), "");
			String line = enchant.getName().trim().replace("_", "");
			if (startWith(line, argument)) {
				list.add(text);
			}
		}
		return list;

	}

	public static List<String> getSounds(String argument) {
		if (argument == null) {
			argument = "";
		}
		argument = argument.trim().replace("_", "");
		List<String> list = new ArrayList<>();

		for (Sound enchant : Sound.values()) {
			String text = Mine.toTitle(enchant.name(), "");
			String line = enchant.name().trim().replace("_", "");
			if (startWith(line, argument)) {
				list.add(text);
			}
		}
		return list;

	}

	@SuppressWarnings("deprecation")
	public static Enchantment getEnchant(Object object) {
		String str = object.toString().replace("_", "").trim();
		for (Enchantment enchant : Enchantment.values()) {
			if (str.equals("" + enchant.getId())) {
				return enchant;
			}
			if (str.equalsIgnoreCase(enchant.getName().replace("_", ""))) {
				return enchant;
			}
		}
		return null;
	}

	public static boolean getChance(double chance) {

		return Extra.getChance(chance);
	}

	public static boolean random(double chance) {
		return getChance(chance);
	}

	public static int randomInt(int minValue, int maxValue) {
		return getRandomInt(minValue, maxValue);
	}

	public static boolean hasPerm(CommandSender sender, String permission, int max, int min) {

		boolean has = false;
		for (int i = max; i >= min; i--) {
			if (sender.hasPermission(permission + "." + i)) {
				has = true;
			}
		}
		return has;

	}

	public static PluginCommand command(String commandName, CommandExecutor command, String permission,
			String permissionMessage) {

		PluginCommand cmd = Bukkit.getPluginCommand(commandName);
		cmd.setExecutor(command);
		cmd.setPermission(permission);
		cmd.setPermissionMessage(permissionMessage);
		return cmd;
	}

	public static boolean hasPlugin(String plugin) {
		return Bukkit.getPluginManager().getPlugin(plugin) != null;
	}

	public static String getCmd(String message) {
		return Extra.getCmd(message);
	}

	/**
	 * Retorna se (now < (seconds + before));
	 * 
	 * @param before
	 *            (Antes)
	 * @param seconds
	 *            ()
	 * @return
	 */
	public static boolean inCooldown(long before, long seconds) {
		return Extra.inCooldown(before, seconds);

	}

	public static long getCooldown(long before, long seconds) {
		return Extra.getCooldown(before, seconds);

	}

	public static boolean hasMine() {
		return hasPlugin("EduardMine");
	}

	public static Scoreboard getMainScoreboard() {
		return Bukkit.getScoreboardManager().getMainScoreboard();
	}

	public static long getNow() {
		return Extra.getNow();
	}

	@SafeVarargs
	public static <E> E getRandom(E... objects) {
		return Extra.getRandom(objects);
	}

	public static <E> E getRandom(List<E> objects) {
		return Extra.getRandom(objects);
	}

	public static boolean isMultBy(int number1, int numer2) {

		return Extra.isMultBy(number1, numer2);
	}

	public static void addPermission(String permission) {
		Bukkit.getPluginManager().addPermission(new Permission(permission));
	}

	public static boolean newExplosion(Location location, float power, boolean breakBlocks, boolean makeFire) {
		return location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), power,
				breakBlocks, makeFire);
	}

	public static BukkitTask timer(Plugin plugin, long ticks, Runnable run) {
		if (run instanceof BukkitRunnable) {
			BukkitRunnable bukkitRunnable = (BukkitRunnable) run;
			return bukkitRunnable.runTaskTimer(plugin, ticks, ticks);
		}
		return Bukkit.getScheduler().runTaskTimer(plugin, run, ticks, ticks);
	}

	public static BukkitTask timers(Plugin plugin, long ticks, Runnable run) {
		if (run instanceof BukkitRunnable) {
			BukkitRunnable bukkitRunnable = (BukkitRunnable) run;
			return bukkitRunnable.runTaskTimerAsynchronously(plugin, ticks, ticks);
		}
		return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, run, ticks, ticks);
	}

	public static double getRandomDouble(double minValue, double maxValue) {
		return Extra.getRandomDouble(minValue, maxValue);
	}

	public static Firework newFirework(Location location, int high, Color color, Color fade, boolean trail,
			boolean flicker) {
		return newFirework(location, high, color, fade, trail, flicker, FireworkEffect.Type.CREEPER);
	}

	public static Firework newFirework(Location location, int high, Color color, Color fade, boolean trail,
			boolean flicker, FireworkEffect.Type type) {
		Firework firework = location.getWorld().spawn(location, Firework.class);
		FireworkMeta meta = firework.getFireworkMeta();
		meta.setPower(high);
		meta.addEffect(FireworkEffect.builder().with(type).trail(trail).flicker(flicker).withColor(color).withFade(fade)
				.build());
		firework.setFireworkMeta(meta);
		return firework;
	}

	public static void newStepSound(Location location, int blockId) {
		location.getWorld().playEffect(location, Effect.STEP_SOUND, blockId);
	}

	public static void newStepSound(Location location, Material material) {
		location.getWorld().playEffect(location, Effect.STEP_SOUND, material);
	}

	public static int getRandomInt(int minValue, int maxValue) {
		return Extra.getRandomInt(minValue, maxValue);
	}

	public static void runCommand(String command) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
	}

	public static void makeCommand(String command) {
		runCommand(command);
	}

	public static boolean isIpProxy(String ip) {
		return Extra.isIpProxy(ip);
	}

	public static void callEvent(Event event) {

		Bukkit.getPluginManager().callEvent(event);
	}

	public static void event(Listener event, Plugin plugin) {
		registerEvents(event, plugin);
	}

	public static void registerEvents(Listener event, Plugin plugin) {
		Bukkit.getPluginManager().registerEvents(event, plugin);
	}

	public static BukkitTask delay(Plugin plugin, long ticks, Runnable run) {
		return Bukkit.getScheduler().runTaskLater(plugin, run, ticks);
	}

	public static BukkitTask delays(Plugin plugin, long ticks, Runnable run) {
		return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, run, ticks);
	}

	public static String getTime(int time) {

		return getTime(time, " segundo(s)", " minuto(s) ");

	}

	public static String getTime(int time, String second, String minute) {
		return Extra.getTime(time, second, minute);
	}

	public static String getTimeMid(int time) {

		return getTime(time, " seg", " min ");

	}

	public static String getTimeSmall(int time) {

		return getTime(time, "s", "m");

	}

	public static boolean startWith(String message, String text) {
		return message.toLowerCase().startsWith(text.toLowerCase());
	}

	public static String toChatMessage(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static String toConfigMessage(String text) {
		return text.replace("§", "&");
	}

	public static String toDecimal(Object number) {
		return toDecimal(number, 2);
	}

	public static String toDecimal(Object number, int max) {
		return Extra.toDecimal(number, max);
	}

	public static String toText(Collection<String> message) {
		return Extra.toText(message);
	}

	public static String toText(int size, String text) {
		return Extra.toText(size, text);
	}

	public static String removeBrackets(String... message) {
		return Extra.removeBrackets(message);
	}

	public static String toTitle(String name) {
		return Extra.toTitle(name);

	}

	public static String toTitle(String name, String replacer) {
		return Extra.toTitle(name, replacer);
	}

	public static boolean contains(String message, String text) {
		return Extra.contains(message, text);
	}

	public static String getText(int init, String... args) {
		StringBuilder text = new StringBuilder();
		int id = 0;
		for (String arg : args) {
			if (id < init) {
				id++;
				continue;
			}
			text.append(" " + toChatMessage(arg));
			id++;
		}
		return text.toString();
	}

	public static Double toDouble(Object object) {

		return Extra.toDouble(object);

	}

	public static Float toFloat(Object object) {
		return Extra.toFloat(object);

	}

	public static Integer toInt(Object object) {

		return Extra.toInt(object);

	}

	public static Integer toInteger(Object object) {
		return Extra.toInt(object);
	}

	public static Long toLong(Object object) {
		return Extra.toLong(object);
	}

	public static Short toShort(Object object) {
		return Extra.toShort(object);

	}

	public static Boolean toBoolean(Object obj) {
		return Extra.toBoolean(obj);
	}

	public static Byte toByte(Object object) {
		return Extra.toByte(object);

	}

	public static String toString(Object object) {

		return object == null ? "" : object.toString();
	}

	public static void sendActionBar(String message) {
		for (Player player : Mine.getPlayers()) {
			Mine.sendActionBar(player, message);
		}
	}

	public static void addReplacer(String key, Replacer value) {
		replacers.put(key, value);
	}

	public static Replacer getReplacer(String key) {
		return replacers.get(key);
	}

	public static void broadcast(String message, String permission) {
		for (Player player : Mine.getPlayers()) {
			if (player.hasPermission(permission))
				player.sendMessage(message);
		}
	}

	public static String getReplacers(String text, Player player) {
		for (Entry<String, Replacer> value : replacers.entrySet()) {
			if (text.contains(value.getKey())) {
				try {
					text = text.replace(value.getKey(), "" + value.getValue().getText(player));

				} catch (Exception e) {
					// Mine.console("§c"+e.getMessage());
				}

			}

		}
		return text;
	}

	public static List<String> toLines(String text, int size) {
		return Extra.toLines(text, size);

	}

	public static Villager newNPCVillager(Location location, String name) {
		Villager npc = location.getWorld().spawn(location, Villager.class);
		npc.setCustomName(name);
		npc.setCustomNameVisible(true);
		Mine.disableAI(npc);
		return npc;
	}

	/**
	 * Tenha certeza que esta carregado a chunk pois assim funciona, caso contario
	 * buga<br>
	 * location.getChunk().load(true);
	 * 
	 * @param location
	 *            Local
	 * @param line
	 *            Linha
	 * @return ArmorStand
	 */
	public static ArmorStand newHologram(Location location, String line) {
		ArmorStand holo = location.getWorld().spawn(location, ArmorStand.class);
		if (!location.getChunk().isLoaded())
			location.getChunk().load(true);
		holo.setGravity(false);
		holo.setVisible(false);
		holo.setCustomNameVisible(true);
		holo.setCustomName(line);
		return holo;
	}

	public static List<ArmorStand> newHologram(Location location, String... lines) {
		List<ArmorStand> lista = new ArrayList<>();
		for (String line : lines) {
			ArmorStand holo = newHologram(location, line);
			lista.add(holo);
			location = location.subtract(0, 0.3, 0);
		}
		return lista;
	}

	public static List<ArmorStand> newHologram(Location location, List<String> lines) {
		return newHologram(location, lines, false);
	}

	public static List<ArmorStand> newHologram(Location location, List<String> lines, boolean toDown) {
		List<ArmorStand> lista = new ArrayList<>();
		for (String line : lines) {
			ArmorStand holo = newHologram(location, line);
			lista.add(holo);
			if (toDown)
				location = location.subtract(0, 0.3, 0);
			else {
				location = location.add(0, 0.3, 0);
			}
		}
		return lista;
	}

	public static String[] wordWrap(String rawString, int lineLength) {
		if (rawString == null) {
			return new String[] { "" };
		}

		if ((rawString.length() <= lineLength) && (!(rawString.contains("\n")))) {
			return new String[] { rawString };
		}

		char[] rawChars = new StringBuilder().append(rawString).append(' ').toString().toCharArray();
		StringBuilder word = new StringBuilder();
		StringBuilder line = new StringBuilder();
		List<String> lines = new LinkedList<>();
		int lineColorChars = 0;

		for (int i = 0; i < rawChars.length; ++i) {
			char c = rawChars[i];

			if (c == 167) {
				word.append(ChatColor.getByChar(rawChars[(i + 1)]));
				lineColorChars += 2;
				++i;
			} else if ((c == ' ') || (c == '\n')) {
				if ((line.length() == 0) && (word.length() > lineLength)) {
					for (String partialWord : word.toString()
							.split(new StringBuilder().append("(?<=\\G.{").append(lineLength).append("})").toString()))
						lines.add(partialWord);
				} else if (line.length() + word.length() - lineColorChars == lineLength) {
					line.append(word);
					lines.add(line.toString());
					line = new StringBuilder();
					lineColorChars = 0;
				} else if (line.length() + 1 + word.length() - lineColorChars > lineLength) {
					for (String partialWord : word.toString().split(
							new StringBuilder().append("(?<=\\G.{").append(lineLength).append("})").toString())) {
						lines.add(line.toString());
						line = new StringBuilder(partialWord);
					}
					lineColorChars = 0;
				} else {
					if (line.length() > 0) {
						line.append(' ');
					}
					line.append(word);
				}
				word = new StringBuilder();

				if (c == '\n') {
					lines.add(line.toString());
					line = new StringBuilder();
				}
			} else {
				word.append(c);
			}
		}

		if (line.length() > 0) {
			lines.add(line.toString());
		}

		if ((lines.get(0).length() == 0) || (lines.get(0).charAt(0) != 167)) {
			lines.set(0, new StringBuilder().append(ChatColor.WHITE).append(lines.get(0)).toString());
		}
		for (int i = 1; i < lines.size(); ++i) {
			String pLine = lines.get(i - 1);
			String subLine = lines.get(i);

			char color = pLine.charAt(pLine.lastIndexOf(167) + 1);
			if ((subLine.length() == 0) || (subLine.charAt(0) != 167)) {
				lines.set(i, new StringBuilder().append(ChatColor.getByChar(color)).append(subLine).toString());
			}
		}

		return (lines.toArray(new String[lines.size()]));
	}

	public static String formatColors(String str) {
		char[] chars = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'n', 'r', 'l',
				'k', 'o', 'm' };
		char[] array = str.toCharArray();
		for (int t = 0; t < array.length - 1; t++) {
			if (array[t] == '&') {
				for (char c : chars) {
					if (c == array[(t + 1)]) {
						array[t] = '§';
					}
				}
			}
		}
		return new String(array);
	}

	public static void box(String[] paragraph, String title) {
		Extra.box(paragraph, title);
	}

	public static List<String> toMessages(List<Object> list) {
		List<String> lines = new ArrayList<String>();
		for (Object line : list) {
			lines.add(toChatMessage(line.toString()));
		}
		return lines;
	}

	public static String claz_mEntityPlayer = "#mEntityPlayer";
	public static String claz_cCraftPlayer = "#cCraftPlayer";
	public static String claz_sPacketTitle = "#sProtocolInjector$PacketTitle";
	public static String claz_sAction = "#sProtocolInjector$PacketTitle$Action";
	public static String claz_sPacketTabHeader = "#sProtocolInjector$PacketTabHeader";
	public static String claz_pPlayOutChat = "#pPlayOutChat";
	public static String claz_pPlayOutTitle = "#pPlayOutTitle";
	public static String claz_pPlayOutWorldParticles = "#pPlayOutWorldParticles";
	public static String claz_pPlayOutPlayerListHeaderFooter = "#pPlayOutPlayerListHeaderFooter";
	public static String claz_pPlayOutNamedEntitySpawn = "#pPlayOutNamedEntitySpawn";
	public static String claz_pPlayInClientCommand = "#pPlayInClientCommand";
	public static String claz_cEnumTitleAction = "#cEnumTitleAction";
	public static String claz_pEnumTitleAction2 = "#pPlayOutTitle$EnumTitleAction";
	public static String claz_mEnumClientCommand = "#mEnumClientCommand";
	public static String claz_mEnumClientCommand2 = "#pPlayInClientCommand$EnumClientCommand";
	public static String claz_mChatSerializer = "#mChatSerializer";
	public static String claz_mIChatBaseComponent = "#mIChatBaseComponent";
	public static String claz_mEntityHuman = "#mEntityHuman";
	public static String claz_mNBTTagCompound = "#mNBTTagCompound";
	public static String claz_mNBTBase = "#mNBTBase";
	public static String claz_mNBTTagList = "#mNBTTagList";
	public static String claz_pPacket = "#p";
	public static String claz_cItemStack = "#cinventory.CraftItemStack";
	public static String claz_mItemStack = "#mItemStack";
	public static String claz_bItemStack = "#bItemStack";
	public static String claz_bBukkit = "#bBukkit";
	public static String claz_mChatComponentText = "#mChatComponentText";
	public static String claz_mMinecraftServer = "#mMinecraftServer";
	static {
		Extra.newReplacer("#v", getVersion());
	}

	/**
	 * Envia o pacote para o jogador
	 * 
	 * @param player
	 *            Jogador
	 * @param packet
	 *            Pacote
	 * @throws Exception
	 */
	public static void sendPacket(Object packet, Player player) throws Exception {

		Extra.getResult(getConnection(player), "sendPacket", Extra.getParameters(claz_pPacket), packet);
	}

	public static int getCurrentTick() throws Exception {
		return (int) Extra.getValue(Mine.claz_mMinecraftServer, "currentTick");
	}

	/**
	 * Pega o TPS do servidor uma expecie de calculador de LAG
	 * 
	 * @return TPS em forma de DOUBLE
	 */
	public static Double getTPS() {
		try {
			return Double.valueOf(Math.min(20.0D, Math.round(getCurrentTick() * 10) / 10.0D));
		} catch (Exception e) {
		}

		return 0D;
	}

	/**
	 * Envia o pacote para o jogador
	 * 
	 * @param player
	 *            Jogador
	 * @param packet
	 *            Pacote
	 * @throws Exception
	 */
	public static void sendPacket(Player player, Object packet) throws Exception {
		sendPacket(packet, player);
	}

	/**
	 * Envia o pacote para todos menos para o jogador
	 * 
	 * @param packet
	 *            Pacote
	 * @param target
	 *            Jogador
	 */
	public static void sendPackets(Object packet, Player target) {
		for (Player player : getPlayers()) {
			if (player.equals(target))
				continue;
			try {
				sendPacket(packet, player);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Envia o pacote para todos jogadores
	 * 
	 * @param packet
	 *            Pacote
	 */
	public static void sendPackets(Object packet) {
		for (Player p : getPlayers()) {
			try {
				sendPacket(packet, p);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * @param text
	 *            Texto
	 * @return "{\"text\":\"" + text + "\"}"
	 */
	public static String getIComponentText(String text) {
		return ("{\"text\":\"" + text + "\"}");

	}

	/**
	 * Inicia um ChatComponentText"IChatBaseComponent" pelo cons(String) da classe
	 * ChatComponentText
	 * 
	 * @param text
	 *            Texto
	 * @return ChatComponentText iniciado
	 * 
	 */
	public static Object getIChatText2(String text) throws Exception {
		return Extra.getNew(claz_mChatComponentText, text);

	}

	/**
	 * Inicia um IChatBaseComponent pelo metodo a(String) da classe ChatSerializer
	 * adicionando componente texto
	 * 
	 * @return IChatBaseComponent iniciado
	 * @param text
	 *            Texto
	 */
	public static Object getIChatText(String text) throws Exception {
		return getIChatBaseComponent(getIComponentText(text));
	}

	/**
	 * Inicia um IChatBaseComponent pelo metodo a(String) da classe ChatSerializer
	 * 
	 * @param component
	 *            Componente (Texto)
	 * @return IChatBaseComponent iniciado
	 */
	public static Object getIChatBaseComponent(String component) throws Exception {
		return Extra.getResult(claz_mChatSerializer, "a", component);
	}

	/**
	 * @param player
	 *            Jogador (CraftPlayer)
	 * @return EntityPlayer pelo metodo getHandle da classe CraftPlayer(Player)
	 * @exception Exception
	 */
	public static Object getHandle(Player player) throws Exception {
		return Extra.getResult(player, "getHandle");
	}

	/**
	 * Retorna Um PlayerConnection pela variavel playerConnection da classe
	 * EntityPlayer <Br>
	 * Pega o EntityPlayer pelo metodo getHandle(player)
	 * 
	 * @param player
	 *            Jogador (CraftPlayer)
	 * @return Conexão do jogador
	 */
	public static Object getConnection(Player player) throws Exception {
		return Extra.getValue(getHandle(player), "playerConnection");
	}

	/**
	 * 
	 * @return Versão do Servidor
	 */
	public static String getVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().replace('.', ',').split(",")[3];
	}

	/**
	 * (Não funciona)
	 * 
	 * @return Versão do Servidor
	 */
	@Deprecated
	public static String getVersion2() {
		return Bukkit.getServer().getClass().getPackage().getName().split("\\")[3];
	}

	/**
	 * Modifica a TabList do Jogador
	 * 
	 * @param player
	 *            Jogador
	 * @param header
	 *            Cabeçalho
	 * @param footer
	 *            Rodapé
	 */
	public static void setTabList(Player player, String header, String footer) {
		try {
			if (isAbove1_8(player)) {
				Object packet = Extra.getNew(claz_sPacketTabHeader,
						Extra.getParameters(claz_mIChatBaseComponent, claz_mIChatBaseComponent), getIChatText(header),
						getIChatText(footer));
				sendPacket(packet, player);
				return;
			}

		} catch (Exception e) {
		}
		try {
			Object packet = Extra.getNew(claz_pPlayOutPlayerListHeaderFooter,
					Extra.getParameters(claz_mIChatBaseComponent), getIChatText(header));

			Extra.setValue(packet, "b", getIChatText(footer));
			sendPacket(packet, player);
		} catch (Exception e) {
		}
		try {
			Object packet = Extra.getNew(claz_pPlayOutPlayerListHeaderFooter,
					Extra.getParameters(claz_mIChatBaseComponent), getIChatText2(header));
			Extra.setValue(packet, "b", getIChatText2(footer));
			sendPacket(packet, player);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * @param player
	 *            Jogador
	 * @return Se o Jogador esta na versão 1.8 ou pra cima
	 */
	public static boolean isAbove1_8(Player player) {
		try {
			return (int) Extra.getResult(Extra.getValue(getConnection(player), "networkManager"), "getVersion") == 47;

		} catch (Exception ex) {
		}
		return false;
	}

	/**
	 * Envia um Title para os Jogadores
	 * 
	 * 
	 * @param title
	 *            Titulo
	 * @param subTitle
	 *            SubTitulo
	 * @param fadeIn
	 *            Tempo de Aparececimento (Ticks)
	 * @param stay
	 *            Tempo de Passagem (Ticks)
	 * @param fadeOut
	 *            Tempo de Desaparecimento (Ticks)
	 */
	public static void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
		for (Player player : Mine.getPlayers()) {
			sendTitle(player, title, subTitle, fadeIn, stay, fadeOut);
		}
	}

	/**
	 * Envia um Title para o Jogador
	 * 
	 * @param player
	 *            Jogador
	 * @param title
	 *            Titulo
	 * @param subTitle
	 *            SubTitulo
	 * @param fadeIn
	 *            Tempo de Aparececimento (Ticks)
	 * @param stay
	 *            Tempo de Passagem (Ticks)
	 * @param fadeOut
	 *            Tempo de Desaparecimento (Ticks)
	 */
	public static void sendTitle(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
		try {
			if (isAbove1_8(player)) {

				// sendPacket(player, getNew(PacketTitle, getParameters(Action,
				// int.class, int.class, int.class),
				// getValue(Action, "TIMES"), fadeIn, stay, fadeOut));
				sendPacket(player,
						Extra.getNew(claz_sPacketTitle, Extra.getValue(claz_sAction, "TIMES"), fadeIn, stay, fadeOut));
				sendPacket(player,
						Extra.getNew(claz_sPacketTitle, Extra.getParameters(claz_sAction, claz_mIChatBaseComponent),
								Extra.getValue(claz_sAction, "TITLE"), getIChatText(title)));
				sendPacket(player,
						Extra.getNew(claz_sPacketTitle, Extra.getParameters(claz_sAction, claz_mIChatBaseComponent),
								Extra.getValue(claz_sAction, "SUBTITLE"), getIChatText(subTitle)));

				return;
			}

		} catch (Exception e) {
		}
		try {
			sendPacket(player, Extra.getNew(claz_pPlayOutTitle, fadeIn, stay, fadeOut));
			sendPacket(player,
					Extra.getNew(claz_pPlayOutTitle,
							Extra.getParameters(claz_cEnumTitleAction, claz_mIChatBaseComponent),
							Extra.getValue(claz_cEnumTitleAction, "TITLE"), getIChatText(title)));
			sendPacket(player,
					Extra.getNew(claz_pPlayOutTitle,
							Extra.getParameters(claz_cEnumTitleAction, claz_mIChatBaseComponent),
							Extra.getValue(claz_cEnumTitleAction, "SUBTITLE"), getIChatText(subTitle)));
			return;
		} catch (Exception e) {
		}
		try {
			sendPacket(player, Extra.getNew(claz_pPlayOutTitle, fadeIn, stay, fadeOut));
			sendPacket(player,
					Extra.getNew(claz_pPlayOutTitle,
							Extra.getParameters(claz_pEnumTitleAction2, claz_mIChatBaseComponent),
							Extra.getValue(claz_pEnumTitleAction2, "TITLE"), getIChatText2(title)));
			sendPacket(player,
					Extra.getNew(claz_pPlayOutTitle,
							Extra.getParameters(claz_pEnumTitleAction2, claz_mIChatBaseComponent),
							Extra.getValue(claz_pEnumTitleAction2, "SUBTITLE"), getIChatText2(subTitle)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Modifica a Action Bar do Jogador
	 * 
	 * @param player
	 *            Jogador
	 * @param text
	 *            Texto
	 */
	public static void sendActionBar(Player player, String text) {
		try {
			Object component = getIChatText(text);
			Object packet = Extra.getNew(claz_pPlayOutChat, Extra.getParameters(claz_mIChatBaseComponent, byte.class),
					component, (byte) 2);
			sendPacket(player, packet);
			return;
		} catch (Exception ex) {
		}
		try {
			Object component = getIChatText2(text);
			Object packet = Extra.getNew(claz_pPlayOutChat, Extra.getParameters(claz_mIChatBaseComponent, byte.class),
					component, (byte) 2);
			sendPacket(player, packet);
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(
					"§bRexMine §aNao foi possivel usar o 'setActionBar' pois o servidor esta na versao anterior a 1.8");

		}

	}

	/**
	 * @param player
	 *            Jogador
	 * @return Ping do jogador
	 */
	public static String getPing(Player player) {
		try {
			return Extra.getValue(getHandle(player), "ping").toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * @return Lista de jogadores do servidor
	 */
	public static List<Player> getPlayers() {
		List<Player> list = new ArrayList<>();
		try {

			Object object = Extra.getResult(claz_bBukkit, "getOnlinePlayers");
			if (object instanceof Collection) {
				Collection<?> players = (Collection<?>) object;
				for (Object obj : players) {
					if (obj instanceof Player) {
						Player p = (Player) obj;
						list.add(p);
					}
				}
			} else if (object instanceof Player[]) {
				Player[] players = (Player[]) object;
				for (Player p : players) {
					list.add(p);
				}
			}
		} catch (Exception e) {
		}

		return list;
	}

	/**
	 * Força o Respawn do Jogador (Respawn Automatico)
	 * 
	 * @param player
	 *            Jogador
	 */
	public static void makeRespawn(Player player) {
		try {
			Object packet = Extra.getNew(claz_pPlayInClientCommand,
					Extra.getValue(claz_mEnumClientCommand, "PERFORM_RESPAWN"));
			Extra.getResult(getConnection(player), "a", packet);

		} catch (Exception ex) {
			try {
				Object packet = Extra.getNew(claz_pPlayInClientCommand,
						Extra.getValue(claz_mEnumClientCommand2, "PERFORM_RESPAWN"));
				Extra.getResult(getConnection(player), "a", packet);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Modifica o nome do Jogador para um Novo Nome e<br>
	 * Envia para Todos os outros Jogadores a alteração (Packet)
	 * 
	 * @param player
	 *            Jogador
	 * @param displayName
	 *            Novo Nome
	 */
	public static void changeName(Player player, String displayName) {

		try {
			Object entityplayer = getHandle(player);
			// PacketPlayOutNamedEntitySpawn a;
			// EntityPlayer c;
			// PacketPlayOutEntity d;
			// PacketPlayOutSpawnEntityLiving e;

			// EntityHuman b;
			Field profileField = Extra.getField(Mine.claz_mEntityHuman, "bH");
			Object gameprofile = profileField.get(entityplayer);
			// Object before = Extra.getValue(gameprofile, "name");
			Extra.setValue(gameprofile, "name", displayName);
			// EntityPlayer a;
			// Object packet = Extra.getNew(claz_pPlayOutNamedEntitySpawn,
			// Extra.getParameters(claz_mEntityHuman),
			// entityplayer);
			// // Extra.setValue(Extra.getValue(packet, "b"), "name", displayName);
			// sendPackets(packet, player);
			for (Player p : getPlayers()) {
				if (p.equals(player))
					continue;
				p.hidePlayer(player);
			}
			for (Player p : getPlayers()) {
				if (p.equals(player))
					continue;
				p.showPlayer(player);
			}
			// Extra.setValue(gameprofile, "name", before);
			// System.out.println(Bukkit.getPlayer(displayName));

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Desabilita a Inteligencia da Entidade
	 * 
	 * @param entity
	 *            Entidade
	 */
	public static void disableAI(Entity entity) {
		try {
			// net.minecraft.server.v1_8_R3.Entity NMS = ((CraftEntity)
			// entidade).getHandle();
			// NBTTagCompound compound = new NBTTagCompound();
			// NMS.c(compound);
			// compound.setByte("NoAI", (byte) 1);
			// NMS.f(compound);
			Object compound = Extra.getNew(claz_mNBTTagCompound);
			Object getHandle = Extra.getResult(entity, "getHandle");
			Extra.getResult(getHandle, "c", compound);
			Extra.getResult(compound, "setByte", "NoAI", (byte) 1);
			Extra.getResult(getHandle, "f", compound);

		} catch (Exception e) {
		}

	}

	/**
	 * Pega o Ip do Jogador atual
	 * 
	 * @param player
	 *            Jogador
	 * @return Ip do Jogador
	 */
	public static String getIp(Player player) {
		return player.getAddress().getAddress().getHostAddress();
	}

	public static List<LivingEntity> getNearbyEntities(LivingEntity player, double x, double y, double z,
			EntityType... types) {
		List<LivingEntity> list = new ArrayList<>();
		for (Entity item : player.getNearbyEntities(x, y, z)) {
			if (item instanceof LivingEntity) {
				LivingEntity livingEntity = (LivingEntity) item;
				if (types != null) {
					for (EntityType entitie : types) {
						if (livingEntity.getType().equals(entitie)) {
							if (!list.contains(livingEntity))
								list.add(livingEntity);
						}
					}
				} else
					list.add(livingEntity);
			}
		}
		return list;

	}

	@SuppressWarnings("unchecked")
	public static <T extends Player> T getTarget(Player entity, Iterable<T> entities) {
		if (entity == null)
			return null;
		Player target = null;
		// double threshold = 1.0D;
		for (Player other : entities) {
			if (other.equals(entity))
				continue;
			Vector n = other.getLocation().toVector().subtract(entity.getLocation().toVector());
			if ((entity.getLocation().getDirection().normalize().crossProduct(n).lengthSquared() < 1.0D)
					&& (n.normalize().dot(entity.getLocation().getDirection().normalize()) >= 0.0D)) {
				if ((target == null) || (target.getLocation().distanceSquared(entity.getLocation()) > other
						.getLocation().distanceSquared(entity.getLocation()))) {
					target = other;
				}
			}
		}
		return (T) target;
	}

	public static List<LivingEntity> getNearbyEntities(LivingEntity entity, double radio, EntityType... entities) {

		return getNearbyEntities(entity, radio, radio, radio, entities);

	}

	public static List<Player> getPlayerAtRange(Location location, double range) {

		List<Player> players = new ArrayList<>();
		for (Player p : location.getWorld().getPlayers()) {
			if (!location.getWorld().equals(p.getWorld()))
				continue;
			if (p.getLocation().distance(location) <= range) {
				players.add(p);
			}
		}
		return players;
	}

	public static boolean hasLightOn(Entity entity) {
		return hasLightOn(entity.getLocation());
	}

	public static boolean hasLightOn(Location location) {
		return hasLightOn(location.getBlock());
	}

	public static boolean hasLightOn(Block block) {
		return block.getLightLevel() > 10;
	}

	public static Player getRandomPlayer() {
		return getRandomPlayer(getPlayers());
	}

	public static Player getRandomPlayer(List<Player> list) {
		return list.get(Mine.getRandomInt(1, list.size()) - 1);
	}

	public static void setDirection(Entity entity, Entity target) {
		entity.teleport(entity.getLocation().setDirection(target.getLocation().getDirection()));
	}

	public static void hide(Player player) {
		for (Player target : getPlayers()) {
			if (target != player) {
				target.hidePlayer(player);
			}
		}
	}

	public static boolean isOnGround(Entity entity) {
		return entity.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR;
	}

	public static void makeInvunerable(Player player) {
		player.setNoDamageTicks(Extra.DAY_IN_SECONDS * 20);

	}

	public static void makeInvunerable(Player player, int seconds) {
		player.setNoDamageTicks(seconds * 20);

	}

	public static void makeVulnerable(Player player) {

		player.setNoDamageTicks(0);
	}

	public static void moveTo(Entity entity, Location target, double gravity) {
		Location location = entity.getLocation().clone();
		double distance = target.distance(location);
		double x = -(gravity - ((target.getX() - location.getX()) / distance));
		double y = -(gravity - ((target.getY() - location.getY()) / distance));
		double z = -(gravity - ((target.getZ() - location.getZ()) / distance));
		Vector vector = new Vector(x, y, z);
		entity.setVelocity(vector);
	}

	public static void moveTo(Entity entity, Location target, double staticX, double staticY, double staticZ,
			double addX, double addY, double addZ) {
		Location location = entity.getLocation();
		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			location = livingEntity.getEyeLocation();
		}
		entity.setVelocity(getVelocity(location, target, staticX, staticY, staticZ, addX, addY, addZ));
	}

	public static boolean isFlying(Entity entity) {
		return entity.getLocation().getBlock().getRelative(BlockFace.DOWN, 2).getType() == Material.AIR;
	}

	public static boolean isInvulnerable(Player player) {
		return player.getNoDamageTicks() > 1;
	}

	public static LightningStrike strike(LivingEntity living, int maxDistance) {
		return strike(getTargetLoc(living, maxDistance));
	}

	public static LightningStrike strike(Location location) {
		return location.getWorld().strikeLightning(location);
	}

	public static void teleport(Entity entity, Location target) {
		entity.teleport(target.setDirection(entity.getLocation().getDirection()));
	}

	public static void removeEffects(Player player) {
		player.setFireTicks(0);
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
	}

	public static void resetLevel(Player player) {
		player.setLevel(0);
		player.setExp(0);
		player.setTotalExperience(0);
	}

	public static void setDirection(Entity entity, Location target) {
		Location location = entity.getLocation().clone();
		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			location = livingEntity.getEyeLocation().clone();

		}
		entity.teleport(entity.getLocation().setDirection(getDiretion(location, target)));

	}

	public static Vector getDiretion(Location location, Location target) {
		double distance = target.distance(location);
		double x = ((target.getX() - location.getX()) / distance);
		double y = ((target.getY() - location.getY()) / distance);
		double z = ((target.getZ() - location.getZ()) / distance);
		return new Vector(x, y, z);
	}

	public static void changeTabName(Player player, String displayName) {
		player.setPlayerListName(Mine.getText(32, displayName));
	}

	public static Vector getVelocity(Location entity, Location target, double staticX, double staticY, double staticZ,
			double addX, double addY, double addZ) {
		double distance = target.distance(entity);
		double x = (staticX + (addX * distance)) * ((target.getX() - entity.getX()) / distance);
		double y = (staticY + (addY * distance)) * ((target.getY() - entity.getY()) / distance);
		double z = (staticZ + (addZ * distance)) * ((target.getZ() - entity.getZ()) / distance);
		return new Vector(x, y, z);

	}

	public static void resetScoreboard(Player player) {
		player.setScoreboard(Mine.getMainScoreboard());
	}
	// Parei aqui

	public static void refreshAll(Player player) {
		Mine.clearInventory(player);
		removeEffects(player);
		refreshLife(player);
		refreshFood(player);
		makeVulnerable(player);
		resetLevel(player);
	}

	public static void refreshFood(Player player) {
		player.setFoodLevel(20);
		player.setSaturation(20);
		player.setExhaustion(0);
	}

	public static void refreshLife(Player p) {
		p.setHealth(p.getMaxHealth());
	}

	public static void setSpawn(Entity entity) {

		entity.getWorld().setSpawnLocation((int) entity.getLocation().getX(), (int) entity.getLocation().getY(),
				(int) entity.getLocation().getZ());
	}

	public static void show(Player player) {
		for (Player target : getPlayers()) {
			if (target != player) {
				target.showPlayer(player);
			}
		}
	}

	public static void teleport(LivingEntity entity, int range) {
		teleport(entity, getTargetLoc(entity, range));
	}

	public static void teleportToSpawn(Entity entity) {

		entity.teleport(entity.getWorld().getSpawnLocation().setDirection(entity.getLocation().getDirection()));
	}

	public static boolean isFalling(Entity entity) {
		return entity.getVelocity().getY() < Extra.WALKING_VELOCITY;
	}

	public static List<Player> getOnlinePlayers() {
		return getPlayers();
	}

	public static Location getTargetLoc(LivingEntity entity, int distance) {
		@SuppressWarnings("deprecation")
		Block block = entity.getTargetBlock((HashSet<Byte>) null, distance);
		return block.getLocation();
	}

	public static Player getNearestPlayer(Player player) {
		double dis = 0.0D;
		Player target = null;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (dis == 0.0D) {
				dis = p.getLocation().distance(player.getLocation());
				target = p;
			} else {
				double newdis = p.getLocation().distance(player.getLocation());
				if (newdis < dis) {
					dis = newdis;
					target = p;
				}
			}
		}

		return target;
	}

	public static void fixDrops(List<ItemStack> drops) {
		HashMap<ItemStack, ItemStack> itens = new HashMap<>();
		for (ItemStack drop : drops) {
			Material type = drop.getType();
			if (type == Material.AIR | type == null) {
				continue;
			}
			boolean find = false;
			for (Entry<ItemStack, ItemStack> entry : itens.entrySet()) {
				if (drop.isSimilar(entry.getKey())) {
					ItemStack item = entry.getKey();
					item.setAmount(item.getAmount() + drop.getAmount());
					find = true;
					break;
				}
			}
			if (!find) {
				itens.put(drop, drop);
			}

		}
		drops.clear();
		drops.addAll(itens.values());
	}

	/**
	 * Transforma um Texto em Vetor de Itens
	 * 
	 * @param data
	 *            Texto
	 * @return Vetor de Itens (Lista)
	 * 
	 */
	public static ItemStack[] itemFromBase64(final String data) {
		try {
			final ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			final BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			final ItemStack[] stacks = new ItemStack[dataInput.readInt()];
			for (int i = 0; i < stacks.length; ++i) {

				stacks[i] = (ItemStack) dataInput.readObject();

			}
			dataInput.close();
			return stacks;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Transforma um Vetor de Itens em um Texto
	 * 
	 * @param contents
	 *            Vetor de Itens
	 * @return Texto
	 */
	public static String itemtoBase64(final ItemStack[] contents) {

		try {
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput;
			dataOutput = new BukkitObjectOutputStream(outputStream);
			dataOutput.writeInt(contents.length);
			for (final ItemStack stack : contents) {
				dataOutput.writeObject(stack);
			}
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isDirectory(File file) {
		try {
			return (file.isDirectory());
		} catch (Exception e) {
			return isDirectory(file.getName());
		}

	}

	public static boolean isDirectory(String name) {

		if (name.endsWith(File.separator)) {
			return true;
		}
		if (name.endsWith("/")) {
			return true;
		}
		if (name.endsWith(File.pathSeparator)) {
			return true;

		}
		return false;

	}

	public static InputStream getResource(ClassLoader loader, String name) throws IOException {
		URL url = loader.getResource(name);
		if (url == null)
			return null;
		URLConnection connection = url.openConnection();
		connection.setUseCaches(false);
		return connection.getInputStream();
	}

	public static void copyAsUTF8(InputStream is, File file) throws IOException {
		if (is == null)
			return;
		InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8);
		BufferedReader br = new BufferedReader(in);
		List<String> lines = new ArrayList<>();
		String line;
		while ((line = br.readLine()) != null) {
			lines.add(line);
		}
		br.close();
		in.close();
		is.close();
		Files.write(file.toPath(), lines, StandardCharsets.UTF_8);

	}

	public static void copyAsUTF8(Path path, File file) throws IOException {
		List<String> lines = Files.readAllLines(path);
		Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
	}

	public static List<String> readLines(File file) {
		Path path = file.toPath();
		try {
			Mine.console("§bConfigAPI §a-> " + file.getName() + " §futf-8");
			return Files.readAllLines(path);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		try {
			Mine.console("§bConfigAPI §a-> " + file.getName() + " §f" + Charset.defaultCharset().displayName());
			return Files.readAllLines(path, Charset.defaultCharset());
		} catch (Exception e) {
		}
		List<String> lines = new ArrayList<>();
		try {
			Mine.console("§bConfigAPI §a-> " + file.getName());
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				// System.out.println(line);
				lines.add(line);
			}
			reader.close();

		} catch (Exception e) {
		}
		return lines;

	}

	public static void writeLines(File file, List<String> lines) {
		Path path = file.toPath();
		try {
			Files.write(path, lines, StandardCharsets.UTF_8);
			return;
		} catch (Exception e) {
		}
		try {
			Files.write(path, lines, Charset.defaultCharset());
		} catch (Exception e) {
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (String line : lines) {
				writer.write(line + "\n");
			}
			writer.close();
		} catch (Exception e) {
		}

	}

	@SuppressWarnings("deprecation")
	public static void setItem(ConfigurationSection section, ItemStack item) {
		section.set("id", item.getTypeId());
		section.set("data", item.getDurability());
		if (item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			if (meta.hasDisplayName()) {
				section.set("name", meta.getDisplayName());
			}
			if (meta.hasLore()) {
				List<String> lines = new ArrayList<>();
				for (String line : meta.getLore()) {
					lines.add(line);
				}
				section.set("lore", lines);
			}
		}
		StringBuilder text = new StringBuilder();
		for (Entry<Enchantment, Integer> enchant : item.getEnchantments().entrySet()) {
			text.append(enchant.getKey().getId() + "-" + enchant.getValue() + ",");
		}
		section.set("enchant", text.toString());
	}

	public static void setLocation(ConfigurationSection section, Location location) {
		section.set("x", location.getX());
		section.set("y", location.getY());
		section.set("z", location.getZ());
		section.set("yaw", location.getYaw());
		section.set("pitch", location.getPitch());
	}

	public static Location getLocation(ConfigurationSection section) {
		World world = Bukkit.getWorld(section.getString("world"));
		double x = section.getDouble("x");
		double y = section.getDouble("y");
		double z = section.getDouble("z");
		float yaw = (float) section.getDouble("yaw");
		float pitch = (float) section.getDouble("pitch");
		return new Location(world, x, y, z, yaw, pitch);
	}

	public static Location toLocation(String text) {
		String[] split = text.split(",");
		World world = Bukkit.getWorld(split[0]);
		double x = Double.parseDouble(split[1]);
		double y = Double.parseDouble(split[2]);
		double z = Double.parseDouble(split[3]);
		float yaw = Float.parseFloat(split[4]);
		float pitch = Float.parseFloat(split[5]);
		return new Location(world, x, y, z, yaw, pitch);
	}

	public static String saveLocation(Location location) {
		StringBuilder text = new StringBuilder();
		text.append(location.getWorld().getName() + ",");
		text.append(location.getX() + ",");
		text.append(location.getY() + ",");
		text.append(location.getZ() + ",");
		text.append(location.getYaw() + ",");
		text.append(location.getPitch());
		return text.toString();
	}

	/**
	 * Pega um Objecto serializavel do Arquivo
	 * 
	 * @param file
	 *            Arquivo
	 * @return Objeto
	 */
	public static Object getSerializable(File file) {
		if (!file.exists()) {
			return null;
		}
		try {

			FileInputStream getStream = new FileInputStream(file);
			ObjectInputStream get = new ObjectInputStream(getStream);
			Object object = get.readObject();
			get.close();
			getStream.close();
			return object;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Salva um Objecto no Arquivo em forma de serialização Java
	 * 
	 * @param object
	 *            Objeto (Dado)
	 * @param file
	 *            Arquivo
	 */
	public static void setSerializable(Object object, File file) {
		try {
			FileOutputStream saveStream = new FileOutputStream(file);
			ObjectOutputStream save = new ObjectOutputStream(saveStream);
			if (object instanceof Serializable) {
				save.writeObject(object);
			} else {
			}
			save.close();
			saveStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Desfazr o ZIP do Arquivo
	 * 
	 * @param zipFilePath
	 *            Arquivo
	 * @param destDirectory
	 *            Destino
	 */
	public static void unzip(String zipFilePath, String destDirectory)

	{
		try {
			File destDir = new File(destDirectory);
			if (!destDir.exists()) {
				destDir.mkdir();
			}
			ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
			ZipEntry entry = zipIn.getNextEntry();

			while (entry != null) {
				String filePath = destDirectory + File.separator + entry.getName();
				if (!entry.isDirectory()) {
					extractFile(zipIn, filePath);
				} else {
					File dir = new File(filePath);
					dir.mkdir();
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
			zipIn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Defaz o ZIP do Arquivo
	 * 
	 * @param zipIn
	 *            Input Stream (Coneção de Algum Arquivo)
	 * @param filePath
	 *            Destino Arquivo
	 */
	public static void extractFile(ZipInputStream zipIn, String filePath) {
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
			byte[] bytesIn = new byte[4096];
			int read = 0;
			while ((read = zipIn.read(bytesIn)) != -1) {
				bos.write(bytesIn, 0, read);
			}
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Efeito a fazer na localização
	 * 
	 * @author Eduard
	 *
	 */
	public static interface LocationEffect {

		boolean effect(Location location);
	}

	/**
	 * Ponto de direção usado para fazer um RADAR
	 * 
	 * @author Eduard
	 *
	 */
	public static enum Point {
		N('N'), NE('/'), E('O'), SE('\\'), S('S'), SW('/'), W('L'), NW('\\');

		public final char asciiChar;

		private Point(char asciiChar) {
			this.asciiChar = asciiChar;
		}

		@Override
		public String toString() {
			return String.valueOf(this.asciiChar);
		}

		public String toString(boolean isActive, ChatColor colorActive, String colorDefault) {
			return (isActive ? colorActive : colorDefault) + String.valueOf(this.asciiChar);
		}

		public String toString(boolean isActive, String colorActive, String colorDefault) {
			return (isActive ? colorActive : colorDefault) + String.valueOf(this.asciiChar);
		}
	}

	public static boolean equals(Location location1, Location location2) {

		return getBlockLocation1(location1).equals(getBlockLocation1(location2));
	}

	public static boolean equals2(Location location1, Location location2) {
		return location1.getBlock().getLocation().equals(location2.getBlock().getLocation());
	}

	public static List<Location> getLocations(Location location1, Location location2) {
		return getLocations(location1, location2, new LocationEffect() {

			@Override
			public boolean effect(Location location) {
				return true;
			}
		});
	}

	public static Location getHighLocation(Location loc, double high, double size) {

		loc.add(size, high, size);
		return loc;
	}

	public static void copyWorldFolder(File source, File target) {
		try {
			List<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
			if (!ignore.contains(source.getName())) {
				if (source.isDirectory()) {
					if (!target.exists())
						target.mkdirs();
					String files[] = source.list();
					for (String file : files) {
						File srcFile = new File(source, file);
						File destFile = new File(target, file);
						copyWorldFolder(srcFile, destFile);
					}
				} else {
					InputStream in = new FileInputStream(source);
					OutputStream out = new FileOutputStream(target);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = in.read(buffer)) > 0)
						out.write(buffer, 0, length);
					in.close();
					out.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void deleteWorld(String name) {
		unloadWorld(name);
		deleteFolder(getWorldFolder(name));
	}

	public static void unloadWorld(String name) {
		World world = Bukkit.getWorld(name);
		if (world != null) {
			for (Player p : world.getPlayers()) {
				p.kickPlayer("§cRestarting Server!");
			}
		}
		Bukkit.unloadWorld(name, false);
	}

	public static void deleteFolder(File file) {
		if (file.exists()) {
			File files[] = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteFolder(files[i]);
					files[i].delete();
				} else {
					files[i].delete();
				}
			}
			file.delete();
		}
	}

	public static void copyWorld(String fromWorld, String toWorld) {
		unloadWorld(fromWorld);
		unloadWorld(toWorld);
		deleteWorld(toWorld);
		copyWorldFolder(getWorldFolder(fromWorld), getWorldFolder(toWorld));
		WorldCreator copy = new WorldCreator(toWorld);
		copy.createWorld();
	}

	public static World loadWorld(String name) {
		return new WorldCreator(name).createWorld();
	}

	public static World newEmptyWorld(String worldName) {
		World world = new WorldCreator(worldName).generator(new EmptyWorldGenerator()).createWorld();
		world.setSpawnLocation(100, 100, 100);
		return world;
	}

	public static File getWorldFolder(String name) {
		File file = new File(Bukkit.getWorldContainer().getParentFile(), name);
		return file;
	}

	public static Location getHighLocation(Location loc1, Location loc2) {

		double x = Math.max(loc1.getX(), loc2.getX());
		double y = Math.max(loc1.getY(), loc2.getY());
		double z = Math.max(loc1.getZ(), loc2.getZ());
		return new Location(loc1.getWorld(), x, y, z);
	}

	public static boolean equals(ItemStack item, ItemStack stack) {
		return getLore(item).equals(getLore(stack)) && getName(item).equals(getName(stack))
				&& item.getType() == stack.getType() && item.getAmount() == stack.getAmount()
				&& item.getDurability() == stack.getDurability();
	}

	public static List<Location> getLocations(Location location1, Location location2, LocationEffect effect) {

		Location min = getLowLocation(location1, location2);
		Location max = getHighLocation(location1, location2);
		List<Location> locations = new ArrayList<>();
		for (double x = min.getX(); x <= max.getX(); x++) {
			for (double y = min.getY(); y <= max.getY(); y++) {
				for (double z = min.getZ(); z <= max.getZ(); z++) {
					Location loc = new Location(min.getWorld(), x, y, z);
					try {
						boolean r = effect.effect(loc);
						if (r) {
							try {
								locations.add(loc);
							} catch (Exception ex) {
							}
						}
					} catch (Exception ex) {
					}

				}
			}
		}
		return locations;

	}

	public static Location getLowLocation(Location loc, double low, double size) {

		loc.subtract(size, low, size);
		return loc;
	}

	public static Location getLowLocation(Location location1, Location location2) {
		double x = Math.min(location1.getX(), location2.getX());
		double y = Math.min(location1.getY(), location2.getY());
		double z = Math.min(location1.getZ(), location2.getZ());
		return new Location(location1.getWorld(), x, y, z);
	}

	public static Location getBlockLocation1(Location location) {

		return new Location(location.getWorld(), (int) location.getX(), (int) location.getY(), (int) location.getZ());
	}

	public static Location getBlockLocation2(Location location) {

		return location.getBlock().getLocation();
	}

	public static List<Location> getBox(Location playerLocation, double higher, double lower, double size,
			LocationEffect effect) {
		Location high = getHighLocation(playerLocation.clone(), higher, size);
		Location low = getLowLocation(playerLocation.clone(), lower, size);
		return getLocations(low, high, effect);
	}

	public static List<Location> setBox(Location playerLocation, double higher, double lower, double size,
			Material wall, Material up, Material down, boolean clearInside) {
		return getBox(playerLocation, higher, lower, size, new LocationEffect() {

			@Override
			public boolean effect(Location location) {

				if (location.getBlockY() == playerLocation.getBlockY() + higher) {
					location.getBlock().setType(up);
					return true;
				}
				if (location.getBlockY() == playerLocation.getBlockY() - lower) {
					location.getBlock().setType(down);
					return true;
				}

				if (location.getBlockX() == playerLocation.getBlockX() + size
						|| location.getBlockZ() == playerLocation.getBlockZ() + size
						|| location.getBlockX() == playerLocation.getBlockX() - size
						|| location.getBlockZ() == playerLocation.getBlockZ() - size) {
					location.getBlock().setType(wall);
					return true;
				}
				if (clearInside) {
					if (location.getBlock().getType() != Material.AIR)
						location.getBlock().setType(Material.AIR);
				}
				return false;
			}
		});
	}

	public static List<Location> getBox(Location playerLocation, double higher, double lower, double size) {
		return getBox(playerLocation, higher, lower, size, new LocationEffect() {

			@Override
			public boolean effect(Location location) {
				return true;
			}
		});
	}

	public static List<Location> getBox(Location playerLocation, double xHigh, double xLow, double zHigh, double zLow,
			double yLow, double yHigh) {
		Location low = playerLocation.clone().subtract(xLow, yLow, zLow);
		Location high = playerLocation.clone().add(xHigh, yHigh, zHigh);
		return getLocations(low, high);
	}

	public static Location getRandomPosition(Location location, int xVar, int zVar) {
		return getHighPosition(getRandomLocation(location, xVar, 0, zVar));

	}

	public static double distanceX(Location loc1, Location loc2) {
		return loc1.getX() - loc2.getX();
	}

	public static double distanceZ(Location loc1, Location loc2) {
		return loc1.getZ() - loc2.getZ();
	}

	public static Location getRandomLocation(Location location, int xVar, int yVar, int zVar) {
		int x = location.getBlockX();
		int z = location.getBlockZ();
		int y = location.getBlockY();
		int xR = Mine.getRandomInt(x - xVar, x + xVar);
		int zR = Mine.getRandomInt(z - zVar, z + zVar);
		int yR = Mine.getRandomInt(y - yVar, y + zVar);
		return new Location(location.getWorld(), xR, yR, zR);
	}

	public static Location getHighPosition(Location location) {
		return location.getWorld().getHighestBlockAt(location).getLocation();
	}

	public static Location getSpawn() {
		return Bukkit.getWorlds().get(0).getSpawnLocation();
	}

	public static Point getCompassPointForDirection(double inDegrees) {
		double degrees = (inDegrees - 180.0D) % 360.0D;
		if (degrees < 0.0D) {
			degrees += 360.0D;
		}

		if ((0.0D <= degrees) && (degrees < 22.5D))
			return Point.N;
		if ((22.5D <= degrees) && (degrees < 67.5D))
			return Point.NE;
		if ((67.5D <= degrees) && (degrees < 112.5D))
			return Point.E;
		if ((112.5D <= degrees) && (degrees < 157.5D))
			return Point.SE;
		if ((157.5D <= degrees) && (degrees < 202.5D))
			return Point.S;
		if ((202.5D <= degrees) && (degrees < 247.5D))
			return Point.SW;
		if ((247.5D <= degrees) && (degrees < 292.5D))
			return Point.W;
		if ((292.5D <= degrees) && (degrees < 337.5D))
			return Point.NW;
		if ((337.5D <= degrees) && (degrees < 360.0D)) {
			return Point.N;
		}
		return null;
	}

	public static ArrayList<String> getAsciiCompass(Point point, ChatColor colorActive, String colorDefault) {

		return getAsciiCompass(point, colorActive.toString(), colorDefault);
	}

	public static ArrayList<String> getAsciiCompass(Point point, String colorActive, String colorDefault) {
		ArrayList<String> ret = new ArrayList<>();

		String row = "";
		row = row + Point.NW.toString(Point.NW == point, colorActive, colorDefault);
		row = row + Point.N.toString(Point.N == point, colorActive, colorDefault);
		row = row + Point.NE.toString(Point.NE == point, colorActive, colorDefault);
		ret.add(row);

		row = "";
		row = row + Point.W.toString(Point.W == point, colorActive, colorDefault);
		row = row + colorDefault + "+";
		row = row + Point.E.toString(Point.E == point, colorActive, colorDefault);
		ret.add(row);

		row = "";
		row = row + Point.SW.toString(Point.SW == point, colorActive, colorDefault);
		row = row + Point.S.toString(Point.S == point, colorActive, colorDefault);
		row = row + Point.SE.toString(Point.SE == point, colorActive, colorDefault);
		ret.add(row);

		return ret;
	}

	public static ArrayList<String> getAsciiCompass(double inDegrees, ChatColor colorActive, String colorDefault) {
		return getAsciiCompass(getCompassPointForDirection(inDegrees), colorActive, colorDefault);
	}

	public static ArrayList<String> getAsciiCompass(double inDegrees, String colorActive, String colorDefault) {
		return getAsciiCompass(getCompassPointForDirection(inDegrees), colorActive, colorDefault);
	}

	/**
	 * Gerador de Mundo Vasio
	 * 
	 * @author Eduard
	 *
	 */
	public static class EmptyWorldGenerator extends ChunkGenerator {

		@Override
		public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ,
				ChunkGenerator.BiomeGrid biomeGrid) {
			byte[][] result = new byte[world.getMaxHeight() / 16][];
			return result;
		}

		@Override
		public Location getFixedSpawnLocation(World world, Random random) {
			return new Location(world, 100, 100, 100);
		}

		public void setBlock(byte[][] result, int x, int y, int z, byte blockID) {
			if (result[(y >> 4)] == null) {
				result[(y >> 4)] = new byte[4096];
			}
			result[(y >> 4)][((y & 0xF) << 8 | z << 4 | x)] = blockID;
		}

		@SuppressWarnings("deprecation")
		public byte getId(Material material) {
			return (byte) material.getId();
		}

		public byte getId(Material material, short data) {
			return 0;
		}

		public void setLayer(byte[][] result, int level, Material material) {
			int x, z;
			for (x = 0; x < 16; x++) {
				for (z = 0; z < 16; z++) {
					setBlock(result, x, level, z, getId(material));
				}
			}
		}

		public void setCorner(byte[][] result, int level, Material material) {
			int x, z;
			for (x = 0; x < 16; x++) {
				setBlock(result, x, level, 0, getId(material));
			}
			for (z = 0; z < 16; z++) {
				setBlock(result, 0, level, z, getId(material));
			}
		}

		public void setLayer(byte[][] result, int minLevel, int maxLevel, Material material) {
			int y;
			for (y = minLevel; y <= maxLevel; y++) {
				setLayer(result, y, material);
			}
		}
	}

	/**
	 * Gerador de Mundo Plano
	 * 
	 * @author Eduard
	 *
	 */
	public static class FlatWorldGenerator extends EmptyWorldGenerator {

		@Override
		public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ,
				ChunkGenerator.BiomeGrid biomeGrid) {
			byte[][] result = new byte[world.getMaxHeight() / 16][];
			setLayer(result, 0, Material.BEDROCK);
			setLayer(result, 1, 3, Material.DIRT);
			setLayer(result, 4, Material.GRASS);
			setCorner(result, 8, Material.DIAMOND_BLOCK);
			return result;
		}

	}

	/**
	 * Mine de Cooldown para Habilidades e Kits
	 * 
	 * @author Eduard
	 *
	 */
	public static abstract class Cooldowns {
		/**
		 * Tempo de Cooldown<br>
		 * Em forma de Ticks<br>
		 * Cada TICK = 1/20 de SEGUNDO
		 */
		private long ticks;

		/**
		 * Mapa contendo os Cooldowns em Andamento<br>
		 * KEY = UUID = Id do Jogador<br>
		 * VALUE = CooldownEvent = Evento do Cooldown<br>
		 */
		private Map<UUID, CooldownEvent> cooldowns = new HashMap<>();

		/**
		 * Metodo abstrato para oque vai acontecer quando sair do Cooldown
		 * 
		 * @param player
		 *            Jogador
		 */
		public abstract void onLeftCooldown(Player player);

		/**
		 * Metodo abstrato para oque vai acontecer quando começar o Cooldown
		 * 
		 * @param player
		 *            Jogador
		 */
		public abstract void onStartCooldown(Player player);

		/**
		 * Metodo abstrato para oque vai acontecer quando estiver ainda em Coodlwon
		 * 
		 * @param player
		 */
		public abstract void onInCooldown(Player player);

		/**
		 * Iniciando o Sistema de Cooldown
		 * 
		 * @param seconds
		 *            Segundos de Cooldown
		 */
		public Cooldowns(int seconds) {
			setTime(seconds);
		}

		/**
		 * Define o Tempo de Cooldown
		 * 
		 * @param seconds
		 *            Segundos
		 */
		public void setTime(int seconds) {
			ticks = seconds * 20;
		}

		/**
		 * 
		 * @return Tempo de Cooldown em Ticks
		 */
		public long getTicks() {
			return ticks;
		}

		public void setTicks(long ticks) {
			this.ticks = ticks;
		}

		public void setOnCooldown(Player player) {
			removeFromCooldown(player);
			onStartCooldown(player);
			CooldownEvent event = new CooldownEvent(ticks) {

				@Override
				public void run() {
					removeFromCooldown(player);
				}

			};
			event.runTaskLater(getPlugin(), ticks);
			cooldowns.put(player.getUniqueId(), event);
		}

		public int getCooldown(Player player) {
			if (onCooldown(player)) {
				CooldownEvent cooldown = cooldowns.get(player.getUniqueId());
				int result = (int) ((-cooldown.getStartTime() + System.currentTimeMillis()) / 1000);
				return (int) (cooldown.getCooldownTime() - result);
			}
			return -1;
		}

		public JavaPlugin getPlugin() {
			return JavaPlugin.getProvidingPlugin(getClass());
		}

		public boolean onCooldown(Player player) {
			return cooldowns.containsKey(player.getUniqueId());
		}

		public void removeFromCooldown(Player player) {
			if (onCooldown(player)) {
				onLeftCooldown(player);
				cooldowns.get(player.getUniqueId()).cancel();
				cooldowns.remove(player.getUniqueId());
			}
		}

		public boolean cooldown(Player player) {
			if (onCooldown(player)) {
				onInCooldown(player);
				return false;
			}
			setOnCooldown(player);
			return true;

		}
	}

	public static abstract class CooldownEvent extends BukkitRunnable {

		public CooldownEvent(long cooldownTime) {
			setStartTime(System.currentTimeMillis());
			setCooldownTime(cooldownTime);
		}

		private long cooldownTime;
		private long startTime;

		public long getStartTime() {
			return startTime;
		}

		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}

		public long getCooldownTime() {
			return cooldownTime;
		}

		public void setCooldownTime(long cooldownTime) {
			this.cooldownTime = cooldownTime;
		}

	}

	/**
	 * Mapa que armazena as Armaduras dos jogadores
	 */
	public static Map<Player, ItemStack[]> INV_ARMOURS = new HashMap<>();
	/**
	 * Mapa que armazena os Itens dos jogadores tirando as Armaduras
	 */
	public static Map<Player, ItemStack[]> INV_ITEMS = new HashMap<>();

	/**
	 * Cria um item da Cabeça do Jogador
	 * 
	 * @param name
	 *            Nome
	 * @param owner
	 *            Nome do Jogador
	 * @param amount
	 *            Quantidade
	 * @param lore
	 *            Descrição (Lista)
	 * @return O Item da Cabeça do jogador criada
	 */
	public static ItemStack newHead(String name, String owner, int amount, List<String> lore) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (short) SkullType.PLAYER.ordinal());
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(owner);
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);

		return item;
	}

	/**
	 * Cria um item da Cabeça do Jogador
	 * 
	 * @param name
	 *            Nome
	 * @param owner
	 *            Nome do Jogador
	 * @param amount
	 *            Quantidade
	 * @param lore
	 *            Descrição (Lista)
	 * @return O Item da Cabeça do jogador criada
	 */
	public static ItemStack newHead(String name, String owner, int amount, String... lore) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (short) SkullType.PLAYER.ordinal());
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(owner);
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);

		return item;
	}

	/**
	 * Descobre qual é a coluna baseada no numero
	 * 
	 * @param index
	 *            Numero
	 * @return A coluna
	 */
	public static int getColumn(int index) {
		if (index < 9) {
			return index + 1;
		}
		return (index % 9) + 1;
	}

	/**
	 * Testa se o numero passado é da coluna expecificada
	 * 
	 * @param index
	 *            Numero
	 * @param colunm
	 *            Coluna
	 * @return O resultado do teste
	 */
	public static boolean isColumn(int index, int colunm) {
		return getColumn(index) == colunm;
	}

	/**
	 * Pega um Item aleatorio baseado na lista
	 * 
	 * @param items
	 *            Lista de Itens
	 * @return O item aletario
	 */
	public static ItemStack getRandomItem(List<ItemStack> items) {

		return Mine.getRandom(items);
	}

	/**
	 * Pega um Item aleatorio baseado no vetor
	 * 
	 * @param items
	 *            Vetor de Itens
	 * @return O item aletario
	 */
	public static ItemStack getRandomItem(ItemStack... items) {

		return Mine.getRandom(items);
	}

	/**
	 * Limpa o Inventario da Entidade viva
	 * 
	 * @param entity
	 *            Entidade viva
	 */
	public static void clearArmours(LivingEntity entity) {
		entity.getEquipment().setArmorContents(null);
	}

	/**
	 * Limpa a Hotbar do Jogador
	 * 
	 * @param player
	 *            Jogador
	 */
	public static void clearHotBar(Player player) {
		for (int i = 0; i < 9; i++) {
			player.getInventory().setItem(i, null);
		}
	}

	/**
	 * Cria um item da cabeça do Jogador
	 * 
	 * @param name
	 * @param skull
	 * @return
	 */

	public static ItemStack newSkull(String name, String skull) {

		return setSkull(newItem(name, Material.SKULL_ITEM, 3), skull);
	}

	@SuppressWarnings("deprecation")
	public static ItemStack newSkull(EntityType type, String name) {
		return newSkull(name,
				("MHF_") + (type.getName() == null ? Mine.toTitle(type.name().replace("_", "")) : type.getName()));
	}

	public static int getPosition(int line, int column) {
		int value = (line - 1) * 9;
		return value + column - 1;
	}

	/**
	 * Modifica um Item transformando ele na Cabeça do Jogador
	 * 
	 * @param item
	 *            Item
	 * @param name
	 * @return Nome do Jogador
	 */
	public static ItemStack setSkull(ItemStack item, String name) {
		item.setType(Material.SKULL_ITEM);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(name);
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * Limpa todo o Inventario do Jogador
	 * 
	 * @param player
	 */
	public static void clearInventory(Player player) {
		clearItens(player);
		clearArmours(player);
	}

	/**
	 * Limpa os itens da Entidade viva
	 * 
	 * @param entity
	 *            Entidade viva
	 */
	public static void clearItens(LivingEntity entity) {
		entity.getEquipment().clear();

	}

	/**
	 * Restaura os itens armazenado no Jogador
	 * 
	 * @param player
	 *            Jogador
	 */
	public static void getItems(Player player) {
		if (INV_ITEMS.containsKey(player)) {
			player.getInventory().setContents(INV_ITEMS.get(player));
			player.updateInventory();
		}
		getArmours(player);

	}

	/**
	 * Restaura as armaduras armazenado no Jogador
	 * 
	 * @param player
	 *            Jogador
	 */
	public static void getArmours(Player player) {
		if (INV_ARMOURS.containsKey(player)) {
			player.getInventory().setArmorContents(INV_ARMOURS.get(player));
			player.updateInventory();
		}
	}

	/**
	 * Pega a quantidade de itens do Invetario
	 * 
	 * @param inventory
	 *            Inventario
	 * @return Quantidade
	 */
	public static int getItemsAmount(Inventory inventory) {
		int amount = 0;
		for (ItemStack item : inventory.getContents()) {
			if (item != null) {
				amount++;
			}
		}

		return amount;
	}

	/**
	 * Modifca toda Hotbar para um Item
	 * 
	 * @param player
	 *            Jogador
	 * @param item
	 *            Item
	 */
	public static void setHotBar(Player player, ItemStack item) {
		PlayerInventory inv = player.getInventory();
		for (int i = 0; i < 8; i++) {
			inv.setItem(i, item);
		}
	}

	/**
	 * Modifica a Descrição do Item
	 * 
	 * @param item
	 *            Item
	 * @param lore
	 *            Descrição
	 * @return Item
	 */
	public static ItemStack setLore(ItemStack item, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * Pega a quantidade total dos itens do Inventario
	 * 
	 * @param inventory
	 *            Inventario
	 * @return quantidade
	 */
	public static int getTotalAmount(Inventory inventory) {
		int amount = 0;
		for (ItemStack item : inventory.getContents()) {
			if (item != null) {
				amount += item.getAmount();
			}
		}
		return amount;
	}

	/**
	 * Pega a quantidade total do Material do Inventario
	 * 
	 * @param inventory
	 *            Inventario
	 * @param material
	 *            Tipo do Material
	 * @return Quantidade
	 */
	public static int getTotalAmount(Inventory inventory, Material material) {
		int amount = 0;
		for (ItemStack id : inventory.all(material).values()) {
			amount += id.getAmount();
		}
		return amount;
	}

	/**
	 * Pega a quantidade total do Item do Inventario
	 * 
	 * @param inventory
	 *            Inventario
	 * @param item
	 *            Item
	 * @return Quantidade
	 */
	public static int getTotalAmount(Inventory inventory, ItemStack item) {
		int amount = 0;
		for (ItemStack id : inventory.all(item.getType()).values()) {
			if (id.isSimilar(item)) {
				amount += id.getAmount();
			}
		}
		return amount;
	}

	/**
	 * Remove itens se for igual a este<br>
	 * O inv.remove(...) também remove porem remove qualquer item não importanto
	 * nome, descrição, encantamentos
	 * 
	 * @param inventory
	 *            Inventario
	 * @param item
	 *            Item
	 */
	public static void remove(Inventory inventory, ItemStack item) {
		for (Entry<Integer, ? extends ItemStack> map : inventory.all(item.getType()).entrySet()) {
			if (map.getValue().isSimilar(item)) {
				inventory.clear(map.getKey());
			}
		}
	}

	/**
	 * Remove itens se for igual a este tipo de Material<br>
	 * O inv.remove(...) também remove porem remove qualquer item não importanto
	 * nome, descrição, encantamentos
	 * 
	 * @param inventory
	 *            Inventario
	 * @param material
	 *            Tipo do Material
	 */
	public static void remove(Inventory inventory, Material material, int amount) {
		remove(inventory, new ItemStack(material), amount);
	}

	/**
	 * Remove alguns itens se for igual a este Item<br>
	 * O inv.remove(...) também remove porem remove qualquer item não importanto
	 * nome, descrição, encantamentos
	 * 
	 * @param inventory
	 *            Inventario
	 * @param material
	 *            Tipo do Material
	 * @param amount
	 *            Quantidade
	 */
	public static void remove(Inventory inventory, ItemStack item, int amount) {
		for (Entry<Integer, ? extends ItemStack> map : inventory.all(item.getType()).entrySet()) {
			if (map.getValue().isSimilar(item)) {
				ItemStack currentItem = map.getValue();
				if (currentItem.getAmount() <= amount) {
					amount -= currentItem.getAmount();
					inventory.clear(map.getKey());
				} else {
					currentItem.setAmount(currentItem.getAmount() - amount);
					amount = 0;
				}

			}
			if (amount == 0)
				break;
		}
	}

	/**
	 * Testa se o Inventario tem determinada quantidade do Item
	 * 
	 * @param inventory
	 *            Inventario
	 * @param item
	 *            Item
	 * @param amount
	 *            Quantidade
	 * @return Teste
	 */
	public static boolean contains(Inventory inventory, ItemStack item, int amount) {
		return getTotalAmount(inventory, item) >= amount;
	}

	/**
	 * Testa se o Inventario tem determinada quantidade do Tipo do Material
	 * 
	 * @param inventory
	 * @param item
	 * @param amount
	 * @return
	 */
	public static boolean contains(Inventory inventory, Material item, int amount) {
		return getTotalAmount(inventory, item) >= amount;
	}

	/**
	 * Adiciona um Encantamento no Item
	 * 
	 * @param item
	 *            Item
	 * @param type
	 *            Tipo do Material
	 * @param level
	 *            Nivel do Entamento
	 * @return Item
	 */
	public static ItemStack addEnchant(ItemStack item, Enchantment type, int level) {
		item.addUnsafeEnchantment(type, level);
		return item;
	}

	/**
	 * Adiciona itens na HotBar do Jogador
	 * 
	 * @param player
	 *            Jogador
	 * @param item
	 *            Item
	 */
	public static void addHotBar(Player player, ItemStack item) {
		PlayerInventory inv = player.getInventory();
		if (item == null)
			return;
		if (item.getType() == Material.AIR)
			return;
		if (isFull(inv))
			return;
		int i;
		while ((i = inv.firstEmpty()) < 9) {
			inv.setItem(i, item);
		}
	}

	/**
	 * Cria um Inventario
	 * 
	 * @param name
	 *            Nome
	 * @param size
	 *            Tamanho do Inventario
	 * @return Inventario
	 */
	public static Inventory newInventory(String name, int size) {

		return Bukkit.createInventory(null, size, Extra.toText(32, name));
	}

	/**
	 * Cria um Set de Couro para entidade viva
	 * 
	 * @param entity
	 *            Entidade viva
	 * @param color
	 *            Cor
	 * @param name
	 *            Nome
	 */
	public static void setEquip(LivingEntity entity, Color color, String name) {
		EntityEquipment inv = entity.getEquipment();
		inv.setBoots(setName(setColor(new ItemStack(Material.LEATHER_BOOTS), color), name));
		inv.setHelmet(setName(setColor(new ItemStack(Material.LEATHER_HELMET), color), name));
		inv.setChestplate(setName(setColor(new ItemStack(Material.LEATHER_CHESTPLATE), color), name));
		inv.setLeggings(setName(setColor(new ItemStack(Material.LEATHER_LEGGINGS), color), name));
	}

	/**
	 * Ganha todos os Itens do Inventario
	 * 
	 * @param items
	 *            Itens
	 * @param inventory
	 *            Inventario
	 */
	public static void give(Collection<ItemStack> items, Inventory inventory) {
		for (ItemStack item : items) {
			inventory.addItem(item);
		}
	}

	/**
	 * Pega o descrição do Item
	 * 
	 * @param item
	 *            Item
	 * @return Descrição
	 */
	public static List<String> getLore(ItemStack item) {
		if (item != null) {
			if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
				return item.getItemMeta().getLore();
			}
		}
		return new ArrayList<String>();
	}

	/**
	 * Testa se o Inventario esta cheio
	 * 
	 * @param inventory
	 *            Inventario
	 * @return Teste
	 */
	public static boolean isFull(Inventory inventory) {
		return inventory.firstEmpty() == -1;
	}

	/**
	 * Testa se a Entidade viva esta usando na mao o Tipo do Material
	 * 
	 * @param entity
	 *            Entitade
	 * @param material
	 *            Tipo de Material
	 * @return Teste
	 */
	public static boolean isUsing(LivingEntity entity, Material material) {
		return (getHandType(entity) == material);
	}

	/**
	 * Testa se a Entidade viva esta usando na mao um Tipo do Material com este nome
	 * 
	 * @param entity
	 *            Entidade
	 * @param material
	 *            Material
	 * @return
	 */
	public static boolean isUsing(LivingEntity entity, String material) {
		return getHandType(entity).name().toLowerCase().contains(material.toLowerCase());
	}

	/**
	 * Dropa um item no Local da entidade
	 * 
	 * @param entity
	 *            Entidade
	 * @param item
	 *            Item
	 */
	public static void drop(Entity entity, ItemStack item) {
		drop(entity.getLocation(), item);
	}

	/**
	 * Pega o tipo do material da mao da Entidade viva
	 * 
	 * @param entity
	 *            Entidade viva
	 * @return Tipo do Material
	 */
	public static Material getHandType(LivingEntity entity) {
		EntityEquipment inv = entity.getEquipment();
		if (inv == null) {
			return Material.AIR;
		}
		ItemStack item = inv.getItemInHand();
		if (item == null) {
			return Material.AIR;
		}

		return item.getType();
	}

	/**
	 * Cria um Item da Cabeça do Jogador
	 * 
	 * @param name
	 *            Nome de Jogador
	 * @return Item
	 */
	public static ItemStack getHead(String name) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(name);
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * Testa se o Inventario esta vasio
	 * 
	 * @param inventory
	 * @return Teste
	 */
	public static boolean isEmpty(Inventory inventory) {

		for (ItemStack item : inventory.getContents()) {
			if (item != null) {
				return false;
			}

		}
		return true;
	}

	/**
	 * Modifica a Cor do Item (Usado somente para Itens de Couro)
	 * 
	 * @param item
	 *            Item de Couro
	 * @param color
	 *            Cor
	 * @return Item
	 */
	public static ItemStack setColor(ItemStack item, Color color) {
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(color);
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * Modifica a Descrição do Item
	 * 
	 * @param item
	 *            Item
	 * @param lore
	 *            Descrição
	 * @return Item
	 */
	public static ItemStack setLore(ItemStack item, String... lore) {

		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.setLore(Arrays.asList(lore));
			item.setItemMeta(meta);
		}
		return item;
	}

	/**
	 * Modifica o Nome do Item
	 * 
	 * @param item
	 *            Item
	 * @param name
	 *            Novo Nome
	 * @return Item
	 */
	public static ItemStack setName(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(name);
			item.setItemMeta(meta);
		}

		return item;
	}

	/**
	 * Restaura o Nome Original do Item
	 * 
	 * @param item
	 *            Item
	 * @return Nome
	 */
	public static ItemStack resetName(ItemStack item) {
		setName(item, "");
		return item;
	}

	/**
	 * Pega o Nome do Item
	 * 
	 * @param item
	 *            Item
	 * @return Nome
	 */
	public static String getName(ItemStack item) {

		return item.hasItemMeta() ? item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : "" : "";
	}

	/**
	 * Dropa o Item no Local (Joga no Local)
	 * 
	 * @param location
	 *            Local
	 * @param item
	 *            --------- * Item
	 */
	public static void drop(Location location, ItemStack item) {
		location.getWorld().dropItemNaturally(location, item);
	}

	/**
	 * Enche o Invetario com o Item
	 * 
	 * @param inventory
	 *            Inventario
	 * @param item
	 *            Item
	 */
	public static void fill(Inventory inventory, ItemStack item) {
		int id;
		while ((id = inventory.firstEmpty()) != -1) {
			inventory.setItem(id, item);
		}
	}

	/**
	 * Pega a quantidade de dano causada pelo Item
	 * 
	 * @param item
	 *            Item
	 * @return Quantidade
	 */
	public static double getDamage(ItemStack item) {
		if (item == null)
			return 0;
		double damage = 0;
		String name = item.getType().name();
		for (int id = 0; id <= 4; id++) {
			String value = "";
			if (id == 0) {
				value = "DIAMOND_";
				damage += 3;
			}
			if (id == 1) {
				value = "IRON_";
				damage += 2;
			}
			if (id == 2) {
				value = "GOLD_";
			}
			if (id == 3) {
				value = "STONE_";
				damage++;
			}
			if (id == 4) {
				value = "WOOD_";
			}

			for (int x = 0; x <= 3; x++) {
				double newDamage = damage;
				if (x == 0) {
					value = "SWORD";
					newDamage += 4;
				}
				if (x == 1) {
					value = "AXE";
					newDamage += 3;
				}
				if (x == 2) {
					value = "PICKAXE";
					newDamage += 2;
				}
				if (x == 3) {
					value = "SPADE";
					newDamage++;
				}

				if (name.equals(value)) {
					return newDamage;
				}
			}
			damage = 0;
		}
		return damage;
	}

	/**
	 * Armazena os Itens do Jogador
	 * 
	 * @param player
	 */
	public static void saveItems(Player player) {
		saveArmours(player);
		INV_ITEMS.put(player, player.getInventory().getContents());
	}

	/**
	 * Armazena as armaduras do Jogador
	 * 
	 * @param player
	 */
	public static void saveArmours(Player player) {
		INV_ARMOURS.put(player, player.getInventory().getArmorContents());
	}

	/**
	 * Cria um Item
	 * 
	 * @param material
	 * @param name
	 * @return
	 */
	public static ItemStack newItem(Material material, String name) {
		ItemStack item = new ItemStack(material);
		setName(item, name);
		return item;
	}

	/**
	 * Cria um Item
	 * 
	 * @param material
	 *            Material
	 * @param name
	 *            Nome
	 * @param amount
	 *            Quantidade
	 * @return Item
	 */
	public static ItemStack newItem(Material material, String name, int amount) {
		return newItem(material, name, amount, 0);
	}

	/**
	 * Cria um Item
	 * 
	 * @param material
	 *            Material
	 * @param name
	 *            Nome
	 * @param amount
	 *            Quantidade
	 * @param data
	 *            MetaData
	 * @param lore
	 *            Descrição
	 * @return Item
	 */
	public static ItemStack newItem(Material material, String name, int amount, int data, String... lore) {

		ItemStack item = newItem(material, name);
		setLore(item, lore);
		item.setAmount(amount);
		item.setDurability((short) data);
		return item;
	}

	/**
	 * Cria um Item
	 * 
	 * @param material
	 *            Material
	 * @param name
	 *            Nome
	 * @param amount
	 *            Quantidade
	 * @param data
	 *            MetaData
	 * @param lore
	 *            Descrição
	 * @return Item
	 */
	public static ItemStack newItem(int id, String name, int amount, int data, String... lore) {

		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(id, amount, (short) data);
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(name);
			meta.setLore(Arrays.asList(lore));
			item.setItemMeta(meta);
		}
		return item;
	}

	/**
	 * Cria um Item
	 * 
	 * @param material
	 *            Material
	 * @param name
	 *            Nome
	 * @param amount
	 *            Quantidade
	 * @param data
	 *            MetaData
	 * @param lore
	 *            Descrição
	 * @return Item
	 */
	public static ItemStack newItem(int id, String name, int amount, int data, List<String> lore) {

		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(id, amount, (short) data);
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(name);
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		return item;
	}

	/**
	 * Cria um Item
	 * 
	 * @param name
	 *            Nome
	 * @param material
	 *            Material
	 * @return
	 */
	public static ItemStack newItem(String name, Material material) {
		ItemStack item = new ItemStack(material);
		setName(item, name);
		return item;
	}

	/**
	 * Cria um Item
	 * 
	 * @param name
	 *            Nome
	 * @param material
	 *            Material
	 * @param data
	 *            MetaData
	 * @return Item
	 */
	public static ItemStack newItem(String name, Material material, int data) {
		return newItem(material, name, 1, data);
	}

	/**
	 * Cria um Item
	 * 
	 * @param name
	 *            Nome
	 * @param material
	 *            Material
	 * @param amount
	 *            Quantidade
	 * @param data
	 *            Data
	 * @param lore
	 *            Descrição
	 * @return Item
	 */
	public static ItemStack newItem(String name, Material material, int amount, int data, String... lore) {
		return newItem(material, name, amount, data, lore);
	}

	public static Player getPlayer(String name) {
		return Bukkit.getPlayerExact(name);
	}

	public static World getWorld(String name) {
		return Bukkit.getWorld(name);
	}

	public static void removeReplacer(String replacer) {
		replacers.remove(replacer);
	}

	public static void send(CommandSender sender, String message) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			sender.sendMessage(Mine.getReplacers(message, player));
		} else {
			sender.sendMessage(message);
			;
		}

	}

	public static void sendAll(Player p, String message) {
		broadcast(getReplacers(message, p));

	}
	public static PlayerManager getPlayerManager() {
		return playerManager;
	}
	public static void setPlayerManager(PlayerManager playerManager) {
		Mine.playerManager = playerManager;
	}

	

}
