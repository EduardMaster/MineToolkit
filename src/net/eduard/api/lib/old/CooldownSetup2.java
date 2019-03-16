package net.eduard.api.lib.old;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Cooldowns;



/**
 * Sistema de cooldown com suporte a pegar o Tempo Restante<br>
 * Versão nova {@link net.eduard.api.lib.old.Cooldowns} 1.0
 * @since 0.7
 * @version 1.0
 * @author Eduard
 * @deprecated Versão atual {@link Cooldowns}
 *
 */
public class CooldownSetup2
{
  private HashMap<UUID, Integer> seconds = new HashMap<>();
  
  private HashMap<UUID, Long> times = new HashMap<>();
  
  public boolean has(Player p)
  {
    if (this.times.containsKey(p.getUniqueId())) return getTime(p) > 0L;
    return false;
  }
  
  public void start(Player p, int seconds)
  {
    if (!has(p)) {
      this.times.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
      this.seconds.put(p.getUniqueId(), Integer.valueOf(seconds));
    }
  }
  
  public void remove(Player p)
  {
    this.times.remove(p.getUniqueId());
    this.seconds.remove(p.getUniqueId());
  }
  
  public long getTime(Player p)
  {
    Long last = (Long)this.times.get(p.getUniqueId());
    Integer delay = (Integer)this.seconds.get(p.getUniqueId());
    delay = Integer.valueOf(delay.intValue() * 1000);
    long now = System.currentTimeMillis();
    long wait = last.longValue() - (now - delay.intValue());
    return wait / 1000L;
  }
}
