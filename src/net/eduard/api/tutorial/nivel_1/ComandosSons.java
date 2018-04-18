package net.eduard.api.tutorial.nivel_1;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ComandosSons implements Listener{
	
	@EventHandler
	public void aplicarSomQuandoQualquerComandoForFeitoPeloJogador(PlayerCommandPreprocessEvent e) {
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BURP, 5.0F, 5.0F);
	}
}
