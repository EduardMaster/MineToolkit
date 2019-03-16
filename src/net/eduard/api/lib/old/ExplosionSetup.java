package net.eduard.api.lib.old;

import org.bukkit.Location;

import net.eduard.api.lib.game.Explosion;
/**
 * Representa uma Explosão
 * @version 1.0
 * @since 0.7
 * @deprecated Versão Atual {@link Explosion}<br>
 * Versão nova {@link net.eduard.api.lib.old.Explosion}
 * @author Eduard
 *
 */
public class ExplosionSetup
{
  private float force;
  private boolean fire;
  private boolean breakBlock;
  
  public ExplosionSetup() {}
  
  public static void createNewExplosion(ExplosionSetup explosion) {}
  
  public ExplosionSetup(float force, boolean fire, boolean breakBlock)
  {
    this.force = force;
    this.fire = fire;
    this.breakBlock = breakBlock;
  }
  
  public final void create(Location location)
  {
    location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), 
      getForce(), isFire(), isBreakBlock());
  }
  
  public final boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ExplosionSetup)) {
      return false;
    }
    ExplosionSetup other = (ExplosionSetup)obj;
    if (this.breakBlock != other.breakBlock) {
      return false;
    }
    if (this.fire != other.fire) {
      return false;
    }
    if (Float.floatToIntBits(this.force) != Float.floatToIntBits(other.force)) {
      return false;
    }
    return true;
  }
  
  public final float getForce()
  {
    return this.force;
  }
  

  
  public final boolean isBreakBlock()
  {
    return this.breakBlock;
  }
  
  public final boolean isFire()
  {
    return this.fire;
  }
  
  public final void setBreakBlock(boolean breakBlock)
  {
    this.breakBlock = breakBlock;
  }
  
  public final void setFire(boolean fire)
  {
    this.fire = fire;
  }
  
  public final void setForce(float force)
  {
    this.force = force;
  }
  
  public final String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("ExplosionSetup [force=");
    builder.append(this.force);
    builder.append(", fire=");
    builder.append(this.fire);
    builder.append(", breakBlock=");
    builder.append(this.breakBlock);
    builder.append("]");
    return builder.toString();
  }
}
