package net.eduard.api.lib.old;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.eduard.api.lib.menu.Menu;

/**
 * Sistema de criar menu
 * 
 * @since 0.9
 * @version 2.0
 * @author Eduard
 * @deprecated Versão atual {@link Menu}<br>
 *             Versão anterior {@link GuiInventorySetup} 1.0
 *
 */

public class Gui {
	private Inventory inventory;
	private Item key;
	private Player player;
	private HashMap<Integer, Slot> slots = new HashMap<Integer, Slot>();

	private Sounds sound;

	public Gui(GuiType type, String name) {
		setInventory(Bukkit.createInventory(null, type.getSize(), name));
	}

	public boolean addSlot(int id, Slot slot) {
		if (id > getInventory().getSize())
			return false;
		if (slot == null)
			return false;
		this.slots.put(Integer.valueOf(id), slot);
		getInventory().setItem(id, slot);
		return true;
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public Item getKey() {
		return this.key;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Slot getSlot(int id) {
		if (this.slots.containsKey(Integer.valueOf(id))) {
			return (Slot) this.slots.get(Integer.valueOf(id));
		}
		return null;
	}

	public Sounds getSound() {
		return this.sound;
	}

	private void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public void setKey(Item key) {
		this.key = key;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setSound(Sounds sound) {
		this.sound = sound;
	}
}
