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

import net.eduard.api.lib.BukkitConfig;
import net.eduard.api.lib.modules.Configs;



/**
 * Sistema de criação de arquivos de configuração usando
 * {@link YamlConfiguration} e {@link FileConfiguration} <br>
 * 
 * Versão posterior é esta {@link Config1} 2.0
 * 
 * @version 1.0
 * @since EduardAPI 0.7
 * @author Eduard
 * @deprecated Futura versões {@link BukkitConfig} e {@link Configs}<br>
 */

public class ConfigSetup
{
  private FileConfiguration config;
  private File file;
  private JavaPlugin java;
  
  public ConfigSetup(JavaPlugin java)
  {
    this(java, "config.yml");
  }
  
  public ConfigSetup(JavaPlugin java, String name)
  {
    this.java = java;
    this.file = new File(java.getDataFolder(), name);
    reloadConfig();
  }
  
  public JavaPlugin getPlugin()
  {
    return this.java;
  }
  
  public ConfigSetup getConfigSetup()
  {
    return this;
  }
  
  public File getFile()
  {
    return this.file;
  }
  
  public FileConfiguration getConfig()
  {
    return this.config;
  }
  
  public void resetConfig()
  {
    ConfigurationSection section = this.config.getConfigurationSection("");
    if (section != null) {
      for (String key : section.getKeys(false)) {
        remove(key);
      }
    }
  }
  
  public void reloadConfig()
  {
    this.config = YamlConfiguration.loadConfiguration(this.file);
    InputStream defaultConfig = this.java.getResource(this.file.getName());
    if (defaultConfig != null) {
      YamlConfiguration loadConfig = YamlConfiguration.loadConfiguration(defaultConfig);
      this.config.setDefaults(loadConfig);
    }
  }
  
  public void saveDefault()
  {
    this.config.options().copyDefaults(true);
    saveConfig();
  }
  
  public void saveConfig()
  {
    try {
      this.config.save(this.file);
    }
    catch (Exception localException) {}
  }
  
  public void saveDefaultConfig()
  {
    this.java.saveResource(this.file.getName(), true);
  }
  

  public void set(String key, Object value)
  {
    this.config.set(key, value);
  }
  

  public void add(String key, Object value)
  {
    this.config.addDefault(key, value);
  }
  
  public void remove(String key)
  {
    set(key, null);
  }
  
  public void create(String key)
  {
    this.config.createSection(key);
    saveConfig();
  }
  
  public boolean has(String key)
  {
    return this.config.contains(key);
  }
  
  public Object get(String key)
  {
    return this.config.get(key);
  }
  
  public String message(String key)
  {
    return ChatColor.translateAlternateColorCodes('&', (String)get(key));
  }
  
  public ArrayList<ConfigSetup> getConfigs()
  {
    ArrayList<ConfigSetup> list = new ArrayList<>();
    for (String key : getNames()) {
      list.add(new ConfigSetup(getPlugin(), key + ".yml"));
    }
    return list;
  }
  
  public ArrayList<String> getNames()
  {
    ArrayList<String> list = new ArrayList<>();
    String[] arrayOfString; int j = (arrayOfString = this.file.list()).length; for (int i = 0; i < j; i++) { String key = arrayOfString[i];
      list.add(key.replace(".yml", ""));
    }
    return list;
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
  
  public boolean hasLocation(String name)
  {
    return (has(name + ".world")) && (has(name + ".x")) && (has(name + ".y")) && 
      (has(name + ".z")) && (has(name + ".yaw")) && (has(name + ".pitch"));
  }
  
  public Location getLocation(String name)
  {
    if (hasLocation(name)) {
      World world = Bukkit.getWorld((String)get(name + ".world"));
      double x = ((Double)get(name + ".x")).doubleValue();
      double y = ((Double)get(name + ".y")).doubleValue();
      double z = ((Double)get(name + ".z")).doubleValue();
      float yaw = (float)getConfig().getDouble(name + ".yaw");
      float pitch = (float)getConfig().getDouble(name + ".pitch");
      return new Location(world, x, y, z, yaw, pitch);
    }
    return null;
  }
  
  public void setItemSetup(String name, ItemSetup item) {
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
        List<String> list = new ArrayList<>();
        for (String lore : item.getItemMeta().getLore()) {
          list.add(lore.replace('§', '&'));
        }
        set(name + ".lore", list);
      }
    }
    int id = 1;
    for (Map.Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet()) {
      set(name + ".enchants.enchant-" + id + ".id", Integer.valueOf(((Enchantment)ench.getKey()).getId()));
      set(name + ".enchants.enchant-" + id + ".name", ((Enchantment)ench.getKey()).getName());
      set(name + ".enchants.enchant-" + id + ".level", ench.getValue());
      id++;
    }
    saveConfig();
  }
  
  public boolean hasItemSetup(String name)
  {
    return has(name + ".id") & has(name + ".data") & has(name + ".amount");
  }
  
  public ItemSetup getItemSetup(String name)
  {
    if (hasItemSetup(name)) {
      ItemSetup item = 
        new ItemSetup(Material.getMaterial(((Integer)get(name + ".id")).intValue()), ((Integer)get(name + ".amount")).intValue(), 
        (short)getConfig().getInt(name + ".data"));
      ItemMeta meta = item.getItemMeta();
      if (has(name + ".name")) meta.setDisplayName(
          ChatColor.translateAlternateColorCodes('&', (String)get(name + ".name")));
      List<String> lore = new ArrayList<>();
      if (has(name + ".lore")) {
        for (String key : getConfig().getStringList(name + ".lore")) {
          lore.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        meta.setLore(lore);
      }
      item.setItemMeta(meta);
      if (has(name + ".enchants"))
      {
        Iterator<String> teste = getConfig().getConfigurationSection(name + ".enchants").getKeys(false).iterator();
        while (teste.hasNext()) {
          String enchant = (String)teste.next();
          item.addUnsafeEnchantment(
            Enchantment.getById(((Integer)get(name + ".enchants." + enchant + ".id")).intValue()), 
            ((Integer)get(name + ".enchants." + enchant + ".level")).intValue());
        }
      }
      
      return item;
    }
    return null;
  }
  
  public void setSoundSetup(String name, SoundSetup sound) {
    if (sound == null) {
      create(name);
      return;
    }
    set(name + ".name", sound.getSound().name());
    set(name + ".volume", Float.valueOf(sound.getVolume()));
    set(name + ".pitch", Float.valueOf(sound.getPitch()));
    saveConfig();
  }
  
  public boolean hasSoundSetup(String name) { return has(name + ".name") & has(name + ".volume") & has(name + ".pitch"); }
  
  public SoundSetup getSoundSetup(String name) {
    if (hasSoundSetup(name)) {
      Sound sound = Sound.valueOf((String)get(name + ".name"));
      float volume = (float)getConfig().getDouble(name + ".volume");
      float pitch = (float)getConfig().getDouble(name + ".pitch");
      return new SoundSetup(sound, volume, pitch);
    }
    return null;
  }
}
