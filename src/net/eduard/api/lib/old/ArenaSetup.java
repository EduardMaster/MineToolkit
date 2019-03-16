package net.eduard.api.lib.old;

import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.server.minigame.MinigameRoom;

/**
 * Sistema de criação de arquivos arenas para minigames<br>
 * Extende a classe {@link ConfigSetup}<br>
 * Versão nova {@link Arena} 2.0
 * @version 1.0
 * @since 0.7
 * 
 * @author Eduard
 * @deprecated Versão atual {@link MinigameRoom}
 *
 */
public class ArenaSetup
  extends ConfigSetup
{
  private String arenaName;
  
  public ArenaSetup(JavaPlugin java, String configName, String arenaName)
  {
    super(java, configName);
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
  
  public boolean exist()
  {
    return getFile().exists();
  }
  
  public void enable(boolean value)
  {
    set("enable", Boolean.valueOf(value));
    saveConfig();
  }
  
  public boolean enable()
  {
    return ((Boolean)get("enable")).booleanValue();
  }
  
  public String getArenaName()
  {
    return this.arenaName;
  }
  
  public void setArenaName(String arenaName)
  {
    this.arenaName = arenaName;
  }
}
