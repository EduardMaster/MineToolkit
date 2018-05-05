package net.eduard.api.setup.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import net.eduard.api.setup.StorageAPI.Storable;
import net.eduard.api.setup.util.RecipeBuilder;

public class SimpleRecipe implements Storable, RecipeBuilder {

	private ItemStack result = null;
	private List<ItemStack> items = new ArrayList<>();

	public SimpleRecipe() {
		// TODO Auto-generated constructor stub
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

	public ItemStack getResult() {

		return result;
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

	@Override
	public Object restore(Map<String, Object> map) {
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
	}

	public void setResult(ItemStack result) {
		this.result = result;
	}

}
