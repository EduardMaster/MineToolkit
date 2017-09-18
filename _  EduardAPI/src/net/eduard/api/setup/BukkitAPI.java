package net.eduard.api.setup;

import org.bukkit.entity.Player;

/**
 * API traduzida do Bukkit e de outras classes
 * @author Eduard
 *
 */
public final class BukkitAPI {
	
	/**
	 * Seta o nivel do jogador
	 * @param jogador
	 * @param novoNivel
	 */
	public static void setarNivelJogador(Player jogador,int novoNivel) {
		jogador.setLevel(novoNivel);
	}
	/**
	 * Seta o nivel da barra de fome do jogador
	 * @param jogador
	 * @param quantidade
	 */
	public static void setarNivelFomeJogador(Player jogador, int quantidade) {
		jogador.setFoodLevel(quantidade);
	}
	
	/**
	 * Seta a barra de Xp do jogador <br>
	 * 100 = Barra cheia <br>
	 * 0 = Barra vazia <br>
	 * @param jogador
	 * @param porcentagem
	 */
	public static void setarBarraXpJogador(Player jogador, int porcentagem) {
		jogador.setExp(porcentagem == 0? 0F :porcentagem/100);
	}
	
	

}
