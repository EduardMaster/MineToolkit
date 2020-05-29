package net.eduard.api.lib.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import net.eduard.api.lib.storage.Storable;

/**
 * Representa um Craft sendo montado quais itens precisam ter para craftar este item
 * @version 2.0
 * @since 1.0 
 * @author Eduard
 *
 */
public class NormalRecipe implements Storable {
	public boolean addRecipe() {
		if (getResult() == null)
			return false;
		return Bukkit.addRecipe(getRecipe());
	}

	private Map<Integer, ItemStack> items = new HashMap<>();
	private ItemStack result = null;

	public NormalRecipe() {
		// TODO Auto-generated constructor stub
	}

	public NormalRecipe set(int slot, ItemStack item) {
		items.put(slot, item);
		return this;
	}

	public NormalRecipe remove(int slot) {
		items.remove(slot);
		return this;
	}

	public ItemStack getIngridient(int slot) {
		return items.get(slot);
	}

	public ShapedRecipe getRecipe() {
		if (result == null)
			return null;
		ShapedRecipe recipe = new ShapedRecipe(result);
		recipe.shape("789", "456", "123");

		for (Entry<Integer, ItemStack> entry : items.entrySet()) {
			try {
				recipe.setIngredient(getSlot(entry.getKey()), entry.getValue().getData());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return recipe;
	}

	public NormalRecipe(ItemStack craftResult) {
		setResult(craftResult);
	}

	@SuppressWarnings("unused")
	private char getSlot2(int slot) {
		char x = 'A';
		slot--;
		for (int id = 1; id <= slot; id++) {
			x++;
		}
		return x;
	}

	private char getSlot(int slot) {

		return Character.forDigit(slot, 10);
	}



	public Map<Integer, ItemStack> getItems() {
		return items;
	}

	public void setItems(Map<Integer, ItemStack> items) {
		this.items = items;
	}

	public ItemStack getResult() {
		return result;
	}

	public void setResult(ItemStack result) {
		this.result = result;
	}
}

