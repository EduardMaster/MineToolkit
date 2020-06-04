package net.eduard.api.listener

import net.eduard.api.EduardAPI
import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.server.currency.CurrencyController
import net.eduard.api.server.currency.list.CurrencyJHCash
import net.eduard.api.server.currency.list.CurrencyNetworkStoryRankupToken
import org.bukkit.event.EventHandler
import org.bukkit.event.server.PluginEnableEvent

class SupportActivations : EventsManager() {



    companion object{
        var tasks = mutableMapOf<String,Function<Unit>>()

    }


    @EventHandler
    fun event(e: PluginEnableEvent) {
        val api = EduardAPI.instance


        when (e.plugin.name) {
            "JH_Shop" -> {

                CurrencyController.getInstance().register(CurrencyJHCash())

            }
            "LegitRankUP" -> {
                CurrencyController.getInstance().register(CurrencyNetworkStoryRankupToken())
            }

        }
        var task = tasks[e.plugin.name]?:return



    }

}