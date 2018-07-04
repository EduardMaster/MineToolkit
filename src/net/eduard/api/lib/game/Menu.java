package net.eduard.api.lib.game;

import java.util.Arrays;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.eduard.api.lib.Copyable;
import net.eduard.api.lib.core.Mine;
import net.eduard.api.lib.storage.Storable;

/**
 * Um Interface em um Invetario do Minecraft
 * 
 * @version
 * @author Eduard
 *
 */
public class Menu implements Listener, Storable, Copyable {

	private transient ClickEffect[] clicks;
	private transient ClickEffect click = new ClickEffect() {

		@Override
		public void onClick(InventoryClickEvent event, int page) {

		}
	};
	private transient Inventory[] inventories;

	private String name;
	private int lines = 6;
	private int pages = 10;
	private String pagPrefix = " §8P§gina: ";
	private String command = "abrirGuiExemplo";
	private ItemStack item = new ItemStack(Material.COMPASS);
	private boolean hasPageSystem = true;

	private int nextPageSlot = 8;
	private int previousPageSlot = 0;
	private ItemStack nextPage = Mine.newItem(Material.ARROW, "§aPr§xima P§gina");

	public Menu copy() {
		return copy(this);
	}

	public void unregister() {
		HandlerList.unregisterAll(this);
	}

