package net.eduard.api.lib.config;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.eduard.api.lib.game.SoundEffect;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;

/**
 * Sistema Interprador de YAML usando a secao {@link ConfigSection}
 * 
 * 
 * @author Eduard
 *
 * 
 */
public class Config implements Storable {

	private transient ConfigSection root;
	private transient File file;
	private transient File folder;
	private transient Object plugin;
	private String name;
	

	transient List<String> lines;

	public Config() {

	}

	public Config(String folder, String name) {
		this.name = name;
		this.folder = new File(folder);
		init();
	}

	public Config(Object plugin, String name) {
		this.name = name;
		this.plugin = plugin;
		this.folder = getDataFolder();
		init();
	}
	
	public void init() {
		file = new File(folder, name);
		root = new ConfigSection("", "{}");
		lines = new ArrayList<>();

		root.lineSpaces = 1;
		this.root.father = root;
		reloadConfig();
	}

	public File getDataFolder() {
		try {
			return (File) plugin.getClass().getMethod("getDataFolder").invoke(plugin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List<Integer> getIntList(String path) {
		return root.getIntList(path);
	}

	public void log(String msg) {
		System.out.println("[Config] " + msg);
	}

	public void saveDefaultConfig() {
		file.getParentFile().mkdirs();

		if (!file.exists()) {
			log("<- SAVING DEFAULT " + name);
			if (Extra.isDirectory(file)) {
				file.mkdirs();
			} else {

			

				try {
					InputStream is = Extra.getResource(plugin.getClass().getClassLoader(), name);
					Extra.copyAsUTF8(is, file);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
				// } else {
				// plugin.saveResource(name, true);

//				if (plugin.getResource(name) != null) {
//					plugin.saveResource(name, true);
//				} else {
//					try {
//						file.createNewFile();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
			}
		}
	}

	public void saveConfig() {
		log("<- SAVING " + name);
		lines.clear();
		root.save(lines, -1);
		try {
			if (!Extra.isDirectory(file)) {
				Extra.writeLines(file, lines);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void reloadConfig() {
		try {
			log("<- RELOADING " + name);
			saveDefaultConfig();

			if (file.isFile()) {
				lines = Extra.readLines(file);
				root.getMap().clear();
				root.reload(lines);
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
		config.root.save(config.lines, -1);
		// reload();
	}

	public Config createConfig(String name) {
		return new Config(folder.getName(), name);
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

	public List<Config> getConfigsChildren() {
		ArrayList<Config> list = new ArrayList<>();
		if (Files.isDirectory(file.toPath())) {
			for (String subFile : file.list()) {
				list.add(createConfig(name + subFile));
			}
		}
		return list;
	}

	public List<String> getConfigsChildrenNames() {
		ArrayList<String> list = new ArrayList<>();
		for (Config config : getConfigsChildren()) {
			list.add(config.getTitle());
		}
		return list;
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

	public Set<String> getKeys() {
		return root.getKeys();
	}

	public Set<String> getKeys(String path) {
		return root.getKeys(path);
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

	public ConfigSection getSection(String path) {
		return root.getSection(path);
	}

	public SoundEffect getSound(String path) {
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


	public void setIndent(int amount) {
		root.setIndent(amount);
	}

	@Override
	public Object restore(Map<String, Object> map) {
		this.folder = new File((String) map.get("folder"));
		init();
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
		map.put("folder", getDataFolder());
	}

}
