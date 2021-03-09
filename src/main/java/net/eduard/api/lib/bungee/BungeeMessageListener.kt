package net.eduard.api.lib.bungee

import net.md_5.bungee.api.event.PluginMessageEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.IOException

class BungeeMessageListener(var bungeeController: BungeeController) : Listener {

    @EventHandler
    fun onMessage(event: PluginMessageEvent) {
        if (event.tag == BungeeAPI.channel) {
            val arrayIn = ByteArrayInputStream(event.data)
            val data = DataInputStream(arrayIn)
            try {
                val server = data.readUTF()
                val tag = data.readUTF()
                val line = data.readUTF()
                bungeeController.receiveMessage(server, tag, line)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}