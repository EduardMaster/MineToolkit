package net.eduard.api.server.currency;

import net.eduard.api.server.currency.list.CurrencyVaultEconomy;

import java.util.HashMap;
import java.util.Map;

public class CurrencyController {

    private CurrencyController(){}
    private static final CurrencyController INSTANCE = new CurrencyController();

    public static CurrencyController getInstance(){
        return INSTANCE;
    }

    private Map<String,CurrencyHandler> currencies = new HashMap<>();

    public void register(String currencyName, CurrencyHandler currencyHandler){
        currencies.put(currencyName.toLowerCase(),currencyHandler);
    }
    public boolean isRegister(String currencName){
        return currencies.containsKey(currencName.toLowerCase());
    }
    public CurrencyHandler getCurrencyHandler(String currencyName){
        return currencies.get(currencyName.toLowerCase());
    }
    static {
        getInstance().register("VaultEconomy",new CurrencyVaultEconomy());
    }
}
