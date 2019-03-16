package net.eduard.api.lib.old;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Classe criada no intuito de facilitar a criação de Menus, Scoreboard, Items e
 * com alguns métodos para facilitar outras coisas<br>
 * Versão do Java necessária para usar: Java 8 ou superior
 * 
 * @version 1.1
 * @since 0.5
 * @author Eduard
 * @deprecated Não use mais esta classe pois criei outras classes melhores com
 *             mais métodos e facilidades
 *
 */
public abstract interface Eduard_Java8 {
	public static Eduard_Java8 getEduard() {
		return new Eduard_Java8() {
		};
	}

	public default void setPermission(String cmd, String perm) {
		getPlugin().getCommand(cmd).setPermission(perm);
	}

	public default void setPermissions(Map<String, String> perms) {
		for (Map.Entry<String, String> perm : perms.entrySet()) {
			setPermission((String) perm.getKey(), (String) perm.getValue());
		}
	}

	public default void setError(String cmd) {
		getPlugin().getCommand(cmd).setPermissionMessage(getMessage("no-perm"));
	}

	public default void setErrors(String[] cmds) {
		String[] arrayOfString;
		int j = (arrayOfString = cmds).length;
		for (int i = 0; i < j; i++) {
			String cmd = arrayOfString[i];
			setError(cmd);
		}
	}

	public default void setCommand(String cmd, CommandExecutor ex) {
		getPlugin().getCommand(cmd).setExecutor(ex);
	}

	public default void setCommands(Map<String, CommandExecutor> cmds) {
		for (Map.Entry<String, CommandExecutor> cmd : cmds.entrySet()) {
			setCommand((String) cmd.getKey(), (CommandExecutor) cmd.getValue());
		}
	}

	public default void setEvent(Listener reg) {
		Bukkit.getPluginManager().registerEvents(reg, getPlugin());
	}

	public default void setEvents(Listener[] regs) {
		Listener[] arrayOfListener;
		int j = (arrayOfListener = regs).length;
		for (int i = 0; i < j; i++) {
			Listener reg = arrayOfListener[i];
			setEvent(reg);
		}
	}

	public default void saveConfig() {
		getPlugin().saveConfig();
	}

