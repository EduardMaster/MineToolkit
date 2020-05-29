package net.eduard.api.server.currency;


import net.eduard.api.lib.game.FakePlayer;
import net.eduard.api.lib.modules.Mine;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Currency {

    DEFAULT,
    EDU_CASH,PLAYER_POINTS,
    LEGIT_RANKUP_TOKEN
    ,
    JH_CASH

    ;
    private Currency(){}


/*
    DEFAULT("Dinheiro Padrão", Material.EMERALD, '$') {
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
    }, EDU_CASH("Cash (EduCash)", Material.DIAMOND, '✪') {
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
    }, PLAYER_POINTS("Cash (PlayerPoints)", Material.NETHER_STAR, '✪') {
        @Override
        public double get(FakePlayer player) {
            return 0;
        }

        @Override
        public boolean remove(FakePlayer player, double amount) {
            return false;
        }

        @Override
        public boolean check(FakePlayer player, double amount) {

            return false;
        }

        @Override
        public boolean add(FakePlayer player, double amount) {
            return false;
        }
    }, JH_CASH("Cash (JH)", Material.GOLD_INGOT, '✪') {
        @Override
        public double get(FakePlayer player) {

            return JH_Shop.Main.getAPI().getPontos(player.getName());
        }

        @Override
        public boolean remove(FakePlayer player, double amount) {
            JH_Shop.Main.getAPI().removePontos(player.getName(), amount);
            return true;
        }

        @Override
        public boolean check(FakePlayer player, double amount) {
            return JH_Shop.Main.getAPI().getPontos(player.getName()) >= amount;

        }

        @Override
        public boolean add(FakePlayer player, double amount) {
            JH_Shop.Main.getAPI().addPontos(player.getName(), amount);
            return true;
        }
    },
    LEGIT_RANKUP_TOKEN("Token (Rankup", Material.NETHER_STAR, '✪') {
        public double get(FakePlayer player) {

            return TokenSistema.getInstance().getTokensDouble(player.getName());
        }

        @Override
        public boolean check(FakePlayer player, double amount) {
            return TokenSistema.getInstance().hasTokens(player.getName(), amount);
        }

        @Override
        public boolean remove(FakePlayer player, double amount) {
            TokenSistema.getInstance().takeTokens(player.getName(), amount);
            return true;
        }

        @Override
        public boolean add(FakePlayer player, double amount) {
            TokenSistema.getInstance().addTokens(player.getName(), amount);
            return true;
        }
    };

 */
    private String name;
    private Material material;
    private char symbol;
    private boolean enabled = true;
    private Currency(String name, Material mat, char symbol) {
        setName(name);
        setSymbol(symbol);
        setMaterial(mat);

    }

    public  double get(FakePlayer player){
        return -1;
    }

    public ItemStack getIcon() {
        return Mine.newItem(material, "§a" + name);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean check(FakePlayer player, double amount){
        return false;
    }

    public boolean remove(FakePlayer player, double amount){
        return false;
    }

    public boolean add(FakePlayer player, double amount){
        return false;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
