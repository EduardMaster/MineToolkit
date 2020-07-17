package net.eduard.api.hooks

import net.eduard.api.server.PluginHook
import net.eduard.api.server.currency.CurrencyController
import net.eduard.api.server.currency.list.CurrencyJHCash

class JHCashHook : PluginHook("JH_SHOP") {
    override fun onPluginActive() {
        CurrencyController.getInstance().register(CurrencyJHCash())
    }
}