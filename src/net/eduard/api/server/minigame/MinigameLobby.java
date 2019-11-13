package net.eduard.api.server.minigame;

import java.util.ArrayList;

import net.eduard.api.lib.storage.Storable;

public class MinigameLobby implements Storable{
	
	private int id=1;
	
	private int slot;
	

	private transient ArrayList<MinigamePlayer> players = new ArrayList<>();

	public ArrayList<MinigamePlayer> getPlayers() {
		return players;
	}
	
	public void join(MinigamePlayer player) {
		for (MinigamePlayer p : players) {
			p.show(player);
			player.show(p);
		}
		player.setLobby(this);
		if (!players.contains(player))
		players.add(player);
		
	}
	public void leave(MinigamePlayer player) {
		for (MinigamePlayer p : players) {
			p.hide(player);
			player.hide(p);
		}
		player.setLobby(null);
		players.remove(player);
	}
	public void setPlayers(ArrayList<MinigamePlayer> players) {
		this.players = players;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

}
