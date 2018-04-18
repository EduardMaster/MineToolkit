package net.eduard.api.tutorial.nivel_6;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ExemploTimer extends BukkitRunnable {
	
	
	private boolean estado = true;
	
	
	public int contagem = 10;

	public static ExemploTimer criarTimerExemplo(Plugin plugin) {

		ExemploTimer timer = new ExemploTimer();
		timer.runTaskTimerAsynchronously(plugin, 20, 20);
		return timer;
	}

	@Override
	public void run() {

		contagem--;

		Bukkit.broadcastMessage("§aA contagem vai acabar em " + contagem + " segundos");

		if (contagem == 0) {

			Bukkit.broadcastMessage("§aAcabou o timer de 10s");
			cancel();
			setEstado(false);
		}

		// TODO Auto-generated method stub

	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

}
