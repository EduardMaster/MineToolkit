package net.eduard.api.lib.old.advanced;

import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import net.eduard.api.lib.old.EventSetup;
import net.eduard.api.lib.old.TimeSetup;



/**
 * Representa a CraftPlayer do NMS<br>
 * Versão anterior usava {@link TimeSetup} para o método makeRespawn();
 * @version 2.0
 * @since 0.7
 * @author Eduard
 *
 */
@Deprecated
public class CraftPlayer
  extends Packet
{
  private Player player;
  
  public CraftPlayer(Player p)
  {
    setPlayer(p);
  }
  
  public <classCraftPlayer extends Player> Object getCraftPlayer() throws Exception
  {
    return this.player;
  }
  
  public MinePlayer getEntityPlayer()
    throws Exception
  {
    Object craftPlayer = getCraftPlayer();
    Method getHandle = craftPlayer.getClass().getMethod("getHandle", new Class[0]);
    getHandle.setAccessible(true);
    MinePlayer entityPlayer = new MinePlayer();
    entityPlayer.setEntityPlayer(getHandle.invoke(craftPlayer, new Object[0]));
    return entityPlayer;
  }
  
  public Player getPlayer()
  {
    return this.player;
  }
  
  public void makeRespawn()
  {
    new EventSetup(1, 1)
    {
      public void run()
      {
        try {
          if (!CraftPlayer.this.getPlayer().isDead()) return;
          PacketPlayInClientCommand packet = 
            new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
          Object pc = CraftPlayer.this.getEntityPlayer().getPlayerConnection().getConnection();
          Method a = pc.getClass().getMethod("a", new Class[] { packet.getPacket().getClass() });
          a.setAccessible(true);
          a.invoke(pc, new Object[] { packet.getPacket() });
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    
  }
  

  private void setPlayer(Player player)
  {
    this.player = player;
  }
}
