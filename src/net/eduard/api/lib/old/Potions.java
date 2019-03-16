package net.eduard.api.lib.old;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;





/**
 * Representa um Efeito de poção sendo aplicado
 * @since 0.9
 * @version 1.0
 * @deprecated Versão atual potion
 * @author Eduard
 *
 */
public class Potions
{
  private PotionEffect effect;
  private int level;
  private int time;
  private PotionEffectType type;
  
  public Potions(PotionEffectType type, int level, int time)
  {
    this.type = type;
    this.level = level;
    this.time = time;
  }
  

  public PotionEffect create(LivingEntity entity)
  {
    this.effect = new PotionEffect(getType(), getTime(), getLevel(), true);
    entity.addPotionEffect(this.effect, true);
    return this.effect;
  }
  
  public int getLevel()
  {
    return this.level;
  }
  
  public int getTime()
  {
    return this.time;
  }
  
  public PotionEffectType getType()
  {
    return this.type;
  }
  
  public void setLevel(int level)
  {
    this.level = level;
  }
  
  public void setTime(int time)
  {
    this.time = time;
  }
  
  public void setType(PotionEffectType type)
  {
    this.type = type;
  }
}
