package net.eduard.api.lib;

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
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
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

import net.eduard.api.lib.event.PlayerTargetEvent;
import net.eduard.api.lib.game.Schematic;
import net.eduard.api.lib.game.Sounds;
import net.eduard.api.lib.manager.PlayersManager;
import net.eduard.api.lib.manager.TimeManager;
import net.eduard.api.lib.modules.EmptyWorldGenerator;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.modules.FakePlayer;

/**
 * API principal da Lib contendo muitos codigos bons e utilitarios
 * 
 * @author Eduard
 * @version 3.0
 */
public final class Mine {
	/**
	 * Efeito a fazer na localização
	 * 
	 * @author Eduard
	 *
	 */
	public static interface LocationEffect {

		boolean effect(Location location);
	}

	static {
		Extra.newReplacer("#v", Mine.getVersion());
	}

	public static Class<?> getClassFrom(Object object) throws Exception {
		return Extra.getClassFrom(object);
	}

	public static String formatDate(long date) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTimeInMillis(date);

		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");

		return formatador.format(calendario.getTime());
	}

	public static String formatHours(long milisegundos) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTimeInMillis(milisegundos);

		SimpleDateFormat formatador = new SimpleDateFormat("HH:mm:ss");

		return formatador.format(calendario.getTime());
	}

	public static String classMineEntityPlayer = "#mEntityPlayer";
	public static String classCraftCraftPlayer = "#cCraftPlayer";
	public static String classSpigotPacketTitle = "#sProtocolInjector$PacketTitle";
	public static String classSpigotAction = "#sProtocolInjector$PacketTitle$Action";
	public static String classSpigotPacketTabHeader = "#sProtocolInjector$PacketTabHeader";
	public static String classPacketPlayOutChat = "#pPlayOutChat";
	public static String classPacketPlayOutTitle = "#pPlayOutTitle";
	public static String classPacketPlayOutWorldParticles = "#pPlayOutWorldParticles";
	public static String classPacketPlayOutPlayerListHeaderFooter = "#pPlayOutPlayerListHeaderFooter";
	public static String classPacketPlayOutNamedEntitySpawn = "#pPlayOutNamedEntitySpawn";
	public static String classPacketPlayInClientCommand = "#pPlayInClientCommand";
	public static String classCraftEnumTitleAction = "#cEnumTitleAction";
	public static String classPacketEnumTitleAction2 = "#pPlayOutTitle$EnumTitleAction";
	public static String classMineEnumClientCommand = "#mEnumClientCommand";
	public static String classMineEnumClientCommand2 = "#pPlayInClientCommand$EnumClientCommand";
	public static String classMineChatSerializer = "#mChatSerializer";
	public static String classMineIChatBaseComponent = "#mIChatBaseComponent";
	public static String classMineEntityHuman = "#mEntityHuman";
	public static String classMineNBTTagCompound = "#mNBTTagCompound";
	public static String classMineNBTBase = "#mNBTBase";
	public static String classMineNBTTagList = "#mNBTTagList";
	public static String classPacketPacket = "#p";
	public static String classCraftItemStack = "#cinventory.CraftItemStack";
	public static String classMineItemStack = "#mItemStack";
	public static String classBukkitItemStack = "#bItemStack";
	public static String classBukkitBukkit = "#bBukkit";
	public static String classMineChatComponentText = "#mChatComponentText";
	public static String classMineMinecraftServer = "#mMinecraftServer";
	static {
		Extra.newReplacer("#v", Mine.getVersion());
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

	public static interface Replacer {

		Object getText(Player p);
	}

	/**
	 * Gerenciador dos jogadores
	 */
	private static PlayersManager playerManager;

	/*
	 * Mapa de Arenas registradas
	 */
	public static Map<String, Schematic> MAPS = new HashMap<>();

	public static Map<Player, Schematic> MAPS_CACHE = new HashMap<>();
	public static ConfigAPI MAPS_CONFIG;
	/**
	 * Som do rosnar do gato
	 */
	@SuppressWarnings("unused")
	private static final Sounds ROSNAR = Sounds.create("CAT_PURR");

	/**
	 * Mensagem de quando console digita um comando
	 */
	public static String MSG_ONLY_PLAYER = "§cApenas jogadores pode fazer este comando!";

	/**
	 * Mensagem de quando o Mundo é invalido
	 */
	public static String MSG_WORLD_NOT_EXISTS = "§cEste mundo $world não existe!";

	/**
	 * Mensagem de quando o jogador é invalido
	 */
	public static String MSG_PLAYER_NOT_EXISTS = "§cEste jogador $player não existe!";

	/**
	 * Mensagem de quando plugin é invalido
	 */
	public static String MSG_PLUGIN_NOT_EXITS = "§cEste plugin $plugin não exite!";

	/**
	 * Mensagem de quando não tem permissão
	 */
	public static String MSG_NO_PERMISSION = "§cVoce não tem permissão para usar este comando!";
	/**
	 * Mensagem de quando Entrar no Servidor
	 */
	public static String MSG_ON_JOIN = "§6O jogador $player entrou no Jogo!";
	/**
	 * Mensagem de quando Sair do Servidor
	 */
	public static String MSG_ON_QUIT = "§6O jogador $player saiu no Jogo!";
	/**
	 * Prefixo de Ajuda dos Comandos
	 */
	public static String MSG_USAGE = "§FDigite: §c";
	/**
	 * Lista de Comandos para efeito Positivo
	 */
	public static List<String> OPT_COMMANDS_ON = new ArrayList<>(Arrays.asList("on", "ativar"));
	/**
	 * Lista de Comandos para efeito Negativo
	 */
	public static List<String> OPT_COMMANDS_OFF = new ArrayList<>(Arrays.asList("off", "desativar"));
	/**
	 * Som para o Teleporte
	 */
	public static Sounds OPT_SOUND_TELEPORT = Sounds.create("ENDERMAN_TELEPORT");
	/**
	 * Som para algum sucesso
	 */
	public static Sounds OPT_SOUND_SUCCESS = Sounds.create("LEVEL_UP");
	/**
	 * Som para algum erro
	 */
	public static Sounds OPT_SOUND_ERROR = Sounds.create("NOTE_BASS_DRUM");
	/**
	 * Desativar mensagem de morte
	 */
	public static boolean OPT_NO_DEATH_MESSAGE = true;
	/**
	 * Desativar mensagem de entrada
	 */
	public static boolean OPT_NO_JOIN_MESSAGE = true;
	/**
	 * Desativar mensagem de saida
	 */
	public static boolean OPT_NO_QUIT_MESSAGE = true;
	/**
	 * Velocidade minima de corrida
	 */
	public static final double VALUE_MIN_WALK_SPEED = 0.2;
	/**
	 * Velocidade minima de voo
	 */
	public static final double VALUE_MIN_FLY_SPEED = 0.1;
	/**
	 * Ligar sistema de Respawn Automatico
	 */
	public static boolean OPT_AUTO_RESPAWN = true;
	/**
	 * Controlador de Tempo da Mine
	 */
	public static TimeManager TIME;

	/**
	 * Ligando algumas coisas
	 */
	static {
		try {
			TIME = new TimeManager(getMainPlugin());
			MAPS_CONFIG = new ConfigAPI("maps/", getMainPlugin());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * Mapa que armazena as Armaduras dos jogadores
	 */
	private static final Map<Player, ItemStack[]> PLAYERS_ARMOURS = new HashMap<>();
	/**
	 * Mapa que armazena os Itens dos jogadores tirando as Armaduras
	 */
	private static final Map<Player, ItemStack[]> PLAYERS_ITEMS = new HashMap<>();

	private static Map<String, Replacer> replacers = new HashMap<>();

	/**
	 * Adiciona um Encantamento no Item
	 * 
	 * @param item  Item
	 * @param type  Tipo do Material
	 * @param level Nivel do Entamento
	 * @return Item
	 */
	public static ItemStack addEnchant(ItemStack item, Enchantment type, int level) {
		item.addUnsafeEnchantment(type, level);
		return item;
	}

	/**
	 * Adiciona itens na HotBar do Jogador
	 * 
	 * @param player Jogador
	 * @param item   Item
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

	public static void addPermission(Player p, String permission) {
		p.addAttachment(getMainPlugin(), permission, true);
	}

	public static void addPermission(String permission) {
		Bukkit.getPluginManager().addPermission(new Permission(permission));
	}

	public static void addReplacer(String key, Replacer value) {
		replacers.put(key, value);
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

	public static void box(String[] paragraph, String title) {
		Extra.box(paragraph, title);
	}

	public static void broadcast(String message) {
		Bukkit.broadcastMessage(message);
	}

	public static void broadcast(String message, String permission) {
		for (Player player : Mine.getPlayers()) {
			if (player.hasPermission(permission))
				player.sendMessage(message);
		}
	}

	public static void callEvent(Event event) {

		Bukkit.getPluginManager().callEvent(event);
	}

	/**
	 * Modifica o nome do Jogador para um Novo Nome e<br>
	 * Envia para Todos os outros Jogadores a alteração (Packet)
	 * 
	 * @param player      Jogador
	 * @param displayName Novo Nome
	 */
	public static void changeName(Player player, String displayName) {

		try {
			Object entityplayer = getHandle(player);
			// PacketPlayOutNamedEntitySpawn a;
			// EntityPlayer c;
			// PacketPlayOutEntity d;
			// PacketPlayOutSpawnEntityLiving e;

			// EntityHuman b;
			Field profileField = Extra.getField(Mine.classMineEntityHuman, "bH");
			Object gameprofile = profileField.get(entityplayer);
			// Object before = Extra.getValue(gameprofile, "name");
			Extra.setValue(gameprofile, "name", displayName);
			// EntityPlayer a;
			// Object packet = Extra.getNew(Mine.classPacketPlayOutNamedEntitySpawn,
			// Extra.getParameters(Mine.classMineEntityHuman),
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

	public static void changeTabName(Player player, String displayName) {
		player.setPlayerListName(Mine.getText(32, displayName));
	}

	public static void chat(CommandSender sender, String message) {
		Mine.send(sender, message);

	}

	/**
	 * Limpa o Inventario da Entidade viva
	 * 
	 * @param entity Entidade viva
	 */
	public static void clearArmours(LivingEntity entity) {
		entity.getEquipment().setArmorContents(null);
	}

	/**
	 * Limpa a Hotbar do Jogador
	 * 
	 * @param player Jogador
	 */
	public static void clearHotBar(Player player) {
		for (int i = 0; i < 9; i++) {
			player.getInventory().setItem(i, null);
		}
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
	 * @param entity Entidade viva
	 */
	public static void clearItens(LivingEntity entity) {
		entity.getEquipment().clear();

	}

	public static PluginCommand command(String commandName, CommandExecutor command) {
		PluginCommand cmd = Bukkit.getPluginCommand(commandName);
		cmd.setExecutor(command);
		cmd.setPermissionMessage(Mine.MSG_NO_PERMISSION.replace("$permission", cmd.getPermission()));
		return cmd;
	}

	public static PluginCommand command(String commandName, CommandExecutor command, String permission) {

		PluginCommand cmd = Bukkit.getPluginCommand(commandName);
		cmd.setExecutor(command);
		cmd.setPermission(permission);
		cmd.setPermissionMessage(Mine.MSG_NO_PERMISSION.replace("$permission", cmd.getPermission()));
		return cmd;
	}

	public static PluginCommand command(String commandName, CommandExecutor command, String permission,
			String permissionMessage) {

		PluginCommand cmd = Bukkit.getPluginCommand(commandName);
		cmd.setExecutor(command);
		cmd.setPermission(permission);
		cmd.setPermissionMessage(permissionMessage);
		return cmd;
	}

	public static void console(String message) {
		Bukkit.getConsoleSender().sendMessage(message);
	}

	/**
	 * Testa se o Inventario tem determinada quantidade do Item
	 * 
	 * @param inventory Inventario
	 * @param item      Item
	 * @param amount    Quantidade
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

	public static boolean contains(String message, String text) {
		return Extra.contains(message, text);
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

	public static World copyWorld(String fromWorld, String toWorld) {
		unloadWorld(fromWorld);
		unloadWorld(toWorld);
		deleteWorld(toWorld);
		copyWorldFolder(getWorldFolder(fromWorld), getWorldFolder(toWorld));
		return loadWorld(toWorld);
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

	public static void createCage(Location loc, Material type) {
		loc.clone().add(0, -1, 0).getBlock().setType(type, true);
		loc.clone().add(0, 3, 0).getBlock().setType(type, true);
		loc.clone().add(0, 0, 1).getBlock().setType(type, true);
		loc.clone().add(0, 0, -1).getBlock().setType(type, true);
		loc.clone().add(1, 0, 0).getBlock().setType(type, true);
		loc.clone().add(-1, 0, 0).getBlock().setType(type, true);
		//
		loc.clone().add(0, 1, 1).getBlock().setType(type, true);
		loc.clone().add(0, 1, -1).getBlock().setType(type, true);
		loc.clone().add(1, 1, 0).getBlock().setType(type, true);
		loc.clone().add(-1, 1, 0).getBlock().setType(type, true);
		//
		loc.clone().add(0, 2, 1).getBlock().setType(type, true);
		loc.clone().add(0, 2, -1).getBlock().setType(type, true);
		loc.clone().add(1, 2, 0).getBlock().setType(type, true);
		loc.clone().add(-1, 2, 0).getBlock().setType(type, true);

	}

	public static boolean createCommand(Plugin plugin, Command... cmds) {
		try {
			Class<?> serverClass = Extra.getClassFrom(Bukkit.getServer());
			Field field = serverClass.getDeclaredField("commandMap");
			field.setAccessible(true);
			CommandMap map = (CommandMap) field.get(Bukkit.getServer());
			for (Command cmd : cmds) {
				map.register(plugin.getName(), cmd);
			}
			// }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return OPT_AUTO_RESPAWN;
	}

	// public static ItemStack reloadItem(String text) {
	// return (ItemStack) new Item().get(text);
	// }
	//
	// public static String saveItem(ItemStack item) {
	// return (String) new Item().save(item);
	// }

	public static String cutText(String text, int lenght) {
		return text.length() > lenght ? text.substring(0, lenght) : text;
	}

	public static BukkitTask delay(Plugin plugin, long ticks, Runnable run) {
		return Bukkit.getScheduler().runTaskLater(plugin, run, ticks);
	}

	public static BukkitTask delays(Plugin plugin, long ticks, Runnable run) {
		return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, run, ticks);
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

	public static void deleteWorld(String name) {
		unloadWorld(name);
		deleteFolder(getWorldFolder(name));
	}

	/**
	 * Desabilita a Inteligencia da Entidade
	 * 
	 * @param entity Entidade
	 */
	public static void disableAI(Entity entity) {
		try {
			// net.minecraft.server.v1_8_R3.Entity NMS = ((CraftEntity)
			// entidade).getHandle();
			// NBTTagCompound compound = new NBTTagCompound();
			// NMS.c(compound);
			// compound.setByte("NoAI", (byte) 1);
			// NMS.f(compound);
			Object compound = Extra.getNew(Mine.classMineNBTTagCompound);
			Object getHandle = Extra.getResult(entity, "getHandle");
			Extra.getResult(getHandle, "c", compound);
			Extra.getResult(compound, "setByte", "NoAI", (byte) 1);
			Extra.getResult(getHandle, "f", compound);

		} catch (Exception e) {
		}

	}

	public static double distanceX(Location loc1, Location loc2) {
		return loc1.getX() - loc2.getX();
	}

	public static double distanceZ(Location loc1, Location loc2) {
		return loc1.getZ() - loc2.getZ();
	}

	/**
	 * Dropa um item no Local da entidade
	 * 
	 * @param entity Entidade
	 * @param item   Item
	 */
	public static void drop(Entity entity, ItemStack item) {
		drop(entity.getLocation(), item);
	}

	/**
	 * Dropa o Item no Local (Joga no Local)
	 * 
	 * @param location Local
	 * @param item     --------- * Item
	 */
	public static void drop(Location location, ItemStack item) {
		location.getWorld().dropItemNaturally(location, item);
	}

	/**
	 * Checa se Chunk1 é igual a Chunk2
	 * 
	 * @param chunk1
	 * @param chunk2
	 * @return
	 */
	public static boolean equals(Chunk chunk1, Chunk chunk2) {
		return chunk1.getX() == chunk2.getX() && chunk1.getZ() == chunk2.getZ();
	}

	public static boolean equals(ItemStack item, ItemStack stack) {
		return getLore(item).equals(getLore(stack)) && getName(item).equals(getName(stack))
				&& item.getType() == stack.getType() && item.getAmount() == stack.getAmount()
				&& item.getDurability() == stack.getDurability();
	}

	public static boolean equals(Location location1, Location location2) {

		return getBlockLocation1(location1).equals(getBlockLocation1(location2));
	}

	public static boolean equals2(Location location1, Location location2) {
		return location1.getBlock().getLocation().equals(location2.getBlock().getLocation());
	}

	public static boolean existsPlayer(CommandSender sender, String player) {

		Player p = Bukkit.getPlayer(player);
		if (p == null) {
			sender.sendMessage(Mine.MSG_PLAYER_NOT_EXISTS.replace("$player", player));
			return false;
		}
		return true;
	}

	public static boolean existsPlugin(CommandSender sender, String plugin) {

		Plugin p = getPlugin(plugin);
		if (p == null) {
			sender.sendMessage(Mine.MSG_PLUGIN_NOT_EXITS.replace("$plugin", plugin));
			return false;
		}
		return true;
	}

	public static boolean existsWorld(CommandSender sender, String name) {
		World world = Bukkit.getWorld(name);
		if (world == null) {
			sender.sendMessage(Mine.MSG_WORLD_NOT_EXISTS.replace("$world", name));
			return false;
		}
		return true;
	}

	/**
	 * Enche o Invetario com o Item
	 * 
	 * @param inventory Inventario
	 * @param item      Item
	 */
	public static void fill(Inventory inventory, ItemStack item) {
		int id;
		while ((id = inventory.firstEmpty()) != -1) {
			inventory.setItem(id, item);
		}
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

	public static String formatColors(String str) {
		return Extra.formatColors(str);
	}

	public static String formatDiference(long timestamp) {
		return formatTime(timestamp - System.currentTimeMillis());
	}

	public static String formatTime(long time) {
		return Extra.formatTime(time);
	}

	/**
	 * Restaura as armaduras armazenado no Jogador
	 * 
	 * @param player Jogador
	 */
	public static void getArmours(Player player) {
		if (PLAYERS_ARMOURS.containsKey(player)) {
			player.getInventory().setArmorContents(PLAYERS_ARMOURS.get(player));
			player.updateInventory();
		}
	}

	public static ArrayList<String> getAsciiCompass(double inDegrees, ChatColor colorActive, String colorDefault) {
		return getAsciiCompass(getCompassPointForDirection(inDegrees), colorActive, colorDefault);
	}

	public static ArrayList<String> getAsciiCompass(double inDegrees, String colorActive, String colorDefault) {
		return getAsciiCompass(getCompassPointForDirection(inDegrees), colorActive, colorDefault);
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

	public static Location getBlockLocation1(Location location) {

		return new Location(location.getWorld(), (int) location.getX(), (int) location.getY(), (int) location.getZ());
	}

	public static Location getBlockLocation2(Location location) {

		return location.getBlock().getLocation();
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

	public static List<Location> getBox(Location playerLocation, double higher, double lower, double size,
			LocationEffect effect) {
		Location high = getHighLocation(playerLocation.clone(), higher, size);
		Location low = getLowLocation(playerLocation.clone(), lower, size);
		return getLocations(low, high, effect);
	}

	public static boolean getChance(double chance) {

		return Extra.getChance(chance);
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

	/**
	 * Pega classes de um plugin pela package da Classe
	 * 
	 * @param plugin    Plugin
	 * @param clazzName Classe
	 * @return Lista de Classes
	 */
	public static List<Class<?>> getClasses(JavaPlugin plugin, Class<?> clazzName) {
		return getClasses(plugin, clazzName.getPackage().getName());
	}

	/**
	 * Pega classes de um plugin pela Classe
	 * 
	 * @param plugin  Plugin
	 * @param pkgname Pacote
	 * @return Lista de Classes
	 */
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

	public static String getCmd(String message) {
		return Extra.getCommandName(message);
	}

	/**
	 * Descobre qual é a coluna baseada no numero
	 * 
	 * @param index Numero
	 * @return A coluna
	 */
	public static int getColumn(int index) {
		if (index < 9) {
			return index + 1;
		}
		return (index % 9) + 1;
	}
	
	public static int getLine(int index	) {
		return (index/9)+1;
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

	/**
	 * Retorna Um PlayerConnection pela variavel playerConnection da classe
	 * EntityPlayer <Br>
	 * Pega o EntityPlayer pelo metodo getHandle(player)
	 * 
	 * @param player Jogador (CraftPlayer)
	 * @return Conexão do jogador
	 */
	public static Object getConnection(Player player) throws Exception {
		return Extra.getValue(getHandle(player), "playerConnection");
	}

	public static long getCooldown(long before, long seconds) {
		return Extra.getCooldown(before, seconds);

	}

	public static int getCurrentTick() throws Exception {
		return (int) Extra.getValue(Mine.classMineMinecraftServer, "currentTick");
	}

	/**
	 * Pega a quantidade de dano causada pelo Item
	 * 
	 * @param item Item
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

	public static Vector getDiretion(Location location, Location target) {
		double distance = target.distance(location);
		double x = ((target.getX() - location.getX()) / distance);
		double y = ((target.getY() - location.getY()) / distance);
		double z = ((target.getZ() - location.getZ()) / distance);
		return new Vector(x, y, z);
	}

	public static int getEmptySlotsAmount(Inventory inv) {
		int amount = 0;
		for (ItemStack item : inv.getContents()) {
			if (item == null) {
				amount++;
			} else {
				if (item.getType() == Material.AIR) {
					amount++;
				}
			}

		}
		return amount;
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
		if (str.equalsIgnoreCase("PROTECTION"))
			return Enchantment.PROTECTION_ENVIRONMENTAL;
		if (str.equalsIgnoreCase("UNBREAKING"))
			return Enchantment.DURABILITY;
		if (str.equalsIgnoreCase("FIREPROTECTION"))
			return Enchantment.PROTECTION_FIRE;
		if (str.equalsIgnoreCase("FEATHERFALLING"))
			return Enchantment.PROTECTION_FALL;
		if (str.equalsIgnoreCase("BLASTPROTECTION"))
			return Enchantment.PROTECTION_EXPLOSIONS;
		if (str.equalsIgnoreCase("SHARPNESS"))
			return Enchantment.DAMAGE_ALL;
		if (str.equalsIgnoreCase("KNOCKBACK"))
			return Enchantment.KNOCKBACK;
		if (str.equalsIgnoreCase("FIREASPECT"))
			return Enchantment.FIRE_ASPECT;
		if (str.equalsIgnoreCase("LOOTING"))
			return Enchantment.LOOT_BONUS_MOBS;
		if (str.equalsIgnoreCase("FORTUNE"))
			return Enchantment.LOOT_BONUS_BLOCKS;
		if (str.equalsIgnoreCase("SILKTOUCH"))
			return Enchantment.SILK_TOUCH;
		if (str.equalsIgnoreCase("EFFICIENCY"))
			return Enchantment.DIG_SPEED;
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

	/**
	 * @param player Jogador (CraftPlayer)
	 * @return EntityPlayer pelo metodo getHandle da classe CraftPlayer(Player)
	 * @exception Exception
	 */
	public static Object getHandle(Player player) throws Exception {
		return Extra.getResult(player, "getHandle");
	}

	/**
	 * Pega o tipo do material da mao da Entidade viva
	 * 
	 * @param entity Entidade viva
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
	 * @param name Nome de Jogador
	 * @return Item
	 */
	public static ItemStack getHead(String name) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(name);
		item.setItemMeta(meta);
		return item;
	}

	public static Location getHighLocation(Location loc, double high, double size) {

		loc.add(size, high, size);
		return loc;
	}

	public static Location getHighLocation(Location loc1, Location loc2) {

		double x = Math.max(loc1.getX(), loc2.getX());
		double y = Math.max(loc1.getY(), loc2.getY());
		double z = Math.max(loc1.getZ(), loc2.getZ());
		return new Location(loc1.getWorld(), x, y, z);
	}

	public static Location getHighPosition(Location location) {
		return location.getWorld().getHighestBlockAt(location).getLocation();
	}

	/**
	 * Inicia um IChatBaseComponent pelo metodo a(String) da classe ChatSerializer
	 * 
	 * @param component Componente (Texto)
	 * @return IChatBaseComponent iniciado
	 */
	public static Object getIChatBaseComponent(String component) throws Exception {
		return Extra.getResult(Mine.classMineChatSerializer, "a", component);
	}

	/**
	 * Inicia um IChatBaseComponent pelo metodo a(String) da classe ChatSerializer
	 * adicionando componente texto
	 * 
	 * @return IChatBaseComponent iniciado
	 * @param text Texto
	 */
	public static Object getIChatText(String text) throws Exception {
		return getIChatBaseComponent(getIComponentText(text));
	}

	/**
	 * Inicia um ChatComponentText"IChatBaseComponent" pelo cons(String) da classe
	 * ChatComponentText
	 * 
	 * @param text Texto
	 * @return ChatComponentText iniciado
	 * 
	 */
	public static Object getIChatText2(String text) throws Exception {
		return Extra.getNew(Mine.classMineChatComponentText, text);

	}

	/**
	 * @param text Texto
	 * @return "{\"text\":\"" + text + "\"}"
	 */
	public static String getIComponentText(String text) {
		return ("{\"text\":\"" + text + "\"}");

	}

	/**
	 * Pega o Ip do Jogador atual
	 * 
	 * @param player Jogador
	 * @return Ip do Jogador
	 */
	public static String getIp(Player player) {
		return player.getAddress().getAddress().getHostAddress();
	}

	/**
	 * Restaura os itens armazenado no Jogador
	 * 
	 * @param player Jogador
	 */
	public static void getItems(Player player) {
		if (PLAYERS_ITEMS.containsKey(player)) {
			player.getInventory().setContents(PLAYERS_ITEMS.get(player));
			player.updateInventory();
		}
		getArmours(player);

	}

	/**
	 * Pega a quantidade de itens do Invetario
	 * 
	 * @param inventory Inventario
	 * @return Quantidade
	 */
	public static int getItemsAmount(Inventory inventory) {
		int amount = 0;
		for (ItemStack item : inventory.getContents()) {
			if (item != null&&item.getType()!=Material.AIR) {
				amount++;
			}
		}

		return amount;
	}

	/**
	 * Pega classes de um plugin pela package da Classe que implementam Listener
	 * 
	 * @param plugin    Plugin
	 * @param clazzName Classe
	 * @return Lista de Classes de Listener
	 */
	public static List<Class<?>> getListeners(JavaPlugin plugin, String packname) {

		return getClasses(plugin, packname).stream().filter(classe -> classe != null)
				.filter(classe -> Listener.class.isAssignableFrom(classe)).collect(Collectors.toList());
	}

	/**
	 * Pega uma lista de Entidades baseada em um Argumento (Texto)
	 * 
	 * @param argument Texto
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

	public static List<Location> getLocations(Location location1, Location location2) {
		return getLocations(location1, location2, new LocationEffect() {

			@Override
			public boolean effect(Location location) {
				return true;
			}
		});
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

	/**
	 * Pega o descrição do Item
	 * 
	 * @param item Item
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

	/**
	 * Pega o plugin que ligo a Mine
	 * 
	 * @return Plugin
	 */
	public static JavaPlugin getMainPlugin() {
		return JavaPlugin.getProvidingPlugin(Mine.class);
	}

	public static Scoreboard getMainScoreboard() {
		return Bukkit.getScoreboardManager().getMainScoreboard();
	}

	/**
	 * Pega o Nome do Item
	 * 
	 * @param item Item
	 * @return Nome
	 */
	public static String getName(ItemStack item) {

		return item.hasItemMeta() ? item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : "" : "";
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

	public static List<LivingEntity> getNearbyEntities(LivingEntity entity, double radio, EntityType... entities) {

		return getNearbyEntities(entity, radio, radio, radio, entities);

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

	public static long getNow() {
		return Extra.getNow();
	}

	public static List<Player> getOnlinePlayers() {
		return getPlayers();
	}

	/**
	 * @param player Jogador
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

	public static Player getPlayer(String name) {
		return Bukkit.getPlayerExact(name);
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

	public static PlayersManager getPlayerManager() {
		return playerManager;
	}

	/**
	 * @return Lista de jogadores do servidor
	 */
	public static List<Player> getPlayers() {
		List<Player> list = new ArrayList<>();
		try {

			Object object = Extra.getResult(Mine.classBukkitBukkit, "getOnlinePlayers");
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

	public static Plugin getPlugin(String plugin) {
		return Bukkit.getPluginManager().getPlugin(plugin);
	}

	public static int getPosition(int line, int column) {
		int value = (line - 1) * 9;
		return value + column - 1;
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

	@SafeVarargs
	public static <E> E getRandom(E... objects) {
		return Extra.getRandom(objects);
	}

	public static <E> E getRandom(List<E> objects) {
		return Extra.getRandom(objects);
	}

	public static double getRandomDouble(double minValue, double maxValue) {
		return Extra.getRandomDouble(minValue, maxValue);
	}

	public static int getRandomEmptySlot(Inventory inv) {
		if (Mine.isFull(inv)) {
			return -1;
		}
		for (int i = 0; i < 10; i++) {

			int slot = Mine.getRandomInt(1, inv.getSize());
			if (inv.getItem(slot) == null) {
				return slot;
			}
		}
		return inv.firstEmpty();
	}

	public static int getRandomInt(int minValue, int maxValue) {
		return Extra.getRandomInt(minValue, maxValue);
	}

	/**
	 * Pega um Item aleatorio baseado no vetor
	 * 
	 * @param items Vetor de Itens
	 * @return O item aletario
	 */
	public static ItemStack getRandomItem(ItemStack... items) {

		return Mine.getRandom(items);
	}

	/**
	 * Pega um Item aleatorio baseado na lista
	 * 
	 * @param items Lista de Itens
	 * @return O item aletario
	 */
	public static ItemStack getRandomItem(List<ItemStack> items) {

		return Mine.getRandom(items);
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

	public static Player getRandomPlayer() {
		return getRandomPlayer(getPlayers());
	}

	public static Player getRandomPlayer(List<Player> list) {
		return list.get(Mine.getRandomInt(1, list.size()) - 1);
	}

	public static Location getRandomPosition(Location location, int xVar, int zVar) {
		return getHighPosition(getRandomLocation(location, xVar, 0, zVar));

	}

	/**
	 * Criar um coração vermelho
	 * 
	 * @return
	 */
	public static String getRedHeart() {
		return ChatColor.RED + "♥";
	}

	public static Replacer getReplacer(String key) {
		return replacers.get(key);
	}

	public static String getReplacers(String text, Player player) {
		for (Entry<String, Replacer> value : replacers.entrySet()) {
			if (text.contains(value.getKey())) {
				try {
					text = text.replace(value.getKey(), "" + value.getValue().getText(player));

				} catch (Exception e) {
					Mine.console("§cREPLACE ERROR: " + value.getKey());
					e.printStackTrace();
				}

			}

		}
		return text;
	}

	public static InputStream getResource(ClassLoader loader, String name) throws IOException {
		URL url = loader.getResource(name);
		if (url == null)
			return null;
		URLConnection connection = url.openConnection();
		connection.setUseCaches(false);
		return connection.getInputStream();
	}

	public static Schematic getSchematic(Player player) {
		Schematic schema = MAPS_CACHE.get(player);
		if (schema == null) {
			MAPS_CACHE.put(player, schema = new Schematic());
		}
		return schema;
	}

	/**
	 * Pega um Som baseado num Objeto (Texto)
	 * 
	 * @param object Objeto
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

	public static Location getSpawn() {
		return Bukkit.getWorlds().get(0).getSpawnLocation();
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

	public static Location getTargetLoc(LivingEntity entity, int distance) {
		@SuppressWarnings("deprecation")
		Block block = entity.getTargetBlock((HashSet<Byte>) null, distance);
		return block.getLocation();
	}

	public static Team getTeam(Scoreboard scoreboard, String name) {
		Team team = scoreboard.getTeam(Mine.cutText(name, 16));
		if (team == null) {
			team = scoreboard.registerNewTeam(cutText(name, 16));
		}
		return team;
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

	/**
	 * Pega a quantidade total dos itens do Inventario
	 * 
	 * @param inventory Inventario
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
	 * Pega a quantidade total do Item do Inventario
	 * 
	 * @param inventory Inventario
	 * @param item      Item
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
	 * Pega a quantidade total do Material do Inventario
	 * 
	 * @param inventory Inventario
	 * @param material  Tipo do Material
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

	public static Vector getVelocity(Location entity, Location target, double staticX, double staticY, double staticZ,
			double addX, double addY, double addZ) {
		double distance = target.distance(entity);
		double x = (staticX + (addX * distance)) * ((target.getX() - entity.getX()) / distance);
		double y = (staticY + (addY * distance)) * ((target.getY() - entity.getY()) / distance);
		double z = (staticZ + (addZ * distance)) * ((target.getZ() - entity.getZ()) / distance);
		return new Vector(x, y, z);

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

	public static World getWorld(String name) {
		return Bukkit.getWorld(name);
	}

	public static File getWorldFolder(String name) {
		return new File(Bukkit.getWorldContainer(), name);
	}

	/**
	 * Ganha todos os Itens do Inventario
	 * 
	 * @param items     Itens
	 * @param inventory Inventario
	 */
	public static void give(Collection<ItemStack> items, Inventory inventory) {
		for (ItemStack item : items) {
			inventory.addItem(item);
		}
	}

	public static boolean hasLightOn(Block block) {
		return block.getLightLevel() > 10;
	}

	public static boolean hasLightOn(Entity entity) {
		return hasLightOn(entity.getLocation());
	}

	public static boolean hasLightOn(Location location) {
		return hasLightOn(location.getBlock());
	}

	public static boolean hasMine() {
		return hasPlugin("EduardAPI");
	}

	public static boolean hasPerm(CommandSender sender, String permission) {
		if (!sender.hasPermission(permission)) {
			sender.sendMessage(Mine.MSG_NO_PERMISSION.replace("$permission", permission));
			return false;
		}
		return true;

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

	public static boolean hasPlugin(String plugin) {
		return Bukkit.getPluginManager().getPlugin(plugin) != null;
	}

	public static void hide(Player player) {
		for (Player target : getPlayers()) {
			if (target != player) {
				target.hidePlayer(player);
			}
		}
	}

	/**
	 * Retorna se (now < (seconds + before));
	 * 
	 * @param before  (Antes)
	 * @param seconds ()
	 * @return
	 */
	public static boolean inCooldown(long before, long seconds) {
		return Extra.inCooldown(before, seconds);

	}

	/**
	 * @param player Jogador
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
	 * Testa se o numero passado é da coluna expecificada
	 * 
	 * @param index  Numero
	 * @param colunm Coluna
	 * @return O resultado do teste
	 */
	public static boolean isColumn(int index, int colunm) {
		return getColumn(index) == colunm;
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

	public static boolean isFalling(Entity entity) {
		return entity.getVelocity().getY() < Extra.VALUE_WALKING_VELOCITY;
	}

	public static boolean isFlying(Entity entity) {
		return entity.getLocation().getBlock().getRelative(BlockFace.DOWN, 2).getType() == Material.AIR;
	}

	/**
	 * Testa se o Inventario esta cheio
	 * 
	 * @param inventory Inventario
	 * @return Teste
	 */
	public static boolean isFull(Inventory inventory) {
		return inventory.firstEmpty() == -1;
	}

	public static boolean isInvulnerable(Player player) {
		return player.getNoDamageTicks() > 1;
	}

	public static boolean isIpProxy(String ip) {
		return Extra.isIpProxy(ip);
	}

	public static boolean isMultBy(int number1, int numer2) {

		return Extra.isMultBy(number1, numer2);
	}

	public static boolean isOnGround(Entity entity) {
		return entity.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR;
	}

	/**
	 * Testa se a Entidade viva esta usando na mao o Tipo do Material
	 * 
	 * @param entity   Entitade
	 * @param material Tipo de Material
	 * @return Teste
	 */
	public static boolean isUsing(LivingEntity entity, Material material) {
		return (getHandType(entity) == material);
	}

	/**
	 * Testa se a Entidade viva esta usando na mao um Tipo do Material com este nome
	 * 
	 * @param entity   Entidade
	 * @param material Material
	 * @return
	 */
	public static boolean isUsing(LivingEntity entity, String material) {
		return getHandType(entity).name().toLowerCase().contains(material.toLowerCase());
	}

	/**
	 * Transforma um Texto em Vetor de Itens
	 * 
	 * @param data Texto
	 * @return Vetor de Itens (Lista)
	 * 
	 */
	public static ItemStack[] fromBase64toItems(final String data) {
		try {
			final ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			final BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			final ItemStack[] stacks = new ItemStack[dataInput.readInt()];
			for (int slot = 0; slot < stacks.length; ++slot) {

				stacks[slot] = (ItemStack) dataInput.readObject();

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
	 * @param contents Vetor de Itens
	 * @return Texto
	 */
	public static String fromItemsToBase64(final ItemStack[] contents) {

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

	public static void loadMaps() {

		File file = MAPS_CONFIG.getFile();
		file.mkdirs();

		for (File subfile : file.listFiles()) {
			while (subfile.isDirectory()) {
//				File[] files = subfile.listFiles();
//				for (File myfile : files) {
//					
//				}
				return;
			}
			if (!subfile.isDirectory()) {
				MAPS.put(subfile.getName().replace(".map", ""), Schematic.load(subfile));
			}
		}

	}

	public static World loadWorld(String name) {
		return new WorldCreator(name).generator(new EmptyWorldGenerator()).createWorld();
	}

	public static void makeCommand(String command) {
		runCommand(command);
	}

	public static void makeInvunerable(Player player) {
		player.setNoDamageTicks(Extra.DAY_IN_SECONDS * 20);

	}

	public static void makeInvunerable(Player player, int seconds) {
		player.setNoDamageTicks(seconds * 20);

	}

	/**
	 * Força o Respawn do Jogador (Respawn Automatico)
	 * 
	 * @param player Jogador
	 */
	public static void makeRespawn(Player player) {
		try {
			Object packet = Extra.getNew(Mine.classPacketPlayInClientCommand,
					Extra.getValue(Mine.classMineEnumClientCommand, "PERFORM_RESPAWN"));
			Extra.getResult(getConnection(player), "a", packet);

		} catch (Exception ex) {
			try {
				Object packet = Extra.getNew(Mine.classPacketPlayInClientCommand,
						Extra.getValue(Mine.classMineEnumClientCommand2, "PERFORM_RESPAWN"));
				Extra.getResult(getConnection(player), "a", packet);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
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

	public static ItemStack newBook(String name, String title, String author, String... pages) {
		ItemStack item = newItem(Material.WRITTEN_BOOK, name);
		BookMeta meta = (BookMeta) item.getItemMeta();
		meta.addPage(pages);
		meta.setAuthor(author);
		meta.setTitle(title);
		item.setItemMeta(meta);
		return item;
	}

	public static World newEmptyWorld(String worldName) {
		World world = loadWorld(worldName);
		world.setSpawnLocation(100, 100, 100);
		world.getSpawnLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.STONE);
		world.setKeepSpawnInMemory(true);
		return world;
	}

	public static boolean newExplosion(Location location, float power, boolean breakBlocks, boolean makeFire) {
		return location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), power,
				breakBlocks, makeFire);
	}

	public static ItemStack newFirework() {
		ItemStack fire = new ItemStack(Material.FIREWORK);
		FireworkMeta meta = (FireworkMeta) fire.getItemMeta();
		// meta.getEffects()
		fire.setItemMeta(meta);
		return fire;
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

	public static ItemStack newFireworkCharge() {
		ItemStack fire = new ItemStack(Material.FIREWORK_CHARGE);
		FireworkEffectMeta meta = (FireworkEffectMeta) fire.getItemMeta();
		meta.setEffect(FireworkEffect.builder().withColor(Color.RED).with(Type.STAR).build());
		fire.setItemMeta(meta);
		return fire;
	}

	/**
	 * Cria um item da Cabeça do Jogador
	 * 
	 * @param name   Nome
	 * @param owner  Nome do Jogador
	 * @param amount Quantidade
	 * @param lore   Descrição (Lista)
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
	 * @param name   Nome
	 * @param owner  Nome do Jogador
	 * @param amount Quantidade
	 * @param lore   Descrição (Lista)
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

	/**
	 * Tenha certeza que esta carregado a chunk pois assim funciona, caso contario
	 * buga<br>
	 * location.getChunk().load(true);
	 * 
	 * @param location Local
	 * @param line     Linha
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

	/**
	 * Cria um Inventario
	 * 
	 * @param name Nome
	 * @param size Tamanho do Inventario
	 * @return Inventario
	 */
	public static Inventory newInventory(String name, int size) {

		return Bukkit.createInventory(null, size, Extra.toText(32, name));
	}

	/**
	 * Cria um Item
	 * 
	 * @param material Material
	 * @param name     Nome
	 * @param amount   Quantidade
	 * @param data     MetaData
	 * @param lore     Descrição
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
	 * @param material Material
	 * @param name     Nome
	 * @param amount   Quantidade
	 * @param data     MetaData
	 * @param lore     Descrição
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
	 * @param material Material
	 * @param name     Nome
	 * @param amount   Quantidade
	 * @return Item
	 */
	public static ItemStack newItem(Material material, String name, int amount) {
		return newItem(material, name, amount, 0);
	}

	/**
	 * Cria um Item
	 * 
	 * @param material Material
	 * @param name     Nome
	 * @param amount   Quantidade
	 * @param data     MetaData
	 * @param lore     Descrição
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
	 * @param name     Nome
	 * @param material Material
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
	 * @param name     Nome
	 * @param material Material
	 * @param data     MetaData
	 * @return Item
	 */
	public static ItemStack newItem(String name, Material material, int data) {
		return newItem(material, name, 1, data);
	}

	/**
	 * Cria um Item
	 * 
	 * @param name     Nome
	 * @param material Material
	 * @param amount   Quantidade
	 * @param data     Data
	 * @param lore     Descrição
	 * @return Item
	 */
	public static ItemStack newItem(String name, Material material, int amount, int data, String... lore) {
		return newItem(material, name, amount, data, lore);
	}

	public static Villager newNPCVillager(Location location, String name) {
		Villager npc = location.getWorld().spawn(location, Villager.class);
		npc.setCustomName(name);
		npc.setCustomNameVisible(true);
		Mine.disableAI(npc);
		return npc;
	}

	public static Scoreboard newScoreboard(Player player, String title, String... lines) {
		return applyScoreboard(player, title, lines);
	}

	@SuppressWarnings("deprecation")
	public static ItemStack newSkull(EntityType type, String name) {
		return newSkull(name,
				("MHF_") + (type.getName() == null ? Mine.toTitle(type.name().replace("_", "")) : type.getName()));
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

	public static void newStepSound(Location location, int blockId) {
		location.getWorld().playEffect(location, Effect.STEP_SOUND, blockId);
	}

	public static void newStepSound(Location location, Material material) {
		location.getWorld().playEffect(location, Effect.STEP_SOUND, material);
	}

	public static boolean noConsole(CommandSender sender) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(Mine.MSG_ONLY_PLAYER);
			return false;
		}
		return true;
	}

	public static boolean onlyPlayer(CommandSender sender) {
		return noConsole(sender);
	}

	/**
	 * Abrir um Menu Gui paginado
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

	public static long parseDateDiff(String time, boolean future) throws Exception {
		return Extra.parseDateDiff(time, future);
	}

	public static boolean random(double chance) {
		return getChance(chance);
	}

	public static int randomInt(int minValue, int maxValue) {
		return getRandomInt(minValue, maxValue);
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

	public static void registerEvents(Listener event, Plugin plugin) {
		Bukkit.getPluginManager().registerEvents(event, plugin);
	}

	/**
	 * Remove itens se for igual a este<br>
	 * O inv.remove(...) também remove porem remove qualquer item não importanto
	 * nome, descrição, encantamentos
	 * 
	 * @param inventory Inventario
	 * @param item      Item
	 */
	public static void remove(Inventory inventory, ItemStack item) {
		for (Entry<Integer, ? extends ItemStack> map : inventory.all(item.getType()).entrySet()) {
			if (map.getValue().isSimilar(item)) {
				inventory.clear(map.getKey());
			}
		}
	}

	/**
	 * Remove alguns itens se for igual a este Item<br>
	 * O inv.remove(...) também remove porem remove qualquer item não importanto
	 * nome, descrição, encantamentos
	 * 
	 * @param inventory Inventario
	 * @param material  Tipo do Material
	 * @param amount    Quantidade
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
	 * Remove itens se for igual a este tipo de Material<br>
	 * O inv.remove(...) também remove porem remove qualquer item não importanto
	 * nome, descrição, encantamentos
	 * 
	 * @param inventory Inventario
	 * @param material  Tipo do Material
	 */
	public static void remove(Inventory inventory, Material material, int amount) {
		remove(inventory, new ItemStack(material), amount);
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

	public static String removeBrackets(String... message) {
		return Extra.removeBrackets(message);
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

	public static void removeEffects(Player player) {
		player.setFireTicks(0);
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
	}

	public static void removePermission(Player p, String permission) {
		p.addAttachment(getMainPlugin(), permission, false);
	}

	public static void removeReplacer(String replacer) {
		replacers.remove(replacer);
	}

	public static void resetLevel(Player player) {
		player.setLevel(0);
		player.setExp(0);
		player.setTotalExperience(0);
	}

	/**
	 * Restaura o Nome Original do Item
	 * 
	 * @param item Item
	 * @return Nome
	 */
	public static ItemStack resetName(ItemStack item) {
		setName(item, "");
		return item;
	}

	public static void resetScoreboard(Player player) {
		player.setScoreboard(Mine.getMainScoreboard());
	}
	// Parei aqui

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

	public static void runCommand(String command) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
	}

	/**
	 * Armazena as armaduras do Jogador
	 * 
	 * @param player
	 */
	public static void saveArmours(Player player) {
		PLAYERS_ARMOURS.put(player, player.getInventory().getArmorContents());
	}

	/**
	 * Armazena os Itens do Jogador
	 * 
	 * @param player
	 */
	public static void saveItems(Player player) {
		saveArmours(player);
		PLAYERS_ITEMS.put(player, player.getInventory().getContents());
	}

	public static void saveMaps() {

		for (Entry<String, Schematic> entry : MAPS.entrySet()) {
			Schematic mapa = entry.getValue();
			String name = entry.getKey();

			mapa.save(new File(MAPS_CONFIG.getFile(), name + ".map"));
		}
	}

	public static String saveVector(Vector vector) {
		StringBuilder text = new StringBuilder();

		text.append(vector.getX() + ",");
		text.append(vector.getY() + ",");
		text.append(vector.getZ() + ",");
		return text.toString();
	}

	public static void send(CommandSender sender, String message) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			sender.sendMessage(Mine.getReplacers(message, player));
		} else {
			sender.sendMessage(message);

		}

	}

	/**
	 * Modifica a Action Bar do Jogador
	 * 
	 * @param player Jogador
	 * @param text   Texto
	 */
	public static void sendActionBar(Player player, String text) {
		try {
			Object component = getIChatText(text);
			Object packet = Extra.getNew(Mine.classPacketPlayOutChat,
					Extra.getParameters(Mine.classMineIChatBaseComponent, byte.class), component, (byte) 2);
			sendPacket(player, packet);
			return;
		} catch (Exception ex) {
		}
		try {
			Object component = getIChatText2(text);
			Object packet = Extra.getNew(Mine.classPacketPlayOutChat,
					Extra.getParameters(Mine.classMineIChatBaseComponent, byte.class), component, (byte) 2);
			sendPacket(player, packet);
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(
					"§bRexMine §aNao foi possivel usar o 'setActionBar' pois o servidor esta na versao anterior a 1.8");

		}

	}

	public static void sendActionBar(String message) {
		for (Player player : Mine.getPlayers()) {
			Mine.sendActionBar(player, message);
		}
	}

	public static void sendAll(Player p, String message) {
		broadcast(getReplacers(message, p));

	}

	/**
	 * Envia o pacote para o jogador
	 * 
	 * @param player Jogador
	 * @param packet Pacote
	 * @throws Exception
	 */
	public static void sendPacket(Object packet, Player player) throws Exception {

		Extra.getResult(getConnection(player), "sendPacket", Extra.getParameters(Mine.classPacketPacket), packet);
	}

	/**
	 * Envia o pacote para o jogador
	 * 
	 * @param player Jogador
	 * @param packet Pacote
	 * @throws Exception
	 */
	public static void sendPacket(Player player, Object packet) throws Exception {
		sendPacket(packet, player);
	}

	/**
	 * Envia o pacote para todos jogadores
	 * 
	 * @param packet Pacote
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
	 * Envia o pacote para todos menos para o jogador
	 * 
	 * @param packet Pacote
	 * @param target Jogador
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
	 * Envia um Title para o Jogador
	 * 
	 * @param player   Jogador
	 * @param title    Titulo
	 * @param subTitle SubTitulo
	 * @param fadeIn   Tempo de Aparececimento (Ticks)
	 * @param stay     Tempo de Passagem (Ticks)
	 * @param fadeOut  Tempo de Desaparecimento (Ticks)
	 */
	public static void sendTitle(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
		try {
			if (isAbove1_8(player)) {

				// sendPacket(player, getNew(PacketTitle, getParameters(Action,
				// int.class, int.class, int.class),
				// getValue(Action, "TIMES"), fadeIn, stay, fadeOut));
				sendPacket(player, Extra.getNew(Mine.classSpigotPacketTitle,
						Extra.getValue(Mine.classSpigotAction, "TIMES"), fadeIn, stay, fadeOut));
				sendPacket(player,
						Extra.getNew(Mine.classSpigotPacketTitle,
								Extra.getParameters(Mine.classSpigotAction, Mine.classMineIChatBaseComponent),
								Extra.getValue(Mine.classSpigotAction, "TITLE"), getIChatText(title)));
				sendPacket(player,
						Extra.getNew(Mine.classSpigotPacketTitle,
								Extra.getParameters(Mine.classSpigotAction, Mine.classMineIChatBaseComponent),
								Extra.getValue(Mine.classSpigotAction, "SUBTITLE"), getIChatText(subTitle)));

				return;
			}

		} catch (Exception e) {
		}
		try {
			sendPacket(player, Extra.getNew(Mine.classPacketPlayOutTitle, fadeIn, stay, fadeOut));
			sendPacket(player,
					Extra.getNew(Mine.classPacketPlayOutTitle,
							Extra.getParameters(Mine.classCraftEnumTitleAction, Mine.classMineIChatBaseComponent),
							Extra.getValue(Mine.classCraftEnumTitleAction, "TITLE"), getIChatText(title)));
			sendPacket(player,
					Extra.getNew(Mine.classPacketPlayOutTitle,
							Extra.getParameters(Mine.classCraftEnumTitleAction, Mine.classMineIChatBaseComponent),
							Extra.getValue(Mine.classCraftEnumTitleAction, "SUBTITLE"), getIChatText(subTitle)));
			return;
		} catch (Exception e) {
		}
		try {
			sendPacket(player, Extra.getNew(Mine.classPacketPlayOutTitle, fadeIn, stay, fadeOut));
			sendPacket(player,
					Extra.getNew(Mine.classPacketPlayOutTitle,
							Extra.getParameters(Mine.classPacketEnumTitleAction2, Mine.classMineIChatBaseComponent),
							Extra.getValue(Mine.classPacketEnumTitleAction2, "TITLE"), getIChatText2(title)));
			sendPacket(player,
					Extra.getNew(Mine.classPacketPlayOutTitle,
							Extra.getParameters(Mine.classPacketEnumTitleAction2, Mine.classMineIChatBaseComponent),
							Extra.getValue(Mine.classPacketEnumTitleAction2, "SUBTITLE"), getIChatText2(subTitle)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Envia um Title para os Jogadores
	 * 
	 * 
	 * @param title    Titulo
	 * @param subTitle SubTitulo
	 * @param fadeIn   Tempo de Aparececimento (Ticks)
	 * @param stay     Tempo de Passagem (Ticks)
	 * @param fadeOut  Tempo de Desaparecimento (Ticks)
	 */
	public static void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
		for (Player player : Mine.getPlayers()) {
			sendTitle(player, title, subTitle, fadeIn, stay, fadeOut);
		}
	}

	public static void sendTo(Collection<Player> players, String message) {
		for (Player player : players) {
			player.sendMessage(message);
		}

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
	 * Seta o nivel do jogador
	 * 
	 * @param jogador
	 * @param novoNivel
	 */
	public static void setarNivelJogador(Player jogador, int novoNivel) {
		jogador.setLevel(novoNivel);
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

	/**
	 * Modifica a Cor do Item (Usado somente para Itens de Couro)
	 * 
	 * @param item  Item de Couro
	 * @param color Cor
	 * @return Item
	 */
	public static ItemStack setColor(ItemStack item, Color color) {
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(color);
		item.setItemMeta(meta);
		return item;
	}

	public static void setDirection(Entity entity, Entity target) {
		entity.teleport(entity.getLocation().setDirection(target.getLocation().getDirection()));
	}

	public static void setDirection(Entity entity, Location target) {
		Location location = entity.getLocation().clone();
		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			location = livingEntity.getEyeLocation().clone();

		}
		entity.teleport(entity.getLocation().setDirection(getDiretion(location, target)));

	}

	/**
	 * Cria um Set de Couro para entidade viva
	 * 
	 * @param entity Entidade viva
	 * @param color  Cor
	 * @param name   Nome
	 */
	public static void setEquip(LivingEntity entity, Color color, String name) {
		EntityEquipment inv = entity.getEquipment();
		inv.setBoots(setName(setColor(new ItemStack(Material.LEATHER_BOOTS), color), name));
		inv.setHelmet(setName(setColor(new ItemStack(Material.LEATHER_HELMET), color), name));
		inv.setChestplate(setName(setColor(new ItemStack(Material.LEATHER_CHESTPLATE), color), name));
		inv.setLeggings(setName(setColor(new ItemStack(Material.LEATHER_LEGGINGS), color), name));
	}

	/**
	 * Modifca toda Hotbar para um Item
	 * 
	 * @param player Jogador
	 * @param item   Item
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
	 * @param item Item
	 * @param lore Descrição
	 * @return Item
	 */
	public static ItemStack setLore(ItemStack item, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * Modifica a Descrição do Item
	 * 
	 * @param item Item
	 * @param lore Descrição
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
	 * @param item Item
	 * @param name Novo Nome
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

	public static void setPlayerManager(PlayersManager playerManager) {
		Mine.playerManager = playerManager;
	}

	/**
	 * Modifica um Item transformando ele na Cabeça do Jogador
	 * 
	 * @param item Item
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

	public static void setSpawn(Entity entity) {

		entity.getWorld().setSpawnLocation((int) entity.getLocation().getX(), (int) entity.getLocation().getY(),
				(int) entity.getLocation().getZ());
	}

	/**
	 * Modifica a TabList do Jogador
	 * 
	 * @param player Jogador
	 * @param header Cabeçalho
	 * @param footer Rodapé
	 */
	public static void setTabList(Player player, String header, String footer) {
		try {
			if (isAbove1_8(player)) {
				Object packet = Extra.getNew(Mine.classSpigotPacketTabHeader,
						Extra.getParameters(Mine.classMineIChatBaseComponent, Mine.classMineIChatBaseComponent),
						getIChatText(header), getIChatText(footer));
				sendPacket(packet, player);
				return;
			}

		} catch (Exception e) {
		}
		try {
			Object packet = Extra.getNew(Mine.classPacketPlayOutPlayerListHeaderFooter,
					Extra.getParameters(Mine.classMineIChatBaseComponent), getIChatText(header));

			Extra.setValue(packet, "b", getIChatText(footer));
			sendPacket(packet, player);
		} catch (Exception e) {
		}
		try {
			Object packet = Extra.getNew(Mine.classPacketPlayOutPlayerListHeaderFooter,
					Extra.getParameters(Mine.classMineIChatBaseComponent), getIChatText2(header));
			Extra.setValue(packet, "b", getIChatText2(footer));
			sendPacket(packet, player);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void show(Player player) {
		for (Player target : getPlayers()) {
			if (target != player) {
				target.showPlayer(player);
			}
		}
	}

	public static boolean startWith(String message, String text) {
		return message.toLowerCase().startsWith(text.toLowerCase());
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

	public static void teleport(LivingEntity entity, int range) {
		teleport(entity, getTargetLoc(entity, range));
	}

	public static void teleportToSpawn(Entity entity) {

		entity.teleport(entity.getWorld().getSpawnLocation().setDirection(entity.getLocation().getDirection()));
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

	public static Boolean toBoolean(Object obj) {
		return Extra.toBoolean(obj);
	}

	public static Byte toByte(Object object) {
		return Extra.toByte(object);

	}

	public static String toChatMessage(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static String toConfigMessage(String text) {
		return text.replace(ChatColor.COLOR_CHAR, '&');
	}

	public static String toDecimal(Object number) {
		return toDecimal(number, 2);
	}

	public static String toDecimal(Object number, int max) {
		return Extra.toDecimal(number, max);
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

	public static List<String> toLines(String text, int size) {
		return Extra.toLines(text, size);

	}

	public static Long toLong(Object object) {
		return Extra.toLong(object);
	}

	public static List<String> toMessages(List<Object> list) {
		List<String> lines = new ArrayList<String>();
		for (Object line : list) {
			lines.add(toChatMessage(line.toString()));
		}
		return lines;
	}

	public static Short toShort(Object object) {
		return Extra.toShort(object);

	}

	public static String toString(Object object) {

		return object == null ? "" : object.toString();
	}

	public static String toText(Collection<String> message) {
		return Extra.toText(message);
	}

	public static String toText(int size, String text) {
		return Extra.toText(size, text);
	}

	public static String toTitle(String name) {
		return Extra.toTitle(name);

	}

	public static String toTitle(String name, String replacer) {
		return Extra.toTitle(name, replacer);
	}

	public static Vector toVector(String text) {
		String[] split = text.split(",");

		double x = Double.parseDouble(split[0]);
		double y = Double.parseDouble(split[1]);
		double z = Double.parseDouble(split[2]);

		return new Vector(x, y, z);
	}

	public static void unloadWorld(String name) {
		World world = Bukkit.getWorld(name);
		if (world != null) {
			World mundoPadrao = Bukkit.getWorlds().get(0);
			for (Player p : world.getPlayers()) {
				p.teleport(mundoPadrao.getSpawnLocation());
			}

		}
		Bukkit.unloadWorld(name, true);
	}

	public static void updateTargets() {
		for (Player p : getPlayers()) {

			PlayerTargetEvent event = new PlayerTargetEvent(p,
					Mine.getTarget(p, Mine.getPlayerAtRange(p.getLocation(), 100)));
			Mine.callEvent(event);

		}
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

	public boolean isBeaconPlaced(Location loc) {
		int yMin = loc.getBlockY();
		int xMin = loc.getBlockX();
		int zMin = loc.getBlockZ();
		boolean is = false;
		for (int y = yMin; y < yMin + 5; y++) {
			for (int x = xMin - 2; x > xMin + 2; x++) {
				for (int z = zMin - 2; x > zMin + 2; z++) {
					Location subloc = new Location(loc.getWorld(), x, y, z);
					if (subloc.getBlock().getType() == Material.BEACON) {
						is = true;
					} else
						is = false;
				}
			}
		}
		return is;
	}

}