	public void register(Plugin plugin) {
		unregister();
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	private ItemStack previosPage = Mine.newItem(Material.ARROW, "§aP§gina Anterior");

	private ItemStack[] slots;

	public Menu() {
		resetInventories();
	}

	public Menu(String name) {
		this(name, 6);
	}

	public Menu(String name, int lines) {
		this.name = name;
		this.lines = lines;
		resetInventories();
	}

	public void resetInventories() {
		slots = new ItemStack[lines * 9 * pages];
		clicks = new ClickEffect[lines * 9 * pages];
		inventories = new Inventory[pages];
		resetPages();

	}

	public void resetPages() {
		for (int page = 1; page <= pages; page++) {
			addSlot(previosPage, page, previousPageSlot);
			addSlot(nextPage, page, nextPageSlot);
		}
	}

	public void onCopy() {
		resetInventories();
	}

	public boolean isHasPageSystem() {
		return hasPageSystem;
	}

	public void setHasPageSystem(boolean hasPageSystem) {
		this.hasPageSystem = hasPageSystem;
	}

	public int getFirstEmptySlot() {
		return getFirstEmptySlot(1);
	}

	public int getFirstEmptySlot(int page) {
		if (hasInventory(page)) {
			return inventories[page - 1].firstEmpty();
		} else {
			ItemStack[] array = Arrays.copyOfRange(slots, getIndex(page, 0), getIndex(page + 1, 0));
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null)
					return i;
			}
		}
		return -1;
	}

	public boolean hasInventory(int page) {
		return inventories[page - 1] != null;
	}

	public boolean isFull() {
		return isFull(1);
	}

	public boolean isFull(int page) {
		return Mine.isFull(inventories[page - 1]);
	}

	public boolean hasPages() {
		return pages > 1;
	}

	public boolean isUniquePage() {
		return pages == 1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPagPrefix() {
		return pagPrefix;
	}

	public void setPagPrefix(String pagPrefix) {
		this.pagPrefix = pagPrefix;
	}

	public ItemStack[] getItems() {
		return slots;
	}

	public void setItems(ItemStack[] items) {
		this.slots = items;
	}

	public void openGui(Player player) {
		openGui(player, 1);
	}

	public boolean hasSlot(int page, int slot) {
		return getSlot(page, slot) != null;
	}

	public boolean hasClick(int page, int slot) {
		return clicks[getIndex(page, slot)] != null;
	}

	public double getPageBySlot(int slot) {
		return slot / (lines * 9);
	}

	public double getSlotMinor(int slot) {
		return slot % (lines * 9);
	}

	public void addSlot(ItemStack item) {
		for (int slot = 0; slot < slots.length; slot++) {
			ItemStack slotItem = slots[slot];
			if (slotItem == null) {
				double pagina = getPageBySlot(slot);
				if (pagina == 0) {
					pagina++;
				}
				double slotmenor = getSlotMinor(slot);
				addSlot(item, (int) pagina, (int) slotmenor);
				break;
			}
		}

	}

	public void addSlot(ItemStack item, int page, int slot) {
		addSlot(item, page, slot, null);
	}

	public void addSlot(ItemStack item, int page, int slot, ClickEffect click) {
		int index = getIndex(page, slot);
		slots[index] = item;
		clicks[index] = click;
		if (hasInventory(page)) {
			getPage(page).setItem(index, item);
		}
	}

	public void removeSlot(int page, int slot) {
		int index = getIndex(page, slot);
		slots[index] = null;
		clicks[index] = null;
		if (hasInventory(page)) {
			getPage(page).clear(index);
		}
	}

	public Inventory getPage(int page) {
		return inventories[page - 1];
	}

	public ItemStack getSlot(int page, int slot) {
		int index = getIndex(page, slot);
		return slots[index];
	}

	public ClickEffect getClick(int page, int slot) {
		int index = getIndex(page, slot);
		return clicks[index];
	}

	protected int getIndex(int page, int slot) {
		int result = ((page - 1) * lines * 9) + (slot);
		return result;
	}

	public void openGui(Player player, int page) {
		Inventory inventory = inventories[page - 1];
		if (inventory == null) {
			inventory = Bukkit.createInventory(null, lines * 9, name + pagPrefix + page);
			inventory.setContents(Arrays.copyOfRange(slots, getIndex(page, 0), getIndex(page + 1, 0)));
			inventories[page - 1] = inventory;
		}
		player.openInventory(inventory);
	}

	@EventHandler
	public void onOpenGui(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.getItemInHand() == null)
			return;
		if (item == null)
			return;
		if (item.isSimilar(player.getItemInHand())) {
			openGui(player);
		}

	}

	@EventHandler
	public void onOpenGui(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		String cmd = Mine.getCmd(message);
		if (command == null)
			return;
		if (Mine.startWith(cmd, "/" + command)) {
			event.setCancelled(true);
			openGui(player);
		}
	}

	@EventHandler
	public void onClickInGui(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if (event.getInventory().getName().startsWith(name)) {
				ItemStack item = event.getCurrentItem();
				event.setCancelled(true);
				if (item == null)
					return;

				int slot = event.getRawSlot();
				int page = 1;
				if (!isUniquePage()) {
					page = Integer.valueOf(event.getInventory().getTitle().split(name + pagPrefix)[1]);
				}
				if (hasPageSystem) {
					if (item.equals(nextPage)) {
						if (page == pages) {
							return;
						}
						openGui(player, ++page);
						return;
					}
					if (item.equals(previosPage)) {
						if (page <= 1) {
							return;
						}
						openGui(player, --page);
						return;
					}
				}
				ClickEffect click = getClick(page, slot);
				if (click != null) {
					click.onClick(event, page);
				} else {
					this.click.onClick(event, page);
				}

			}

		}
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public ItemStack getNextPage() {
		return nextPage;
	}

	public void setNextPage(ItemStack nextPage) {
		this.nextPage = nextPage;
	}

	public ItemStack getPreviosPage() {
		return previosPage;
	}

	public void setPreviosPage(ItemStack previosPage) {
		this.previosPage = previosPage;
	}

	public int getPreviousPageSlot() {
		return previousPageSlot;
	}

	public void setPreviousPageSlot(int previousPageSlot) {
		this.previousPageSlot = previousPageSlot;
	}

	public int getNextPageSlot() {
		return nextPageSlot;
	}

	public void setNextPageSlot(int nextPageSlot) {
		this.nextPageSlot = nextPageSlot;
	}

	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public Inventory[] getInventories() {
		return inventories;
	}

	public void setInventories(Inventory[] inventories) {
		this.inventories = inventories;
	}

	public ItemStack[] getSlots() {
		return slots;
	}

	public void setSlots(ItemStack[] slots) {
		this.slots = slots;
	}

	public ClickEffect[] getClicks() {
		return clicks;
	}

	public void setClicks(ClickEffect[] clicks) {
		this.clicks = clicks;
	}

	public ClickEffect getClick() {
		return click;
	}

	public void setClick(ClickEffect click) {
		this.click = click;
	}

	@Override
	public Object restore(Map<String, Object> map) {
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {

	}

}
