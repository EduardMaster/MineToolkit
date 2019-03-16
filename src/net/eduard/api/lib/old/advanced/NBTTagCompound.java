package net.eduard.api.lib.old.advanced;

import java.lang.reflect.Method;
/**
 * Representa a NBTTagCompound do NMS
 * @since 0.7
 * @version 1.0
 * @author Eduard
 *
 */
public class NBTTagCompound
  extends Packet
{
  private Object tagCompound;
  
  public NBTTagCompound()
    throws Exception
  {
    Class<?> classNBTTagCompound = getMinecraftClass("NBTTagCompound");
    setTagCompound(classNBTTagCompound.newInstance());
  }
  
  public Object getTagCompound()
  {
    return this.tagCompound;
  }
  
  public void set(String value) throws Exception
  {
    Class<?> classNBTBase = getMinecraftClass("NBTBase");
    Method set = getTagCompound().getClass().getMethod("set", new Class[] { String.class, classNBTBase });
    set.setAccessible(true);
    NBTTagList list = new NBTTagList();
    set.invoke(getTagCompound(), new Object[] { value, list });
  }
  

  public void setTagCompound(Object tagCompound)
  {
    this.tagCompound = tagCompound;
  }
}
