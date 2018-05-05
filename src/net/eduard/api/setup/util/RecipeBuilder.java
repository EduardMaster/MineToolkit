package net.eduard.api.setup.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public interface RecipeBuilder {

	public Recipe getRecipe();

	public default boolean addRecipe() {
		if (getResult() == null)
			return false;
		return Bukkit.addRecipe(getRecipe());
	}

	public ItemStack getResult();

	public void setResult(ItemStack result);

}