package net.eduard.api.lib.old;


import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;






/**
 * Slot do menu {@link Gui}
 * @deprecated Versão atual {@link Slot}<br>
 * Versão anterior {@link GuiItemSetup}
 * @version 2.0
 * @since 0.9
 * @author Eduard
 *
 */
public abstract class Slot
  extends Item
  implements PlayerRunnable
{
  private boolean clearArmour;
  private boolean clearInventory;
  private boolean closeInventory;
  private String command;
  private String message;
  private boolean refresh;
  
  public Slot(ItemStack item)
  {
    super(item);
  }
  
  public String getCommand()
  {
    return this.command;
  }
  
  public String getMessage()
  {
    return this.message;
  }
  
  public boolean isClearArmour()
  {
    return this.clearArmour;
  }
  
  public boolean isClearInventory()
  {
    return this.clearInventory;
  }
  
  public boolean isCloseInventory()
  {
    return this.closeInventory;
  }
  
  public boolean isRefresh()
  {
    return this.refresh;
  }
  
  public void run(Player p)
  {
    if (isCloseInventory()) {
      p.closeInventory();
    }
    if (isClearInventory()) {
      p.getInventory().clear();
    }
    if (isClearArmour()) {
      p.getInventory().setArmorContents(null);
    }
    if (isRefresh()) {
      p.setFoodLevel(20);
      p.setHealth(p.getMaxHealth());
      p.setExhaustion(0.0F);
      p.setSaturation(20.0F);
    }
    
    if (getMessage() != null) {
      p.sendMessage(getMessage());
    }
    if (getCommand() != null) {
      p.performCommand(getCommand());
    }
  }
  

  public void setClearArmour(boolean clearArmour)
  {
    this.clearArmour = clearArmour;
  }
  
  public void setClearInventory(boolean clearInventory)
  {
    this.clearInventory = clearInventory;
  }
  
  public void setCloseInventory(boolean closeInventory)
  {
    this.closeInventory = closeInventory;
  }
  
  public void setCommand(String command)
  {
    this.command = command;
  }
  
  public void setMessage(String message)
  {
    this.message = message;
  }
  
  public void setRefresh(boolean refresh)
  {
    this.refresh = refresh;
  }
}
