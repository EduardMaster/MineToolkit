package net.eduard.api.lib.old.advanced;




/**
 * Representa a Packet do NMS
 * @version 1.0
 * @since 0.7
 * @author Eduard
 *
 */
public class Packet
{
  protected static final String SERVER_VERSION = org.bukkit.Bukkit.getServer().getClass().getPackage()
    .getName().replace('.', ',').split(",")[3];
  
  protected static final String CRAFTBUKKIT_PACKAGE_NAME = "org.bukkit.craftbukkit." + 
    SERVER_VERSION;
  

  protected static final Package CRAFTBUKKIT_PACKAGE = Package.getPackage(CRAFTBUKKIT_PACKAGE_NAME);
  
  protected static final String MINECRAFT_PACKAGE_NAME = "net.minecraft.server." + SERVER_VERSION;
  
  protected static final Package MINECRAFT_PACKAGE = Package.getPackage(MINECRAFT_PACKAGE_NAME);
  



  protected final Class<?> getCraftBukkitClass(String className)
    throws Exception
  {
    return Class.forName(CRAFTBUKKIT_PACKAGE_NAME + "." + className);
  }
  
  protected final Class<?> getMinecraftClass(String className) throws Exception
  {
    return Class.forName(MINECRAFT_PACKAGE_NAME + "." + className);
  }
}
