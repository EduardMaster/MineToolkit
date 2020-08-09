package net.eduard.api.listener

import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.server.PluginHook
import org.bukkit.event.EventHandler
import org.bukkit.event.server.PluginEnableEvent

class SupportActivations : EventsManager() {


    @EventHandler
    fun onEnableRookEvent(e: PluginEnableEvent) {

        val rooks = PluginHook.getRooks(e.plugin.name)
        rooks.forEach(PluginHook::onPluginActive)

    }

}