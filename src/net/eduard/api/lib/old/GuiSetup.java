package net.eduard.api.lib.old;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.menu.Menu;
/**
 * Sistema de gerneciamentos de menus {@link GuiInventorySetup}
 * @since 0.7
 * @version 1.0
 * @author Eduard
 * @deprecated Versão atual {@link Menu}<br>
 * Versão nova {@link Gui} 2.0
 *
 */
public class GuiSetup implements Listener, ActiveManager {
	private List<GuiInventorySetup> guiInventorys = new ArrayList<GuiInventorySetup>();

	private boolean active;

	public GuiSetup()
  {
    Bukkit.getPluginManager().registerEvents(this,getInstance());
  }

	public void addNewGui(GuiInventorySetup gui) {
		if (gui == null)
			return;
		List<GuiInventorySetup> list = new ArrayList<GuiInventorySetup>();
		for (GuiInventorySetup inv : this.guiInventorys) {
			if (inv.getInventory().getTitle().equals(gui.getInventory().getTitle())) {
				list.add(inv);
			}
		}
		for (GuiInventorySetup inv : list) {
			this.guiInventorys.remove(inv);
		}
		this.guiInventorys.add(gui);
	}

	@EventHandler
	private void ClickInGUIEvent(InventoryClickEvent e) {
		if (!this.active)
			return;
		HumanEntity who = e.getWhoClicked();
		if (!(who instanceof Player))
			return;
		Player p = (Player) who;
		Inventory inv = e.getInventory();
		int normalSlot = e.getSlot();
		int size = inv.getSize();
		if (normalSlot > size)
			return;
		int slot = e.getRawSlot();
		ItemStack item = e.getCurrentItem();
		if (item == null)
			return;
		for (GuiInventorySetup gui : this.guiInventorys) {
			if (gui.getInventory().getTitle().equals(inv.getTitle())) {
				e.setCancelled(true);
				if (gui.getItemKey().isSimilar(item))
					return;
				for (GuiItemSetup key : gui.getItems()) {
					if (key.getSlot() == slot) {
						key.playerEvent(p);
						break;
					}
				}
				break;
			}
		}
	}

	public List<GuiInventorySetup> getGuiInventorys() {
		return this.guiInventorys;
	}

	public boolean isActive() {
		return this.active;
	}

	@EventHandler
	private void OpenGuiEvent(PlayerInteractEvent e) {
		if (!this.active)
			return;
		Player p = e.getPlayer();
		Action action = e.getAction();
		ItemStack item = e.getItem();
		if (item == null) {
			return;
		}
		boolean open = false;
		for (GuiInventorySetup inv : this.guiInventorys) {
			if ((inv.getItemKey() != null) && (new ItemSetup(item).isSimilar(inv.getItemKey()))) {

				if (inv.getOpenType() == GuiOpenType.BOTH) {
					open = action != Action.PHYSICAL;
				} else {
					if (inv.getOpenType() == GuiOpenType.RIGHT) {
						if (action == Action.RIGHT_CLICK_AIR) {
							open = true;
						}else if (action == Action.RIGHT_CLICK_BLOCK) {
							open = true;
						}
				
					} else {
						if (action == Action.LEFT_CLICK_AIR) {
							open = true;
						}else if (action == Action.LEFT_CLICK_BLOCK) {
							open = true;
						}
					}
				}
				if (open) {
					p.openInventory(inv.getInventory());
					if (inv.getOpenSound() != null) {
						inv.getOpenSound().create(p);
					}

					e.setCancelled(true);
					break;
				}
			}
		}
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
