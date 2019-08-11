package net.eduard.api.server.kits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.EventsManager;
import net.eduard.api.lib.menu.Menu;
import net.eduard.api.lib.menu.MenuButton;
import net.eduard.api.lib.menu.Slot;
import net.eduard.api.lib.modules.ClickEffect;
import net.eduard.api.lib.modules.KitType;
import net.eduard.api.lib.modules.VaultAPI;

public class KitManager extends EventsManager {

	private transient Map<Player, KitAbility> playersKits = new HashMap<>();
	private transient Map<Player, Menu> playerKitsMenus = new HashMap<>();
	private transient Map<Player, Menu> playerShops = new HashMap<>();
	private Slot openKits = new Slot(Mine.newItem(Material.CHEST, "§6§lSelecionar Kit"), 0);
	private Slot openShop = new Slot(Mine.newItem(Material.EMERALD, "§6§lComprar Kit"), 8);

	private List<ItemStack> globalItems = new ArrayList<>();
	private ItemStack soup = Mine.newItem("§6Sopa", Material.MUSHROOM_SOUP);
	private ItemStack emptySlotItem = Mine.newItem(" ", Material.STAINED_GLASS_PANE, 15);
	private ItemStack hotBarItem = Mine.newItem("§6§lKit§f§lPvP", Material.STAINED_GLASS_PANE, 10);

	private boolean kitsEnabled = true;
	private double defaultKitPrice = 0;
	private boolean giveSoups;
	private boolean fillHotBar = true;
	private boolean onSelectGainKit = true;
	private String noneKit = "§8Nenhum";
	private String kitsDisabled = "§cOs kits foram desabilitados!";
	private String kitSelected = "§6Voce escolheu o Kit §e$kit";
	private String kitGived = "§6Voce ganhou o Kit §e$kit";
	private String kitBuyed = "§§Voce comprou o Kit §e$kit";
	private String noKitBuyed = "§§Voce nao tem dinheiro para comprar o kit §e$kit";
	private String guiShopTitle = "§cKit §4§l$kit §cseu pre§o: §a§l$price";
	private Menu shopGui = new Menu("§8Loja de Kits", 6);
	private Menu kitsGui = new Menu("§8Seus  Kits", 6);;
	private ArrayList<KitAbility> kits = new ArrayList<>();

	public KitManager() {
		globalItems.add(new ItemStack(Material.STONE_SWORD));
		kitsGui.setOpenWithItem(null);
		shopGui.setOpenWithItem(null);
		kitsGui.setEffect(new ClickEffect() {

			@Override
			public void onClick(InventoryClickEvent event, int page) {
				if (event.getWhoClicked() instanceof Player) {
					Player player = (Player) event.getWhoClicked();
					ItemStack item = event.getCurrentItem();

					for (KitAbility kit : kits) {
						if (kit.getIcon().equals(item)) {
							selectKit(player, kit);
							break;
						}
					}

				}
			}
		});
		shopGui.setEffect(new ClickEffect() {

			@Override
			public void onClick(InventoryClickEvent event, int page) {
				if (event.getWhoClicked() instanceof Player) {
					Player player = (Player) event.getWhoClicked();

					ItemStack item = event.getCurrentItem();

					for (KitAbility kit : kits) {
						if (kit.getIcon().equals(item)) {
							buyKit(player, kit);
							break;
						}
					}

				}
			}
		});

	}

	public void openShop(Player player) {
		openShop(player, 1);
	}

	public void openShop(Player player, int page) {
		getShop(player).open(player, page);
	}

	public void openKitSelector(Player player) {
		openKitSelector(player, 1);
	}

	public void openKitSelector(Player player, int page) {
		getKitSelector(player).open(player, page);
	}

	public void giveItems(Player player) {
		PlayerInventory inv = player.getInventory();
		openShop.give(inv);
		openKits.give(inv);
		if (fillHotBar) {
			Mine.addHotBar(player, hotBarItem);
		}

	}

