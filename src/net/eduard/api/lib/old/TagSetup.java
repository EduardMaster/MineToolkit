package net.eduard.api.lib.old;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.eduard.api.lib.game.Tag;
/**
 * Tag do jogador<br>
 * Versão nova {@link net.eduard.api.lib.old.Tag} 2.0
 * @version 1.0
 * @since 0.7
 * @author Eduard
 * @deprecated Versão Atual {@link Tag}
 *
 */
public class TagSetup
  implements TextSetup
{
  private static final HashMap<String, TagInfoSetup> ALL_TAGS = new HashMap<String, TagInfoSetup>();
  
  public void addNewTag(String player, TagInfoSetup tag)
  {
    player = getText(player);
    ALL_TAGS.put(player, tag);
  }
  
  public void setup()
  {
    new TimeSetup(20, 100000)
    {
      public void event()
      {
      
        for (Player p : Bukkit.getOnlinePlayers())
        {
         
          Scoreboard score = p.getScoreboard();
          if ((score != null) && 
            (!score.equals(Bukkit.getScoreboardManager().getMainScoreboard()))) {
            for (Entry<String, TagInfoSetup> teams : TagSetup.ALL_TAGS.entrySet())
            {
              String name = (String)teams.getKey();
              if (score.getTeam(name) == null) {
                score.registerNewTeam(name);
              }
              Team team = score.getTeam(name);
              TagInfoSetup tag = (TagInfoSetup)teams.getValue();
              team.setPrefix(tag.getPrefix());
              team.setSuffix(tag.getSuffix());
              OfflinePlayer player = Bukkit.getOfflinePlayer(name);
              if (!team.hasPlayer(player)) {
                team.addPlayer(player);
              }
            }
          }
        }
        Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Object teams : TagSetup.ALL_TAGS.entrySet())
        {
          String name = (String)((Entry<?, ?>)teams).getKey();
          if (score.getTeam(name) == null) {
            score.registerNewTeam(name);
          }
          Team team = score.getTeam(name);
          TagInfoSetup tag = (TagInfoSetup)((Entry<?, ?>)teams).getValue();
          team.setPrefix(tag.getPrefix());
          team.setSuffix(tag.getSuffix());
          OfflinePlayer player = Bukkit.getOfflinePlayer(name);
          if (!team.hasPlayer(player)) {
            team.addPlayer(player);
          }
        }
      }
    }.start();
  }
}
