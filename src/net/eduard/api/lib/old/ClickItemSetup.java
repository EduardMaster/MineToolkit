package net.eduard.api.lib.old;


import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.click.PlayerClick;
/**
 * Efeito ao clicar no Slot
 * @deprecated Versão atual {@link PlayerClick}<br>
 * @version 1.0
 * @since 0.7
 * @author Eduard
 *
 */
public abstract class ClickItemSetup
  extends ItemSetup
  implements ClickItemManager
{
  public ClickItemSetup(ItemStack item)
  {
    super(item);
  }
}
