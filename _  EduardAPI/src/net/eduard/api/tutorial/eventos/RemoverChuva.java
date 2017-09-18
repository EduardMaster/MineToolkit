package net.eduard.api.tutorial.eventos;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class RemoverChuva implements Listener{

	@EventHandler
	public void RemoveChuva(WeatherChangeEvent e) {

		if (e.toWeatherState()) {
			e.setCancelled(true);
		}
	}
}
