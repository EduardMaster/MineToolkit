package net.eduard.api.lib.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.lib.storage.StorageObject;

/**
 * API simplificada de criar configura§§o Path = Endere§o = Secao
 * 
 * @author Eduard
 * @version 1.1
 * @since 1.0
 */
public class ConfigAPI {

	private Plugin plugin;

	private String name;

	private File file;

	private FileConfiguration config;

	public String getName() {
		return name;
	}

	/**
	 * Seta o Plugin da Config
	 * 
	 * @param plugin
	 *            Plugin
	 */
	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * 
	 * @return Arquivo
	 */
	public File getFile() {
		return file;
	}

	public ConfigAPI createConfig(String name) {
		return new ConfigAPI(name, plugin);
	}

	/**
	 * 
	 * @return Config ({@link FileConfiguration}
	 */
	public FileConfiguration getConfig() {
		return config;
	}

	public ConfigAPI(String name, Plugin plugin) {
		this.plugin = plugin;
		if (plugin == null)
			this.plugin = JavaPlugin.getProvidingPlugin(getClass());
		this.name = name;
		reloadConfig();
	}

	public ConfigAPI(String name) {
		this(name, null);
	}

	/**
	 * Recarrega a config pelo conteudo do Arquivo
	 * 
	 * @return Config
	 */
	public ConfigAPI reloadConfig() {
		file = new File(plugin.getDataFolder(), name);
		config = YamlConfiguration.loadConfiguration(file);
		InputStream defaults = plugin.getResource(file.getName());
		if (defaults != null) {
			@SuppressWarnings("deprecation")
			YamlConfiguration loadConfig = YamlConfiguration.loadConfiguration(defaults);
			config.setDefaults(loadConfig);
		}
		return this;
	}

	/**
	 * Salva Config em forma de Texto no Arquivo
	 * 
	 * @return Config
	 */
	public ConfigAPI saveConfig() {
		try {
			config.save(file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * @param path
	 *            Path
	 * @return Pega uma mensagem
	 */
	public String message(String path) {
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path));
	}

	public List<String> getMessages(String path) {
		List<String> messages = new ArrayList<>();

		for (String line : getStringList(path)) {
			messages.add(toChatMessage(line));
		}

		return messages;

	}

	/**
	 * Salva a Config padr§o caso n§o existe a Arquivo
	 */
	public void saveDefaultConfig() {
		if (plugin.getResource(name) != null)
			plugin.saveResource(name, false);
		reloadConfig();
	}

	/**
	 * Salva a config padr§o
	 */
	public void saveResource() {
		plugin.saveResource(name, true);
	}

	/**
	 * Remove a Secao
	 * 
	 * @param path
	 *            Secao
	 */
	public void remove(String path) {
		config.set(path, null);
	}

	/**
	 * Salva os padr§es da Config
	 * 
	 * @return
	 */
	public ConfigAPI saveDefault() {
		config.options().copyDefaults(true);
		saveConfig();
		return this;
	}

	/**
	 * 
	 * @param path
	 *            Secao
	 * @return Item da Secao
	 */
	public ItemStack getItem(String path) {
		return (ItemStack) get(path);
	}

	public Location getLocation(String path) {
		return (Location) get(path);
	}

	public static String toChatMessage(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static String toConfigMessage(String text) {
		return text.replace("§", "&");
	}

	public boolean delete() {
		return file.delete();
	}

	public boolean exists() {
		return file.exists();
	}

	public void add(String path, Object value) {
		config.addDefault(path, value);
	}

	public boolean contains(String path) {
		return config.contains(path);
	}

	public ConfigurationSection create(String path) {
		return config.createSection(path);
	}

	public Object get(String path) {
		Object obj = config.get(path);
		if (obj instanceof ConfigurationSection) {
			obj = toMap(path);
		}
		if (obj instanceof Map) {

		}
		return new StorageObject(null, false).restore(obj);
		// return StorageAPI.restoreValue(obj);
	}

	public Map<String, Object> toMap(String path) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Map<String, Object> sec = getSection(path).getValues(false);
		for (Entry<String, Object> entry : sec.entrySet()) {
			String name = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof ConfigurationSection) {
				map.put(name, toMap(path + "." + name));
			} else {
				map.put(name, value);
			}
		}
		return map;

	}

	public boolean getBoolean(String path) {
		return config.getBoolean(path);
	}

	public ConfigurationSection getSection(String path) {
		if (!config.isConfigurationSection(path)) {
			config.createSection(path);
		}
		return config.getConfigurationSection(path);
	}

	public double getDouble(String path) {
		return config.getDouble(path, 0);
	}

	public int getInt(String path) {
		return config.getInt(path);
	}

	public List<Integer> getIntegerList(String path) {
		return config.getIntegerList(path);
	}

	public ItemStack getItemStack(String path) {
		return config.getItemStack(path);
	}

	public Set<String> getKeys(boolean deep) {
		return config.getKeys(deep);
	}

	public List<?> getList(String path) {
		return config.getList(path);
	}

	public long getLong(String path) {
		return config.getLong(path);
	}

	public List<Long> getLongList(String path) {
		return config.getLongList(path);
	}

	public List<Map<?, ?>> getMapList(String path) {
		return config.getMapList(path);
	}

	public String getString(String path) {
		return config.getString(path);
	}

	public List<String> getStringList(String path) {
		return config.getStringList(path);
	}

	public Map<String, Object> getValues(boolean deep) {
		return config.getValues(deep);
	}

	public void set(String path, Object value) {
		if (value == null) {
			config.set(path, null);
		} else {
			config.set(path, new StorageObject(value.getClass(), false).store(value));
		}
	}

	public ConfigAPI createSubConfig(String name) {
		return createConfig(getName() + name);
	}

}