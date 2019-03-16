package net.eduard.api.lib.old;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.server.minigame.Minigame;



/**
 * Representa um minigame que extende a classe {@link ConfigSetup}
 * @version 1.0
 * @since 0.7
 * @author Eduard
 * @deprecated Versão atual {@link Minigame}
 *
 */
public class MinigameSetup
  extends ConfigSetup
{
  private String minigameName;
  private String arenasFolder;
  
  private void setArenasFolder(String arenasFolder)
  {
    this.arenasFolder = arenasFolder;
  }
  
  public MinigameSetup(JavaPlugin java, String name) {
    super(java, name + "/config.yml");
    setMinigameName(name);
    setArenasFolder(name + "/Arenas/");
  }
  
  public void setLobby(Location loc)
  {
    setLocation("Lobby", loc);
    saveConfig();
  }
  
  public boolean hasLobby()
  {
    return hasLocation("Lobby");
  }
  

  public Location getLobby()
  {
    return getLocation("Lobby");
  }
  
  private HashMap<UUID, ArenaSetup> players = new HashMap<UUID, ArenaSetup>();
  
  public void setPlayer(Player p, String arena) {
    this.players.put(p.getUniqueId(), getArena(arena));
  }
  
  public void removePlayer(Player p) { this.players.remove(p.getUniqueId()); }
  

  public List<Player> getPlayers()
  {
    List<Player> player = new ArrayList<Player>();
    for (UUID p : this.players.keySet()) {
      player.add(Bukkit.getPlayer(p));
    }
    return player;
  }
  

  public List<Player> getPlayers(String arena)
  {
    List<Player> player = new ArrayList<Player>();
    for (Entry<UUID, ArenaSetup> p : this.players.entrySet()) {
      if (((ArenaSetup)p.getValue()).equals(getArena(arena))) {
        player.add(Bukkit.getPlayer((UUID)p.getKey()));
      }
    }
    return player;
  }
  
  public List<String> getArenaList()
  {
    ConfigSetup config = new ConfigSetup(getPlugin(), this.arenasFolder);
    config.getFile().mkdirs();
    return Arrays.asList(Arrays.asList(config.getFile().list()).toString()
      .replace(".yml", "").split(", "));
  }
  
  public List<String> getEnabledArenaList()
  {
    ArrayList<String> list = new ArrayList<String>();
    for (String arena : getArenaList()) {
      if (getArena(arena).enable()) {
        list.add(arena);
      }
    }
    return list;
  }
  
  public boolean hasPlayer(Player p)
  {
    return this.players.containsKey(p.getUniqueId());
  }
  
  public boolean hasPlayer(Player p, String arena)
  {
    if (hasPlayer(p)) return getArenaName(p).equals(arena);
    return false;
  }
  
  public ArenaSetup getArena(Player p)
  {
    return (ArenaSetup)this.players.get(p.getUniqueId());
  }
  
  public String getArenaName(Player p)
  {
    return getArena(p).getArenaName();
  }
  
  public ArenaSetup getArena(String name)
  {
    return new ArenaSetup(getPlugin(), this.arenasFolder + name + ".yml", name);
  }
  
  public String getMinigameName() {
    return this.minigameName;
  }
  
  private void setMinigameName(String minigameName) {
    this.minigameName = minigameName;
  }
}
