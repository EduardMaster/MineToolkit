package net.eduard.api.lib.old;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Cooldowns;




/**
 * Sistema de cooldown Melhorado usando {@link TimeSetup}<br>
 * 
 * @since 0.7
 * @version 1.0
 * @author Eduard
 * @deprecated Versão atual {@link Cooldowns}
 *
 */
public class CooldownSetup1
{
  private HashMap<UUID, TimeSetup> times = new HashMap<>();
  
  public int getTime(Player p)
  {
    return ((TimeSetup)this.times.get(p.getUniqueId())).getTime();
  }
  

  public boolean has(Player p)
  {
    return this.times.containsKey(p.getUniqueId());
  }
  
  public void start(final Player p, int seconds, final PlayerManager event) {
    TimeSetup time = new TimeSetup(20, seconds)
    {
      public void event() {
        if (getTime() == 1) {
          event.playerEvent(p);
          CooldownSetup1.this.times.remove(p.getUniqueId());
        }
        
      }
      
    };
    this.times.put(p.getUniqueId(), time);
  }
  
  public void start(Player p, int seconds, final String message) { start(p, seconds, new PlayerManager()
    {

      public void playerEvent(Player p)
      {
        p.sendMessage(message);
      }
    }); }
  


  public void stop(Player p)
  {
    if (has(p)) {
      ((TimeSetup)this.times.get(p.getUniqueId())).stop();
      this.times.remove(p.getUniqueId());
    }
  }
}
