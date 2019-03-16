package net.eduard.api.lib.old;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import net.eduard.api.lib.click.PlayerClickEffect;


/**
 * Representa o clique que o jogador faz em um Item
 * @deprecated Versão atual {@link PlayerClickEffect}
 * @version 1.0
 * @since 0.7
 * @author Eduard
 *
 */
public class ClickSetup
  implements Listener, ActiveManager
{
  private List<ClickItemSetup> clickItems = new ArrayList<>();
  
  private boolean active;
  

  public ClickSetup()
  {
    Bukkit.getPluginManager().registerEvents(this, getInstance());
  }
  
  @EventHandler
  private void ClickEvent(PlayerInteractEvent event) {
    if (this.active) {
      if (event.getItem() == null) return;
      for (ClickItemSetup item : this.clickItems) {
        if (item.isSimilar(event.getItem())) {
          item.clickEvent(event);
          return;
        }
      }
    }
  }
  
  public boolean isActive()
  {
    return this.active;
  }
  
  public void setActive(boolean active)
  {
    this.active = active;
  }
  

  public List<ClickItemSetup> getClickItems()
  {
    return this.clickItems;
  }
}
