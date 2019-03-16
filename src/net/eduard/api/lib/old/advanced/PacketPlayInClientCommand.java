package net.eduard.api.lib.old.advanced;

import java.lang.reflect.Constructor;
/**
 * Representa a PacketPlayInClientCommand do NMS
 * @since 0.7
 * @version 1.0
 * @author Eduard
 *
 */
public class PacketPlayInClientCommand
  extends Packet
{
  private Object packet;
  
  public PacketPlayInClientCommand(EnumClientCommand type)
    throws Exception
  {
    Class<?> classPacketPlayInClientCommand = getMinecraftClass("PacketPlayInClientCommand");
    Class<?> classEnumClientCommand = getMinecraftClass("EnumClientCommand");
    Constructor<?> cs = 
      classPacketPlayInClientCommand.getDeclaredConstructor(new Class[] { classEnumClientCommand });
    Object[] arrayOfObject; int j = (arrayOfObject = classEnumClientCommand.getEnumConstants()).length; for (int i = 0; i < j; i++) { Object con = arrayOfObject[i];
      if (con.toString().equals(type.toString())) {
        setPacket(cs.newInstance(new Object[] { con }));
      }
    }
  }
  

  public Object getPacket()
  {
    return this.packet;
  }
  
  private void setPacket(Object packet)
  {
    this.packet = packet;
  }
}
