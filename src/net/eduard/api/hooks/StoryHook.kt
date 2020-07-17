package net.eduard.api.hooks

import net.eduard.api.server.PluginHook
import net.eduard.api.server.currency.CurrencyController
import net.eduard.api.server.currency.list.CurrencyNetworkStoryRankupToken

class StoryHook : PluginHook("LegitRankUP") {
    override fun onPluginActive() {
        CurrencyController.getInstance().register(CurrencyNetworkStoryRankupToken())
    }
}