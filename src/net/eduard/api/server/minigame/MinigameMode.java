package net.eduard.api.server.minigame;

import java.util.Map;

import net.eduard.api.setup.StorageAPI.Storable;

public class MinigameMode implements Storable {
	private String name;
	private GameChest chests = new GameChest();
	private GameChest chestsFeast= new GameChest();
	private GameChest miniFeast= new GameChest();
	public MinigameMode() {
		// TODO Auto-generated constructor stub
	}

	public MinigameMode(String name) {
		super();
		this.name = name;
	}

	public GameChest getChests() {
		return chests;
	}

	public void setChests(GameChest chests) {
		this.chests = chests;
	}

	public GameChest getChestsFeast() {
		return chestsFeast;
	}

	public void setChestsFeast(GameChest chestsFeast) {
		this.chestsFeast = chestsFeast;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public GameChest getMiniFeast() {
		return miniFeast;
	}

	public void setMiniFeast(GameChest miniFeast) {
		this.miniFeast = miniFeast;
	}

}
