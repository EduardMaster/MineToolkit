package net.eduard.api.lib.old;

import org.bukkit.Location;



/**
 * Representa uma Explosão
 * @version 2.0
 * @since 0.9
 * @deprecated Versão Atual {@link net.eduard.api.lib.game.Explosion}<br>
 * Versão anterior {@link ExplosionSetup}
 * @author Eduard
 *
 */
public class Explosion
{
  private boolean breakBlock;
  private boolean fire;
  private float force;
  
  public Explosion(float force, boolean fire, boolean breakBlock)
  {
    this.force = force;
    this.fire = fire;
    this.breakBlock = breakBlock;
  }
  
  public void create(Location location)
  {
    location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), 
      getForce(), isFire(), isBreakBlock());
  }
  
  public float getForce()
  {
    return this.force;
  }
  
  public boolean isBreakBlock()
  {
    return this.breakBlock;
  }
  
  public boolean isFire()
  {
    return this.fire;
  }
  
  public void setBreakBlock(boolean breakBlock)
  {
    this.breakBlock = breakBlock;
  }
  
  public void setFire(boolean fire)
  {
    this.fire = fire;
  }
  
  public void setForce(float force)
  {
    this.force = force;
  }
}
