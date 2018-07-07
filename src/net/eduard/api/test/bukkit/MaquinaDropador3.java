package net.eduard.api.test.bukkit;

import org.bukkit.scheduler.BukkitRunnable;

public class MaquinaDropador3 extends BukkitRunnable {

	@Override
	public void run() {
//		for (MaquinaInstalada maquina : MaquinaPOO.manager.getInstaladas()) {
//			if (maquina.isLigada()) {
//				long ultimoDrop = maquina.getUltimoDropFeito();
//				long agora = System.currentTimeMillis();
//				long diferenca = agora - ultimoDrop;
////				if (ultimoDrop == 0) {
////					diferenca = 0;
////				}
//				long tempoRestante = maquina.getDuracaoRestante() - diferenca;
//				if (tempoRestante <= 0) {
//					maquina.setLigada(false);
//					maquina.setDuracaoRestante(0);
//					String nome = maquina.getDono();
//					Player jogador = Bukkit.getPlayer(nome);
//					// desligar
//				} else {
//					new BukkitRunnable() {
//						
//						@Override
//						public void run() {
//							maquina.getLocal().getWorld().dropItem(maquina.getLocal(), maquina.getMaquina().getDrop());
//						}
//					}.runTask(Main.getInstance());
//					maquina.setUltimoDropFeito(agora);
//					maquina.setDuracaoRestante(tempoRestante);
//
//				}
//
//			}
//		}

	}

}
