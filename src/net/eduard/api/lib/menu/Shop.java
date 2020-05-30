package net.eduard.api.lib.menu;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import net.eduard.api.lib.game.FakePlayer;
import net.eduard.api.lib.modules.*;
import net.eduard.api.server.currency.CurrencyController;
import net.eduard.api.server.currency.CurrencyHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.manager.CurrencyManager;
import net.eduard.api.lib.storage.Storable.*;

@StorageAttributes(indentificate = true)
public class Shop extends Menu {


    public static String MESSAGE_CHOICE_AMOUNT = "§aEscolha uma quantidade para $trader este produto $product";
    public static String MESSAGE_BOUGHT_ITEM = "§aVoce adquiriu $amount ($product) produto(s) da Loja!";
    public static String MESSAGE_SOLD_ITEM = "§aVocê vendeu $amount ($product) produtos(s) para a loja!";
    public static String MESSAGE_WITHOUT_ITEMS = "§cVoce não tem items suficiente!";
    public static String MESSAGE_WITHOUT_BALANCE = "§cVoce não tem dinheiro suficiente!";
    public static String MESSAGE_WITHOUT_PERMISSION = "§cVoce não tem permissão para comprar este produto!";
    public static List<String> TEMPLATE_BUY = Arrays.asList("§fCompre o produto §e$product_name",
            "§2Quantidade: §a$product_stock", "§2Preço por 1: §a$product_buy_unit_price", "§2Preço por 64: §a$product_buy_pack_price");

    public static List<String> TEMPLATE_SELL = Arrays.asList("§fVende o produto: §e$product_name",
            "§2Quantidade: §a$product_stock", "§2Preço por 64: §a$product_sell_pack_price", "§2Preço por Inventario: §a$product_sell_inventory_price");
    public static List<String> TEMPLATE_BUY_SELL = Arrays.asList("§fCompra e venda de: §e$product_name",
            "§2Quantidade: §a$product_stock", "§2Preço por 64: §a$product_sell_pack_price", "§2Preço por Inventario: §a$product_sell_inventory_price", "", "§2Preço por 1: §a$product_buy_unit_price", "§2Preço por 64: §a$product_buy_pack_price");


    private String currencyType = "VaultEconomy";

    transient private CurrencyHandler currency;
    private static final int PLAYER_INVENTORY_LIMIT = 4 * 64 * 9;
    private ShopSortType sortType = ShopSortType.BUY_PRICE_ASC;
    private transient Map<Player, Product> selectingAmount = new HashMap<>();
    private transient Map<Player, TradeType> trading = new HashMap<>();
    private boolean amountPerChat;
    private List<String> buyTemplate = new ArrayList<>(TEMPLATE_BUY);
    private List<String> sellTemplate = new ArrayList<>(TEMPLATE_SELL);
    private List<String> sellBuyTemplate = new ArrayList<>(TEMPLATE_BUY_SELL);

    private String messageChoiceAmount = MESSAGE_CHOICE_AMOUNT;
    private String messageBoughtItem = MESSAGE_BOUGHT_ITEM;
    private String messageSoldItem = MESSAGE_SOLD_ITEM;
    private String messageWithoutItems = MESSAGE_WITHOUT_ITEMS;
    private String messageWithoutBalance = MESSAGE_WITHOUT_BALANCE;
    private String messageWithoutPermission = MESSAGE_WITHOUT_PERMISSION;


    public Shop() {
        this("Menu", 3);
    }

    public Shop copy() {
        return copy(this);
    }

    public void organize() {

        if (sortType == ShopSortType.BUY_PRICE_ASC) {
            Stream<Product> lista = getButtons().stream().filter(b -> b instanceof Product).map(b -> (Product) b)
                    .sorted(Comparator.comparing(Product::getUnitBuyPrice));
            lista.forEach(new Consumer<Product>() {
                int id = 0;

                public void accept(Product t) {
                    t.setIndex(id);
                    id++;
                }
            });
        }

    }

