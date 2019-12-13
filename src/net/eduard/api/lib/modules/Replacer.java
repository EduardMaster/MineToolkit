package net.eduard.api.lib.modules;

import org.bukkit.entity.Player;

/**
 * Interface de criar Replacer (Placeholders)
 * 
 * @author Eduard
 *
 */
public  interface Replacer {
	/**
	 * Retorna o valor do Placeholder
	 * 
	 * @param player Jogador
	 * @return O placeholder
	 */
	Object getText(Player player);
}
