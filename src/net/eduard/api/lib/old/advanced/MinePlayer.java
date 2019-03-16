package net.eduard.api.lib.old.advanced;

import java.lang.reflect.Field;

/**
 * Representa a EntityPlayer do NMS
 * @version 1.0
 * @since 0.7
 * @author Eduard
 *
 */
public class MinePlayer
  extends Packet
{
  private Object entityPlayer;
  
  public Object getEntityPlayer()
  {
    return this.entityPlayer;
  }
  
  public PlayerConnection getPlayerConnection() throws Exception
  {
    Field playerConnection = getEntityPlayer().getClass().getField("playerConnection");
    playerConnection.setAccessible(true);
    return new PlayerConnection(playerConnection.get(getEntityPlayer()));
  }
  


  public GameProfile getProfile()
    throws Exception
  {
    GameProfile profile = new GameProfile();
    Field i = getEntityPlayer().getClass().getSuperclass().getDeclaredField("i");
    i.setAccessible(true);
    profile.setProfile(i.get(getEntityPlayer()));
    return profile;
  }
  
  public void setEntityPlayer(Object entityPlayer)
  {
    this.entityPlayer = entityPlayer;
  }
}
