package net.eduard.api.lib.old;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * Extensão da classe {@link ItemStack} com mais métodos<br>
 * Versão anterior {@link ItemSetup} 1.0
 * 
 * @deprecated Versão atual {@link net.eduard.api.lib.game.Item}
 * @version 2.0
 * @since 0.7
 * @author Eduard
 *
 */
public class Item
  extends ItemStack
{
  public Item(ItemStack item)
  {
    copy(item);
  }
  
  public Item(Material type)
  {
    this(type, 1);
  }
  
  public Item(Material type, int amount)
  {
    this(type, amount, 0);
  }
  
  public Item(Material type, int amount, int data)
  {
    this(type, amount, data, null);
  }
  
  public Item(Material type, int amount, int data, String name)
  {
    this(type, amount, data, name, "");
  }
  
  public Item(Material type, int amount, int data, String name, String... lore)
  {
    this(new ItemStack(type, amount, Principal.toShort(Integer.valueOf(data))));
    
    ItemMeta meta = getItemMeta();
    if (name != null) {
      meta.setDisplayName(name);
    }
    if (lore != null) {
      meta.setLore(Arrays.asList(lore));
    }
    setItemMeta(meta);
  }
  
  public Item(Material type, int data, String name)
  {
    this(type, 1, data, name);
  }
  
  public Item(Material type, String name)
  {
    this(type, 1, 0, name);
  }
  
  public Item(Material type, String name, String... lore)
  {
    this(type, 1, 0, name, lore);
  }
  
  public Item addEnchant(Enchantment enchant, int level)
  {
    ItemMeta meta = getItemMeta();
    meta.addEnchant(enchant, level, true);
    setItemMeta(meta);
    return this;
  }
  
  public void copy(ItemStack item)
  {
    setType(item.getType());
    setDurability(item.getDurability());
    setAmount(item.getAmount());
    setItemMeta(item.getItemMeta());
  }
  
  public List<String> getLore()
  {
    if (hasLore()) return getItemMeta().getLore();
    return null;
  }
  
  public String getName()
  {
    if (hasName()) return getItemMeta().getDisplayName();
    return null;
  }
  
  public boolean hasLore()
  {
    if (hasItemMeta()) return getItemMeta().hasLore();
    return false;
  }
  
  public boolean hasName()
  {
    if (hasItemMeta()) return getItemMeta().hasDisplayName();
    return false;
  }
  
  public Item setLore(List<String> lore)
  {
    ItemMeta meta = getItemMeta();
    meta.setLore(lore);
    setItemMeta(meta);
    return this;
  }
  
  public Item setLore(String... lore)
  {
    ItemMeta meta = getItemMeta();
    meta.setLore(Arrays.asList(lore));
    setItemMeta(meta);
    return this;
  }
  
  public Item setName(Object name)
  {
    ItemMeta meta = getItemMeta();
    meta.setDisplayName(name.toString());
    setItemMeta(meta);
    return this;
  }
}
