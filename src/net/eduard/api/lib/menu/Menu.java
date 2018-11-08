package net.eduard.api.lib.menu;

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
import net.eduard.api.lib.manager.EventsManager;
import net.eduard.api.lib.modules.ClickEffect;
import net.eduard.api.lib.modules.Copyable;
import net.eduard.api.lib.modules.Extra;

/**
 * Sistema proprio de criacao de Menus Interativos automaticos para facilitar
 * sua vida
 * 
 * @author Eduard
 *
 */
public class Menu extends EventsManager implements Copyable, PagedMenu {

	private String title = "Menu";
	private int lineAmount = 1;
	private int pageAmount = 1;
	private String pagePrefix = "";
	private String pageSuffix = "($page/$max_page)";
	private boolean translateIcon;
	private boolean autoAlignItems;
	private boolean cacheInventories;
	private ItemStack openWithItem = Mine.newItem(Material.COMPASS, "§aMenu Exemplo", 1, 0, "§2Clique abrir o menu");
	private String openWithCommand;
	private Slot previousPage = new Slot(
			Mine.newItem(Material.ARROW, "§aVoltar Página", 1, 0, "§2Clique para ir para a página anterior"), 1, 2);
	private Slot nextPage = new Slot(
			Mine.newItem("§aPróxima Página", Material.ARROW, 1, 0, "§2Clique para ir para a próxima página"), 9, 2);
	private ArrayList<MenuButton> buttons = new ArrayList<>();
	@NotCopyable
	private transient ClickEffect effect;
	@NotCopyable
	private transient Map<Integer, Inventory> pagesCache = new HashMap<>();
	@NotCopyable
	private transient Map<Player, Integer> pageOpened = new HashMap<>();

	public String getFullTitle() {
		if (isPageSystem()) {
			return pagePrefix + title + pageSuffix;
		}
		return title;
	}

	public String getPagePrefix() {
		return pagePrefix;
	}

	public void setPagePrefix(String pagePrefix) {
		this.pagePrefix = pagePrefix;
	}

	public String getPageSuffix() {
		return pageSuffix;
	}

	public void setPageSuffix(String pageSuffix) {
		this.pageSuffix = pageSuffix;
	}

