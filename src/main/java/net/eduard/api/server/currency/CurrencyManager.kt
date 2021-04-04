package net.eduard.api.server.currency

import net.eduard.api.EduardAPI
import net.eduard.api.lib.hybrid.Hybrid
import net.eduard.api.lib.storage.annotations.StorageAttributes
import net.eduard.api.lib.modules.FakePlayer
import net.eduard.api.server.CurrencySystem
import org.bukkit.inventory.ItemStack

open class CurrencyManager : SimpleCurrencySystem() {

    companion object {
        private val currencies = mutableMapOf<String, CurrencySystem>()
        private val currenciesByPosition = mutableMapOf<Int, CurrencySystem>()

        fun getNextCurrency(currency: CurrencySystem): CurrencySystem? {
            return currenciesByPosition[currency.position + 1]
        }

        fun getPreviousCurrency(currency: CurrencySystem): CurrencySystem? {
            return currenciesByPosition[currency.position - 1]
        }

        fun getCurrencyByIcon(icon: ItemStack): CurrencySystem? {
            return currencies.values.firstOrNull { it.icon == icon }
        }

        fun register(currency: SimpleCurrencySystem) {
            var simpleCurrency = currency
            EduardAPI.instance.configs.add(
                "currency." +
                        simpleCurrency.name, simpleCurrency
            )
            EduardAPI.instance.configs.saveConfig()
            simpleCurrency =
                EduardAPI.instance.configs.get(
                    "currency." +
                            simpleCurrency.name, SimpleCurrencySystem::class.java
                )
            EduardAPI.instance.log(
                "§aMoeda registrada: §f"
                        + simpleCurrency.name
            )
            register(simpleCurrency.name, simpleCurrency)
            currenciesByPosition[simpleCurrency.position] = simpleCurrency

        }

        fun register(currencyName: String, currencyHandler: CurrencySystem) {
            currencies[currencyName.toLowerCase()] = currencyHandler
        }

        fun isRegistred(currencName: String): Boolean {
            return currencies.containsKey(currencName.toLowerCase())
        }

        fun getCurrency(currencyName: String): CurrencySystem? {
            return currencies[currencyName.toLowerCase()]
        }

    }


    @StorageAttributes(inline = true)
    var currency = mutableMapOf<FakePlayer, Double>()


    override fun get(player: FakePlayer): Double {
        return currency.getOrDefault(player, inicialAmount.toDouble())
    }

    override fun contains(player: FakePlayer, amount: Double): Boolean {
        return get(player) >= amount
    }

    override fun remove(player: FakePlayer, amount: Double): Boolean {
        set(player, get(player) - amount)
        return true
    }

    override fun add(player: FakePlayer, amount: Double): Boolean {
        set(player, get(player) + amount)
        return true
    }

    override fun set(player: FakePlayer, amount: Double) {
        currency[player] = amount
    }
}