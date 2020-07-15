package net.eduard.api.rooks

import net.eduard.api.server.PluginRook
import net.eduard.api.server.currency.CurrencyController
import net.eduard.api.server.currency.list.CurrencyNetworkStoryRankupToken

class StoryRook : PluginRook("LegitRankUP") {
    override fun onPluginActive() {
        CurrencyController.getInstance().register(CurrencyNetworkStoryRankupToken())
    }
}