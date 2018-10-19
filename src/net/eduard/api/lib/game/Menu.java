package net.eduard.api.lib.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.EffectManager;
import net.eduard.api.lib.manager.EventsManager;
import net.eduard.api.lib.modules.ClickEffect;
import net.eduard.api.lib.modules.Extra;

/**
 * Sistema proprio de criacao de Menus Interativos automaticos para facilitar
 * sua vida
 * 
 * @author Eduard
 *
 */
public class Menu extends EventsManager {

	private String title = "Menu";

	private int lines = 1;

	private int pages = 1;

	private String pagesPrefix = " ";
	private String pagesSuffix = " ($page/$max_page)";
	private boolean autoAlignItems;
	private boolean openWithItem = true;
	private boolean openWithCommand = true;
	private boolean cacheInventories;
	private String commandKey = "menugui";
	private int slotPreviousPage = 2 * 9 ;
	private int slotNextPage = 3 * 9 - 1;
	private ItemStack itemKey = Mine.newItem(Material.COMPASS, "§aMenu Exemplo", 1, 0, "§2Clique abrir o menu");
	private ItemStack itemNextPage = Mine.newItem("§aPróxima Página", Material.ARROW, 1, 0,
			"§2Clique para ir para a próxima página");
	private ItemStack itemPreviousPage = Mine.newItem(Material.ARROW, "§aVoltar Página", 1, 0,
			"§2Clique para ir para a página anterior");

	private ArrayList<MenuButton> buttons = new ArrayList<>();
	private transient ClickEffect effect;
	private transient Map<Integer, Inventory> pagesCache = new HashMap<>();
	private transient Map<Player, Integer> pageOpened = new HashMap<>();

	public MenuButton getButton(ItemStack icon) {
		for (MenuButton button : buttons) {
			if (button.getIcon().equals(icon)) {
				return button;
			}
		}

		return null;
	}

	public MenuButton getButton(String name) {
		for (MenuButton button : buttons) {
			if (button.getName().equalsIgnoreCase(name)) {
				return button;
			}
		}

		return null;
	}

	public MenuButton getButton(int page, int index) {
		for (MenuButton button : buttons) {
			if (button.getIndex() == index && page == button.getPage())
				return button;
		}
		return null;
	}

	public void addButton(MenuButton button) {
		addButton(button.getPage(), button);
	}

	public void addButton(int page, MenuButton button) {
		button.setPage(page);
		buttons.add(button);
		updateCache(page);
	}

	public void addButton(int page, int positionX, int positionY, MenuButton button) {
		button.setPage(page);
		button.setPositionX(positionX);
		button.setPositionY(positionY);
		buttons.add(button);
		updateCache(page);
	}

	public void addButton(int page, int index, MenuButton button) {
		button.setPage(page);
		button.setIndex(index);
		buttons.add(button);
		updateCache(page);
	}

	public void removeButton(MenuButton button) {
		buttons.remove(button);

	}

	public void updateCache(int page) {
		if (isCacheInventories()) {

		}
	}

	public void open(Player player) {

		open(player, 1);
	}

	public Menu() {
	}

	public Menu(String title, int linesAmount) {
		this.title = title;
		this.lines = linesAmount;
	}

	protected int getIndex(int page, int slot) {
		return getPageSlot(page, slot);
	}

	public int getPageSlot(int page, int slot) {
		return getMinPageSlot(page) + slot;
	}

	public int getMinPageSlot(int page) {
		return (page - 1) * 9 * lines;
	}

	public int getMaxPageSlot(int page) {
		return page * 9 * lines;
	}

	public void clearCache() {
		pagesCache.clear();
	}

	public int getFirstEmptySlotOnInventories() {
		int page = 1;
		int id = -1;

		while (id == -1 && page <= pages)
			id = getFirstEmptySlot(++page);

		return id;
	}

	public int getFirstEmptySlot(int page) {

		for (int slot = getMinPageSlot(page); slot < getMaxPageSlot(page); slot++) {
			MenuButton button = getButton(page, slot);
			if (button == null)
				return slot;
		}

		return -1;
	}

	public boolean isFull() {
		return isFull(1);
	}

	public boolean isFull(int page) {

		for (int slot = getMinPageSlot(page); slot < getMaxPageSlot(page); slot++) {
			MenuButton button = getButton(page, slot);
			if (button == null)
				return false;

		}
		return true;
	}

	public void open(Player player, int page) {
		if (page < 1) {
			page = 1;
		}
		if (page > pages) {
			page = pages;
		}
		pageOpened.put(player, page);
		if (isCacheInventories() && pagesCache.containsKey(page)) {
			player.openInventory(pagesCache.get(page));
			return;
		}
		if (lines < 1 | lines > 6) {
			lines = 1;
		}
		int minSlot = (page - 1) * lines * 9;
		int maxSlot = (page) * lines * 9;

		String prefix = isPageSystem() ? pagesPrefix : "";
		String suffix = isPageSystem() ? pagesSuffix : "";
		if (prefix == null) {
			prefix = "";
		}
		if (suffix == null) {
			suffix = "";
		}
	
		
		prefix = prefix.replace("$page", "" + page).replace("$max_page", "" + pages);
		suffix = suffix.replace("$page", "" + page).replace("$max_page", "" + pages);
		String menuTitle = Extra.toText(32, prefix + title + suffix);
		Inventory menu = Bukkit.createInventory(player, 9 * lines, menuTitle);
		if (isPageSystem()) {
			if (page > 1)
				menu.setItem(slotPreviousPage, itemPreviousPage);
			if (page < pages)
				menu.setItem( slotNextPage, itemNextPage);
		}
		if (autoAlignItems) {
			int slot = 0;
			for (MenuButton button : buttons) {
				if (slot < minSlot | slot > maxSlot) {
					slot++;
					continue;
				}
				slot -= minSlot;
				menu.setItem(slot, button.getIcon());
				slot++;
			}
		} else {

			for (MenuButton button : buttons) {
				int slot = button.getIndex();

				if (slot < minSlot | slot > maxSlot) {
					continue;
				}
				slot -= minSlot;
				menu.setItem(slot, button.getIcon());
			}
		}

		player.openInventory(menu);
		if (isCacheInventories() && !pagesCache.containsKey(page)) {
			pagesCache.put(page, menu);
		}
	}

