package net.eduard.api.hooks

import net.eduard.api.lib.kotlin.fake
import net.eduard.api.lib.modules.Mine
import net.eduard.api.server.PluginHook
import net.eduard.api.server.currency.CurrencyManager
import net.eduard.api.supports.CurrencyJHCash

class JHCashHook : PluginHook("JH_Shop") {
    override fun onPluginActive() {
        val cashSystem = CurrencyJHCash()
        CurrencyManager.register(cashSystem)
        Mine.addReplacer("jhcash"){
            cashSystem.get(it.fake)
        }
    }
}