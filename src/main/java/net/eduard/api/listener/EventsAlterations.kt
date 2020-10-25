package net.eduard.api.listener

import net.eduard.api.lib.kotlin.event
import org.bukkit.event.weather.WeatherChangeEvent

class EventsAlterations {


    init {
        event<WeatherChangeEvent>
        {
            if (toWeatherState())
                isCancelled = true
        }


    }
}