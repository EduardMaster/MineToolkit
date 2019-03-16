package net.eduard.api.lib.old;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
/**
 * Interface com métodos relacionados a tocar Som<br>
 * 
 * Implementações: {@link SoundSetup}
 * @version 1.0
 * @since 0.7
 * @author Eduard
 * @deprecated Descontinuada
 */
public abstract interface SoundManager
{
  public abstract void create(Location paramLocation);
  
  public abstract void create(Player paramPlayer);
  
  public abstract Sound getSound();
  
  public abstract void setSound(Sound paramSound);
  
  public abstract float getVolume();
  
  public abstract void setVolume(float paramFloat);
  
  public abstract float getPitch();
  
  public abstract void setPitch(float paramFloat);
}
