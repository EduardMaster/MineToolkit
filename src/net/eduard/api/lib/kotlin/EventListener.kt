package net.eduard.api.lib.kotlin

import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class EventListener<T : Event>

(val eventAlteration : T.() -> Unit )

    : Listener{

    init{
        register(javaClass.plugin)
    }



    @EventHandler
    fun onEvent(e : T){
        eventAlteration(e)
    }



}