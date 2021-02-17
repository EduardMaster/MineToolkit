package net.eduard.api.lib.game;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * API de criação de Craft predefinido com Slots e Items expecificos
 * @version 1.0
 * @since 1.0 
 * @author Eduard
 * @see SimpleRecipe
 */
@SuppressWarnings("unused")
public class NormalRecipe   {
	/**
	 * Retorna false se não tiver Recipe pronto
	 * @return Novo recipe criado
	 */
	public boolean addRecipe() {
		if (getResult() == null)
			return false;
		return Bukkit.addRecipe(getRecipe());
	}

	/**
	 * Posições dos items do craft
	 */
	private Map<Integer, ItemStack> items = new HashMap<>();
	/**
	 * Resultado do Craft
	 */
	private ItemStack result = null;

	public NormalRecipe() {

	}

	/**
	 * Define um ingrediente fixo
	 * @param slot Posição
	 * @param item Item
	 * @return a classe mesma
	 */
	public NormalRecipe set(int slot, ItemStack item) {
		items.put(slot, item);
		return this;
	}

	/**
	 * Remove um ingrediente fixo
	 * @param slot Posição
	 * @return a classe mesma
	 */
	public NormalRecipe remove(int slot) {
		items.remove(slot);
		return this;
	}

	public ItemStack getIngridient(int slot) {
		return items.get(slot);
	}

	/**
	 *  Monta o recipe
	 * @return o Recipe criado
	 */
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

