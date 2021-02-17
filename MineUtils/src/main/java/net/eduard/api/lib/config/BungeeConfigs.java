package net.eduard.api.lib.config;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *API de criação de Configuração além da config.yml, para BungeeCord
 * @author Eduard
 * @version 1.0
 */
@SuppressWarnings("unused")
public class BungeeConfigs {

	private final Plugin plugin;
	private final String name;
	private final File file;
	private Configuration config;

	public String getName() {
		return name;
	}

	public File getFile() {
		return file;
	}

	public Configuration getConfig() {
		return config;
	}

	public BungeeConfigs(String name, Plugin plugin) {
		this.plugin = plugin;
		file = new File(plugin.getDataFolder(), name);
		this.name = name;
		reloadConfig();
	}

	public void reloadConfig() {
		try {
			saveDefaultConfig();
			if (!file.exists()) {
				file.createNewFile();
			}
				config = getProvider().load(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void saveDefaultConfig() {
		try {
			file.getParentFile().mkdirs();
			if (!file.exists()) {
				InputStream stream = plugin.getResourceAsStream(name);
				if (stream != null) {
					Files.copy(stream, file.toPath());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private ConfigurationProvider getProvider() {
		return ConfigurationProvider.getProvider(YamlConfiguration.class);
	}

	public void saveConfig() {
		try {
			getProvider().save(config, file);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public String message(String path) {
		return ChatColor.translateAlternateColorCodes('&',
				getConfig().getString(path));
	}

	public void remove(String path) {
		config.set(path, null);
	}

	public static String toChatMessage(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public String toConfigMessage(String text) {
		return text.replace("§", "&");
	}

	public boolean delete() {
		return file.delete();
	}

	public boolean exists() {
		return file.exists();
	}

	public boolean contains(String path) {
		return config.contains(path);
	}

	public Configuration create(String path) {
		return config.getSection(path);
	}

	public Object get(String path) {
		return config.get(path);
	}
	@SuppressWarnings("unchecked")
	private Map<String, Object> getValues(Configuration config) {
		try {
			Field field = config.getClass().getDeclaredField("self");
			field.setAccessible(true);
			Map<String, Object> map = (Map<String, Object>) field.get(config);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public Map<String, Object> toMap(Configuration config) {
		Map<String, Object> sec = getValues(config);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		for (Entry<String, Object> entry : sec.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Configuration) {
				Configuration configuration = (Configuration) value;
				map.put(key, toMap(configuration));
			} else {
				map.put(key, value);
			}
		}
		return map;
	}
	public Map<String, Object> toMap(String path) {

		return toMap(getSection(path));

	}
	public boolean getBoolean(String path) {
		return config.getBoolean(path);
	}

	public double getDouble(String path) {
		return config.getDouble(path);
	}

	public int getInt(String path) {
		return config.getInt(path);
	}

	public List<Integer> getIntegerList(String path) {
		return config.getIntList(path);
	}

	public Collection<String> getKeys() {
		return config.getKeys();
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

	public String getString(String path) {
		return config.getString(path);
	}

	public List<String> getStringList(String path) {
		return config.getStringList(path);
	}

	public Configuration getSection(String path) {
		return config.getSection(path);
	}

	public void set(String path, Object value) {
		config.set(path, value);
	}

	public void add(String path, Object value) {
		if (!config.contains(path)) {
			set(path, value);
		}
		
	}

}