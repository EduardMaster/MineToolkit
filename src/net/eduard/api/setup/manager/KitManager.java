package net.eduard.api.setup.manager;

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

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.VaultAPI;
import net.eduard.api.setup.game.Ability;
import net.eduard.api.setup.game.ClickEffect;
import net.eduard.api.setup.game.KitType;
import net.eduard.api.setup.game.Menu;
import net.eduard.api.setup.game.Slot;


public class KitManager extends EventsManager {
	
	private transient Map<Player, Ability> playersKits = new HashMap<>();
	private transient Map<Player, Menu> kitsGuis = new HashMap<>();
	private transient Map<Player, Menu> shopsGuis = new HashMap<>();
	private Slot openKits = new Slot(
			Mine.newItem(Material.CHEST, "§6§lSelecionar Kit"), 0);
	private Slot openShop = new Slot(
			Mine.newItem(Material.EMERALD, "§6§lComprar Kit"), 8);
	private Menu shopGui = new Menu("§8Loja de Kits", 6);
	private Menu kitsGui = new Menu("§8Seus  Kits", 6);;
	private Map<String, Ability> kits = new HashMap<>();
	private List<ItemStack> globalItems = new ArrayList<>();
	private ItemStack soup = Mine.newItem("§6Sopa", Material.MUSHROOM_SOUP);
	private ItemStack emptySlotItem = Mine.newItem(" ",
			Material.STAINED_GLASS_PANE, 15);
	private ItemStack hotBarItem = Mine.newItem("§6§lKit§f§lPvP",
			Material.STAINED_GLASS_PANE, 10);

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
	private String guiShopTitle = "§cKit §4§l$kit §cseu preço: §a§l$price";

