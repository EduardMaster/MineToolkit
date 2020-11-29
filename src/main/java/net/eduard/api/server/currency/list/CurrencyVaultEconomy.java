package net.eduard.api.server.currency.list;

import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.game.ItemBuilder;
import net.eduard.api.lib.modules.VaultAPI;
import net.eduard.api.lib.storage.annotations.StorageAttributes;
import net.eduard.api.server.currency.SimpleCurrencyHandler;
import org.bukkit.Material;

@StorageAttributes(indentificate = true)
public class CurrencyVaultEconomy extends SimpleCurrencyHandler {

    public CurrencyVaultEconomy(){
            setName("VaultEconomy");
            setDisplayName("Dinheiro Padrão");
            setIcon( new ItemBuilder(Material.EMERALD).name("§aDinheiro Padrão"));
            setSymbol("$");
        }

        @Override
        public double get(FakePlayer player) {
            if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {
                return VaultAPI.getEconomy().getBalance(player);
            }
            return -1;

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
