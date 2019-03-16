package net.eduard.api.lib.old;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import net.eduard.api.lib.game.SimpleRecipe;
/**
 * Sistema de criar crafts mais facil do tipo {@link ShapelessRecipe}<br>
 * Versão nova {@link Crafts} 2.0
 * @version 1.0
 * @since 0.7
 * @author Eduard
 * @deprecated Versão atual {@link SimpleRecipe}
 *
 */
public class CraftSetup2
{
  private ShapelessRecipe recipe;
  private ItemStack result;
  
  public CraftSetup2(ItemStack result)
  {
    setResult(result);
    setRecipe(new ShapelessRecipe(result));
  }
  
  public void add(Material ingredient, int data)
  {
    this.recipe.addIngredient(ingredient, data);
  }
  
  public void remove(Material ingredient, int data)
  {
    this.recipe.removeIngredient(ingredient, data);
  }
  
  public ShapelessRecipe getRecipe()
  {
    return this.recipe;
  }
  
  public void setRecipe(ShapelessRecipe recipe)
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
