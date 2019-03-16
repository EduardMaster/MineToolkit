package net.eduard.api.lib.old.advanced;

import java.lang.reflect.Method;
import org.bukkit.inventory.ItemStack;


/**
 * Representa a ItemStack do NMS
 * @version 1.0
 * @since 0.7
 * @author Eduard
 *
 */
public class MineItemStack
  extends Packet
{
  private Object itemStack;
  
  public Object getItemStack()
  {
    return this.itemStack;
  }
  
  public NBTTagCompound getTag() throws Exception
  {
    Class<?> classItemStack = getMinecraftClass("ItemStack");
    Method getTag = classItemStack.getMethod("getTag", new Class[0]);
    getTag.setAccessible(true);
    NBTTagCompound compound = new NBTTagCompound();
    compound.setTagCompound(getTag.invoke(getItemStack(), new Object[0]));
    return compound;
  }
  
  public boolean hasTag() throws Exception
  {
    Class<?> classItemStack = getMinecraftClass("ItemStack");
    Method hasTag = classItemStack.getMethod("hasTag", new Class[0]);
    hasTag.setAccessible(true);
    return ((Boolean)hasTag.invoke(getItemStack(), new Object[0])).booleanValue();
  }
  
  public void setItemStack(Object itemStack)
  {
    this.itemStack = itemStack;
  }
  
  public void setTag(NBTTagCompound compound) throws Exception
  {
    Method setTag = 
      getItemStack().getClass().getMethod("setTag", new Class[] { compound.getTagCompound().getClass() });
    setTag.setAccessible(true);
    setTag.invoke(getItemStack(), new Object[] { compound.getTagCompound() });
  }
  
  public CraftItemStack toCraftItemStack() throws Exception
  {
    Class<?> classCraftItemStack = getCraftBukkitClass("inventory.CraftItemStack");
    Class<?> classItemStack = getMinecraftClass("ItemStack");
    Method asCraftMirror = classCraftItemStack.getMethod("asCraftMirror", new Class[] { classItemStack });
    ItemStack item = (ItemStack)asCraftMirror.invoke(classCraftItemStack, new Object[] { getItemStack() });
    CraftItemStack craftItemStack = new CraftItemStack(item);
    return craftItemStack;
  }
}
