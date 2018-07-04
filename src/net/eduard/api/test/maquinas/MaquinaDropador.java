package net.eduard.api.test.maquinas;

import org.bukkit.scheduler.BukkitRunnable;

public class MaquinaDropador extends BukkitRunnable {

	@Override
	public void run() {
//		for (MaquinaInstalada maquina : MaquinaPOO.manager.getInstaladas()) {
//			if (maquina.isLigada()) {
//
//				int nivel = maquina.getLevel();
//				MaquinaLevel level = maquina.getMaquina().getMaquinaLevel(nivel);
//				double tempoEntreDrops = 50;
//				long potencia = level.getPotencia();
//				tempoEntreDrops = tempoEntreDrops - (Velocidade.getReducaoPelaPotencia(potencia));
//
//				if (tempoEntreDrops < 1) {
//					tempoEntreDrops = 1;
//				}
//
//				long ultimoDrop = maquina.getUltimoDropFeito();
//				long agora = System.currentTimeMillis();
//				long diferenca = agora - ultimoDrop;
//				if (ultimoDrop == 0) {
//					diferenca = 0;
//				}
//
//				if (tempoEntreDrops <= diferenca) {
//
//					long tempoRestante = maquina.getDuracaoRestante() - diferenca;
//					if (tempoRestante <= 0) {
//						maquina.setLigada(false);
//						maquina.setDuracaoRestante(0);
//						String nome = maquina.getDono();
//						Player jogador = Bukkit.getPlayer(nome);
//						// desligar
//					} else {
//						new BukkitRunnable() {
//
//							@Override
//							public void run() {
//								maquina.getLocal().getWorld().dropItem(maquina.getLocal(),
//										maquina.getMaquina().getDrop());
//							}
//						}.runTask(Main.getInstance());
//						maquina.setUltimoDropFeito(agora);
//						maquina.setDuracaoRestante(tempoRestante);
//
//					}
//				} else {
//				}
//			}
//		}

	}

}
