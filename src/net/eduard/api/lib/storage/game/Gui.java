package net.eduard.api.lib.storage.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.storage.manager.EffectManager;
/**
 * 
 * @author Eduard
 *
 */
public class Gui extends Menu {

	private Map<Integer, EffectManager> effects = new HashMap<>();

	public Gui() {
	}
	public Gui(String name, int lines) {
		super(name, lines);
	}
	public Gui(String name) {
		super(name);
	}

	public void addSlot(int page, Slot slot) {
		addSlot(page, slot.getIndex(), slot.getItem(), slot);
	}
	public void addSlot(int page,int slot, ItemStack item,EffectManager effect) {
		addSlot(item, page, slot, new ClickEffect() {

			@Override
			public void onClick(InventoryClickEvent event, int page) {
				if (event.getWhoClicked() instanceof Player) {
					Player p = (Player) event.getWhoClicked();
					effect.effect(p);
				}
			}
		});
		effects.put(getIndex(page, slot), effect);
	}
	public void removeSlot(int page, int slot) {
		super.removeSlot(page, slot);
		effects.remove(getIndex(page, slot));
	}

	public EffectManager getEffect(int page, int index) {
		return effects.get(getIndex(page, index));
	}
	public void updateGui() {
		for (Entry<Integer, EffectManager> entry : effects.entrySet()) {
			EffectManager slot = entry.getValue();
			Integer id = entry.getKey();
			getClicks()[id] = new ClickEffect() {
				
				@Override
				public void onClick(InventoryClickEvent event, int page) {
					if (event.getWhoClicked() instanceof Player) {
						Player p = (Player) event.getWhoClicked();
						slot.effect(p);;
						
					}
				}
			};
		}
	}
	public void onCopy() {
		updateGui();
	}
	public Object restore(Map<String, Object> map) {
		updateGui();
		return null;
	}

}