	public Menu getKitSelector(Player player) {
		Menu kitSelector = playerKitsMenus.get(player);
		if (kitSelector == null) {
			kitSelector = kitsGui.copy();
//	
			for (KitAbility kit : kits) {

				if (!player.hasPermission(kit.REQUIRE_PERMISSION)) {

					kitSelector.addButton(new MenuButton(kit.getIcon()));

				}

			}
			playerKitsMenus.put(player, kitSelector);
			kitSelector.register(getPluginInstance());
		}
		return kitSelector;

	}

	public Menu getShop(Player player) {
		Menu shop = playerShops.get(player);
		if (shop == null) {
			shop = shopGui.copy();
//	
			for (KitAbility kit : kits) {

				if (!player.hasPermission(kit.REQUIRE_PERMISSION)) {

					shop.addButton(new MenuButton(kit.getIcon()));

				}

			}
			playerShops.put(player, shop);
			shop.register(getPluginInstance());
		}
		return shop;

	}

	public KitAbility getKit(KitType type) {
		for (KitAbility kit : kits) {
			if (kit.getName().equalsIgnoreCase(type.name())) {
				return kit;
			}
		}
		return null;
	}

	public void gainKit(Player player) {
		if (!kitsEnabled)
			return;
		KitAbility kit = playersKits.get(player);
		removeKits(player);
		Mine.refreshAll(player);
		PlayerInventory inv = player.getInventory();
		for (ItemStack item : kit.GIVE_ITEMS) {
			inv.addItem(item);
		}
		for (KitAbility subkit : kit.getKits()) {
			for (ItemStack item : subkit.GIVE_ITEMS) {
				inv.addItem(item);
			}
		}
		for (ItemStack item : globalItems) {
			inv.addItem(item);
		}
		if (giveSoups) {
			Mine.fill(inv, soup);
			Mine.setEquip(player, Color.GREEN, "§4§lINSANE");
		}
		player.sendMessage(kitGived.replace("$kit", kit.getName()));
	}

	public void register(Plugin plugin) {
		for (KitAbility kit : kits) {
			kit.register(plugin);
			if (kit.getClick() != null) {
				kit.getClick().register(plugin);
			}
			if (kit.getPrice() == 0) {
				kit.setPrice(defaultKitPrice);
			}
		}

		super.register(plugin);

	}

	public void removeKits(Player player) {
		playersKits.remove(player);
	}

	public void selectKit(Player player, KitAbility kit) {
		player.closeInventory();
		playersKits.put(player, kit);
		player.sendMessage(kitSelected.replace("$kit", kit.getName()));
		if (onSelectGainKit) {
			gainKit(player);
		}

	}

	public void selectKit(Player player, KitType type) {
		KitAbility kit = getKit(type);
		selectKit(player, kit);
	}

	public void unregister(Player player) {
		Menu kitSelector = playerKitsMenus.get(player);
		if (kitSelector != null) {
			kitSelector.unregisterListener();
		}
		Menu shop = playerShops.get(player);
		if (shop != null) {
			shop.unregisterListener();
		}
	}

	public Map<Player, KitAbility> getPlayersKits() {
		return playersKits;
	}

	public void setPlayersKits(Map<Player, KitAbility> playersKits) {
		this.playersKits = playersKits;
	}

	public void setShopGui(Menu shopGui) {
		this.shopGui = shopGui;
	}

	public Menu getKitsGui() {
		return kitsGui;
	}

//	public void setKitsGui(Menu kitsGui) {
//		this.kitsGui = kitsGui;
//	}
	public List<ItemStack> getGlobalItems() {
		return globalItems;
	}

	public void setGlobalItems(List<ItemStack> globalItems) {
		this.globalItems = globalItems;
	}

	public ItemStack getSoup() {
		return soup;
	}

