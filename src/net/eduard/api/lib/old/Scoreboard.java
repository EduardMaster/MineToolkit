package net.eduard.api.lib.old;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import net.eduard.api.lib.game.DisplayBoard;
import net.eduard.api.lib.modules.Scoreboards;





/**
 * Sistema de criar scoreboard mais facil
 * 
 * @deprecated Versão atual {@link DisplayBoard} e {@link Scoreboards}<br>
 *             Versão anterior {@link ScoreboardSetup}
 * @version 2.0
 * @author Eduard
 * 
 */
public class Scoreboard
{
  private Objective objective;
  private org.bukkit.scoreboard.Scoreboard scoreboard;
  private HashMap<Integer, OfflinePlayer> slots = new HashMap<Integer, OfflinePlayer>();
  
  private HashMap<Integer, Team> teams = new HashMap<Integer, Team>();
  
  public Scoreboard(String name)
  {
    setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    setObjective(getScoreboard().registerNewObjective(Principal.getText(name), "dummy"));
    setName(name);
    getObjective().setDisplaySlot(DisplaySlot.SIDEBAR);
    for (int i = 1; i < 16; i++) {
      this.slots.put(Integer.valueOf(i), getFake("SLOT-" + i));
    }
    for (int i = 1; i < 16; i++) {
      this.teams.put(Integer.valueOf(i), getScoreboard().registerNewTeam("TEAM-" + i));
    }
    for (int i = 1; i <= 5; i++) {
      setEmpty(i);
    }
  }
  
  private String getEmpty(int size)
  {
    StringBuilder empty = new StringBuilder();
    for (int i = 0; i < size; i++) {
      empty.append(" ");
    }
    return empty.toString();
  }
  
  private OfflinePlayer getFake(String name)
  {
    for (int i = 1; i <= 16; i++) {
      if (name.equals(getEmpty(i))) {
        if (name.length() >= 4) {
          name = 
          
            ChatColor.values()[i] + getEmpty(i > 12 ? 12 : i) + ChatColor.RESET;
          break; } if (name.length() < 2) break;
        name = ChatColor.RESET + getEmpty(i > 14 ? 14 : i);
        
        break;
      }
    }
    return Bukkit.getOfflinePlayer(Principal.getText(name));
  }
  
  private int getId(int id)
  {
    return id > 15 ? 15 : id < 1 ? 1 : id;
  }
  
  public String getName()
  {
    return getObjective().getDisplayName();
  }
  
  public Objective getObjective()
  {
    return this.objective;
  }
  
  public org.bukkit.scoreboard.Scoreboard getScoreboard()
  {
    return this.scoreboard;
  }
  
  public void remove(int id)
  {
    id = getId(id);
    getScoreboard().resetScores((OfflinePlayer)this.slots.get(Integer.valueOf(id)));
  }
  
  public void set(int id, String display)
  {
    id = getId(id);
    ((Team)this.teams.get(Integer.valueOf(id))).removePlayer((OfflinePlayer)this.slots.get(Integer.valueOf(id)));
    getScoreboard().resetScores((OfflinePlayer)this.slots.get(Integer.valueOf(id)));
    display = Principal.getText(48, display);
    OfflinePlayer p = null;
    if (display.length() > 32) {
      p = getFake(display.substring(16, 32));
      Team team = (Team)this.teams.get(Integer.valueOf(id));
      team.addPlayer(p);
      team.setPrefix(display.substring(0, 16));
      team.setSuffix(display.substring(32, display.length() - 1));
    }
    else if (display.length() > 16) {
      p = getFake(display.substring(16, display.length() - 1));
      Team team = (Team)this.teams.get(Integer.valueOf(id));
      team.addPlayer(p);
      team.setPrefix(display.substring(0, 16));
    } else {
      p = getFake(display);
      this.slots.put(Integer.valueOf(id), p);
    }
    getObjective().getScore(p).setScore(id);
  }
  

  public void setEmpty(int id)
  {
    id = getId(id);
    set(id, getEmpty(16));
  }
  
  public void setName(String name)
  {
    getObjective().setDisplayName(Principal.getText(32, name));
  }
  
  private void setObjective(Objective objective)
  {
    this.objective = objective;
  }
  
  private void setScoreboard(org.bukkit.scoreboard.Scoreboard scoreboard)
  {
    this.scoreboard = scoreboard;
  }
}
