package net.eduard.api.lib.old;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;

import net.eduard.api.lib.menu.Menu;




/**
 * Sistema de criar menu
 * @since 0.7
 * @version 1.0
 * @author Eduard
 * @deprecated Versão atual {@link Menu}<br>
 * Versão nova {@link Gui} 2.0
 *
 */

public class GuiInventorySetup
{
  private Inventory inventory;
  private List<GuiItemSetup> items = new ArrayList<>();
  
  private GuiType type;
  
  private ItemSetup itemKey;
  
  private SoundSetup openSound;
  
  private GuiOpenType openType;
  
  public GuiInventorySetup(GuiType type, String name)
  {
    setInventory(Bukkit.createInventory(null, type.getSize(), name));
    setType(type);
    setItemKey(null);
    setOpenSound(new SoundSetup(Sound.CLICK, 1.0F, 1.0F));
    setItems(new ArrayList<>());
    setOpenType(GuiOpenType.BOTH);
  }
  
  public boolean addNewItem(GuiItemSetup item) {
    if (item == null) return false;
    List<GuiItemSetup> list = new ArrayList<>();
    for (GuiItemSetup key : this.items) {
      if (key.getSlot() == item.getSlot()) {
        list.add(key);
      }
    }
    for (GuiItemSetup key : list) {
      this.items.remove(key);
    }
    if (item.getSlot() <= getType().getSize()) {
      this.items.add(item);
      getInventory().setItem(item.getSlot(), item);
      return true;
    }
    return false;
  }
  
  public List<GuiItemSetup> getItems()
  {
    return this.items;
  }
  
  private void setItems(List<GuiItemSetup> items)
  {
    this.items = items;
  }
  
  public GuiType getType()
  {
    return this.type;
  }
  
  private void setType(GuiType type)
  {
    this.type = type;
  }
  
  public Inventory getInventory()
  {
    return this.inventory;
  }
  
  private void setInventory(Inventory inventory)
  {
    this.inventory = inventory;
  }
  
  public ItemSetup getItemKey()
  {
    return this.itemKey;
  }
  
  public void setItemKey(ItemSetup itemKey)
  {
    this.itemKey = itemKey;
  }
  
  public SoundSetup getOpenSound()
  {
    return this.openSound;
  }
  
  public void setOpenSound(SoundSetup openSound)
  {
    this.openSound = openSound;
  }
  
  public GuiOpenType getOpenType()
  {
    return this.openType;
  }
  
  public void setOpenType(GuiOpenType openType)
  {
    this.openType = openType;
  }
}
