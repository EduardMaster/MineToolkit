package net.eduard.api.lib.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * API simplificada de criar Config
 *
 * @author Eduard
 * @version 1.0
 * @since Lib v1.0
 */
@SuppressWarnings({"unused","unchecked"})
public class BukkitConfigs {

    private Plugin plugin;

    private final String name;

    private File file;

    private FileConfiguration config;

    public String getName() {

        return name;
    }


    public Location getLocation(String path) {
        ConfigurationSection section = getSection(path);
        World world = Bukkit.getWorld(section.getString("world"));
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        float yaw = (float) section.getDouble("yaw");
        float pitch = (float) section.getDouble("pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

    public void setLocation(String path, Location location) {
        ConfigurationSection section = getSection(path);
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());
        section.set("world", location.getWorld().getName());

    }

     <E> E getAlgumaCoisa(String path, Class<E> classe) {
        return (E) get(path);
    }



    int getNumeroInteiro(String path) {

        return getAlgumaCoisa(path, Integer.class);
    }

    Map<String, Object> getMapa(String path) {


        return getAlgumaCoisa(path, Map.class);
    }

    double getDecimal(String path) {

        return getAlgumaCoisa(path, Double.class);
    }

    boolean getBoleano(String path) {

        return getAlgumaCoisa(path, Boolean.class);
    }

    /**
     * Seta o Plugin da Config
     *
     * @param plugin Plugin
     */
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * @return Arquivo
     */
    public File getFile() {
        return file;
    }

    /**
     * @return Config ({@link FileConfiguration}
     */
    public FileConfiguration getConfig() {
        return config;
    }

    public BukkitConfigs(String name, Plugin plugin) {
        this.plugin = plugin;
        if (plugin == null)
            this.plugin = JavaPlugin.getProvidingPlugin(getClass());
        this.name = name;
        saveDefaultConfig();
    }

    public BukkitConfigs(String name) {
        this(name, null);
    }


    /**
     * Recarrega a config pelo conteudo do Arquivo
     *
     */
    public void reloadConfig() {
        file = new File(plugin.getDataFolder(), name);

        config = YamlConfiguration.loadConfiguration(file);

        InputStream defaults = plugin.getResource(file.getName());
        if (defaults != null) {


            YamlConfiguration loadConfig = YamlConfiguration.loadConfiguration(defaults);
            config.setDefaults(loadConfig);
        }
    }

    /**
     * Salva Config em forma de Texto no Arquivo
     *
     */
    public void saveConfig() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            fileOutputStream.write(config.saveToString().getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param path Path
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
     * Salva a Config padrao caso n§o existe a Arquivo
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
     * @param path Secao
     */
    public void remove(String path) {
        config.set(path, null);
    }

    /**
     * Salva os padr§es da Config
     *
     * @return A mesma classe
     */
    public BukkitConfigs saveDefault() {
        config.options().copyDefaults(true);
        saveConfig();
        return this;
    }

    public Location toLocation(String path) {
        String text = getString(path);
        String[] split = text.split(",");
        World world = Bukkit.getWorld(split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        float yaw = Float.parseFloat(split[4]);
        float pitch = Float.parseFloat(split[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public void saveLocation(String path, Location location) {
        String text = location.getWorld().getName() + "," +
                location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getYaw() + "," +
                location.getPitch();
        set(path, text);
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

    @SuppressWarnings("deprecation")
    public void setItem(String path, ItemStack item) {
        ConfigurationSection section = getSection(path);
        section.set("id", item.getTypeId());
        section.set("data", item.getDurability());
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                section.set("name", meta.getDisplayName());
            }
            if (meta.hasLore()) {
                List<String> lines = new ArrayList<>(meta.getLore());
                section.set("lore", lines);
            }
            if (meta.hasEnchants()) {

                StringBuilder text = new StringBuilder();
                for (Entry<Enchantment, Integer> enchant : item.getEnchantments().entrySet()) {
                    text.append(enchant.getKey().getId()).append("-").append(enchant.getValue()).append(",");
                }
                section.set("enchants", text.toString());
            }
        }

    }

    public ItemStack getItem(String path) {

        ConfigurationSection section = getSection(path);

        int id = section.getInt("id");
        int amout = section.getInt("amount");
        int durability = section.getInt("durability");
        @SuppressWarnings("deprecation")
        ItemStack item = new ItemStack(id, amout, (short) durability);

        ItemMeta meta = item.getItemMeta();

        if (section.contains("name")) {
            String name = section.getString("name").replace("§", "&");
            meta.setDisplayName(name);
        }

        if (section.contains("lore")) {
            List<String> lore = section.getStringList("lore");
            meta.setLore(lore);

        }
        item.setItemMeta(meta);
        if (section.contains("enchants")) {
            String enchants = section.getString("enchants");
            if (enchants.contains(",")) {

                try {
                    String[] encantamentosSplit = enchants.split(",");
                    for (String enchantmentPart : encantamentosSplit) {
                        String[] enchInfo = enchantmentPart.split("-");
                        @SuppressWarnings("deprecation")
                        Enchantment encantamento = Enchantment.getById(Integer.parseInt(enchInfo[0]));
                        int nivel = Integer.parseInt(enchInfo[1]);
                        item.addUnsafeEnchantment(encantamento, nivel);
                    }
                } catch (Exception e) {
                    System.out.println("§aFalha ao adicionar encanmento para o item");
                }
            } else {
                try {
                    String[] enchInfo = enchants.split("-");
                    @SuppressWarnings("deprecation")
                    Enchantment encantamento = Enchantment.getById(Integer.parseInt(enchInfo[0]));
                    int nivel = Integer.parseInt(enchInfo[1]);
                    item.addUnsafeEnchantment(encantamento, nivel);
                } catch (Exception e) {
                    System.out.println("Falha ao adicionar encantamento");
                }
            }

        }

        return item;

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


    /**
     * Classe CooldownConfig foi colocada dentro da BukkitConfigs<br>
     * Sistema de configuração focada em armazenamento de cooldowns
     *
     * @author Eduard
     */
    private String tag = "Cooldowns.";


    public void addCooldown(Player player, String cooldown, long timeToWait) {
        set(tag + getCooldownPath(player) + cooldown + ".before", System.currentTimeMillis());
        set(tag + getCooldownPath(player) + cooldown + ".cooldown", timeToWait);
    }

    private String getCooldownPath(Player player) {
        return player.getUniqueId() + ".";
    }

    public boolean inInCooldown(Player player, String cooldown) {
        return getCooldown(player, cooldown) != -1;

    }

    public String getCooldownFormated(Player player, String cooldown) {
        return ""+getCooldown(player, cooldown);
    }

    public long getCooldown(Player player, String cooldown) {

        if (contains(tag + getCooldownPath(player) + cooldown)) {
            long before = getBefore(player, cooldown);
            long now = System.currentTimeMillis();
            long wait = getDelay(player, cooldown);
            long result = -now + (before + wait);
            // -14 + 10 + 5
            // antes = 10
            // cd = 5
            // agora = 20
            // - agora + cd + antes = resultado

            return result > 0 ? result : -1;

        }
        return -1;

    }

    public long getDelay(Player player, String cooldown) {
        return getLong(tag + getCooldownPath(player) + cooldown + ".cooldown");
    }

    public long getBefore(Player player, String cooldown) {
        return getLong(tag + getCooldownPath(player) + cooldown + ".before");
    }

    public void removeCooldown(Player player, String cooldown) {
        remove(tag + getCooldownPath(player) + cooldown);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}