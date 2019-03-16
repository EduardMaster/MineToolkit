package net.eduard.api.lib.old;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import net.eduard.api.lib.BukkitConfig;
import net.eduard.api.lib.modules.Configs;

/**
 * Sistema de criação de arquivos de configuração usando
 * {@link YamlConfiguration} e {@link FileConfiguration} <br>
 * 
 * Versão anterior a esta {@link ConfigSetup} 1.0
 * 
 * @version 2.0
 * 
 * @author Eduard
 * @deprecated Futura versões {@link BukkitConfig} e {@link Configs}<br>
 */

public class Config1 {
	private File file;
	private FileConfiguration fileConfig;
	private String fileName;
	private JavaPlugin javaPlugin;

	public Config1() {
		this("config.yml");
	}

	public Config1(JavaPlugin plugin) {
		this(plugin, "config.yml");
	}

	public Config1(JavaPlugin plugin, String name) {
		this.javaPlugin = plugin;
		this.file = new File(plugin.getDataFolder(), name);
		setFileName(name);
		reloadConfig();
	}

	public Config1(String name) {
		this(JavaPlugin.getProvidingPlugin(Config1.class), name);
	}

	public void add(String key, Object value) {
		this.fileConfig.addDefault(key, value);
	}

	public void create(String key) {
		this.fileConfig.createSection(key);
		saveConfig();
	}

	public Config1 createConfig(String name) {
		return new Config1(getPlugin(), name);
	}

	public Object get(String key) {
		return this.fileConfig.get(key);
	}

	public Boolean getBoolean(String key) {
		return Boolean.valueOf(this.fileConfig.getBoolean(key));
	}

	public Byte getByte(String name) {
		return Byte.valueOf(Principal.toByte(this.fileConfig.get(name)));
	}

	public FileConfiguration getConfig() {
		return this.fileConfig;
	}

	public ArrayList<Config1> getConfigs() {
		ArrayList<Config1> list = new ArrayList<Config1>();
		for (String key : getNames()) {
			list.add(new Config1(getPlugin(), key + ".yml"));
		}
		return list;
	}

	public Double getDouble(String key) {
		return Double.valueOf(this.fileConfig.getDouble(key));
	}

	public File getFile() {
		return this.file;
	}

	public String getFileName() {
		return this.fileName;
	}

	public Float getFloat(String key) {
		return Float.valueOf(Principal.toFloat(this.fileConfig.get(key)));
	}

	public Integer getInt(String key) {
		return Integer.valueOf(this.fileConfig.getInt(key));
	}

	public Item getItem(String name) {
		if (hasItem(name)) {
			Item item = new Item(Material.getMaterial(getInt(name + ".id").intValue()),
					getInt(name + ".amount").intValue(), getShort(name + ".data").shortValue());
			ItemMeta meta = item.getItemMeta();
			if (has(name + ".name"))
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getString(name + ".name")));
			List<String> lore = new ArrayList<String>();
			if (has(name + ".lore")) {
				for (String key : getConfig().getStringList(name + ".lore")) {
					lore.add(ChatColor.translateAlternateColorCodes('&', key));
				}
				meta.setLore(lore);
			}
			item.setItemMeta(meta);
			if (has(name + ".enchants")) {
				Iterator<String> teste = getConfig().getConfigurationSection(name + ".enchants").getKeys(false)
						.iterator();
				while (teste.hasNext()) {
					String enchant = (String) teste.next();
					item.addUnsafeEnchantment(
							Enchantment.getById(getInt(name + ".enchants." + enchant + ".id").intValue()),
							getInt(name + ".enchants." + enchant + ".level").intValue());
				}
			}

