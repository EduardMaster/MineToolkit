package net.eduard.api.lib.manager;

import java.util.HashMap;
import java.util.Map;

import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.storage.annotations.StorageAttributes;
import net.eduard.api.server.currency.SimpleCurrencyHandler;
import org.bukkit.inventory.ItemStack;


public class CurrencyManager extends SimpleCurrencyHandler {

    private double inicialAmount;

    @StorageAttributes(inline = true)
    private Map<FakePlayer, Double> currency = new HashMap<>();

    public Map<FakePlayer, Double> getCurrency() {
        return currency;
    }

    public void setCurrency(Map<FakePlayer, Double> currency) {
        this.currency = currency;
    }


    public double getInicialAmount() {
        return inicialAmount;
    }


    public CurrencyManager(String name, String symbol, double inicialAmount) {
        super();
        setName(name);
        setSymbol(symbol);
        this.inicialAmount = inicialAmount;
    }

    public CurrencyManager() {

    }

    public void setBalance(FakePlayer player, double amount) {
        currency.put(player, amount);
    }

    public double getBalance(FakePlayer player) {

        return currency.getOrDefault(player, inicialAmount);
    }

    public boolean containsBalance(FakePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    public void addBalance(FakePlayer player, double amount) {
        setBalance(player, getBalance(player) + amount);
    }

    public void removeBalance(FakePlayer player, double amount) {
        setBalance(player, getBalance(player) - amount);
    }


    @Override
    public double get(FakePlayer player) {
        return getBalance(player);
    }

    @Override
    public ItemStack getIcon() {
        return null;
    }

    @Override
    public boolean check(FakePlayer player, double amount) {
        return containsBalance(player, amount);
    }

    @Override
    public boolean remove(FakePlayer player, double amount) {
        removeBalance(player, amount);
        return true;
    }

    @Override
    public boolean add(FakePlayer player, double amount) {
        addBalance(player, amount);
        return true;
    }


}
