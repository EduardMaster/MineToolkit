package net.eduard.api.server.currency.list;

import net.eduard.api.lib.game.FakePlayer;
import net.eduard.api.lib.game.ItemBuilder;
import net.eduard.api.lib.modules.VaultAPI;
import net.eduard.api.server.currency.CurrencyHandler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CurrencyVaultEconomy implements CurrencyHandler {

    @Override
    public String getName() {
        return "VaultEconomy";
    }

    @Override
    public double get(FakePlayer player) {
        if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {
            return VaultAPI.getEconomy().getBalance(player);
        }
        return -1;

    }

    @Override
    public ItemStack getIcon() {
        return new ItemBuilder(Material.EMERALD).name("§aDinheiro Padrão");
    }

    @Override
    public char getSymbol() {
        return '$';
    }

    @Override
    public boolean add(FakePlayer player, double amount) {
        if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {
            VaultAPI.getEconomy().depositPlayer(player, amount);
            return true;
        }
        return false;
    }

    @Override
    public boolean check(FakePlayer player, double amount) {
        if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {
            return VaultAPI.getEconomy().has(player, amount);
        }
        return false;
    }

    @Override
    public boolean remove(FakePlayer player, double amount) {
        if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {
            VaultAPI.getEconomy().withdrawPlayer(player, amount);
            return true;
        }
        return false;
    }
}
