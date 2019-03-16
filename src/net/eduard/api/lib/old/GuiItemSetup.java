package net.eduard.api.lib.old;

import net.eduard.api.lib.menu.Slot;

/**
 * Slot do menu {@link GuiInventorySetup}
 * @deprecated Versão atual {@link Slot}<br>
 * Versão nova {@link net.eduard.api.lib.old.Slot}
 * @version 1.0
 * @since 0.7
 * @author Eduard
 *
 */
public abstract class GuiItemSetup
  extends ItemSetup implements PlayerManager
{
  private int slot;
  
  public GuiItemSetup(int slot, ItemSetup item)
  {
    super(item);
    setSlot(slot);
  }
  
  public int getSlot()
  {
    return this.slot;
  }
  
  public void setSlot(int slot)
  {
    this.slot = slot;
  }
}
