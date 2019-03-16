package net.eduard.api.lib.old.advanced;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;


/**
 * Representa a PacketPlayOutNamedEntitySpawn do NMS
 * @since 0.7
 * @version 1.0
 * @author Eduard
 *
 */
public class PacketPlayOutNamedEntitySpawn
  extends Packet
{
  private Object packet;
  
  public PacketPlayOutNamedEntitySpawn(MinePlayer player)
    throws Exception
  {
    Class<?> classPacketPlayOutNamedEntitySpawn = 
      getMinecraftClass("PacketPlayOutNamedEntitySpawn");
    Class<?> classEntityHuman = getMinecraftClass("EntityHuman");
    Constructor<?> cs = classPacketPlayOutNamedEntitySpawn.getConstructor(new Class[] { classEntityHuman });
    setPacket(cs.newInstance(new Object[] { player.getEntityPlayer() }));
  }
  
  public Object getPacket()
  {
    return this.packet;
  }
  
  public GameProfile getProfile() throws Exception
  {
    GameProfile profile = new GameProfile();
    Field b = getPacket().getClass().getDeclaredField("b");
    b.setAccessible(true);
    profile.setProfile(b.get(getPacket()));
    return profile;
  }
  
  private void setPacket(Object packet)
  {
    this.packet = packet;
  }
}
