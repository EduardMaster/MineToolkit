package net.eduard.api.setup.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.manager.EventsManager;
/**
 * Anti dano de queda dos Jogadores
 * @author Eduard
 *
 */
public class NoFall extends EventsManager {
	/**
	 * Lista de jogador que nao vão levar dano de queda
	 */
	private List<Player> players = new ArrayList<>();

	
	/**
	 * Alterador de evento, que ao jogador cair e estiver na lista nao cancela o dano
	 * @param event Entidade toma dano Evento
	 */
	@EventHandler
	public void onPlayerTakeDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (event.getCause() == DamageCause.FALL && players.contains(p)) {
				event.setCancelled(true);
				players.remove(p);
			}
		}
	}
	/**
	 * Manipulador de evento, que quando o jogador estiver no chão e não estiver caindo remove da lista
	 * @param event Jogador anda evento
	 */
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (event.getTo().getBlock().equals(event.getTo().getBlock().getLocation())) {
			if (Mine.isOnGround(p) && !Mine.isFalling(p)) {
				players.remove(p);
			}
		}
	}
	/**
	 * 
	 * @return a Lista de jogadores
	 */
	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	@Override
	public Object restore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub
		
	}

}
