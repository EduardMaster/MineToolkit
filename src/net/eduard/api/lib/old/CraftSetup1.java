package net.eduard.api.lib.old;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import net.eduard.api.lib.game.NormalRecipe;
/**
 * Sistema de criar crafts mais facil do tipo {@link ShapedRecipe}<br>
 * Versão nova {@link Craft} 2.0
 * @version 1.0
 * @since 0.7
 * @author Eduard
 * @deprecated Versão atual {@link NormalRecipe}
 *
 */
@Deprecated
public class CraftSetup1
{
  private ShapedRecipe recipe;
  private ItemStack result;
  private Material[] items = new Material[9];
  private int[] datas = new int[9];
  
  public CraftSetup1(ItemStack result)
  {
    setResult(result);
    setRecipe(new ShapedRecipe(result));
    for (int x = 0; x < this.datas.length; x++) {
      this.datas[x] = 0;
    }
  }
  
  public void set(int slot, Material material)
  {
    set(slot, material, 0);
  }
  
  public void set(int slot, Material material, int data)
  {
    if ((slot < 1) || (slot > 9)) {
      return;
    }
    this.items[(slot - 1)] = material;
    this.datas[(slot - 1)] = data;
  }
  
  public ShapedRecipe getRecipe()
  {
    try
    {
      this.recipe.shape(new String[] {(this.items[0] == null ? " " : "A") + (this.items[1] == null ? " " : "B") + (
        this.items[2] == null ? " " : "C"), (this.items[3] == null ? " " : "D") + (
        this.items[4] == null ? " " : "E") + (this.items[5] == null ? " " : "F"), 
        (this.items[6] == null ? " " : "G") + (this.items[7] == null ? " " : "H") + (
        this.items[8] == null ? " " : "I") });
      
      char shape = 'A';
      for (int x = 0; x < this.items.length; x++) {
        if (this.items[x] == null)
        {
          shape = (char)(shape + '\001');
        }
        else
        {
          this.recipe.setIngredient(shape, this.items[x], this.datas[x]);
          shape = (char)(shape + '\001');
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return this.recipe;
  }
  
  public void setRecipe(ShapedRecipe recipe)
  {
    this.recipe = recipe;
  }
  
  public ItemStack getResult()
  {
    return this.result;
  }
  
  public void setResult(ItemStack result)
  {
    this.result = result;
  }
}
