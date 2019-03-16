package net.eduard.api.lib.old.advanced;


import org.bukkit.entity.Player;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.old.Principal;
/**
 * API de alterar nome do jogador eternamente ou temporario
 * @since 0.9
 * @version 1.0
 * @author Eduard
 * @deprecated Métodos adicionados na {@link Mine}
 *
 */
public class Fake
{
  public static void changeName(Player p, String name)
  {
    try
    {
      MinePlayer mp = new CraftPlayer(p).getEntityPlayer();
      mp.getProfile().setName(name);
      PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(mp);
      for (Player player : Principal.getOnlinePlayers()) {
        if (!player.equals(p)) {
          MinePlayer mp2 = new CraftPlayer(player).getEntityPlayer();
          mp2.getPlayerConnection().sendPacket(packet.getPacket());
        }
      }
    }
    catch (Exception localException) {}
  }
  
  public static void changeNameTemporaly(Player p, String name) {
    try {
      MinePlayer mp = new CraftPlayer(p).getEntityPlayer();
      String firstName = p.getName();
      mp.getProfile().setName(name);
      PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(mp);
      for (Player player : Principal.getOnlinePlayers())
        if (!player.equals(p)) {
          MinePlayer mp2 = new CraftPlayer(player).getEntityPlayer();
          mp2.getPlayerConnection().sendPacket(packet.getPacket());
        }
      mp.getProfile().setName(firstName);
    }
    catch (Exception localException) {}
  }
}