	public KitManager() {
		globalItems.add(new ItemStack(Material.STONE_SWORD));
		kitsGui.setItem(null);
		shopGui.setItem(null);
		kitsGui.setHasPageSystem(false);
		shopGui.setHasPageSystem(false);
		kitsGui.setClick(new ClickEffect() {

			@Override
			public void onClick(InventoryClickEvent event, int page) {
				if (event.getWhoClicked() instanceof Player) {
					Player player = (Player) event.getWhoClicked();
					ItemStack item = event.getCurrentItem();
					if (kitsGui.getNextPage().equals(item)) {
						if (page == kitsGui.getPages())
							return;
						openKitSelector(player, ++page);
					} else if (kitsGui.getPreviosPage().equals(item)) {
						if (page == 1)
							return;
						openKitSelector(player, --page);
					} else {
						for (Ability kit : kits.values()) {
							if (kit.getIcon().equals(item)) {
								selectKit(player, kit);
								break;
							}
						}
					}
				}
			}
		});
		shopGui.setClick(new ClickEffect() {

			@Override
			public void onClick(InventoryClickEvent event, int page) {
				if (event.getWhoClicked() instanceof Player) {
					Player player = (Player) event.getWhoClicked();

					ItemStack item = event.getCurrentItem();

					if (kitsGui.getNextPage().equals(item)) {
						openKitSelector(player, ++page);
					} else if (kitsGui.getPreviosPage().equals(item)) {
						openKitSelector(player, --page);
					} else {
						for (Ability kit : kits.values()) {
							if (kit.getIcon().equals(item)) {
								buyKit(player, kit);
								break;
							}
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
		shopsGuis.get(player).openGui(player, page);
	}

	public void openKitSelector(Player player) {
		openKitSelector(player, 1);
	}
	public void openKitSelector(Player player, int page) {
		kitsGuis.get(player).openGui(player, page);
	}
	public void giveItems(Player player) {
		PlayerInventory inv = player.getInventory();
		openShop.give(inv);
		openKits.give(inv);
		if (fillHotBar) {
			Mine.addHotBar(player, hotBarItem);
		}

	}
	public Ability getKit(KitType type) {
		return kits.get(type.name());
	}
	public void gainKit(Player player) {
		if (!kitsEnabled)
			return;
		Ability kit = playersKits.get(player);
		removeKits(player);
		Mine.refreshAll(player);
		PlayerInventory inv = player.getInventory();
		for (ItemStack item : kit.getItems()) {
			inv.addItem(item);
		}
		for (String sub : kit.getKits()) {
			Ability subKit = kits.get(sub);
			for (ItemStack item : subKit.getItems()) {
				inv.addItem(item);
			}
			subKit.add(player);
		}
		kit.add(player);
		for (ItemStack item : globalItems) {
			inv.addItem(item);;
		}
		if (giveSoups) {
			Mine.fill(inv, soup);
			Mine.setEquip(player, Color.GREEN, "§4§lINSANE");
		}
		player.sendMessage(kitGived.replace("$kit", kit.getName()));
	}

	public void register(Player player) {

		Menu select = kitsGui.copy();
		Menu shop = shopGui.copy();
		kitsGuis.put(player, select);
		shopsGuis.put(player, shop);
		int kitPage = 0, shopPage = 0;
		int kitIndex = -1, shopIndex = -1;
		for (Ability kit : kits.values()) {

			while (kitIndex == -1) {
				kitPage++;
				kitIndex = select.getFirstEmptySlot(kitPage);
			}
			while (shopIndex == -1) {
				shopPage++;
				shopIndex = shopGui.getFirstEmptySlot(shopPage);
			}
			if (kit.hasPermission(player)) {

				select.addSlot(kit.getIcon(), kitPage, kitIndex);
				kitIndex = select.getFirstEmptySlot(kitPage);
			} else {
				shopGui.addSlot(kit.getIcon(), shopPage, shopIndex);
				shopIndex = shopGui.getFirstEmptySlot(shopPage);
			}

		}
	}
	public boolean register(String name, Ability kit) {
		if (kits.containsKey(name))
			return false;
		kits.put(name, kit);
		return true;
	}
	public void register(Plugin plugin) {
		for (Ability kit : kits.values()) {
			kit.register(plugin);
			if (kit.getClick() != null) {
				kit.getClick().register(plugin);
			}
			if (kit.getPrice() == 0) {
				kit.setPrice(defaultKitPrice);
			}
		}

		super.register(plugin);
		Mine.event(kitsGui, getPlugin());
		Mine.event(shopGui, getPlugin());
	}

	public void reloadDefaults() {
		registerKits(KitType.values());
	}

	public void removeKits(Player player) {
		playersKits.remove(player);
		for (Ability kit : kits.values()) {
			kit.getPlayers().remove(player);
		}
	}

	public void selectKit(Player player, Ability kit) {
		player.closeInventory();
		playersKits.put(player, kit);
		player.sendMessage(kitSelected.replace("$kit", kit.getName()));
		if (onSelectGainKit) {
			gainKit(player);
		}

	}
	public void selectKit(Player player, KitType type) {
		if (kits.containsKey(type.name())) {
			Ability kit = kits.get(type.name());
			selectKit(player, kit);
		}
	}
	public void registerKits(KitType... kits) {
		for (KitType kit : kits) {
			if (kit.getAbility() != null)
				this.kits.put(kit.name(), kit.getAbility());
		}
	}
	public void unregister(Player player) {
		kitsGuis.remove(player);
		shopsGuis.remove(player);
	}

	public Map<Player, Ability> getPlayersKits() {
		return playersKits;
	}
	public void setPlayersKits(Map<Player, Ability> playersKits) {
		this.playersKits = playersKits;
	}
	public Map<Player, Menu> getKitsGuis() {
		return kitsGuis;
	}
	public void setKitsGuis(Map<Player, Menu> kitsGuis) {
		this.kitsGuis = kitsGuis;
	}
	public Map<Player, Menu> getShopsGuis() {
		return shopsGuis;
	}
	public void setShopsGuis(Map<Player, Menu> shopsGuis) {
		this.shopsGuis = shopsGuis;
	}
	public Slot getOpenKits() {
		return openKits;
	}
	public void setOpenKits(Slot openKits) {
		this.openKits = openKits;
	}
	public Slot getOpenShop() {
		return openShop;
	}
	public void setOpenShop(Slot openShop) {
		this.openShop = openShop;
	}
	public Menu getShopGui() {
		return shopGui;
	}
	public void setShopGui(Menu shopGui) {
		this.shopGui = shopGui;
	}
	public Menu getKitsGui() {
		return kitsGui;
	}
	public void setKitsGui(Menu kitsGui) {
		this.kitsGui = kitsGui;
	}
	public Map<String, Ability> getKits() {
		return kits;
	}
	public void setKits(Map<String, Ability> kits) {
		this.kits = kits;
	}
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
	public void buyKit(Player player, Ability kit) {
		if (VaultAPI.hasVault()) {
			if (VaultAPI.getEconomy().has(player, kit.getPrice())) {
				VaultAPI.getEconomy().withdrawPlayer(player, kit.getPrice());
				VaultAPI.getPermission().playerAdd(player, kit.getPermission());
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
	public Ability getKit(Player player) {
		if (hasKit(player)) {
			return playersKits.get(player);
		}
		Ability def = getKit(KitType.DEFAULT);
		playersKits.put(player, def);
		return def;
	}
	public boolean hasKit(Player player) {
		return playersKits.containsKey(player);
	}
}
