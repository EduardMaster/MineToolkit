package net.eduard.api.server.currency;

import net.eduard.api.lib.storage.Storable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class SimpleCurrencyHandler implements  CurrencyHandler {
    private String name;
    private ItemStack icon;
    private String symbol;
    private String displayName;

    public SimpleCurrencyHandler(){
        setName("MoedaCustom");
        setIcon(new ItemStack(Material.DIAMOND_BLOCK));
        setSymbol("$");
        setDisplayName("Moeda customizada");
    }
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }


    @Override
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
