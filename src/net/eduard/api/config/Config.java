package net.eduard.api.config;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.game.Sounds;
import net.eduard.api.lib.storage.Storable;

/**
 * Sistema Interprador de YAML usando a secao {@link ConfigSection}
 * 

 * @author Eduard

 *
 */
public class Config implements Storable {
	private boolean saveAsUTF8 = false;

	public static void saveConfigs() {
		for (Config config : CONFIGS) {
			config.saveConfig();
		}
	}

	public static void reloadConfigs() {
		for (Config config : CONFIGS) {
			config.reloadConfig();
		}

	}

	public static void saveConfigs(Plugin plugin) {
		for (Config config : CONFIGS) {
			if (config.getPlugin().equals(plugin)) {
				config.saveConfig();
			}

		}
	}

	public static void reloadConfigs(Plugin plugin) {
		for (Config config : CONFIGS) {
			if (config.getPlugin().equals(plugin)) {
				config.reloadConfig();
			}

		}
	}

	public final static List<Config> CONFIGS = new ArrayList<>();

	private transient ConfigSection root;
	private transient File file;
	private transient Plugin plugin;
	private String name;
	private boolean autoSave;

	transient List<String> lines;

	public Config() {
		this("config.yml");
	}

	public Config(String name) {
		this(Mine.getMainPlugin(), name);
	}

	public Config(Plugin plugin) {
		this(plugin, "config.yml");
	}

	public Config(Plugin plugin, String name) {
		this.name = name;
		this.plugin = plugin;
		init();

	}

	public void init() {
		boolean contains = false;
		for (Config config : CONFIGS) {
			if (config.equals(this)) {
				file = config.file;
				lines = config.lines;
				root = config.root;
				contains = true;
				break;
			}
		}
		if (!contains) {
			// System.out.println("avancar");
			file = new File(plugin.getDataFolder(), name);
			root = new ConfigSection("", "{}");
			lines = new ArrayList<>();
			CONFIGS.add(this);
			root.lineSpaces = 1;
			this.root.father = root;
			reloadConfig();
		} else {
			// System.out.println("voltar");
		}
	}

	public List<Integer> getIntList(String path) {
		return root.getIntList(path);
	}

	public void saveDefaultConfig() {

		if (!file.exists()) {
			Mine.console("�bConfigAPI �a<- DEFAULT " + file.getName());
			if (saveAsUTF8) {
				try {
					InputStream is = Mine.getResource(plugin.getClass().getClassLoader(), name);
					Mine.copyAsUTF8(is, file);

				} catch (Exception e) {
					e.printStackTrace();
				}
				// } else {
				// plugin.saveResource(name, true);
			} else {
				if (plugin.getResource(name) != null) {
					plugin.saveResource(name, true);
				}
			}

		}
	}

	public void saveConfig() {
		lines.clear();
		root.save(this, -1);
		try {
			if (!Mine.isDirectory(file)) {
				Mine.writeLines(file, lines);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void reloadConfig() {
		try {
			file.getParentFile().mkdirs();
			if (!file.exists()) {
				if (Mine.isDirectory(file)) {
					file.mkdirs();
				} else {
					saveDefaultConfig();
				}

			}
			if (file.isFile()) {
				lines = Mine.readLines(file);
				root.reload(this);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public ConfigSection add(String path, Object value, String... comments) {
		return root.add(path, value, comments);
	}

	public Config newConfig(String name) {
		return createConfig(name);
	}

	public void addHeader(String... header) {
		if (header != null) {
			setHeader(header);
		}
	}

	public boolean contains(String path) {
		return root.contains(path);
	}

	public void copyContents(Config config) {
		config.root.save(this, -1);
		// reload();
	}

	public Config createConfig(String name) {
		return new Config(getPlugin(), name);
	}

	public void deleteConfig() {
		file.delete();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Config other = (Config) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (plugin == null) {
			if (other.plugin != null)
				return false;
		} else if (!plugin.equals(other.plugin))
			return false;
		return true;
	}

	public boolean existConfig() {
		return file.exists();
	}

	public Object get(String path) {
		return root.get(path);
	}

	public boolean getBoolean(String path) {
		return root.getBoolean(path);
	}

	public ConfigSection getConfig() {
		return root;
	}

	public List<Config> getConfigs() {
		ArrayList<Config> list = new ArrayList<>();
		if (Files.isDirectory(file.toPath())) {
			for (String subFile : file.list()) {
				list.add(createConfig(name + subFile));
			}
		}
		return list;
	}

	public List<String> getConfigsNames() {
		ArrayList<String> list = new ArrayList<>();
		for (Config config : getConfigs()) {
			list.add(config.getTitle());
		}
		return list;
	}

	public File getDataFolder() {
		return plugin.getDataFolder();
	}

	public Double getDouble(String path) {
		return root.getDouble(path);
	}

	public File getFile() {
		return file;
	}

	public Float getFloat(String path) {
		return root.getFloat(path);
	}

	public Integer getInt(String path) {
		return root.getInt(path);
	}

	public ItemStack getItem(String path) {
		return root.getItem(path);
	}

	public Set<String> getKeys() {
		return root.getKeys();
	}

	public Set<String> getKeys(String path) {
		return root.getKeys(path);
	}

	public Location getLocation(String path) {
		return root.getLocation(path);
	}

	public Long getLong(String path) {
		return root.getLong(path);
	}

	public List<String> getMessages(String path) {
		return root.getMessages(path);
	}

	public String getName() {
		return file.getName();
	}

	public String getNameComplete() {
		return name;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public ConfigSection getSection(String path) {
		return root.getSection(path);
	}

	public Sounds getSound(String path) {
		return root.getSound(path);
	}

	public String getString(String path) {
		return root.getString(path);
	}

	public List<String> getStringList(String path) {
		return root.getStringList(path);
	}

	public String getTitle() {
		return file.getName().replace(".yml", "");
	}

	public Collection<ConfigSection> getValues(String path) {
		return root.getValues(path);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((plugin == null) ? 0 : plugin.hashCode());
		return result;
	}

	public String message(String path) {
		return root.message(path);
	}

	public void remove(String path) {
		root.remove(path);
	}

	public ConfigSection set(String path, Object value) {
		return root.set(path, value, new String[0]);
	}

	public ConfigSection set(String path, Object value, String... comments) {
		return root.set(path, value, comments);
	}

	public void setHeader(String... header) {
		if (header != null) {
			root.comments.clear();
			for (String item : header) {
				root.comments.add(item);
			}
		}
	}

	@Override
	public String toString() {
		return "Config [plugin=" + plugin + ", name=" + name + "]";
	}

	public boolean isAutoSave() {
		return autoSave;
	}

	public void setAutoSave(boolean autoSave) {
		this.autoSave = autoSave;
	}

	public void setIndent(int amount) {
		root.setIndent(amount);
	}

	@Override
	public Object restore(Map<String, Object> map) {
		plugin = Bukkit.getPluginManager().getPlugin(Mine.toString(map.get("folder")));
		init();
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
		map.put("folder", plugin.getName());
	}

}
