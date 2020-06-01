package net.eduard.api.server.currency;

import net.eduard.api.EduardAPI;
import net.eduard.api.server.currency.list.CurrencyVaultEconomy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyController {

    private CurrencyController(){}
    private static final CurrencyController INSTANCE = new CurrencyController();

    public static CurrencyController getInstance(){
        return INSTANCE;
    }

    private Map<String,CurrencyHandler> currencies = new HashMap<>();

    public List<CurrencyHandler> getCurrencies(){
        return new ArrayList<>(currencies.values());
    }

    public void register(CurrencyHandler currencyHandler){
        currencies.put(currencyHandler.getName().toLowerCase() , currencyHandler);
    }
    public void register(SimpleCurrencyHandler simpleCurrencyHandler){
        EduardAPI.getInstance().getConfigs().add("currency."+simpleCurrencyHandler.getName(),simpleCurrencyHandler);
        simpleCurrencyHandler = (SimpleCurrencyHandler) EduardAPI.getInstance().getConfigs().get("currency."+simpleCurrencyHandler.getName());
        EduardAPI.getInstance().getConfigs().saveConfig();
        register(simpleCurrencyHandler.getName(),simpleCurrencyHandler);
    }

    public void register(String currencyName, CurrencyHandler currencyHandler){
        currencies.put(currencyName.toLowerCase(),currencyHandler);
    }
    public boolean isRegistred(String currencName){
        return currencies.containsKey(currencName.toLowerCase());
    }
    public CurrencyHandler getCurrencyHandler(String currencyName){
        return currencies.get(currencyName.toLowerCase());
    }

}
