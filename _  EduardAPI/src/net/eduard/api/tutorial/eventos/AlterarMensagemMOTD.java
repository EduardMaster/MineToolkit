package net.eduard.api.tutorial.eventos;

import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

import net.eduard.api.manager.TimeManager;

public class AlterarMensagemMOTD extends TimeManager{
	
	@EventHandler
	public void quandoAlguemVerAMOTD(ServerListPingEvent e) {
		e.setMotd("§6Parabens Por Jogar Primeira linha\n§bAQUI Segunda Linha");
	}
}
