package net.eduard.api.lib.old;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.eduard.api.lib.game.SoundEffect;


/**
 * Sistema de guardar o Som com seu Volume e Tom definido<br>
 * para em qualquer momento poder criar o Som<br>
 * Versão nova {@link Sounds} 2.0
 * @version 1.0
 * @since 0.7
 * @author Eduard
 * @deprecated Versão atual {@link SoundEffect}
 */
public class SoundSetup
  implements SoundManager
{
  private Sound sound;
  private float volume;
  private float pitch;
  
  public SoundSetup(Sound sound, float volume, float pitch)
  {
    setSound(sound);
    setVolume(volume);
    setPitch(pitch);
  }
  
  public void create(Location location)
  {
    location.getWorld().playSound(location, this.sound, this.volume, this.pitch);
  }
  
  public void create(Player p)
  {
    p.playSound(p.getLocation(), this.sound, this.volume, this.pitch);
  }
  
  public Sound getSound()
  {
    return this.sound;
  }
  
  public void setSound(Sound sound)
  {
    this.sound = sound;
  }
  
  public float getVolume()
  {
    return this.volume;
  }
  
  public void setVolume(float volume)
  {
    this.volume = volume;
  }
  
  public float getPitch()
  {
    return this.pitch;
  }
  
  public void setPitch(float pitch)
  {
    this.pitch = pitch;
  }
}
