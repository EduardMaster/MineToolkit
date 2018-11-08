package net.eduard.api.lib.advanced;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.entity.Player;

public class PacketPlayOut
{
  Object packet;
  private static Method getHandle;
  private static Method sendPacket;
  private static Field playerConnection;
  private static String version = "";
  private static Class<?> packetType;

  static
  {
    try
    {
      version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

      packetType = Class.forName(getPacketTeamClasspath());

      Class<?> typeCraftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");

      Class<?> typeNMSPlayer = Class.forName("net.minecraft.server." + version + ".EntityPlayer");

      Class<?> typePlayerConnection = Class.forName("net.minecraft.server." + version + ".PlayerConnection");

      getHandle = typeCraftPlayer.getMethod("getHandle", new Class[0]);
      playerConnection = typeNMSPlayer.getField("playerConnection");
      sendPacket = typePlayerConnection.getMethod("sendPacket", new Class[] { Class.forName("net.minecraft.server." + version + ".Packet") });
    } catch (Exception e) {
      System.out.println("Failed to setup reflection for Packet209Mod!");
      e.printStackTrace();
    }
  }

public PacketPlayOut(String name, String prefix, String suffix, Collection<?> players, int paramInt)
    throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException
  {
    this.packet = packetType.newInstance();
    setField("a", name);
    setField("f", Integer.valueOf(paramInt));

    if ((paramInt == 0) || (paramInt == 2)) {
      setField("b", name);
      setField("c", prefix);
      setField("d", suffix);
      setField("g", Integer.valueOf(1));
    }
    if (paramInt == 0)
      addAll(players);
  }

public PacketPlayOut(String name, Collection<?> players, int paramInt)
    throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException
  {
    this.packet = packetType.newInstance();

    if ((paramInt != 3) && (paramInt != 4)) {
      throw new IllegalArgumentException("Method must be join or leave for player constructor");
    }

    if ((players == null) || (players.isEmpty())) {
      players = new ArrayList<>();
    }

    setField("a", name);
    setField("f", Integer.valueOf(paramInt));
    addAll(players);
  }

  public void sendToPlayer(Player bukkitPlayer) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException
  {
    Object player = getHandle.invoke(bukkitPlayer, new Object[0]);

    Object connection = playerConnection.get(player);

    sendPacket.invoke(connection, new Object[] { this.packet });
  }

  private void setField(String field, Object value) throws NoSuchFieldException, IllegalAccessException {
    Field f = this.packet.getClass().getDeclaredField(field);
    f.setAccessible(true);
    f.set(this.packet, value);
  }

  @SuppressWarnings("unchecked")
private <E> void addAll(Collection<E> col)
    throws NoSuchFieldException, IllegalAccessException
  {
    Field f = this.packet.getClass().getDeclaredField("e");
    f.setAccessible(true);
    ((Collection<E>)f.get(this.packet)).addAll(col);
  }

  private static String getPacketTeamClasspath() {
    if ((Integer.valueOf(version.split("_")[1]).intValue() < 7) && (Integer.valueOf(version.toLowerCase().split("_")[0].replace("v", "")).intValue() == 1))
    {
      return "net.minecraft.server." + version + ".Packet209SetScoreboardTeam";
    }

    return "net.minecraft.server." + version + ".PacketPlayOutScoreboardTeam";
  }
}