			return item;
		}
		return null;
	}

	public Jump getJump(String name) {
		if (hasJump(name)) {
			Object vector = get(name + ".vector");
			boolean withHigh = getBoolean(name + ".with-high").booleanValue();
			double high = getDouble(name + ".high").doubleValue();
			double force = getDouble(name + ".force").doubleValue();
			int ticks = getInt(name + ".delay-ticks").intValue();
			Sounds sound = getSound(name + ".sound");
			Jump secondJump = getJump(name + ".second-effect");
			Jump jump = new Jump(withHigh, high, force, (vector instanceof Vector) ? (Vector) vector : null, sound,
					secondJump, ticks);
			return jump;
		}
		return null;
	}

	public Location getLocation(String name) {
		if (hasLocation(name)) {
			World world = Bukkit.getWorld(getString(name + ".world"));
			double x = getDouble(name + ".x").doubleValue();
			double y = getDouble(name + ".y").doubleValue();
			double z = getDouble(name + ".z").doubleValue();
			float yaw = getFloat(name + ".yaw").floatValue();
			float pitch = getFloat(name + ".pitch").floatValue();
			return new Location(world, x, y, z, yaw, pitch);
		}
		return null;
	}

	public ArrayList<String> getNames() {
		this.file.mkdirs();
		ArrayList<String> list = new ArrayList<String>();
		String[] arrayOfString;
		int j = (arrayOfString = this.file.list()).length;
		for (int i = 0; i < j; i++) {
			String key = arrayOfString[i];
			list.add(key.replace(".yml", ""));
		}
		return list;
	}

	public JavaPlugin getPlugin() {
		return this.javaPlugin;
	}

	public ConfigurationSection getSection(String name) {
		return this.fileConfig.getConfigurationSection(name);
	}

	public Short getShort(String key) {
		return Short.valueOf(Principal.toShort(this.fileConfig.get(key)));
	}

	public Sounds getSound(String name) {
		if (hasSound(name)) {
			Sound sound = Sound.valueOf(getString(name + ".name"));
			float volume = getFloat(name + ".volume").floatValue();
			float pitch = getFloat(name + ".pitch").floatValue();
			return new Sounds(sound, volume, pitch);
		}
		return null;
	}

	public String getString(String key) {
		return this.fileConfig.getString(key);
	}

	public boolean has(String key) {
		return this.fileConfig.contains(key);
	}

	public boolean hasItem(String name) {
		return has(name + ".id") & has(name + ".data") & has(name + ".amount");
	}

	public boolean hasJump(String name) {
		return has(name + ".with-high") & has(name + ".high") & has(name + ".force") & has(name + ".delay-ticks")
				& has(name + ".vector") & has(name + ".sound") & has(name + ".second-effect");
	}

	public boolean hasLocation(String name) {
		return (has(name + ".world")) && (has(name + ".x")) && (has(name + ".y")) && (has(name + ".z"))
				&& (has(name + ".yaw")) && (has(name + ".pitch"));
	}

	public boolean hasSound(String name) {
		return has(name + ".name") & has(name + ".volume") & has(name + ".pitch");
	}

	public String message(String message) {
		return Principal.getMessage(getString(message));
	}

	public void reloadConfig() {
		this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
		InputStream defaultConfig = this.javaPlugin.getResource(this.file.getName());
		if (defaultConfig != null) {
			YamlConfiguration loadConfig = YamlConfiguration.loadConfiguration(defaultConfig);
			this.fileConfig.setDefaults(loadConfig);
		}
	}

	public void remove(String key) {
		set(key, null);
	}

	public void resetConfig() {
		ConfigurationSection section = getSection("");
		if (section != null) {
			for (String name : section.getKeys(false)) {
				remove(name);
			}
		}
	}

	public void saveConfig() {
		try {
			this.fileConfig.save(this.file);
		} catch (Exception localException) {
		}
	}

	public void saveDefault() {
		this.fileConfig.options().copyDefaults(true);
		saveConfig();
	}

	public void saveFile() {
		this.javaPlugin.saveResource(getFileName(), false);
	}

	public void set(String key, Object value) {
		this.fileConfig.set(key, value);
	}

	private void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setItem(String name, Item item) {
		if (item == null) {
			create(name);
			return;
		}
		set(name + ".id", Integer.valueOf(item.getTypeId()));
		set(name + ".data", Short.valueOf(item.getDurability()));
		set(name + ".amount", Integer.valueOf(item.getAmount()));
		set(name + ".type", item.getType().name());
		set(name + ".max-stack", Integer.valueOf(item.getMaxStackSize()));
		if (item.hasItemMeta()) {
			if (item.getItemMeta().hasDisplayName()) {
				set(name + ".name", item.getItemMeta().getDisplayName().replace('§', '&'));
			}
			if (item.getItemMeta().hasLore()) {
				List<String> list = new ArrayList<String>();
				for (String lore : item.getItemMeta().getLore()) {
					list.add(lore.replace('§', '&'));
				}
				set(name + ".lore", list);
			}
		}
		int id = 1;
		for (Map.Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet()) {
			set(name + ".enchants.enchant-" + id + ".id", Integer.valueOf(((Enchantment) ench.getKey()).getId()));
			set(name + ".enchants.enchant-" + id + ".name", ((Enchantment) ench.getKey()).getName());
			set(name + ".enchants.enchant-" + id + ".level", ench.getValue());
			id++;
		}
		saveConfig();
	}

	public void setJump(String name, Jump jump) {
		if (jump == null) {
			create(name);
			return;
		}
		set(name + ".with-high", Boolean.valueOf(jump.isWithHigh()));
		set(name + ".high", Double.valueOf(jump.getHigh()));
		set(name + ".force", Double.valueOf(jump.getForce()));
		set(name + ".delay-ticks", Integer.valueOf(jump.getTicksDelay()));
		if (jump.getVector() == null) {
			create(name + ".vector");
		} else {
			set(name + ".vector", jump.getVector());
		}
		setSound(name + ".sound", jump.getSound());
		setJump(name + ".second-effect", jump.getSecondEffect());
		saveConfig();
	}

	public void setLocation(String name, Location location) {
		if (location == null) {
			create(name);
			return;
		}
		set(name + ".world", location.getWorld().getName());
		set(name + ".x", Double.valueOf(location.getX()));
		set(name + ".y", Double.valueOf(location.getY()));
		set(name + ".z", Double.valueOf(location.getZ()));
		set(name + ".yaw", Float.valueOf(location.getYaw()));
		set(name + ".pitch", Float.valueOf(location.getPitch()));
		saveConfig();
	}

	public void setSound(String name, Sounds sound) {
		if (sound == null) {
			create(name);
			return;
		}
		set(name + ".name", sound.getSound().name());
		set(name + ".volume", Float.valueOf(sound.getVolume()));
		set(name + ".pitch", Float.valueOf(sound.getPitch()));
		saveConfig();
	}
}
