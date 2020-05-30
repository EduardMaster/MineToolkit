package net.eduard.api.server.currency;

import net.eduard.api.lib.game.FakePlayer;
import org.bukkit.inventory.ItemStack;

public interface CurrencyHandler {

    String getName();

    double get(FakePlayer player);

    ItemStack getIcon();

    String getSymbol();

    boolean check(FakePlayer player, double amount);

    default boolean contains(FakePlayer player, double amount) {
        return check(player, amount);
    }


    boolean remove(FakePlayer player, double amount);

    boolean add(FakePlayer player, double amount);

}