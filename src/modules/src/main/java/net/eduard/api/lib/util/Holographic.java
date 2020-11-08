package net.eduard.api.lib.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * @author Internet
 */
@SuppressWarnings("unused")
public class Holographic
{
  private List<Object> destroyCache;
  private List<Object> spawnCache;
  private List<UUID> players;
  private static final String path =
          Bukkit.getServer().getClass().getPackage().getName();
  private static final String version = path.substring(path.lastIndexOf(".")
          + 1, path.length());
  private static Class<?> nmsEntity;
  private static Class<?> craftWorld;
  private static Class<?> packetClass;
  private static Class<?> entityLivingClass;
  private static Constructor<?> armorStandConstructor;
  private static Constructor<?> destroyPacketConstructor;
  private static Class<?> nmsPacket;
  
  static
  {
    try
    {
      Class<?> armorStand = Class.forName("net.minecraft.server." + version + ".EntityArmorStand");
      Class<?> worldClass = Class.forName("net.minecraft.server." + version + ".World");
      nmsEntity = Class.forName("net.minecraft.server." + version + ".Entity");
      craftWorld = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
      packetClass = Class.forName("net.minecraft.server." + version + ".PacketPlayOutSpawnEntityLiving");
      entityLivingClass = Class.forName("net.minecraft.server." + version + ".EntityLiving");
      armorStandConstructor = armorStand.getConstructor(worldClass);

      Class<?> destroyPacketClass = Class.forName("net.minecraft.server." + version + ".PacketPlayOutEntityDestroy");
      destroyPacketConstructor = destroyPacketClass.getConstructor(int[].class);
      
      nmsPacket = Class.forName("net.minecraft.server." + version + ".Packet");
    }
    catch (ClassNotFoundException|NoSuchMethodException|SecurityException ex)
    {
      System.err.println("Error - Classes not initialized!");
      System.err.println("Hologramm is not supported in 1.7!");
    }
  }
  
  public void createHolo(Location loc, List<String> lines)
  {
    this.players = new ArrayList<>();
    this.spawnCache = new ArrayList<>();
    this.destroyCache = new ArrayList<>();
    
    Location displayLoc = loc.clone().add(0.0D,
            0.23D * lines.size() - 1.97D, 0.0D);
    for (String line : lines) {
      Object packet = getPacket(loc.getWorld(), displayLoc.getX(),
              displayLoc.getY(), displayLoc.getZ(), line);
      this.spawnCache.add(packet);
      try {
        Field field = packetClass.getDeclaredField("a");
        field.setAccessible(true);
        this.destroyCache.add(getDestroyPacket((Integer) field.get(packet)));
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      displayLoc.add(0.0D, -0.23D, 0.0D);
    }
  }
  
  public boolean displayHolo(Player p)
  {
    for (Object o : this.spawnCache) {
      sendPacket(p, o);
    }
    this.players.add(p.getUniqueId());
    return true;
  }
  
  public boolean destroyHolo(Player p)
  {
    if (this.players.contains(p.getUniqueId()))
    {
      for (Object o : this.destroyCache) {
        sendPacket(p, o);
      }
      this.players.remove(p.getUniqueId());
      return true;
    }
    return false;
  }
  
  private Object getPacket(World w, double x, double y, double z, String text)
  {
    try
    {
      Object craftWorldObj = craftWorld.cast(w);
      Method getHandleMethod = craftWorldObj.getClass().getMethod("getHandle");
      Object entityObject = armorStandConstructor.newInstance(getHandleMethod
              .invoke(craftWorldObj));
      Method setCustomName = entityObject.getClass().getMethod("setCustomName",
              String.class);
      setCustomName.invoke(entityObject, text);
      Method setCustomNameVisible = nmsEntity.getMethod("setCustomNameVisible",
              Boolean.TYPE);
      setCustomNameVisible.invoke(entityObject, Boolean.TRUE);
      Method setGravity;
      if (Bukkit.getVersion().contains("1.10"))
      {
        setGravity = entityObject.getClass().getMethod("setNoGravity", Boolean.TYPE);
      }
      else
      {
        setGravity = entityObject.getClass().getMethod("setGravity",
                Boolean.TYPE);
      }
      setGravity.invoke(entityObject, Boolean.FALSE);
      Method setLocation = entityObject.getClass().getMethod("setLocation",
              Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE,
              Float.TYPE);
      setLocation.invoke(entityObject, x, y, z, 0.0F, 0.0F);
      Method setInvisible = entityObject.getClass().getMethod("setInvisible",
              Boolean.TYPE);
      setInvisible.invoke(entityObject, Boolean.TRUE);
      Constructor<?> cw = packetClass.getConstructor(entityLivingClass);
      return cw.newInstance(entityObject);
    }
    catch (NoSuchMethodException|SecurityException|InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  private Object getDestroyPacket(int... id)
  {
    try
    {
      return destroyPacketConstructor.newInstance(new Object[] { id });
    }
    catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  private void sendPacket(Player p, Object packet)
  {
    try
    {
      Method getHandle = p.getClass().getMethod("getHandle");
      Object entityPlayer = getHandle.invoke(p);
      Object pConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
      Method sendMethod = pConnection.getClass().getMethod("sendPacket", nmsPacket);
      sendMethod.invoke(pConnection, packet);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}