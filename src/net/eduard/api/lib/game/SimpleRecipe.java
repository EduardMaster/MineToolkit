package net.eduard.api.lib.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

/**
 * Craft simples
 * <br><br>
 * Versões Antigas:
 * <br>
 * net.eduard.craft.CraftSetup1 1.0
 * <br>
 * net.eduard_api.game.craft.Crafts 2.0
 * <br>
 * net.eduard.eduard_api.game.craft.simples.Craft 3.0 
 * <br>
 * net.eduard.eduard_api.game.craft.CraftSimples 4.0
 * <br>
 * net.eduard.api.gui.SimpleCraft 5.0
 * <br>
 * net.eduard.api.setup.Mine$SimpleCraft
 * @version 1.0
 * @since EduardAPI 1.0
 * @author Eduard
 *
 */
public class SimpleRecipe {
	public boolean addRecipe() {
		if (getResult() == null)
			return false;
		return Bukkit.addRecipe(getRecipe());
	}

	private ItemStack result = null;
	private List<ItemStack> items = new ArrayList<>();

	public SimpleRecipe() {

	}

	public SimpleRecipe(ItemStack result) {
		setResult(result);
	}

	public SimpleRecipe add(Material material) {
		return add(new ItemStack(material));
	}

	public SimpleRecipe add(Material material, int data) {
		return add(new ItemStack(material, 1, (short) data));
	}

	public SimpleRecipe add(ItemStack item) {
		items.add(item);
		return this;
	}

	public SimpleRecipe remove(ItemStack item) {
		items.remove(item);
		return this;
	}


	public ShapelessRecipe getRecipe() {
		if (result == null)
			return null;
		ShapelessRecipe recipe = new ShapelessRecipe(result);
		for (ItemStack item : items) {
			recipe.addIngredient(item.getData());
		}
		return recipe;
	}


	public ItemStack getResult() {

		return result;
	}

	public void setResult(ItemStack result) {
		this.result = result;
	}

}
