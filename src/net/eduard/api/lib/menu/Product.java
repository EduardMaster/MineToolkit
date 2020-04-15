package net.eduard.api.lib.menu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.eduard.api.lib.modules.Extra;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.modules.Mine;

public class Product extends MenuButton {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private double sellPrice;
    private double buyPrice;
    private boolean limited = false;
    private double stock;
    private TradeType tradeType = TradeType.BUYABLE;
    private String permission;
    private List<String> commands = new ArrayList<>();
    private ItemStack product;



    public Shop getParentShop(){
        return (Shop) getParentMenu();
    }


    public int getAmount() {
        if (product!=null)
        return product.getAmount();
        return 1;
    }

    public double getUnitSellPrice() {
        return sellPrice / getAmount();
    }

    public double getUnitBuyPrice() {
        return buyPrice / getAmount();
    }

    public ItemStack getItem() {


//		System.out.println(getProduct());
        ItemStack clone = super.getItem();
        if (clone == null) {
            clone = getProduct();
        }
        clone = clone.clone();
        if (limited) {
            clone.setAmount(64);
        }
        List<String> lore = Mine.getLore(clone);
        if (getName() == null) {

            setName("Produto");
        }
        Shop parentShop = getParentShop();
        if (parentShop != null) {
            List<String > template = null;
            if (tradeType == TradeType.BUYABLE){
                template = parentShop.getBuyTemplate();
            }
            if (tradeType == TradeType.SELABLE){
                template = parentShop.getSellTemplate();
            }
            if (tradeType == TradeType.BOTH){
                template = parentShop.getSellBuyTemplate();
            }
            for (String line :template ) {
                lore.add(line.replace("$product_name", getName()).replace("$product_stock", "" + getStock()).replace("$product_buy_unit_price", Extra.formatMoney(getUnitBuyPrice())).replace("$product_buy_pack_price", Extra.formatMoney(getUnitBuyPrice() * 64)).replace("$product_sell_unit_price", Extra.formatMoney(getUnitSellPrice())).replace("$product_sell_pack_price", Extra.formatMoney(getUnitSellPrice() * 64)).replace("$product_sell_inventory_price", Extra.formatMoney(getUnitSellPrice() * 64 * 4 * 9)));
            }


        }
        Mine.setLore(clone, lore);
        return clone;
    }
    public void setItem(ItemStack item) {
        setProduct(item);

    }

    public Product() {
        setName("Produto");
        // TODO Auto-generated constructor stub
    }


    public Product(Shop shop) {
        super(shop);
        setName("Produto");
    }

    public Product(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }


    public Product(ItemStack icon) {
        super(icon);
        setName("Produto");
    }
    public Product(String name, ItemStack icon) {
        super(name, icon);
        // TODO Auto-generated constructor stub
    }

    public ItemStack getProduct() {
        return product;
    }

    public void setProduct(ItemStack product) {
        this.product = product;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public boolean isLimited() {
        return limited;
    }

    public void setLimited(boolean limited) {
        this.limited = limited;
    }



}
