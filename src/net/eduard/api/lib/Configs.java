package net.eduard.api.lib;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * API simplificada de criar configura��o Path = Endere�o = Secao
 * 
 * @author Eduard
 * @version 1.0
 * @since Lib v1.0
 */
public class Configs {

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

	/**
	 * 
	 * @return Config ({@link FileConfiguration}
	 */
	public FileConfiguration getConfig() {
		return config;
	}

	public Configs(String name, Plugin plugin) {
		this.plugin = plugin;
		if (plugin == null)
			this.plugin = JavaPlugin.getProvidingPlugin(getClass());
		this.name = name;
		reloadConfig();
	}

	public Configs(String name) {
		this(name, null);
	}

	/**
	 * Recarrega a config pelo conteudo do Arquivo
	 * 
	 * @return Config
	 */
	public Configs reloadConfig() {
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
	public Configs saveConfig() {
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
	 * Salva a Config padr�o caso n�o existe a Arquivo
	 */
	public void saveDefaultConfig() {
		if (plugin.getResource(name) != null)
			plugin.saveResource(name, false);
		reloadConfig();
	}

	/**
	 * Salva a config padr�o
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
	 * Salva os padr�es da Config
	 * 
	 * @return
	 */
	public Configs saveDefault() {
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
		return text.replace("�", "&");
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

		return config.get(path);

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
		return config.getDouble(path);
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
		config.set(path, value);
	}

}