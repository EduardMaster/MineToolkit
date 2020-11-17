package net.eduard.api.server.currency;

import net.eduard.api.lib.modules.FakePlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface CurrencyHandler {

    String getName();

    String getDisplayName();

    double get(FakePlayer player);

    ItemStack getIcon();

    String getSymbol();

    default Material getMaterial(){
        return getIcon().getType();
    }

    boolean check(FakePlayer player, double amount);

    default boolean contains(FakePlayer player, double amount) {
        return check(player, amount);
    }

    boolean remove(FakePlayer player, double amount);

    boolean add(FakePlayer player, double amount);

}