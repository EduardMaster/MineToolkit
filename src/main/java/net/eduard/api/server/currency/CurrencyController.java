package net.eduard.api.server.currency;

import net.eduard.api.EduardAPI;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CurrencyController {

    private CurrencyController() {

    }
    private final Map<String, CurrencyHandler> currencies = new LinkedHashMap<>();


    private static final CurrencyController INSTANCE = new CurrencyController();

    public static CurrencyController getInstance() {
        return INSTANCE;
    }

    public CurrencyHandler getNextCurrency(CurrencyHandler currencyHandler) {
        Iterator<CurrencyHandler> it = currencies.values().iterator();
        while (it.hasNext()) {
            CurrencyHandler handler = it.next();
            if (currencyHandler.equals(handler)) {
                if (it.hasNext()) {
                    return it.next();
                }
                return currencies.values().iterator().next();
            }
        }

        return null;
    }

    public CurrencyHandler getCurrencyByIcon(ItemStack icon) {
        for (CurrencyHandler currencyHandler : currencies.values()) {
            if (currencyHandler.getIcon().equals(icon)) {
                return currencyHandler;
            }
        }

        return null;
    }



    public void register(SimpleCurrencyHandler simpleCurrencyHandler) {
        EduardAPI.instance.getConfigs().add("currency." + simpleCurrencyHandler.getName(), simpleCurrencyHandler);
        EduardAPI.instance.getConfigs().saveConfig();
        simpleCurrencyHandler = EduardAPI.instance.getConfigs().get("currency." + simpleCurrencyHandler.getName(), SimpleCurrencyHandler.class);
        System.out.println("Moeda registrada: " + simpleCurrencyHandler.getName());
        register(simpleCurrencyHandler.getName(), simpleCurrencyHandler);
    }

    public void register(String currencyName, CurrencyHandler currencyHandler) {
        currencies.put(currencyName.toLowerCase(), currencyHandler);
    }

    public boolean isRegistred(String currencName) {
        return currencies.containsKey(currencName.toLowerCase());
    }

    public CurrencyHandler getCurrencyHandler(String currencyName) {
        return currencies.get(currencyName.toLowerCase());
    }

}
