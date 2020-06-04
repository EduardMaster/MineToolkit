package net.eduard.api.server.currency.list;

import me.yblackdev.legitpvp.sistema.token.TokenSistema;
import net.eduard.api.lib.game.FakePlayer;
import net.eduard.api.lib.game.ItemBuilder;
import net.eduard.api.server.currency.SimpleCurrencyHandler;
import org.bukkit.Material;

public class CurrencyNetworkStoryRankupToken extends SimpleCurrencyHandler {


    public CurrencyNetworkStoryRankupToken(){
        setName("NetworkStoryRankupToten");
        setDisplayName("Sistema de Token");
        setIcon( new ItemBuilder(Material.NETHER_STAR).name("§aToken"));
        setSymbol("$");
    }


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


}
