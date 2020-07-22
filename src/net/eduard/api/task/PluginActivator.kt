package net.eduard.api.task

import net.eduard.api.lib.manager.TimeManager
import net.eduard.api.server.EduardPlugin
import net.eduard.api.server.Licence
import org.bukkit.Bukkit

class PluginActivator : TimeManager() {

    override fun run() {
        for (plugin in Bukkit.getPluginManager().plugins) {
            if (plugin is EduardPlugin) {
                if (!plugin.isFree && !plugin.isActivated) {
                    if (plugin.isEnabled) {
                        Licence.BukkitLicense.test(plugin, Runnable {
                            plugin.isActivated = true
                            plugin.onActivation()
                        })
                    }

                }
            }
        }
    }


}