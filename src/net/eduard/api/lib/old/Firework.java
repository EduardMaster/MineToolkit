package net.eduard.api.lib.old;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * Sistema de criar efeitos de fogos de artifícios
 * 
 * @version 2.0
 * @since 0.9
 * @deprecated Versão anterior {@link FireworkSetup} 1.0
 * @author Eduard
 *
 */
public class Firework
{
  private List<FireworkEffect> effects = new ArrayList<FireworkEffect>();
  
  private int power;
  

  public FireworkEffect.Builder addEffectt(boolean trail, boolean flicker, FireworkType type, Color firstColor, Color lastColor)
  {
    FireworkEffect.Builder effect = 
      FireworkEffect.builder().trail(trail).flicker(flicker)
      .with(FireworkEffect.Type.valueOf(type.name())).withColor(firstColor)
      .withFade(lastColor);
    this.effects.add(effect.build());
    return effect;
  }
  
  public FireworkEffect.Builder addEffectt(FireworkType type, Color firstColor, Color lastColor)
  {
    return addEffectt(true, true, type, firstColor, lastColor);
  }
  
  public org.bukkit.entity.Firework create(Location location)
  {
    org.bukkit.entity.Firework firework = 
      (org.bukkit.entity.Firework)location.getWorld().spawn(location, org.bukkit.entity.Firework.class);
    FireworkMeta meta = firework.getFireworkMeta();
    meta.addEffects(this.effects);
    meta.setPower(getPower());
    
    firework.setFireworkMeta(meta);
    return firework;
  }
  
  public List<FireworkEffect> getEffects()
  {
    return this.effects;
  }
  
  public int getPower()
  {
    return this.power;
  }
  
  public void setPower(int power)
  {
    this.power = power;
  }
}
