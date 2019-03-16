package net.eduard.api.lib.old;

import org.bukkit.inventory.ItemStack;
/**
 * Interface com métodos de mexer com {@link ItemStack}
 * @version 1.0
 * @since 0.7
 * @author Eduard
 * @deprecated Descontinuado
 *
 */
public abstract interface ItemManager
{
  public abstract <E extends ItemStack> void copy(E paramE);
  
  public abstract String getName();
  
  public abstract boolean hasName();
  
  public abstract void setLore(String... paramVarArgs);
  
  public abstract void setName(String paramString);
}