	public void setSoup(ItemStack soup) {
		this.soup = soup;
	}

	public ItemStack getEmptySlotItem() {
		return emptySlotItem;
	}

	public void setEmptySlotItem(ItemStack emptySlotItem) {
		this.emptySlotItem = emptySlotItem;
	}

	public ItemStack getHotBarItem() {
		return hotBarItem;
	}

	public void setHotBarItem(ItemStack hotBarItem) {
		this.hotBarItem = hotBarItem;
	}

	public boolean isKitsEnabled() {
		return kitsEnabled;
	}

	public void setKitsEnabled(boolean kitsEnabled) {
		this.kitsEnabled = kitsEnabled;
	}

	public double getDefaultKitPrice() {
		return defaultKitPrice;
	}

	public void setDefaultKitPrice(double defaultKitPrice) {
		this.defaultKitPrice = defaultKitPrice;
	}

	public boolean isGiveSoups() {
		return giveSoups;
	}

	public void setGiveSoups(boolean giveSoups) {
		this.giveSoups = giveSoups;
	}

	public boolean isFillHotBar() {
		return fillHotBar;
	}

	public void setFillHotBar(boolean fillHotBar) {
		this.fillHotBar = fillHotBar;
	}

	public boolean isOnSelectGainKit() {
		return onSelectGainKit;
	}

	public void setOnSelectGainKit(boolean onSelectGainKit) {
		this.onSelectGainKit = onSelectGainKit;
	}

	public String getNoneKit() {
		return noneKit;
	}

	public void setNoneKit(String noneKit) {
		this.noneKit = noneKit;
	}

	public String getKitsDisabled() {
		return kitsDisabled;
	}

	public void setKitsDisabled(String kitsDisabled) {
		this.kitsDisabled = kitsDisabled;
	}

	public String getKitSelected() {
		return kitSelected;
	}

	public void setKitSelected(String kitSelected) {
		this.kitSelected = kitSelected;
	}

	public String getKitGived() {
		return kitGived;
	}

	public void setKitGived(String kitGived) {
		this.kitGived = kitGived;
	}

	public String getKitBuyed() {
		return kitBuyed;
	}

	public void setKitBuyed(String kitBuyed) {
		this.kitBuyed = kitBuyed;
	}

	public String getNoKitBuyed() {
		return noKitBuyed;
	}

	public void setNoKitBuyed(String noKitBuyed) {
		this.noKitBuyed = noKitBuyed;
	}

	public String getGuiShopTitle() {
		return guiShopTitle;
	}

	public void setGuiShopTitle(String guiShopTitle) {
		this.guiShopTitle = guiShopTitle;
	}

	public void buyKit(Player player, KitAbility kit) {
		if (VaultAPI.hasVault()) {
			if (VaultAPI.getEconomy().has(player, kit.getPrice())) {
				VaultAPI.getEconomy().withdrawPlayer(player, kit.getPrice());
				VaultAPI.getPermission().playerAdd(player, kit.REQUIRE_PERMISSION);
				player.sendMessage(kitBuyed.replace("$kit", kit.getName()));
			} else {
				player.sendMessage(noKitBuyed.replace("$kit", kit.getName()));

			}
		}
	}

	@EventHandler
	public void abrir(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack item = event.getItem();
		if (item == null)
			return;
		if (event.getAction().name().contains("RIGHT")) {
			if (item.isSimilar(openKits.getItem())) {
				event.setCancelled(true);
				openKitSelector(p);
			} else if (item.isSimilar(openShop.getItem())) {
				event.setCancelled(true);
				openShop(p);
			}
		}

	}

	public KitAbility getKit(Player player) {
		if (hasKit(player)) {
			return playersKits.get(player);
		}
		KitAbility def = getKit(KitType.DEFAULT);
		playersKits.put(player, def);
		return def;
	}

	public boolean hasKit(Player player) {
		return playersKits.containsKey(player);
	}
}
