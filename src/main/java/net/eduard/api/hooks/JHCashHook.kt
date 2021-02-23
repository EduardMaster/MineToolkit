package net.eduard.api.hooks

import net.eduard.api.server.currency.CurrencyController
import net.eduard.api.supports.CurrencyJHCash

class JHCashHook : PluginHook("JH_Shop") {
    override fun onPluginActive() {
        CurrencyController.getInstance().register(CurrencyJHCash())
    }
}