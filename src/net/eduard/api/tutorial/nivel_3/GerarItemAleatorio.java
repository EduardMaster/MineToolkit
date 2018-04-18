package net.eduard.api.tutorial.nivel_3;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import net.eduard.api.setup.Mine;

public class GerarItemAleatorio {

	private List<ItemStack> lista = new ArrayList<>();
	private List<ItemAleatorio> items = new ArrayList<>();
	public ItemStack itemAleatorioDaLista() {
		return Mine.getRandomItem(lista);
	}
	public ItemStack itemSequecial() {
		return itemSequencialComChance(Mine.getRandomInt(0, lista.size()-1));
	}
	public ItemStack itemSequencialComChance(int id) {
		ItemAleatorio item = items.get(id);
		if (Mine.getChance(item.chance)) {
			return item.item;
		}
		id++;
		if (id == items.size()) {
			id = 0;
		}
		return itemSequencialComChance(id);
	}
	public static class ItemAleatorio{
		
		private ItemStack item;
		private double chance=0.5;
		
	}
}
