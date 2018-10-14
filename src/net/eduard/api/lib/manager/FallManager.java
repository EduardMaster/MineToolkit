package net.eduard.api.lib.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;

import net.eduard.api.lib.Mine;
/**
 * Anti dano de queda dos Jogadores
 * @author Eduard
 *
 */
public class FallManager extends EventsManager {
	/**
	 * Lista de jogador que nao v�o levar dano de queda
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
	 * Manipulador de evento, que quando o jogador estiver no ch�o e n�o estiver caindo remove da lista
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
