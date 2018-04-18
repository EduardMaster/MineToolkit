package net.eduard.api.tutorial.nivel_1;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

/**
 * MOTD significa Mensagem de entrada do servidor quando atulizamos a lista de servidores aparece uma mensagem de 2 linhas
 * @author Eduard-PC
 *
 */
public class EditarMotd implements Listener{
	
	@EventHandler
	public void aoVerMOTD(ServerListPingEvent e) {
		e.setMotd("§6Parabens Por Jogar Primeira linha\n§bAQUI Segunda Linha");
	}
}
