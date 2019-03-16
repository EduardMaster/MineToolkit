package net.eduard.api.lib.old.advanced;

/**
 * Representa a NBTTagList do NMS
 * @since 0.7
 * @version 1.0
 * @author Eduard
 *
 */
public class NBTTagList
  extends Packet
{
  private Object list;
  
  public NBTTagList()
    throws Exception
  {
    Class<?> classNBTTagList = getMinecraftClass("NBTTagList");
    setList(classNBTTagList.newInstance());
  }
  
  public Object getList()
  {
    return this.list;
  }
  
  public void setList(Object list)
  {
    this.list = list;
  }
}