	private boolean isPageSystem() {
		return pages > 1;
	}

	public void register(Plugin plugin) {

		super.register(plugin);
		for (MenuButton button : buttons) {
			if (button.isCategory()) {
				button.getShop().register(plugin);
			}

		}
	}

	public void unregisterListener() {
		super.unregisterListener();
		for (MenuButton button : buttons) {
			if (button.isCategory()) {
				button.getShop().unregisterListener();
			}

		}
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand() == null)
			return;
		if (isOpenWithItem() && p.getItemInHand().isSimilar(itemKey)) {
			open(p);
		}

	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		String cmd = getCommandName(message);
		if (cmd.toLowerCase().startsWith("/" + commandKey.toLowerCase())) {
			event.setCancelled(true);
			open(player);
		}
	}

	private String getCommandName(String message) {
		String command = message;
		if (message.contains(" "))
			command = message.split(" ")[0];
		return command;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {

			Player player = (Player) e.getWhoClicked();

			if (e.getInventory().getName().contains(title)) {
				e.setCancelled(true);
				ItemStack itemClicked = e.getCurrentItem();
				int page = pageOpened.get(player);

//				int minSlot = (page - 1) * lines * 9;
				int slot = e.getRawSlot();

				if (isPageSystem()) {
					if (slot ==  slotNextPage) {
						open(player, ++page);
					}
					if (slot ==  slotPreviousPage) {
						open(player, --page);
					}
				}

				MenuButton button = getButton(page, slot);
				if (button == null) {
					button = getButton(itemClicked);
				}
				if (effect != null) {
					effect.onClick(e, page);
				}
				if (button != null) {
					if (button.isCategory()) {
						button.getShop().open(player);
						return;

					}

					ClickEffect buttonClick = button.getClick();
					if (buttonClick != null)
						buttonClick.onClick(e, page);
					EffectManager buttonEffects = button.getEffects();
					if (buttonEffects != null) {
						buttonEffects.effect(player);
					}
				}

			}

		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	public ClickEffect getEffect() {
		return effect;
	}

	public void setEffect(ClickEffect effect) {
		this.effect = effect;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public ItemStack getItemNextPage() {
		return itemNextPage;
	}

	public void setItemNextPage(ItemStack itemNextPage) {
		this.itemNextPage = itemNextPage;
	}

	public ItemStack getItemPreviousPage() {
		return itemPreviousPage;
	}

	public void setItemPreviousPage(ItemStack itemPreviousPage) {
		this.itemPreviousPage = itemPreviousPage;
	}

	public int getSlotNextPage() {
		return slotNextPage;
	}

	public void setSlotNextPage(int slotNextPage) {
		this.slotNextPage = slotNextPage;
	}

	public int getSlotPreviousPage() {
		return slotPreviousPage;
	}

	public void setSlotPreviousPage(int slotPreviousPage) {
		this.slotPreviousPage = slotPreviousPage;
	}

	public boolean isAutoAlignItems() {
		return autoAlignItems;
	}

	public void setAutoAlignItems(boolean autoAlignItems) {
		this.autoAlignItems = autoAlignItems;
	}

	public String getCommandKey() {
		return commandKey;
	}

	public void setCommandKey(String commandKey) {
		this.commandKey = commandKey;
	}

	public ArrayList<MenuButton> getButtons() {
		return buttons;
	}

	public void setButtons(ArrayList<MenuButton> buttons) {
		this.buttons = buttons;
	}

	public boolean isOpenWithItem() {
		return openWithItem;
	}

	public void setOpenWithItem(boolean openWithItem) {
		this.openWithItem = openWithItem;
	}

	public ItemStack getItemKey() {
		return itemKey;
	}

	public void setItemKey(ItemStack itemKey) {
		this.itemKey = itemKey;
	}

	public Map<Integer, Inventory> getPagesCache() {
		return pagesCache;
	}

	public void setPagesCache(Map<Integer, Inventory> pagesCache) {
		this.pagesCache = pagesCache;
	}

	public boolean isCacheInventories() {
		return cacheInventories;
	}

	public void setCacheInventories(boolean cacheInventories) {
		this.cacheInventories = cacheInventories;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {

	}

	public boolean isOpenWithCommand() {
		return openWithCommand;
	}

	public void setOpenWithCommand(boolean openWithCommand) {
		this.openWithCommand = openWithCommand;
	}

	public String getPagesPrefix() {
		return pagesPrefix;
	}

	public void setPagesPrefix(String pagesPrefix) {
		this.pagesPrefix = pagesPrefix;
	}

	public String getPagesSuffix() {
		return pagesSuffix;
	}

	public void setPagesSuffix(String pagesSuffix) {
		this.pagesSuffix = pagesSuffix;
	}

}
