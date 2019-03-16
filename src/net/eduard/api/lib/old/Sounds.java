package net.eduard.api.lib.old;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.eduard.api.lib.game.SoundEffect;



/**
 * Sistema de guardar o Som com seu Volume e Tom definido<br>
 * para em qualquer momento poder criar o Som<br>
 * Versão anterior {@link SoundSetup} 2.0
 * @version 2.0
 * @since 0.7
 * @author Eduard
 * 
 * @deprecated Versão atual {@link SoundEffect}
 */
public class Sounds
{
  private float pitch;
  private Sound sound;
  private float volume;
  
  public Sounds(Sound sound)
  {
    this(sound, 1.0F, 1.0F);
  }
  
  public Sounds(Sound sound, float volume, float pitch)
  {
    setSound(sound);
    setVolume(volume);
    setPitch(pitch);
  }
  
  public void create(Entity entity)
  {
    create(entity.getLocation());
  }
  
  public void create(Location location)
  {
    try {
      location.getWorld().playSound(location, this.sound, this.volume, this.pitch);
    }
    catch (Exception localException) {}
  }
  
  public void create(Player p)
  {
    try {
      p.playSound(p.getLocation(), this.sound, this.volume, this.pitch);
    }
    catch (Exception localException) {}
  }
  

  public float getPitch()
  {
    return this.pitch;
  }
  
  public Sound getSound()
  {
    return this.sound;
  }
  
  public float getVolume()
  {
    return this.volume;
  }
  
  public void setPitch(float pitch)
  {
    this.pitch = pitch;
  }
  
  public void setSound(Sound sound)
  {
    this.sound = sound;
  }
  
  public void setVolume(float volume)
  {
    this.volume = volume;
  }
}
