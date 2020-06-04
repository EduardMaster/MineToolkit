package net.eduard.api.server.currency.list;

import net.eduard.api.lib.game.FakePlayer;
import net.eduard.api.server.Systems;
import net.eduard.api.server.currency.SimpleCurrencyHandler;

public class CurrencyEduCash extends SimpleCurrencyHandler {

    public CurrencyEduCash(){
        setName("EduCash");
        setDisplayName("Sistema de Cash");

    }

    @Override
    public double get(FakePlayer player) {
        return Systems.getCashSystem().getCash(player);
    }


    @Override
    public boolean remove(FakePlayer player, double amount) {

        Systems.getCashSystem().removeCash(player, amount);

        return true;
    }

    @Override
    public boolean check(FakePlayer player, double amount) {

        return Systems.getCashSystem().hasCash(player, amount);

    }

    @Override
    public boolean add(FakePlayer player, double amount) {

        Systems.getCashSystem().addCash(player, amount);
        return true;

    }
}
