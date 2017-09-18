package net.eduard.api.setup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import net.eduard.api.setup.StorageAPI.Storable;
public final class CraftAPI {

	public static interface RecipeBuilder {

		public Recipe getRecipe();
		public default boolean addRecipe() {
			;
			if (getResult() == null)
				return false;
			return Bukkit.addRecipe(getRecipe());
		}
		public ItemStack getResult();
		public void setResult(ItemStack result);

	}



public static class SimpleRecipe implements Storable ,RecipeBuilder{
	
	private ItemStack result = null;
	private List<ItemStack> items = new ArrayList<>();

	public SimpleRecipe(ItemStack result) {
		setResult(result);
	}

	public SimpleRecipe add(Material material) {
		return add(new ItemStack(material));
	}
	public SimpleRecipe add(Material material,int data) {
		return add(new ItemStack(material,1,(short)data));
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
		if (result == null)return null;
		ShapelessRecipe recipe = new ShapelessRecipe(result);	
		for (ItemStack item :items) {
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

	public static class NormalRecipe implements Storable ,RecipeBuilder{

		private Map<Integer, ItemStack> items = new HashMap<>();
		private ItemStack result = null;

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
					recipe.setIngredient(getSlot(entry.getKey()),
							entry.getValue().getData());
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

		@Override
		public Object restore(Map<String, Object> map) {
			return null;
		}

		@Override
		public void store(Map<String, Object> map, Object object) {
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
}
