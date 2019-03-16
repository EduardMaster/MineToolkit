package net.eduard.api.lib.old.advanced;

import java.lang.reflect.Method;
import org.bukkit.inventory.ItemStack;


/**
 * Representa a CraftItemStack do NMS
 * @version 1.0
 * @since 0.7
 * @author Eduard
 *
 */
public class CraftItemStack
  extends Packet
{
  private ItemStack itemStack;
  
  public CraftItemStack(ItemStack item)
  {
    setItemStack(item);
  }
  
  public ItemStack getItemStack()
  {
    return this.itemStack;
  }
  
  public MineItemStack getMinecraftItemStack() throws Exception
  {
    Class<?> classCraftItemStack = getCraftBukkitClass("inventory.CraftItemStack");
    Method asNMSCopy = classCraftItemStack.getMethod("asNMSCopy", new Class[] { ItemStack.class });
    asNMSCopy.setAccessible(true);
    MineItemStack minecraftItemStack = new MineItemStack();
    minecraftItemStack.setItemStack(asNMSCopy.invoke(classCraftItemStack, new Object[] { this.itemStack }));
    return minecraftItemStack;
  }
  
  public void makeGlow() throws Exception
  {
    MineItemStack item = getMinecraftItemStack();
    NBTTagCompound tag = new NBTTagCompound();
    if (item.hasTag()) {
      tag.setTagCompound(item.getTag());
    }
    tag.set("ench");
    item.setTag(tag);
    setItemStack(item.toCraftItemStack().getItemStack());
  }
  

  public void setItemStack(ItemStack itemStack)
  {
    this.itemStack = itemStack;
  }
}
