package net.eduard.api.lib.old;

import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.server.minigame.MinigameRoom;
/**
 * Sistema de criação de arquivos arenas para minigames<br>
 * Extende a classe {@link Config1}<br>
 * Versão anterior {@link ArenaSetup} 1.0
 * @version 2.0
 * @since 0.9
 * 
 * @author Eduard
 * @deprecated Versão atual {@link MinigameRoom}
 *
 */
public class Arena
  extends Config1
{
  private String arenaName;
  
  public Arena(JavaPlugin plugin, String folderName, String arenaName)
  {
    super(plugin, folderName + arenaName + ".yml");
    setArenaName(arenaName);
  }
  
  public void create()
  {
    enable(false);
  }
  
  public void delete()
  {
    getFile().delete();
  }
  
  public void enable(boolean value)
  {
    set("enable", Boolean.valueOf(value));
    saveConfig();
  }
  
  public boolean enabled()
  {
    return getConfig().getBoolean("enable");
  }
  
  public boolean exist()
  {
    return getFile().exists();
  }
  
  public String getArenaName()
  {
    return this.arenaName;
  }
  
  private void setArenaName(String arenaName)
  {
    this.arenaName = arenaName;
  }
}
