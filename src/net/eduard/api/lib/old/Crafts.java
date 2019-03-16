package net.eduard.api.lib.old;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import net.eduard.api.lib.game.SimpleRecipe;




/**
 * Sistema de criar crafts mais facil do tipo {@link ShapelessRecipe}<br>
 * Versão anterior {@link CraftSetup2} 1.0
 * @version 2.0
 * @since 0.9
 * @author Eduard
 * @deprecated Versão atual {@link SimpleRecipe}
 *
 */
public class Crafts
{
  private ShapelessRecipe recipe;
  private ItemStack result;
  
  public Crafts(ItemStack result)
  {
    setResult(result);
    setRecipe(new ShapelessRecipe(result));
  }
  
  public void add(Material ingredient, int data)
  {
    this.recipe.addIngredient(ingredient, data);
  }
  
  public void addRecipe()
  {
    Bukkit.addRecipe(getRecipe());
  }
  
  public ShapelessRecipe getRecipe()
  {
    return this.recipe;
  }
  
  public ItemStack getResult()
  {
    return this.result;
  }
  
  public void remove(Material ingredient, int data)
  {
    this.recipe.removeIngredient(ingredient, data);
  }
  
  public void setRecipe(ShapelessRecipe recipe)
  {
    this.recipe = recipe;
  }
  
  public void setResult(ItemStack result)
  {
    this.result = result;
  }
}
