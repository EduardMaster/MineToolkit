package net.eduard.api.lib.config;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

import net.eduard.api.lib.hybrid.Hybrid;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.plugin.IPlugin;

/**
 * Sistema Interprador de YAML usando a secao {@link ConfigSection}
 *
 * @author Eduard
 */
public class Config {

    private transient ConfigSection root;
    private transient File file;
    private final transient File folder;
    private transient Object plugin;
    private final String name;


    private File getDataFolder() {
        try {
            return (File) Extra.getMethodInvoke(plugin, "getDataFolder");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public Config(IPlugin plugin, String name) {
        this.name = name;
        this.plugin = plugin.getPlugin();
        this.folder = plugin.getPluginFolder();
        init();
    }

    public Config(Object plugin, String name) {
        this.name = name;
        this.plugin = plugin;
        this.folder = getDataFolder();
        init();
    }

    public Config(File dataFolder, String name) {
        this.name = name;
        this.folder = dataFolder;
        init();
    }


    public void init() {
        file = new File(folder, name);
        file.getParentFile().mkdirs();
        root = new ConfigSection("", "{}");
        root.lineSpaces = 1;
        this.root.father = root;
        reloadConfig();
    }


    public List<Integer> getIntList(String path) {
        return root.getIntList(path);
    }

    public void log(String msg) {
        Hybrid.instance.getConsole()
                .sendMessage("§b[ConfigAPI] §3(" + name + ") §f" + msg);
    }

    public void saveDefaultConfig() {
        file.getParentFile().mkdirs();
        if (file.exists()) return;
        if (Extra.isDirectory(file)) {
            file.mkdirs();
            return;
        }
        if (plugin == null) return;
        try {
            log("COPYING DEFAULT...");
            InputStream defaultResourceFile = Extra.getResource(plugin.getClass().getClassLoader(), name);
            if (defaultResourceFile == null) {
                log("DEFAULT NOT FOUNDED!");
                return;
            }
            Extra.copyAsUTF8(defaultResourceFile, file);
            log("DEFAULT COPIED!");

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void saveConfig() {

        log("SAVING...");
        try {
            if (Extra.isDirectory(file)) {
                log("NOT SAVED!");
                return;

            }
            List<String> lines = new ArrayList<>();
            root.save(lines, -1);
            Extra.writeLines(file, lines);
            log("SAVED!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void reloadConfig() {
        try {
            saveDefaultConfig();

            if (file.isFile() && file.exists()) {
                log("RELOADING...");

                root.getMap().clear();
                root.reload(Extra.readLines(file));
                log("RELOADED!");
            } else {
                log("NOT RELOADED!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public <E> E get(Class<E> clazz) {
        return root.get("", clazz);
    }

    public <E> E get(String path, Class<E> clazz) {
        return root.get(path, clazz);
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

    public Config createConfig(String name) {
        return new Config(folder, name);
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
            return other.plugin == null;
        } else return plugin.equals(other.plugin);
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
            for (String subFile : Objects.requireNonNull(file.list())) {
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
        return name;
    }

    public ConfigSection getSection(String path) {
        return root.getSection(path);
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

    public ConfigSection set(Object value) {
        return set("", value);
    }

    public ConfigSection set(String path, Object value) {

        return root.set(path, value);
    }

    public ConfigSection set(String path, Object value, String... comments) {
        return root.set(path, value, comments);
    }

    public void setHeader(String... header) {
        if (header != null) {
            root.comments.clear();
            root.comments.addAll(Arrays.asList(header));
        }
    }

    @Override
    public String toString() {
        return "Config [plugin=" + plugin + ", name=" + name + "]";
    }


    public void setIndent(int amount) {
        root.setIndent(amount);
    }

}
