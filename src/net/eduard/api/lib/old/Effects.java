package net.eduard.api.lib.old;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.eduard.api.lib.game.VisualEffect;



/**
 * Representa um Efeito visual
 * @since 0.9
 * @deprecated Versão atual {@link VisualEffect}
 * @version 1.0
 * @author Eduard
 *
 */
public class Effects
{
  private int data;
  private Effect type;
  
  public Effects(Effect type, int data)
  {
    this.data = data;
    this.type = type;
  }
  
  public void create(Entity entity, int radius)
  {
    try
    {
      entity.getLocation().getWorld().playEffect(entity.getLocation(), getType(), getData(), radius);
    }
    catch (Exception localException) {}
  }
  
  public void create(Location loc)
  {
    try {
      loc.getWorld().playEffect(loc, getType(), getData());
    }
    catch (Exception localException) {}
  }
  
  public void create(Location loc, int radius)
  {
    try {
      loc.getWorld().playEffect(loc, getType(), getData(), radius);
    }
    catch (Exception localException) {}
  }
  
  public void create(Player p)
  {
    try {
      p.playEffect(p.getLocation(), getType(), getData());
    }
    catch (Exception localException) {}
  }
  
  public void create(Player p, Location loc)
  {
    try {
      p.playEffect(loc, getType(), getData());
    }
    catch (Exception localException) {}
  }
  
  public int getData()
  {
    return this.data;
  }
  
  public Effect getType()
  {
    return this.type;
  }
  
  public void setData(int data)
  {
    this.data = data;
  }
  
  public void setType(Effect type)
  {
    this.type = type;
  }
}
