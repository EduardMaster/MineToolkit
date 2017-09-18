package net.eduard.api.server;

import org.bukkit.plugin.Plugin;

import net.eduard.api.setup.StorageAPI.Reference;
/**
 * Minigame simplificado para ser Evento
 * 
 * @author Eduard-PC
 *
 */
public abstract class GameEvent extends Minigame {
	@Reference
	private Game game;
	@Reference
	private GameMap map;
	public GameEvent(String name, Plugin plugin) {
		super(name, plugin);
		setMap(new GameMap(name));
		setGame(new Game(this, map));
	}

	public GameEvent() {
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public GameMap getMap() {
		return map;
	}

	public void setMap(GameMap map) {
		this.map = map;
	}

}
