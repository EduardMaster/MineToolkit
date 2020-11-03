package net.eduard.api.lib.click;

import org.bukkit.entity.Player;

import java.util.function.Consumer;


public interface PlayerEffect extends Consumer<Player> {

	/**
	 * Use Lambda em vez
	 * @param player Jogador
	 */
	@Deprecated
	default void effect(Player player){
		accept(player);
	}

}