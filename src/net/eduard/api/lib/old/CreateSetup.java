package net.eduard.api.lib.old;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;



/**
 * API de criar items mais facil
 * @deprecated Versão atual itema
 * @since 0.7
 * @version 1.0
 * @author Eduard
 *
 */
@Deprecated
public abstract interface CreateSetup
{
  public default ItemSetup createItem(Material type, int amount, short data, String name, String... lore)
  {
    return new ItemSetup(type, amount, data, name, lore);
  }
  
  public default ItemSetup createItem(Material type, int amount, short data, String name)
  {
    return new ItemSetup(type, amount, data, name);
  }
  
  public default ItemSetup createItem(Material type, int amount, short data)
  {
    return new ItemSetup(type, amount, data);
  }
  
  public default ItemSetup createItem(Material type, int amount)
  {
    return new ItemSetup(type, amount);
  }
  
  public default ItemSetup createItem(Material type)
  {
    return new ItemSetup(type);
  }
  
  public default ItemSetup createItem(Material type, String name)
  {
    return new ItemSetup(type, 1, (short)0, name);
  }
  
  public default ItemSetup createItem(Material type, String name, String... lore)
  {
    return new ItemSetup(type, 1, (short)0, name, lore);
  }
  
  public default ItemSetup createItem(Material type, int amount, String name)
  {
    return new ItemSetup(type, amount, (short)0, name);
  }
  
  public default ItemSetup createItem(Material type, short data, String name)
  {
    return new ItemSetup(type, 1, data, name);
  }
  
  public default ItemSetup createItem(ItemStack item)
  {
    return new ItemSetup(item);
  }
  
  public default SoundSetup createSound(Sound sound, float volume, float pitch)
  {
    return new SoundSetup(sound, volume, pitch);
  }
  
  public default FireworkSetup createFirework(Location loc)
  {
    return new FireworkSetup(loc);
  }
}
