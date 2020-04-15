package net.eduard.api.lib.advanced_testing;

import java.lang.reflect.Method;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BookUtil
{
	
	public static void openBook1(ItemStack book, Player player) {
		ItemStack itemAntigo = player.getItemInHand();
		player.setItemInHand(book);
		((CraftPlayer)player).getHandle().a(CraftItemStack.asNMSCopy(book),0);
		player.setItemInHand(itemAntigo);
		
	}
	
	
  private static boolean initialised = false;
  private static Method getHandle;
  private static Method openBook;
  /*
  static
  {
    try
    {
      getHandle = ReflectionUtils.getMethod("CraftPlayer", ReflectionUtils.PackageType.CRAFTBUKKIT_ENTITY, "getHandle");
      openBook = ReflectionUtils.getMethod("EntityPlayer", ReflectionUtils.PackageType.MINECRAFT_SERVER, "a", new Class[] { ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("ItemStack"), ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("EnumHand") });
      initialised = true;
    }
    catch (ReflectiveOperationException e)
    {
      e.printStackTrace();
      Bukkit.getServer().getLogger().warning("Cannot force open book!");
      initialised = false;
    }
  }
  
  public static boolean isInitialised()
  {
	  
	  EntityPlayer a;
	  a.a(item)
	  
    return initialised;
  }
  
  public static boolean openBook(ItemStack i, Player p)
  {
    if (!initialised) {
      return false;
    }
    ItemStack held = p.getInventory().getItemInMainHand();
    try
    {
      p.getInventory().setItemInMainHand(i);
      sendPacket(i, p);
    }
    catch (ReflectiveOperationException e)
    {
      e.printStackTrace();
      initialised = false;
    }
    p.getInventory().setItemInMainHand(held);
    return initialised;
  }
  
  private static void sendPacket(ItemStack i, Player p)
    throws ReflectiveOperationException
  {
    Object entityplayer = getHandle.invoke(p, new Object[0]);
    
    Class<?> enumHand = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("EnumHand");
    Object[] enumArray = enumHand.getEnumConstants();
    openBook.invoke(entityplayer, new Object[] { getItemStack(i), enumArray[0] });
  }
  
  public static Object getItemStack(ItemStack item)
  {
    try
    {
      Method asNMSCopy = ReflectionUtils.getMethod(ReflectionUtils.PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftItemStack"), "asNMSCopy", new Class[] { ItemStack.class });
      return asNMSCopy.invoke(ReflectionUtils.PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftItemStack"), new Object[] { item });
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public static void setPages(BookMeta metadata, List<String> pages)
  {
    try
    {
      List<Object> p = (List)ReflectionUtils.getField(ReflectionUtils.PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftMetaBook"), true, "pages").get(metadata);
      for (String text : pages)
      {
        Object page = ReflectionUtils.invokeMethod(ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("IChatBaseComponent$ChatSerializer").newInstance(), "a", new Object[] { text });
        p.add(page);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  */
}
