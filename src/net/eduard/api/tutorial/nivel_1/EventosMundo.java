
package net.eduard.api.tutorial.nivel_1;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class EventosMundo implements Listener {

	@EventHandler
	public void ItemPendurado(HangingPlaceEvent e) {
	}

	@EventHandler
	public void ItemPenduradoQuebrado(HangingBreakEvent e) {

	}

	// Extras
	@EventHandler
	public void ItemPenduradoQuebradoPorEntidade(HangingBreakByEntityEvent e) {

	}

	@EventHandler
	public void MundoCarregar(WorldLoadEvent e) {

	}

	// Mundo
//	@EventHandler
//	public void MundoChuck(ChunkEvent e) {
//
//	}

	@EventHandler
	public void MundoChuvaTroca(WeatherChangeEvent e) {

	}

	@EventHandler
	public void MundoCrear(PortalCreateEvent e) {

	}

	@EventHandler
	public void MundoDescarregar(WorldUnloadEvent e) {

	}

	@EventHandler
	public void MundoIniciar(WorldInitEvent e) {

	}

	// Tempo
	@EventHandler
	public void MundoLightStrike(LightningStrikeEvent e) {

	}

	@EventHandler
	public void MundoStructureNascer(StructureGrowEvent e) {

	}

	@EventHandler
	public void MundoThunderTroca(ThunderChangeEvent e) {

	}

	@EventHandler
	public void MundoTrocarSpawn(SpawnChangeEvent e) {

	}

	@EventHandler
	public void ServerComando(ServerCommandEvent e) {

	}

	@EventHandler
	public void ServerIniciaMapa(MapInitializeEvent e) {

	}

	@EventHandler
	public void ServerListaPing(ServerListPingEvent e) {

	}

//	@EventHandler
//	public void ServerPlugin(PluginEvent e) {
//
//	}
//
//	@EventHandler
//	public void ServerServico(ServiceEvent e) {
//
//	}
}
