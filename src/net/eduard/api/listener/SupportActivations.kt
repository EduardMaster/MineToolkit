package net.eduard.api.listener

import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.server.PluginRook
import org.bukkit.event.EventHandler
import org.bukkit.event.server.PluginEnableEvent

class SupportActivations : EventsManager() {


    @EventHandler
    fun onEnableRookEvent(e: PluginEnableEvent) {

        var rooks = PluginRook.getRooks(e.plugin.name)
        rooks.forEach(PluginRook::onPluginActive)

    }

}