	public default void saveDefault() {
		getPlugin().getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public default void reloadConfig() {
		getPlugin().reloadConfig();
	}

	public default void saveDefaultConfig() {
		getPlugin().saveDefaultConfig();
	}

	public default boolean hasLocation(String name) {
		return hasLocation(getPlugin().getConfig(), name);
	}

	public default void setLocation(String name, Location loc) {
		setLocation(getPlugin().getConfig(), name, loc);
		saveConfig();
	}

	public default Location getLocation(String name) {
		return getLocation(getPlugin().getConfig(), name);
	}

	public default void add(String name, Object value) {
		getPlugin().getConfig().addDefault(name, value);
	}

	public default void set(String name, Object value) {
		getPlugin().getConfig().set(name, value);
	}

	public default Object get(String name) {
		return getPlugin().getConfig().get(name);
	}

	public default boolean has(String name) {
		return getPlugin().getConfig().contains(name);
	}

	public default String getMessage(String name) {
		return ChatColor.translateAlternateColorCodes('&', getString(name));
	}

	public default String colorToString(String name) {
		return name.replace('�', '&');
	}

	public default String getString(String name) {
		return getPlugin().getConfig().getString(name);
	}

	public default int getInt(String name) {
		return getPlugin().getConfig().getInt(name);
	}

	public default double getDouble(String name) {
		return getPlugin().getConfig().getDouble(name);
	}

	public default JavaPlugin getPlugin() {
		return JavaPlugin.getProvidingPlugin(getClass());
	}

	public default float getFloat(String name) {
		return (float) getDouble(name);
	}

	public default long getLong(String name) {
		return getPlugin().getConfig().getLong(name);
	}

	public default boolean getBoolean(String name) {
		return getPlugin().getConfig().getBoolean(name);
	}

	public default ConfigurationSection getSection(String name) {
		return getPlugin().getConfig().getConfigurationSection(name);
	}

	public default Set<String> getKeys(String name) {
		return getSection(name).getKeys(false);
	}

	public default Map<String, Object> getValues(String name) {
		return getSection(name).getValues(false);
	}

	public default Player getPlayer(String name) {
		return Bukkit.getPlayerExact(name);
	}

	public default World getWorld(String name) {
		return Bukkit.getWorld(name);
	}

	public default boolean exist(Player p) {
		return Arrays.asList(Bukkit.getOfflinePlayers()).contains(p);
	}

	public default boolean isOnline(Player p) {
		return p != null;
	}

	public default int random(int x, int y) {
		int min = Math.min(x, y);
		int max = Math.max(x, y);
		Random random = new Random();
		int numberRandom = min + random.nextInt(max - min + 1);
		return numberRandom;
	}

	public default boolean chance(double chance) {
		Random random = new Random();
		return random.nextDouble() <= chance;
	}

	public default void effect(Location loc, Effect effect, int data, int radius) {
		loc.getWorld().playEffect(loc, effect, data, radius);
	}

	public default void effect(Location loc, Effect effect, int data) {
		loc.getWorld().playEffect(loc, effect, data);
	}

	public default void effect(Location loc, Effect effect) {
		effect(loc, effect, 0);
	}

	public default void sound(Location loc, Sound sound, float volume, float pitch) {
		loc.getWorld().playSound(loc, sound, volume, pitch);
	}

	public default void sound(Location loc, Sound sound, float volume) {
		sound(loc, sound, volume, 1.0F);
	}

	public default void sound(Location loc, Sound sound) {
		sound(loc, sound, 2.0F);
	}

	public default void potion(LivingEntity ent, PotionEffectType potion, int seconds) {
		potion(ent, potion, seconds, 1);
	}

	public default void potion(LivingEntity ent, PotionEffectType potion, int seconds, int force) {
		potion(ent, potion, seconds, force, true);
	}

	public default void potion(LivingEntity ent, PotionEffectType potion, int seconds, int force, boolean ambient) {
		ent.addPotionEffect(new PotionEffect(potion, 20 * seconds, force, ambient));
	}

	public default boolean hasMeta(ItemStack item) {
		return item.hasItemMeta();
	}

	public default boolean hasName(ItemStack item) {
		return item.getItemMeta().hasDisplayName();
	}

	public default String getName(ItemStack item) {
		return item.getItemMeta().getDisplayName();
	}

	public default ItemStack setName(String name, ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

	public default ItemStack addLore(String message, ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (meta.getLore() == null) {
			meta.setLore(Arrays.asList(new String[] { message }));
		} else {
			meta.getLore().add(message);
		}
		item.setItemMeta(meta);
		return item;
	}

	public default ItemStack setLore(List<String> lores, ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lores);
		item.setItemMeta(meta);
		return item;
	}

	public default ItemStack clearLore(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (meta.getLore() == null) {
			meta.setLore(new ArrayList<String>());
		} else {
			meta.getLore().clear();
		}
		item.setItemMeta(meta);
		return item;
	}

	public default ItemStack set(Inventory inv, int slot, ItemStack item) {
		inv.setItem(slot, item);
		return item;
	}

	public default ItemStack add(Inventory inv, ItemStack item) {
		inv.addItem(new ItemStack[] { item });
		return item;
	}

	public default ItemStack add(Enchantment ench, ItemStack item) {
		item.addEnchantment(ench, 1);
		return item;
	}

	public default ItemStack add(Enchantment ench, int lvl, ItemStack item) {
		item.addEnchantment(ench, lvl);
		return item;
	}

	public default ItemStack item(Material type, String name) {
		return item(type, (short) 0, name);
	}

	public default ItemStack item(Material type, String name, String... lores) {
		return item(type, 1, (short) 0, name, lores);
	}

	public default ItemStack item(Material type, String name, List<String> lores) {
		return item(type, 1, (short) 0, name, lores);
	}

	public default ItemStack item(Material type, short data) {
		return item(type, 1, data);
	}

	public default ItemStack item(Material type, short data, String name) {
		return item(type, 1, data, name);
	}

	public default ItemStack item(Material type) {
		return item(type, 1);
	}

	public default ItemStack item(Material type, int amount, String name) {
		return item(type, amount, (short) 0, name);
	}

	public default ItemStack item(Material type, int amount) {
		return item(type, amount, (short) 0);
	}

	public default ItemStack item(Material type, int amount, short data) {
		return item(type, amount, data, null);
	}

	public default ItemStack item(Material type, int amount, short data, String name) {
		List<String> lista = null;
		return item(type, amount, data, name, lista);
	}

	public default ItemStack item(Material type, int amount, short data, String name, String... lores) {
		return item(type, amount, data, name, Arrays.asList(lores));
	}

	public default ItemStack item(Material type, int amount, short data, String name, List<String> lores) {
		ItemStack item = new ItemStack(type, amount, data);
		if (name != null) {
			setName(name, item);
		}
		if (lores != null) {
			setLore(lores, item);
		}
		return item;
	}

	public default void setLocation(FileConfiguration config, String name, Location loc) {
		config.set(name + ".world", loc.getWorld().getName());
		config.set(name + ".x", Double.valueOf(loc.getX()));
		config.set(name + ".y", Double.valueOf(loc.getY()));
		config.set(name + ".z", Double.valueOf(loc.getZ()));
		config.set(name + ".yaw", Float.valueOf(loc.getYaw()));
		config.set(name + ".pitch", Float.valueOf(loc.getPitch()));
	}

	public default Location getLocation(FileConfiguration config, String name) {
		World w = Bukkit.getWorld(config.getString(name + ".world"));
		double x = config.getDouble(name + ".x");
		double y = config.getDouble(name + ".y");
		double z = config.getDouble(name + ".z");
		double yaw = config.getDouble(name + ".yaw");
		double pitch = config.getDouble(name + ".pitch");
		return new Location(w, x, y, z, (float) yaw, (float) pitch);
	}

	public default boolean hasLocation(FileConfiguration config, String name) {
		return config.contains(name + ".world");
	}

	public default boolean isLocation(FileConfiguration config, String name) {
		return (config.contains(name + ".world")) && (config.contains(name + ".x")) && (config.contains(name + ".y"))
				&& (config.contains(name + ".z")) && (config.contains(name + ".yaw"))
				&& (config.contains(name + ".pitch"));
	}

	public default boolean compareLocation(Location loc1, Location loc2) {
		return locationToInt(loc1).equals(locationToInt(loc2));
	}

	public default Location locationToInt(Location loc) {
		return new Location(loc.getWorld(), (int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
	}

	public default List<Location> toLocations(List<Block> blocks) {
		List<Location> locations = new ArrayList<Location>();
		for (Block block : blocks) {
			locations.add(block.getLocation());
		}
		return locations;
	}

	public default List<Block> toBlocks(List<Location> locations) {
		List<Block> blocks = new ArrayList<Block>();
		for (Location location : locations) {
			blocks.add(location.getBlock());
		}
		return blocks;
	}

	public default Location getLocation(Location loc) {
		return new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}

	public default void setType(List<Location> locations, Material material) {
		for (Location block : locations) {
			block.getBlock().setType(material);
		}
	}

	public default void setType(List<Location> locations, int id) {
		for (Location block : locations) {
			block.getBlock().setTypeId(id);
		}
	}

	public default void itemFloor(Location loc, int size, Material material) {
		List<Location> locations = blocksSquared(loc, size);
		setType(locations, material);
	}

	public default List<Location> blocksSquared(Location loc, int size) {
		return blocksBox(loc, size, 0);
	}

	public default List<Location> blocksBox(Location loc, int size, int high) {
		return blocksBox(loc, size, high, 0);
	}

	public default List<Location> blocksBox(Location loc, int size, int high, int low) {
		return getLocations(getHighLocation(getLocation(loc), high, size), getLowLocation(getLocation(loc), low, size));
	}

	public default List<Block> getBlocks(Block block1, Block block2) {
		return getBlocks(block1.getLocation(), block2.getLocation());
	}

	public default List<Block> getBlocks(Location loc1, Location loc2) {
		List<Block> blocks = new ArrayList<Block>();
		for (Location block : getLocations(loc1, loc2)) {
			blocks.add(block.getBlock());
		}
		return blocks;
	}

	public default List<Location> getLocations(Block block1, Block block2) {
		return getLocations(block1.getLocation(), block2.getLocation());
	}

	public default List<Location> getLocations(Location loc1, Location loc2) {
		Location min = getMinLocation(loc1, loc2);
		Location max = getMaxLocation(loc1, loc2);
		return getLocs(min, max);
	}

	public default List<Location> getLocs(Location min, Location max) {
		List<Location> locs = new ArrayList<Location>();
		for (double x = min.getX(); x <= max.getX(); x += 1.0D) {
			for (double y = min.getY(); y <= max.getY(); y += 1.0D) {
				for (double z = min.getZ(); z <= max.getZ(); z += 1.0D) {
					Location loc = new Location(min.getWorld(), x, y, z);
					if (!locs.contains(loc)) {
						locs.add(loc);
					}
				}
			}
		}
		return locs;
	}

	public default Location getMinLocation(Location loc1, Location loc2) {
		double x = Math.min(loc1.getX(), loc2.getX());
		double y = Math.min(loc1.getY(), loc2.getY());
		double z = Math.min(loc1.getZ(), loc2.getZ());
		return new Location(loc1.getWorld(), x, y, z);
	}

	public default Location getMaxLocation(Location loc1, Location loc2) {
		double x = Math.max(loc1.getX(), loc2.getX());
		double y = Math.max(loc1.getY(), loc2.getY());
		double z = Math.max(loc1.getZ(), loc2.getZ());
		return new Location(loc1.getWorld(), x, y, z);
	}

	public default Location getHighLocation(Location loc, int high, int size) {
		loc.add(size, high, size);
		return loc;
	}

	public default Location getLowLocation(Location loc, int low, int size) {
		loc.subtract(size, low, size);
		return loc;
	}

	public static class Configs {
		private File file;
		private FileConfiguration config;

		public Configs(String name) {
			this.file = new File(Eduard_Java8.getEduard().getPlugin().getDataFolder(), name);
			reloadConfig();
		}

		public File getFile() {
			return this.file;
		}

		public FileConfiguration getConfig() {
			return this.config;
		}

		public void reloadConfig() {
			this.config = YamlConfiguration.loadConfiguration(this.file);
			InputStream imputStream = Eduard_Java8.getEduard().getPlugin().getResource(this.file.getName());
			if (imputStream != null) {
				YamlConfiguration imputConfig = YamlConfiguration.loadConfiguration(imputStream);
				this.config.setDefaults(imputConfig);
			}
		}

		public void saveDefaultConfig() {
			Eduard_Java8.getEduard().getPlugin().saveResource(this.file.getName(), true);
		}

		public void saveConfig() {
			try {
				this.config.save(this.file);
			} catch (IOException localIOException) {
			}
		}

		public void saveDefault() {
			this.config.options().copyDefaults(true);
			saveConfig();
		}

		public boolean hasLocation(String name) {
			return Eduard_Java8.getEduard().hasLocation(this.config, name);
		}

		public void setLocation(Location loc, String name) {
			Eduard_Java8.getEduard().setLocation(this.config, name, loc);
			saveConfig();
		}

		public Location getLocation(String name) {
			return Eduard_Java8.getEduard().getLocation(this.config, name);
		}

		public void add(String name, Object value) {
			this.config.addDefault(name, value);
		}

		public void set(String name, Object value) {
			this.config.set(name, value);
		}

		public Object get(String name) {
			return this.config.get(name);
		}

		public boolean has(String name) {
			return this.config.contains(name);
		}

		public String getMessage(String name) {
			return ChatColor.translateAlternateColorCodes('&', this.config.getString(name));
		}

		public String getString(String name) {
			return this.config.getString(name);
		}

		public int getInt(String name) {
			return this.config.getInt(name);
		}

		public double getDouble(String name) {
			return this.config.getDouble(name);
		}

		public float getFloat(String name) {
			return (float) getDouble(name);
		}

		public long getLong(String name) {
			return this.config.getLong(name);
		}

		public boolean getBoolean(String name) {
			return this.config.getBoolean(name);
		}

		public ConfigurationSection getSection(String name) {
			return this.config.getConfigurationSection(name);
		}

		public Set<String> getKeys(String name) {
			return getSection(name).getKeys(false);
		}

		public Map<String, Object> getValues(String name) {
			return getSection(name).getValues(false);
		}
	}

	public static class Minigame {
		private String name;
		private String arenas;

		public Minigame() {
			this.name = "";
			this.arenas = "Arenas";
		}

		public HashMap<String, String> players = new HashMap<String, String>();

		public List<Player> getPlayers() {
			List<Player> player = new ArrayList<Player>();
			for (String p : this.players.keySet()) {
				player.add(Bukkit.getPlayerExact(p));
			}
			return player;
		}

		public List<Player> getPlayers(String arena) {
			List<Player> player = new ArrayList<Player>();
			for (Map.Entry<String, String> p : this.players.entrySet()) {
				if (((String) p.getValue()).equals(arena)) {
					player.add(Bukkit.getPlayerExact((String) p.getKey()));
				}
			}
			return player;
		}

		public List<String> getArenas() {
			Eduard_Java8.Configs arenaPasta = new Eduard_Java8.Configs(this.arenas);
			arenaPasta.getFile().mkdirs();
			return Arrays.asList(Arrays.asList(arenaPasta.getFile().list()).toString().replace(".yml", "").split(", "));
		}

		public List<String> getEnabledArenas() {
			ArrayList<String> list = new ArrayList<String>();
			for (String essaArena : getArenas()) {
				if (getArena(essaArena).enable()) {
					list.add(essaArena);
				}
			}
			return list;
		}

		public void setLobby(Player p) {
			Eduard_Java8.getEduard().setLocation(this.name + "." + "Lobby", p.getLocation());
			Eduard_Java8.getEduard().saveConfig();
		}

		public boolean hasLobby() {
			return Eduard_Java8.getEduard().hasLocation(this.name + "." + "Lobby");
		}

		public Location getLobby() {
			return Eduard_Java8.getEduard().getLocation(this.name + "." + "Lobby");
		}

		public Arena getArena(String name) {
			return new Arena(name);
		}

		public class Arena {
			private Eduard_Java8.Configs config;

			public Arena(String name) {
				this.config = new Eduard_Java8.Configs(Eduard_Java8.Minigame.this.arenas + "/" + name + ".yml");
			}

			public void create() {
				enable(false);
			}

			public void delete() {
				this.config.getFile().delete();
			}

			public boolean exist() {
				return this.config.getFile().exists();
			}

			public void enable(boolean value) {
				set("enable", Boolean.valueOf(value));
			}

			public boolean enable() {
				return this.config.getBoolean("enable");
			}

			public void set(String name, Object value) {
				this.config.set(name, value);
				this.config.saveConfig();
			}

			public Object get(String name) {
				return this.config.get(name);
			}

			public boolean has(String name) {
				return this.config.has(name);
			}

			public Location getLocation(String name) {
				return this.config.getLocation(name);
			}

			public boolean hasLocation(String name) {
				return this.config.hasLocation(name);
			}

			public void setLocation(Location loc, String name) {
				this.config.setLocation(loc, name);
			}
		}
	}

	public static class Time {
		private int id = -1;
		private int time = -1;

		public int getTime() {
			return this.time;
		}

		public void setTime(int time) {
			this.time = time;
		}

		public Time(final Runnable run, long ticks, final int times) {
			if (times < 0) {
				this.time = 100000;
			} else {
				this.time = times;
			}
			this.id = new BukkitRunnable() {
				public void run() {
					if (times == Eduard_Java8.Time.this.time) {
						Eduard_Java8.Time.this.time -= 1;
						run.run();
					} else if (Eduard_Java8.Time.this.time < 1) {
						Eduard_Java8.Time.this.stop();
					} else {
						Eduard_Java8.Time.this.time -= 1;
						run.run();
					}
				}
			}.runTaskTimer(Eduard_Java8.getEduard().getPlugin(), ticks, ticks).getTaskId();
		}

		public Time(Runnable run, int seconds, int times) {
			this(run, seconds * 20L, times);
		}

		public Time(Runnable run, int times) {
			this(run, 1, times);
		}

		public Time(Runnable run) {
			this(run, 1);
		}

		public void stop() {
			if (this.id != -1) {
				Bukkit.getScheduler().cancelTask(this.id);
			}
		}
	}

	public static class Cooldown {
		private HashMap<String, Integer> time = new HashMap<String, Integer>();
		private HashMap<String, Long> id = new HashMap<String, Long>();

		public boolean has(Player p) {
			if (this.id.containsKey(p.getUniqueId().toString())) {
				return getTime(p) > 0L;
			}
			return false;
		}

		public void start(Player p, int seconds) {
			if (!has(p)) {
				this.id.put(p.getUniqueId().toString(), Long.valueOf(System.currentTimeMillis()));
				this.time.put(p.getUniqueId().toString(), Integer.valueOf(seconds));
			}
		}

		public void remove(Player p) {
			this.id.remove(p.getUniqueId().toString());
			this.time.remove(p.getUniqueId().toString());
		}

		public long getTime(Player p) {
			Long last = (Long) this.id.get(p.getUniqueId().toString());
			Integer delay = (Integer) this.time.get(p.getUniqueId().toString());
			delay = Integer.valueOf(delay.intValue() * 1000);
			long wait = last.longValue() - (System.currentTimeMillis() - delay.intValue());
			return wait / 1000L;
		}
	}

	public static class Cooldowns {
		private HashMap<String, Eduard_Java8.Time> time = new HashMap<String, Time>();

		public int getTime(Player p) {
			if (has(p)) {
				return ((Eduard_Java8.Time) this.time.get(p.getUniqueId().toString())).getTime();
			}
			return -1;
		}

		public boolean has(Player p) {
			return this.time.containsKey(p.getUniqueId().toString());
		}

		public void start(Player p, int seconds) {
			start(p, seconds, (String) null);
		}

		public void start(final Player p, int seconds, final String message) {
			start(p, seconds, new Runnable() {
				public void run() {
					p.sendMessage(message);
				}
			});
		}

		public void start(final Player p, int seconds, final Runnable end) {
			if (!has(p)) {
				Eduard_Java8.Time time = new Eduard_Java8.Time(new Runnable() {
					public void run() {
						if (Eduard_Java8.Cooldowns.this.getTime(p) == 0) {
							if (end != null) {
								end.run();
							}
							Eduard_Java8.Cooldowns.this.stop(p);
						}
					}
				}, seconds);
				this.time.put(p.getUniqueId().toString(), time);
			}
		}

		public void stop(Player p) {
			if (has(p)) {
				String name = p.getUniqueId().toString();
				((Eduard_Java8.Time) this.time.get(name)).stop();
				this.time.remove(p.getUniqueId().toString());
			}
		}
	}

	public static class Delays {
		private HashMap<String, Eduard_Java8.Time> time = new HashMap<String, Time>();

		public boolean has(Player p) {
			return this.time.containsKey(p.getUniqueId().toString());
		}

		public void start(Player p, int seconds) {
			start(p, seconds, (String) null);
		}

		public void start(final Player p, int seconds, final String message) {
			start(p, seconds, new Runnable() {
				public void run() {
					p.sendMessage(message);
				}
			});
		}

		public void start(final Player p, int seconds, final Runnable end) {
			if (has(p)) {
				return;
			}
			Eduard_Java8.Time time = new Eduard_Java8.Time(new Runnable() {
				public void run() {
					if (end != null) {
						end.run();
					}
					Eduard_Java8.Delays.this.stop(p);
				}
			}, seconds, 1);
			this.time.put(p.getUniqueId().toString(), time);
		}

		public void stop(Player p) {
			if (has(p)) {
				String name = p.getUniqueId().toString();
				((Eduard_Java8.Time) this.time.get(name)).stop();
				this.time.remove(p.getUniqueId().toString());
			}
		}
	}

	public static class Timers {
		private Eduard_Java8.Time time;
		private Eduard_Java8.ITimer game;
		private int[] numbers;

		public Timers() {
			setGame(new Eduard_Java8.ITimer() {
				public void normalRun() {
					Bukkit.broadcastMessage("�6Normal Run: �e" + Eduard_Java8.Timers.this.getTime());
				}

				public void lastRun() {
					Bukkit.broadcastMessage("�6Last Run: �b" + Eduard_Java8.Timers.this.getTime());
				}

				public void finalRun() {
					Bukkit.broadcastMessage("�6Final Run: �eGame Over");
				}
			});
			setNumbers(new int[] { 1, 2, 3, 4, 5, 10, 20, 30, 60 });
		}

		public int getTime() {
			return this.time.getTime();
		}

		public void setTime(int seconds) {
			this.time.setTime(seconds);
		}

		public boolean started() {
			return getTime() > 0;
		}

		public void start(Eduard_Java8.ITimer game, int seconds) {
			if (game != null) {
				setGame(game);
			}
			this.time = new Eduard_Java8.Time(new Runnable() {
				public void run() {
					if (Eduard_Java8.Timers.this.getTime() == 0) {
						Eduard_Java8.Timers.this.getGame().finalRun();
					} else {
						int[] arrayOfInt;
						int j = (arrayOfInt = Eduard_Java8.Timers.this.getNumbers()).length;
						for (int i = 0; i < j; i++) {
							int id = arrayOfInt[i];
							if (id == Eduard_Java8.Timers.this.getTime()) {
								Eduard_Java8.Timers.this.getGame().normalRun();
							}
						}
					}
				}
			}, 1, seconds);
		}

		public void stop() {
			if (this.time != null) {
				this.time.stop();
			}
		}

		public int[] getNumbers() {
			return this.numbers;
		}

		public void setNumbers(int... numbers) {
			this.numbers = numbers;
		}

		public Eduard_Java8.ITimer getGame() {
			return this.game;
		}

		public void setGame(Eduard_Java8.ITimer game) {
			this.game = game;
		}
	}

	public static class Fireworks {
		private Firework firework;
		private FireworkMeta meta;
		private List<FireworkEffect> effects;

		public void reload() {
			this.firework.setFireworkMeta(this.meta);
		}

		public Fireworks(Location loc) {
			this.firework = ((Firework) loc.getWorld().spawn(loc, Firework.class));
			this.meta = this.firework.getFireworkMeta();
			FireworkMeta meta = this.firework.getFireworkMeta();
			this.effects = meta.getEffects();
		}

		public List<FireworkEffect> getEffects() {
			return this.effects;
		}

		public Firework getFirework() {
			return this.firework;
		}

		public void setPower(int power) {
			this.meta.setPower(power);
			reload();
		}

		public void item(Eduard_Java8.FireworkType type, Color firstColor, Color lastColor) {
			item(FireworkEffect.Type.valueOf(type.name()), true, true, firstColor, lastColor);
		}

		public void item(FireworkEffect.Type type, boolean flicker, boolean trail, Color firstColor, Color lastColor) {
			item(FireworkEffect.builder().with(type).flicker(flicker).trail(trail).withColor(firstColor)
					.withFade(lastColor).build());
		}

		public void item(FireworkEffect effect) {
			this.meta.addEffect(effect);
			reload();
		}
	}

	public static class CraftNormal {
		private ShapelessRecipe recipe;
		private ItemStack result;

		public CraftNormal(ItemStack result) {
			setResult(result);
			setRecipe(new ShapelessRecipe(result));
		}

		public void add(Material ingredient, int data) {
			this.recipe.addIngredient(ingredient, data);
		}

		public void remove(Material ingredient, int data) {
			this.recipe.removeIngredient(ingredient, data);
		}

		public ShapelessRecipe getRecipe() {
			return this.recipe;
		}

		public void setRecipe(ShapelessRecipe recipe) {
			this.recipe = recipe;
		}

		public ItemStack getResult() {
			return this.result;
		}

		public void setResult(ItemStack result) {
			this.result = result;
		}
	}

	public static class CraftExtra {
		private ShapedRecipe recipe;
		private ItemStack result;
		private Material[] items = new Material[9];
		private int[] datas = new int[9];

		public CraftExtra(ItemStack result) {
			setResult(result);
			setRecipe(new ShapedRecipe(result));
			for (int x = 0; x < this.datas.length; x++) {
				this.datas[x] = 0;
			}
		}

		public void set(int slot, Material material) {
			set(slot, material, 0);
		}

		public void set(int slot, Material material, int data) {
			if ((slot < 1) || (slot > 9)) {
				return;
			}
			this.items[(slot - 1)] = material;
			this.datas[(slot - 1)] = data;
		}

		public ShapedRecipe getRecipe() {
			try {
				this.recipe.shape(new String[] {
						(this.items[0] == null ? " " : "A") + (this.items[1] == null ? " " : "B")
								+ (this.items[2] == null ? " " : "C"),
						(this.items[3] == null ? " " : "D") + (this.items[4] == null ? " " : "E")
								+ (this.items[5] == null ? " " : "F"),
						(this.items[6] == null ? " " : "G") + (this.items[7] == null ? " " : "H")
								+ (this.items[8] == null ? " " : "I") });

				char shape = 'A';
				for (int x = 0; x < this.items.length; x++) {
					if (this.items[x] == null) {
						shape = (char) (shape + '\001');
					} else {
						this.recipe.setIngredient(shape, this.items[x], this.datas[x]);
						shape = (char) (shape + '\001');
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return this.recipe;
		}

		public void setRecipe(ShapedRecipe recipe) {
			this.recipe = recipe;
		}

		public ItemStack getResult() {
			return this.result;
		}

		public void setResult(ItemStack result) {
			this.result = result;
		}
	}

	public static enum FireworkType {
		BALL, BALL_LARGE, BURST, CREEPER, STAR;
	}

	public static class Scoreboards {
		private Scoreboard score;
		private Objective scoreObj;
		private HashMap<Integer, String> scoreSlots;
		private int scoreSize;
		private int scoreNameSize;
		private String scoreName;
		private String scoreNameColor;
		private int scoreNameId;

		public Scoreboards() {
			this.scoreSlots = new HashMap<Integer, String>();
			this.score = Bukkit.getScoreboardManager().getNewScoreboard();
			this.scoreObj = this.score.registerNewObjective(getName("�6�lSimples ScoreBoard"), "dummy");
			this.scoreObj.setDisplaySlot(DisplaySlot.SIDEBAR);
			setName("�6�lSimples ScoreBoard");
			update();
		}

		private void update() {
			setNameSize(16, "�f�l");
			new Eduard_Java8.Time(new Runnable() {
				public void run() {
					String text = ChatColor.stripColor(Eduard_Java8.Scoreboards.this.scoreName);
					if (text.length() > Eduard_Java8.Scoreboards.this.scoreNameSize) {
						text = text + Eduard_Java8.Scoreboards.this.getFake(5);
						Eduard_Java8.Scoreboards.this.scoreNameId += 1;
						String message = "";
						if (Eduard_Java8.Scoreboards.this.scoreNameId > text.length()) {
							Eduard_Java8.Scoreboards.this.scoreNameId = 0;
							message = text.substring(0, Eduard_Java8.Scoreboards.this.scoreNameSize);
						} else if (Eduard_Java8.Scoreboards.this.scoreNameId > text.length()
								- Eduard_Java8.Scoreboards.this.scoreNameSize) {
							String frontText = text.substring(0, Eduard_Java8.Scoreboards.this.scoreNameId
									+ (Eduard_Java8.Scoreboards.this.scoreNameSize - text.length()));
							message = text.substring(0 + Eduard_Java8.Scoreboards.this.scoreNameId, text.length())
									+ frontText;
						} else {
							message = text.substring(0 + Eduard_Java8.Scoreboards.this.scoreNameId,
									Eduard_Java8.Scoreboards.this.scoreNameSize
											+ Eduard_Java8.Scoreboards.this.scoreNameId);
						}
						Eduard_Java8.Scoreboards.this.scoreObj
								.setDisplayName(Eduard_Java8.Scoreboards.this.scoreNameColor + message);
					}
				}
			}, 5L, -1);
		}

		public Scoreboard getScore() {
			return this.score;
		}

		public void setNameSize(int nameSize, String colorBeforeName) {
			if (nameSize < 5) {
				nameSize = 5;
			}
			if (nameSize + colorBeforeName.length() > 32) {
				nameSize -= colorBeforeName.length();
			}
			this.scoreNameSize = nameSize;
			this.scoreNameColor = colorBeforeName;
		}

		public void setName(String name) {
			this.scoreObj.setDisplayName(getName(32, name));
			this.scoreName = name;
			this.scoreNameId = 0;
		}

		public void show(Player p) {
			p.setScoreboard(this.score);
		}

		public void setSize(int size) {
			if (((size > 15 ? 1 : 0) | (size < 1 ? 1 : 0)) != 0) {
				size = 15;
			}
			this.scoreSize = size;
			for (int x = 1; x < 15; x++) {
				removeSlot(x);
			}
			this.scoreSlots.clear();
			for (int x = 1; x <= size; x++) {
				setFake(x);
			}
		}

		public String getFake(int id) {
			String name = "";
			for (int x = 1; x <= id; x++) {
				name = name + " ";
			}
			return name;
		}

		public void setFake(int id) {
			setSlot(id, getFake(id));
		}

		public void add(String name) {
			List<Integer> time = new ArrayList<Integer>();
			for (Integer list : this.scoreSlots.keySet()) {
				if (((String) this.scoreSlots.get(list)).startsWith(" ")) {
					time.add(list);
				}
			}
			int id = 0;
			for (Integer x : time) {
				if (id < x.intValue()) {
					id = x.intValue();
				}
			}
			if (id == 0) {
				if (this.scoreSlots.keySet().size() + 1 < this.scoreSize) {
					setSlot(this.scoreSlots.keySet().size() + 1, name);
				}
			} else {
				setSlot(id, name);
			}
		}

		public void removeSlot(int id) {
			if (this.scoreSlots.containsKey(Integer.valueOf(id))) {
				this.score.resetScores(Bukkit.getOfflinePlayer((String) this.scoreSlots.get(Integer.valueOf(id))));
				this.scoreSlots.remove(Integer.valueOf(id));
			}
		}

		public void setSlot(int id, String name) {
			if ((id > this.scoreSize) || (id < 1)) {
				return;
			}
			name = getName(name);
			removeSlot(id);
			this.scoreSlots.put(Integer.valueOf(id), name);
			this.scoreObj.getScore(Bukkit.getOfflinePlayer(name)).setScore(id);
		}

		public String getName(String name) {
			return name.length() > 16 ? name.substring(0, 16) : name;
		}

		public String getName(int size, String name) {
			return name.length() > size ? name.substring(0, size) : name;
		}
	}

	public static class GUI {
		public static abstract interface IconEffect {
			public abstract void run(Player paramPlayer);
		}

		public static class Event implements Listener {
			@EventHandler
			public void OpenGui(PlayerInteractEvent e) {
				Player p = e.getPlayer();
				ItemStack item = e.getItem();
				Action action = e.getAction();
				if (item == null) {
					return;
				}
				for (Eduard_Java8.GUI gui : Eduard_Java8.GUI.all_gui) {
					if (gui.getItemKey() != null) {
						boolean itemType = false;
						if (gui.isByMaterial()) {
							if (gui.getItemKey().getType() == item.getType()) {
								itemType = true;
							}
						} else if (gui.getItemKey().equals(item)) {
							itemType = true;
						}
						if (itemType) {
							boolean actionType = false;
							if (gui.getAction() == Eduard_Java8.GUI.ActionGUI.RIGHT) {
								if ((action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK)) {
									actionType = true;
								}
							} else if (gui.getAction() == Eduard_Java8.GUI.ActionGUI.LEFT) {
								if ((action == Action.LEFT_CLICK_AIR) || (action == Action.LEFT_CLICK_BLOCK)) {
									actionType = true;
								}
							} else if ((gui.getAction() == Eduard_Java8.GUI.ActionGUI.BOTH)
									&& (action != Action.PHYSICAL)) {
								actionType = true;
							}
							if (actionType) {
								e.setCancelled(true);
								p.openInventory(gui.getGUI());
								return;
							}
						}
					}
				}
			}

			@EventHandler
			public void InteractGui(InventoryClickEvent e) {
				if (!(e.getWhoClicked() instanceof Player)) {
					return;
				}
				Player p = (Player) e.getWhoClicked();
				if (e.getCurrentItem() == null) {
					return;
				}
				for (Eduard_Java8.GUI gui : Eduard_Java8.GUI.all_gui) {
					if (gui.getGUI().getTitle().equals(e.getInventory().getTitle())) {
						if (gui.effect.containsKey(Integer.valueOf(e.getSlot()))) {
							if (gui.getItemKey().equals(e.getCurrentItem())) {
								return;
							}
							if (gui.getGUI().getSize() < e.getRawSlot()) {
								p.updateInventory();
								return;
							}
							if (gui.getGUI().getItem(e.getSlot()).equals(e.getCurrentItem())) {
								Eduard_Java8.GUI.IconEffect slotEffect = (Eduard_Java8.GUI.IconEffect) gui.effect
										.get(Integer.valueOf(e.getSlot()));
								if (slotEffect != null) {
									slotEffect.run(p);
								}
							}
						}
						e.setCancelled(true);
						return;
					}
				}
			}

			@EventHandler
			public void OpenGui(InventoryOpenEvent e) {
				if (!(e.getPlayer() instanceof Player)) {
					return;
				}
				Inventory inv = e.getInventory();
				Player p = (Player) e.getPlayer();
				for (Eduard_Java8.GUI gui : Eduard_Java8.GUI.all_gui) {
					if (inv.getTitle().equals(gui.getGUI().getTitle())) {
						p.playSound(p.getLocation(), gui.getOpenSound(), 2.0F, 1.0F);
						return;
					}
				}
			}
		}

		public static List<GUI> all_gui = new ArrayList<GUI>();
		public HashMap<Integer, IconEffect> effect = new HashMap<Integer, IconEffect>();
		private Inventory inv;
		private ActionGUI action;
		private Sound openSound;
		private ItemStack itemKey;
		private boolean byMaterial;

		public Inventory getGUI() {
			return this.inv;
		}

		private void setGUI(Inventory invGUI) {
			this.inv = invGUI;
		}

		public GUI(String name, int size) {
			if (name.isEmpty()) {
				name = "No empty Name";
			}
			if (size % 9 > 0) {
				size = 9;
			} else if (((size < 0 ? 1 : 0) | (size > 54 ? 1 : 0)) != 0) {
				size = 9;
			}
			setGUI(Bukkit.createInventory(null, size, name));
			GUI remove = null;
			for (GUI gui : all_gui) {
				if (gui.getGUI().getTitle().equals(name)) {
					remove = gui;
				}
			}
			if (remove != null) {
				all_gui.remove(remove);
			}
			all_gui.add(this);
			setAction(ActionGUI.BOTH);
			setOpenSound(Sound.HORSE_ARMOR);
			setByMaterial(false);
		}

		public void addIcon(ItemStack icon) {
			getGUI().addItem(new ItemStack[] { icon });
		}

		public void addIcon(ItemStack icon, IconEffect run) {
			addIcon(icon);
			int id = getGUI().first(icon);
			this.effect.put(Integer.valueOf(id), run);
		}

		public void setIcon(int slot, ItemStack icon) {
			getGUI().setItem(slot, icon);
		}

		public void setIcon(int slot, ItemStack icon, IconEffect run) {
			getGUI().setItem(slot, icon);
			this.effect.put(Integer.valueOf(slot), run);
		}

		public ItemStack getItemKey() {
			return this.itemKey;
		}

		public void setItemKey(ItemStack itemKey) {
			this.itemKey = itemKey;
		}

		public ActionGUI getAction() {
			return this.action;
		}

		public void setAction(ActionGUI action) {
			this.action = action;
		}

		public boolean isByMaterial() {
			return this.byMaterial;
		}

		public void setByMaterial(boolean byMaterial) {
			this.byMaterial = byMaterial;
		}

		public Sound getOpenSound() {
			return this.openSound;
		}

		public void setOpenSound(Sound openSound) {
			this.openSound = openSound;
		}

		public static enum ActionGUI {
			RIGHT, LEFT, BOTH;
		}
	}

	public static abstract interface IPlayer {
		public abstract void run(Player paramPlayer);
	}

	public static abstract interface ITimer {
		public abstract void finalRun();

		public abstract void lastRun();

		public abstract void normalRun();
	}

}