	@Override
	public Menu copy() {

		return copy(this);
	}

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
		addButton(page, button.getIndex(), button);

	}

	public void addButton(int page, int positionX, int positionY, MenuButton button) {
		button.setPage(page);
		button.setPosition(positionX, positionY);
		buttons.add(button);
		updateCache(page);

	}

	public void addButton(int page, int index, MenuButton button) {
		button.setIndex(index);
		addButton(page, button.getPositionX(), button.getPositionY(), button);
	}

	public void removeButton(MenuButton button) {
		buttons.remove(button);

	}

	public void updateCache(int page) {
		if (isCacheInventories()) {

		}
	}

	public Menu() {
	}

	public Menu(String title, int lineAmount) {
		this.title = title;
		this.lineAmount = lineAmount;

	}

	protected int getIndex(int page, int slot) {
		return getPageSlot(page, slot);
	}

	public int getPageSlot(int page, int slot) {
		return getMinPageSlot(page) + slot;
	}

	public int getMinPageSlot(int page) {
		return (page - 1) * 9 * lineAmount;
	}

	public int getMaxPageSlot(int page) {
		return page * 9 * lineAmount;
	}

	public void clearCache() {
		pagesCache.clear();
	}

	public int getFirstEmptySlotOnInventories() {
		int page = 1;
		int id = -1;

		while (id == -1 && page <= pageAmount)
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

	public Inventory open(Player player, int page) {
		if (page < 1) {
			page = 1;
		}
		if (page > pageAmount) {
			page = pageAmount;
		}

		if (isCacheInventories() && pagesCache.containsKey(page)) {
			player.openInventory(pagesCache.get(page));
		} else {
			if (lineAmount < 1 | lineAmount > 6) {
				lineAmount = 1;
			}
			int minSlot = getMinPageSlot(page);
			int maxSlot = getMaxPageSlot(page);

			String prefix = getPagePrefix().replace("$page", "" + page).replace("$max_page", "" + pageAmount);
			String suffix = getPageSuffix().replace("$page", "" + page).replace("$max_page", "" + pageAmount);
			String menuTitle = Extra.toText(32, prefix + title + suffix);
			if (!isPageSystem()) {
				menuTitle = title;
			}
			Inventory menu = Bukkit.createInventory(player, 9 * lineAmount, menuTitle);
			if (isPageSystem()) {
				if (page > 1)
					previousPage.give(menu);
				if (page < pageAmount)
					nextPage.give(menu);
			}
			if (autoAlignItems) {
				int slot = 0;
				for (MenuButton button : buttons) {
					if (slot < minSlot | slot > maxSlot) {
						slot++;
						continue;
					}
//				slot -= minSlot;
					if (translateIcon) {
						menu.setItem(slot, Mine.getReplacers(button.getIcon(), player));
					} else {
						menu.setItem(slot, button.getIcon());
					}
					slot++;
				}
			} else {

				for (MenuButton button : buttons) {
					int slot = button.getIndex();
					if (slot < minSlot | slot >= maxSlot) {
						continue;
					}
//				slot -= minSlot;
					if (translateIcon) {
						ItemStack icon = Mine.getReplacers(button.getIcon(), player);
						menu.setItem(slot, icon);
					} else {
						menu.setItem(slot, button.getIcon());
					}
				}
			}

			player.openInventory(menu);
			if (isCacheInventories() && !pagesCache.containsKey(page)) {
				pagesCache.put(page, menu);
			}
			pageOpened.put(player, page);
//			Mine.broadcast("Abrindo "+page +" tem "+pageOpened.containsKey(player));
			return menu;
		}
		return Mine.newInventory("Null", 9);

	}

	public boolean isPageSystem() {
		return pageAmount > 1;
	}

	public void register(Plugin plugin) {

		super.register(plugin);
		for (MenuButton button : buttons) {
			if (button.isCategory())
				button.getMenu().register(plugin);

		}
	}

	public void unregisterListener() {
		super.unregisterListener();
		for (MenuButton button : buttons) {
			if (button.isCategory())
				button.getMenu().unregisterListener();

		}

	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand() == null)
			return;
		if (getOpenWithItem() != null && p.getItemInHand().isSimilar(getOpenWithItem())) {
			open(p);
		}

	}

	public ItemStack getOpenWithItem() {
		return openWithItem;
	}

	public void setOpenWithItem(ItemStack openWithItem) {
		this.openWithItem = openWithItem;
	}

	public String getOpenWithCommand() {
		return openWithCommand;
	}

	public void setOpenWithCommand(String openWithCommand) {
		this.openWithCommand = openWithCommand;
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		String cmd = Extra.getCommandName(message);
		if (getOpenWithCommand() != null)
			if (cmd.toLowerCase().startsWith("/" + getOpenWithCommand().toLowerCase())) {
				event.setCancelled(true);
				open(player);
			}
	}

	public Slot getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(Slot previousPage) {
		this.previousPage = previousPage;
	}

	public Slot getNextPage() {
		return nextPage;
	}

	public void setNextPage(Slot nextPage) {
		this.nextPage = nextPage;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {

			Player player = (Player) e.getWhoClicked();
			if (e.getInventory().getTitle().contains(getTitle())) {
				e.setCancelled(true);
				int slot = e.getRawSlot();
				if (pageOpened.containsKey(player)) {
					Integer page = pageOpened.get(player);
					ItemStack itemClicked = e.getCurrentItem();
					MenuButton button = null;
					if (itemClicked != null) {
						if (getPreviousPage().equals(itemClicked)) {
							open(player, ++page);
						} else if (getNextPage().equals(itemClicked)) {
							open(player, --page);
						} else {
							button = getButton(itemClicked);
						}
					} else {
						button = getButton(page, slot);
					}
				
					if (button != null) {
						if (button.getClick() != null) {
							button.getClick().onClick(e, slot);
						}
						if (button.getEffects() != null) {
							button.getEffects().effect(player);
						}
						if (button.isCategory()) {
							button.getMenu().open(player);
//							Mine.console("§cE uma categoria");
							return;
						}
					}
					if (getEffect() != null) {
//						System.out.println(button);
						getEffect().onClick(e, page);
					}
				}else {
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

	public ClickEffect getEffect() {
		return effect;
	}

	public void setEffect(ClickEffect effect) {
		this.effect = effect;
	}

	public boolean isAutoAlignItems() {
		return autoAlignItems;
	}

	public void setAutoAlignItems(boolean autoAlignItems) {
		this.autoAlignItems = autoAlignItems;
	}

	public ArrayList<MenuButton> getButtons() {
		return buttons;
	}

	public void setButtons(ArrayList<MenuButton> buttons) {
		this.buttons = buttons;
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

	public boolean isTranslateIcon() {
		return translateIcon;
	}

	public void setTranslateIcon(boolean translateIcon) {
		this.translateIcon = translateIcon;
	}

	@Override
	public boolean isOpen(Player player) {
		return player.getOpenInventory().getTitle().equals(title);
	}

	public int getLineAmount() {
		return lineAmount;
	}

	public void setLineAmount(int lineAmount) {
		this.lineAmount = lineAmount;
	}

	public int getPageAmount() {
		return pageAmount;
	}

	public void setPageAmount(int pageAmount) {
		this.pageAmount = pageAmount;
	}

	@Override
	public int getPageOpen(Player player) {
		return pageOpened.getOrDefault(player, 0);
	}

}
