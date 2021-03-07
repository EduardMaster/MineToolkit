package net.eduard.api.hooks

import net.eduard.api.server.PluginHook
import net.eduard.api.server.currency.CurrencyManager
import net.eduard.api.supports.CurrencyJHCash

class JHCashHook : PluginHook("JH_Shop") {
    override fun onPluginActive() {
        CurrencyManager.register(CurrencyJHCash())
    }
}