package net.eduard.api.lib.old.advanced;

import java.lang.reflect.Method;
/**
 * Representa a PlayerConnection do NMS
 * @since 0.7
 * @version 1.0
 * @author Eduard
 *
 */
public class PlayerConnection
  extends Packet
{
  private Object connection;
  
  public PlayerConnection(Object entityPlayerConnection)
    throws Exception
  {
    setConnection(entityPlayerConnection);
  }
  

  public Object getConnection()
  {
    return this.connection;
  }
  
  public void sendPacket(Object packet) throws Exception
  {
    Class<?> classPacket = getMinecraftClass("Packet");
    Method sendPacket = getConnection().getClass().getMethod("sendPacket", new Class[] { classPacket });
    sendPacket.setAccessible(true);
    sendPacket.invoke(getConnection(), new Object[] { packet });
  }
  

  private void setConnection(Object connection)
  {
    this.connection = connection;
  }
}
