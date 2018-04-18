package net.eduard.api.util.reflection;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;


public class Tag
{
  public static Tag get()
  {
    return new Tag();
  }

  public Class<?> getNMSClass(String name) {
    String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    try {
      return Class.forName("net.minecraft.server." + version + "." + name);
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
    }return null;
  }

  public void sendPacket(Player player, Object packet)
  {
    try {
      Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
      Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
      playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") }).invoke(playerConnection, new Object[] { packet });
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

private void setField(Object packet, Field field, Object value) {
    field.setAccessible(true);
    try {
      field.set(packet, value);
    }
    catch (IllegalArgumentException|IllegalAccessException e) {
      e.printStackTrace();
    }
    field.setAccessible(!field.isAccessible());
  }

  private Field getField(Class<?> classs, String fieldname) {
    try {
      return classs.getDeclaredField(fieldname);
    }
    catch (NoSuchFieldException|SecurityException e) {
      e.printStackTrace();
    }return null;
  }

  public void sendNameTag(Player p, String prefix, String suffix, String ordem)
  {
    String name = UUID.randomUUID().toString().substring(0, 15);
    PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
    Class<?> clas = packet.getClass();
    Field team_name = getField(clas, "a");
    Field display_name = getField(clas, "b");
    Field prefix2 = getField(clas, "c");
    Field suffix2 = getField(clas, "d");
    Field members = getField(clas, "g");
    Field param_int = getField(clas, "h");
    Field pack_option = getField(clas, "i");
    setField(packet, team_name, ordem + name);
    setField(packet, display_name, p.getName());
    setField(packet, prefix2, prefix);
    setField(packet, suffix2, suffix);
    setField(packet, members, Arrays.asList(new String[] { p.getName() }));
    setField(packet, param_int, Integer.valueOf(0));
    setField(packet, pack_option, Integer.valueOf(1));
    for (Player ps : Bukkit.getOnlinePlayers())
      sendPacket(ps, packet);
  }
}