package net.eduard.api.setup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

import net.eduard.api.setup.StorageAPI.Copyable;
import net.eduard.api.setup.StorageAPI.Storable;

public final class GuiAPI {
	public static interface SimpleClick {
		public void onClick(InventoryClickEvent event, int page);
	}
	public static class SimpleShopGui extends SimpleGui {

		private ItemStack[] products;
		private double[] prices;

		public SimpleShopGui(String name, int lines) {
			super(name, lines);
			setClick(new SimpleClick() {

				@Override
				public void onClick(InventoryClickEvent event, int page) {
					if (event.getWhoClicked() instanceof Player) {
						Player player = (Player) event.getWhoClicked();
						int slot = event.getRawSlot();
						ItemStack product = getProduct(page, slot);
						double price = getPrice(page, slot);
						if (product != null) {
							if (VaultAPI.hasVault()) {
								if (VaultAPI.hasEconomy()) {

									if (VaultAPI.getEconomy().has(player,
											price)) {
										VaultAPI.getEconomy()
												.withdrawPlayer(player, price);
										if (ItemAPI.isFull(
												player.getInventory())) {
											player.getWorld().dropItemNaturally(
													player.getLocation().add(0,
															5, 0),
													product);
										} else {
											player.getInventory()
													.addItem(product);
										}
										player.sendMessage(
												"§aVoce adquiriu um item da Loja!");
									} else {
										player.sendMessage(
												"§cVoce não tem dinheiro suficiente!");
									}

								}
							}
						}

					}
				}
			});

		}
		public void resetInventories() {
			super.resetInventories();
			prices = new double[getLines() * 9 * getPages()];
			products = new ItemStack[getLines() * 9 * getPages()];
		}
		/**
		 * 
		 * @param product
		 * @param page
		 * @param slot
		 * @param price
		 */
		public void addProduct(ItemStack product, int page, int slot,
				double price) {
			int index = getIndex(page, slot);
			List<String> lore = new ArrayList<String>();
			ItemStack clone = product.clone();
			if (clone.getItemMeta().hasLore())
				lore.addAll(clone.getItemMeta().getLore());
			lore.add("");
			lore.add("§aPreço: §2" + price);
			lore.add("");
			ItemAPI.setLore(clone, lore);
			addSlot(clone, page, slot);
			products[index] = product;
			prices[index] = price;
		}

		public void removeProduct(int page, int slot) {
			int index = getIndex(page, slot);
			products[index] = null;
			prices[index] = 0;
			removeSlot(page, slot);

		}

		public ItemStack getProduct(int page, int slot) {
			int index = getIndex(page, slot);
			return products[index];
		}

		public double getPrice(int page, int slot) {
			int index = getIndex(page, slot);
			return prices[index];
		}

	}

	public static class SimpleGui implements Listener, Storable, Copyable {

		private transient SimpleClick[] clicks;
		private transient SimpleClick click = new SimpleClick() {

			@Override
			public void onClick(InventoryClickEvent event, int page) {

			}
		};private transient Inventory[] inventories;
	
		private String name;
		private int lines = 6;
		private int pages = 10;
		private String pagPrefix = " §8Página: ";
		private String command = "abrirGuiExemplo";
		private ItemStack item = new ItemStack(Material.COMPASS);
		private boolean hasPageSystem = true;
		
		
		private int nextPageSlot = 8;
		private int previousPageSlot = 0;
		private ItemStack nextPage = ItemAPI.newItem(Material.ARROW,
				"§aPróxima Página");
		
		public SimpleGui copy() {
			return copy(this);
		}	public void unregister() {
			HandlerList.unregisterAll(this);
		}
		public void register(Plugin plugin) {
			unregister();
			Bukkit.getPluginManager().registerEvents(this, plugin);
		}
		private ItemStack previosPage = ItemAPI.newItem(Material.ARROW,
				"§aPágina Anterior");
		
		private ItemStack[] slots;
		public SimpleGui() {
			resetInventories();
		}
		public SimpleGui(String name) {
			this(name, 6);
		}
		public SimpleGui(String name, int lines) {
			this.name = name;
			this.lines = lines;
			resetInventories();
		}
		public void resetInventories() {
			slots = new ItemStack[lines * 9 * pages];
			clicks = new SimpleClick[lines * 9 * pages];
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
				ItemStack[] array = Arrays.copyOfRange(slots, getIndex(page, 0),
						getIndex(page + 1, 0));
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
			return ItemAPI.isFull(inventories[page - 1]);
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
		public void addSlot(ItemStack item, int page, int slot) {
			addSlot(item, page, slot, null);
		}
		public void addSlot(ItemStack item, int page, int slot,
				SimpleClick click) {
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

		public SimpleClick getClick(int page, int slot) {
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
				inventory = Bukkit.createInventory(null, lines * 9,
						name + pagPrefix + page);
				inventory.setContents(Arrays.copyOfRange(slots,
						getIndex(page, 0), getIndex(page + 1, 0)));
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
			if (command == null)
				return;
			if (ExtraAPI.commandStartWith(message, command)) {
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
						page = Integer.valueOf(event.getInventory().getTitle()
								.split(name + pagPrefix)[1]);
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
					SimpleClick click = getClick(page, slot);
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

		public SimpleClick[] getClicks() {
			return clicks;
		}

		public void setClicks(SimpleClick[] clicks) {
			this.clicks = clicks;
		}

		public SimpleClick getClick() {
			return click;
		}

		public void setClick(SimpleClick click) {
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

}
