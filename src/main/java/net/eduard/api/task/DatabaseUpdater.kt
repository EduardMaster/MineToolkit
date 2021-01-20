package net.eduard.api.task

import net.eduard.api.lib.manager.TimeManager
import net.eduard.api.server.EduardPlugin
import org.bukkit.Bukkit

class DatabaseUpdater  : TimeManager(1){

    override fun run() {
        for (plugin in Bukkit.getPluginManager().plugins){
            if (plugin is EduardPlugin){
                plugin.sqlManager.runDeletesQueue()
                plugin.sqlManager.runUpdatesQueue()
            }
        }
    }
}