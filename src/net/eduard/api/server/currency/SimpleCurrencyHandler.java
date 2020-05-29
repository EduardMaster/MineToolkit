package net.eduard.api.server.currency;

import org.bukkit.inventory.ItemStack;

public abstract class SimpleCurrencyHandler implements  CurrencyHandler{
    private String name;
    private ItemStack icon;
    private char symbol;

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

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
}
