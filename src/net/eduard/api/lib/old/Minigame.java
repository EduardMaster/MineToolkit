package net.eduard.api.lib.old;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;




/**
 * Representa um minigame que extende a classe {@link Config1}
 * @version 2.0
 * @since 0.9
 * @author Eduard
 * @deprecated Versão atual {@link Minigame}
 *
 */
public class Minigame
  extends Config1
{
  public static final String ARENAS = "/Arenas/";
  private String minigameName;
  private HashMap<Player, Arena> players = new HashMap<Player, Arena>();
  
  public Minigame(JavaPlugin java, String name)
  {
    super(java, name + "/config.yml");
    setMinigameName(name);
  }
  
  public Arena getArena(Player p)
  {
    return (Arena)this.players.get(p);
  }
  
  public Arena getArena(String name)
  {
    return new Arena(getPlugin(), getMinigameName() + "/Arenas/", name);
  }
  
  public List<String> getArenaList()
  {
    Config1 config = new Config1(getPlugin(), getMinigameName() + "/Arenas/");
    config.getFile().mkdirs();
    return Arrays.asList(Arrays.asList(config.getFile().list()).toString()
      .replace(".yml", "").split(", "));
  }
  
  public String getArenaName(Player p)
  {
    return getArena(p).getArenaName();
  }
  
  public List<String> getEnabledArenaList()
  {
    ArrayList<String> list = new ArrayList<String>();
    for (String arena : getArenaList()) {
      if (getArena(arena).enabled()) {
        list.add(arena);
      }
    }
    return list;
  }
  
  public Location getLobby()
  {
    return getLocation("Lobby");
  }
  
  public String getMinigameName()
  {
    return this.minigameName;
  }
  
  public List<Player> getPlayers()
  {
    List<Player> player = new ArrayList<Player>();
    for (Player p : this.players.keySet()) {
      player.add(p);
    }
    return player;
  }
  
  public List<Player> getPlayers(String arena)
  {
    List<Player> player = new ArrayList<Player>();
    for (Entry<Player, Arena> p : this.players.entrySet()) {
      if (((Arena)p.getValue()).equals(getArena(arena))) {
        player.add((Player)p.getKey());
      }
    }
    return player;
  }
  
  public boolean hasLobby()
  {
    return hasLocation("Lobby");
  }
  

  public boolean hasPlayer(Player p)
  {
    return this.players.containsKey(p);
  }
  
  public boolean hasPlayer(Player p, String arena)
  {
    if (hasPlayer(p)) return getArenaName(p).equals(arena);
    return false;
  }
  
  public void removePlayer(Player p)
  {
    this.players.remove(p);
  }
  
  public void setLobby(Location loc)
  {
    setLocation("Lobby", loc);
  }
  
  private void setMinigameName(String minigameName)
  {
    this.minigameName = minigameName;
  }
  
  public void setPlayer(Player p, String arena)
  {
    this.players.put(p, getArena(arena));
  }
}