    @EventHandler
    public void chat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (selectingAmount.containsKey(p)) {
            Product product = selectingAmount.get(p);
            Double amount = Extra.fromMoneyToDouble(e.getMessage());
            amount = Math.abs(amount);
            selectingAmount.remove(p);
            TradeType trade = trading.get(p);
            trading.remove(p);
            if (trade == TradeType.BUYABLE) {
                buy(p, product, amount);
            } else if (trade == TradeType.SELABLE) {
                sell(p, product, amount);
            }
            e.setCancelled(true);

        }
    }

    public Shop(String title, int lineAmount) {
        super(title, lineAmount);
        setEffect((event, page) -> {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                if (event.getCurrentItem() == null)
                    return;

                Product product = getProduct(event.getCurrentItem());
                if (product == null) {
                    return;
                }

                if ((event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT)
                        && (product.getTradeType() == TradeType.BOTH
                        || product.getTradeType() == TradeType.BUYABLE)) {


                    if (isAmountPerChat()) {
                        selectingAmount.put(player, product);
                        trading.put(player, TradeType.BUYABLE);
                        player.closeInventory();
                        player.sendMessage(messageChoiceAmount.replace("$product",
                                "" + product.getName()).replace("$trader", "comprar"));
                        return;
                    }

                    int amount = 1;
                    if (event.getClick() == ClickType.SHIFT_RIGHT) {
                        amount = product.getAmount();
                        if (amount < 64) {
                            amount = 64;
                        }

                    }
                    buy(player, product, amount);


                }

                if ((event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT)
                        && (product.getTradeType() == TradeType.BOTH
                        || product.getTradeType() == TradeType.SELABLE)) {
                    if (product.getProduct() != null) {
                        if (isAmountPerChat()) {
                            selectingAmount.put(player, product);
                            trading.put(player, TradeType.SELABLE);
                            player.closeInventory();
                            player.sendMessage(messageChoiceAmount.replace("$product",
                                    "" + product.getName()).replace("$trader", "vender"));
                            return;
                        }

                        int amount = Mine.getTotalAmount(player.getInventory(), product.getProduct());
                        if (event.getClick() == ClickType.LEFT) {
                            if (amount > 64) {
                                amount = 64;
                            }
                        }
                        sell(player, product, amount);

                    }

                }

            }

        });

        // TODO Auto-generated constructor stub
    }


    public void buy(Player player, Product product, double amount) {
        FakePlayer fake = new FakePlayer(player);

        double priceUnit = product.getUnitBuyPrice();
        if (product.isLimited() && amount > product.getStock()) {
            amount = product.getStock();
        }
        double priceFinal = priceUnit * amount;
        ProductTradeEvent evento = new ProductTradeEvent(player);
        evento.setProduct(product);
        evento.setAmount(amount);
        if (product.isLimited()) {
            evento.setNewStock(product.getStock() - amount);
        } else {
            evento.setNewStock(product.getStock());
        }
        ;


        evento.setBalance(getCurrency().get(fake));


        evento.setType(TradeType.BUYABLE);
        evento.setPriceTotal(priceFinal);
        evento.setShop(Shop.this);

        Mine.callEvent(evento);
        if (evento.isCancelled()) {
            return;
        }

        if (getCurrency().get(fake) >= evento.getPriceTotal()) {
            getCurrency().remove(fake, evento.getPriceTotal());
        } else {
            player.sendMessage(messageWithoutBalance);
            return;
        }

        product.setStock(evento.getNewStock());

        for (String cmd : product.getCommands()) {
            Mine.runCommand(cmd.replace("$player", player.getName()).replace("$formated_amount", Extra.formatMoney(amount)).replace("$amount", "" + amount));
        }
        player.sendMessage(messageBoughtItem.replace("$amount", Extra.formatMoney(amount)).replace("$product",
                "" + product.getName()));
        if (product.getProduct() == null) {
            return;
        }

        ItemStack clone = product.getProduct().clone();
        if (evento.getAmount() > PLAYER_INVENTORY_LIMIT) {
            clone = MineReflect.toStack(clone, evento.getAmount());


        } else {
            clone.setAmount((int) evento.getAmount());
        }

        if (Mine.isFull(player.getInventory())) {
            Inventory inv = Mine.newInventory("Pegue seus items comprados", 6 * 9);
            inv.addItem(clone);
            player.openInventory(inv);
            player.sendMessage("§cPegue seus items comprados e coloca no seu inventário.");

            //player.getWorld().dropItemNaturally(player.getLocation().add(0, 5, 0),
            //      clone);
        } else {
            player.getInventory().addItem(clone);
        }
    }

    public void sell(Player player, Product product, double amount) {
        FakePlayer fake = new FakePlayer(player);
        double priceUnit = product.getUnitSellPrice();


        if (amount > product.getStock()) {
            amount = product.getStock();
        }
        if (amount < 1) {
            player.sendMessage(messageWithoutItems);
            return;
        }
        double finalPrice = amount * priceUnit;
        ProductTradeEvent evento = new ProductTradeEvent(player);
        evento.setProduct(product);
        evento.setAmount(amount);
        evento.setNewStock(product.getStock() - amount);

        evento.setBalance(getCurrency().get(fake));


        evento.setType(TradeType.SELABLE);
        evento.setPriceTotal(finalPrice);
        evento.setShop(Shop.this);
//
        Mine.callEvent(evento);
        if (evento.isCancelled()) {
            return;
        }
        if (evento.getAmount() > PLAYER_INVENTORY_LIMIT) {
            evento.setAmount(PLAYER_INVENTORY_LIMIT);
        }
        Mine.remove(player.getInventory(), product.getProduct(), (int) evento.getAmount());
        player.sendMessage(messageSoldItem.replace("$amount", "" + amount).replace("$product",
                "" + product.getName()));

        getCurrency().add(fake, finalPrice);


    }

    public CurrencyHandler getCurrency() {

        if (currency == null) {
            currency = CurrencyController.getInstance().getCurrencyHandler(currencyType);
        }

        return currency;
    }

    public String getMessageBoughtItem() {
        return messageBoughtItem;
    }

    public String getMessageWithoutBalance() {
        return messageWithoutBalance;
    }

    public Product getProduct(ItemStack icon) {
        MenuButton button = getButton(icon);
        if (button != null) {
            if (button instanceof Product) {
                Product product = (Product) button;
                return product;
            }
        }
        return null;
    }

    public Product getProductFrom(ItemStack item) {
        for (MenuButton button : getButtons()) {
            if (button instanceof Product) {
                Product product = (Product) button;
                if (product.getItem().isSimilar(item))
                    return product;
            }

        }
        return null;
    }

    public void setCurrency(CurrencyManager currency) {
        if (currency == null)
            return;

        this.currency = currency;
        for (MenuButton button : getButtons()) {
            if (button.getMenu() instanceof Shop) {
                Shop shop = (Shop) button.getMenu();
                shop.setCurrency(currency);

            }

        }
    }

    public void setMessageBoughtItem(String messageBoughtItem) {
        this.messageBoughtItem = messageBoughtItem;
    }

    public void setMessageWithoutBalance(String messageWithoutBalance) {
        this.messageWithoutBalance = messageWithoutBalance;
    }


    public String getMessageWithoutPermission() {
        return messageWithoutPermission;
    }

    public void setMessageWithoutPermission(String messageWithoutPermission) {
        this.messageWithoutPermission = messageWithoutPermission;
    }

    public ShopSortType getSortType() {
        return sortType;
    }

    public void setSortType(ShopSortType sortType) {
        this.sortType = sortType;
    }

    public String getMessageSoldItem() {
        return messageSoldItem;
    }

    public void setMessageSoldItem(String messageSoldItem) {
        this.messageSoldItem = messageSoldItem;
    }

    public String getMessageWithoutItems() {
        return messageWithoutItems;
    }

    public void setMessageWithoutItems(String messageWithoutItems) {
        this.messageWithoutItems = messageWithoutItems;
    }

    public boolean isAmountPerChat() {
        return amountPerChat;
    }

    public void setAmountPerChat(boolean amountPerChat) {
        this.amountPerChat = amountPerChat;
    }

    public String getMessageChoiceAmount() {
        return messageChoiceAmount;
    }

    public void setMessageChoiceAmount(String messageChoiceAmount) {
        this.messageChoiceAmount = messageChoiceAmount;
    }

    public Map<Player, TradeType> getTrading() {
        return trading;
    }

    public void setTrading(Map<Player, TradeType> trading) {
        this.trading = trading;
    }

    public List<String> getBuyTemplate() {
        return buyTemplate;
    }

    public void setBuyTemplate(List<String> buyTemplate) {
        this.buyTemplate = buyTemplate;
    }

    public List<String> getSellTemplate() {
        return sellTemplate;
    }

    public void setSellTemplate(List<String> sellTemplate) {
        this.sellTemplate = sellTemplate;
    }

    public List<String> getSellBuyTemplate() {
        return sellBuyTemplate;
    }

    public void setSellBuyTemplate(List<String> sellBuyTemplate) {
        this.sellBuyTemplate = sellBuyTemplate;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
}
