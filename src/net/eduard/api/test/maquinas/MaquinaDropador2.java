package net.eduard.api.test.maquinas;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MaquinaDropador2 extends BukkitRunnable {

	@Override
	public void run() {
//		for (MaquinaInstalada maquina : MaquinaPOO.manager.getInstaladas()) {
//			if (maquina.isLigada()) {
//				int minutosRestantes = maquina.getMinutosRestantes();
//				long tempoRestante = minutosRestantes * 60 * 1000L;
//				
//				// 1000 milisegundo = 1 segundo
//				// 60000 = 1 minutos ou 60 segundos
//				// 600000 = 10 minutos ou 600 segundos
//				long inicio = maquina.getMomentoLigou();
//				long agora = System.currentTimeMillis();
//				// inicio = 10
//				// agora = 30
//				// temporestante = 15
//				// 10 + 15 = 25
//				// temporestante = 35
//				// 35 + 10 = 45
//				// 30 < 45
//				// temporestante = 5
//				// 10 + 5 = 15
//				// 30 < 15
//				// 
//				if (agora < inicio + tempoRestante) {
//					maquina.getLocal().getWorld().dropItem(maquina.getLocal(), maquina.getMaquina().getDrop());
//					// tem combustivel
//				}else {
//					maquina.setLigada(false);
//					String nome = maquina.getDono();
//					Player jogador = Bukkit.getPlayer(nome);
//					maquina.setMinutosRestantes(0);
//					// desligar
//				}
//			}
//		}

	}

}
