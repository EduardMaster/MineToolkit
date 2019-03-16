package net.eduard.api.lib.old;

import java.util.HashMap;

import org.bukkit.entity.Player;



/**
 * Sistema de cooldown com suporte a pegar o Tempo Restante<br>
 * Versão anterior {@link CooldownSetup2} 1.0
 * @since 0.7
 * @version 2.0
 * @author Eduard
 * @deprecated Versão atual {@link Cooldowns}
 *
 */
public class Cooldowns
{
  private HashMap<Player, Integer> seconds = new HashMap<Player, Integer>();
  
  private HashMap<Player, Long> times = new HashMap<Player, Long>();
  
  public long getTime(Player p)
  {
    Long last = (Long)this.times.get(p);
    Integer delay = (Integer)this.seconds.get(p);
    delay = Integer.valueOf(delay.intValue() * 1000);
    long now = System.currentTimeMillis();
    long wait = last.longValue() - (now - delay.intValue());
    return wait / 1000L;
  }
  
  public boolean has(Player p)
  {
    if (this.times.containsKey(p)) return getTime(p) > 0L;
    return false;
  }
  
  public void remove(Player p)
  {
    this.times.remove(p);
    this.seconds.remove(p);
  }
  
  public void start(Player p, int seconds)
  {
    if (!has(p)) {
      this.times.put(p, Long.valueOf(System.currentTimeMillis()));
      this.seconds.put(p, Integer.valueOf(seconds));
    }
  }
}
