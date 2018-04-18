package net.eduard.api.util.reflection;

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

public class Holographic
{
  private List<Object> destroyCache;
  private List<Object> spawnCache;
  private List<UUID> players;
  private List<String> lines;
  private Location loc;
  private static String path = Bukkit.getServer().getClass().getPackage().getName();
  private static String version = path.substring(path.lastIndexOf(".") + 1, path.length());
  private static Class<?> armorStand;
  private static Class<?> worldClass;
  private static Class<?> nmsEntity;
  private static Class<?> craftWorld;
  private static Class<?> packetClass;
  private static Class<?> entityLivingClass;
  private static Constructor<?> armorStandConstructor;
  private static Class<?> destroyPacketClass;
  private static Constructor<?> destroyPacketConstructor;
  private static Class<?> nmsPacket;
  
  static
  {
    try
    {
      armorStand = Class.forName("net.minecraft.server." + version + ".EntityArmorStand");
      worldClass = Class.forName("net.minecraft.server." + version + ".World");
      nmsEntity = Class.forName("net.minecraft.server." + version + ".Entity");
      craftWorld = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
      packetClass = Class.forName("net.minecraft.server." + version + ".PacketPlayOutSpawnEntityLiving");
      entityLivingClass = Class.forName("net.minecraft.server." + version + ".EntityLiving");
      armorStandConstructor = armorStand.getConstructor(new Class[] { worldClass });
      
      destroyPacketClass = Class.forName("net.minecraft.server." + version + ".PacketPlayOutEntityDestroy");
      destroyPacketConstructor = destroyPacketClass.getConstructor(new Class[] { int[].class });
      
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
    this.lines = lines;
    this.loc = loc;
    this.players = new ArrayList<UUID>();
    this.spawnCache = new ArrayList<Object>();
    this.destroyCache = new ArrayList<Object>();
    
    Location displayLoc = loc.clone().add(0.0D, 0.23D * lines.size() - 1.97D, 0.0D);
    for (int i = 0; i < lines.size(); i++)
    {
      Object packet = getPacket(this.loc.getWorld(), displayLoc.getX(), displayLoc.getY(), displayLoc.getZ(), this.lines.get(i));
      this.spawnCache.add(packet);
      try
      {
        Field field = packetClass.getDeclaredField("a");
        field.setAccessible(true);
        this.destroyCache.add(getDestroyPacket(new int[] { ((Integer)field.get(packet)).intValue() }));
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
      displayLoc.add(0.0D, -0.23D, 0.0D);
    }
  }
  
  public boolean displayHolo(Player p)
  {
    for (int i = 0; i < this.spawnCache.size(); i++) {
      sendPacket(p, this.spawnCache.get(i));
    }
    this.players.add(p.getUniqueId());
    return true;
  }
  
  public boolean destroyHolo(Player p)
  {
    if (this.players.contains(p.getUniqueId()))
    {
      for (int i = 0; i < this.destroyCache.size(); i++) {
        sendPacket(p, this.destroyCache.get(i));
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
      Method getHandleMethod = craftWorldObj.getClass().getMethod("getHandle", new Class[0]);
      Object entityObject = armorStandConstructor.newInstance(new Object[] { getHandleMethod.invoke(craftWorldObj, new Object[0]) });
      Method setCustomName = entityObject.getClass().getMethod("setCustomName", new Class[] { String.class });
      setCustomName.invoke(entityObject, new Object[] { text });
      Method setCustomNameVisible = nmsEntity.getMethod("setCustomNameVisible", new Class[] { Boolean.TYPE });
      setCustomNameVisible.invoke(entityObject, new Object[] { Boolean.valueOf(true) });
      Method setGravity = null;
      if (Bukkit.getVersion().contains("1.10"))
      {
        setGravity = entityObject.getClass().getMethod("setNoGravity", new Class[] { Boolean.TYPE });
        setGravity.invoke(entityObject, new Object[] { Boolean.valueOf(false) });
      }
      else
      {
        setGravity = entityObject.getClass().getMethod("setGravity", new Class[] { Boolean.TYPE });
        setGravity.invoke(entityObject, new Object[] { Boolean.valueOf(false) });
      }
      Method setLocation = entityObject.getClass().getMethod("setLocation", new Class[] { Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE });
      setLocation.invoke(entityObject, new Object[] { Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), Float.valueOf(0.0F), Float.valueOf(0.0F) });
      Method setInvisible = entityObject.getClass().getMethod("setInvisible", new Class[] { Boolean.TYPE });
      setInvisible.invoke(entityObject, new Object[] { Boolean.valueOf(true) });
      Constructor<?> cw = packetClass.getConstructor(new Class[] { entityLivingClass });
      return cw.newInstance(new Object[] { entityObject });
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
      Method getHandle = p.getClass().getMethod("getHandle", new Class[0]);
      Object entityPlayer = getHandle.invoke(p, new Object[0]);
      Object pConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
      Method sendMethod = pConnection.getClass().getMethod("sendPacket", new Class[] { nmsPacket });
      sendMethod.invoke(pConnection, new Object[] { packet });
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}