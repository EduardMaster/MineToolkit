package net.eduard.api.lib.kotlin.examples

import net.eduard.api.lib.kotlin.event
import org.bukkit.event.weather.WeatherChangeEvent

class EventsAlterations {


    init{
        event<WeatherChangeEvent>
        {
            isCancelled=true
        }


    }
}