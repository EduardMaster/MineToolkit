package net.eduard.api.lib.old;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Cooldowns;




/**
 * Sistema de cooldown Melhorado usando {@link EventSetup}<br>
 * 
 * @since 0.7
 * @version 2.0
 * @author Eduard
 * @deprecated Versão atual {@link Cooldowns}
 *
 */
public class Cooldown
{
  private HashMap<Player, EventSetup> times = new HashMap<Player, EventSetup>();
  
  public int getTime(Player p)
  {
    return this.times.get(p).getTime();
  }
  

  public boolean has(Player p)
  {
    return this.times.containsKey(p);
  }
  
  public void start(final Player p, int seconds, final PlayerRunnable effect)
  {
    if (has(p)) {
      stop(p);
    }
    EventSetup time = new EventSetup(20, seconds)
    {
      public void run()
      {
        if (getTime() == 1) {
          effect.run(p);
          Cooldown.this.stop(p);
        }
        
      }
      
    };
    this.times.put(p, time);
  }
  
  public void start(Player p, int seconds, final String message)
  {
    start(p, seconds, new PlayerRunnable()
    {
      public void run(Player p)
      {
        p.sendMessage(message);
      }
    });
  }
  

  public void stop(Player p)
  {
    if (has(p)) {
      this.times.get(p).stop();
      this.times.remove(p);
    }
  }
}
