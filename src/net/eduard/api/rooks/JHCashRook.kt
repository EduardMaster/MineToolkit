package net.eduard.api.rooks

import net.eduard.api.server.PluginRook
import net.eduard.api.server.currency.CurrencyController
import net.eduard.api.server.currency.list.CurrencyJHCash

class JHCashRook : PluginRook("JH_SHOP") {
    override fun onPluginActive() {
        CurrencyController.getInstance().register(CurrencyJHCash())
    }
}