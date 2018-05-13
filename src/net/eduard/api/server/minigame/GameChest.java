package net.eduard.api.server.minigame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.eduard.api.lib.storage.StorageAPI.Storable;
import net.eduard.api.lib.storage.game.ItemRandom;

public class GameChest implements Storable{
	private String name;
	private int refilTime;
	private List<ItemRandom> items = new ArrayList<>();
	public int getRefilTime() {
		return refilTime;
	}
	public void setRefilTime(int refilTime) {
		this.refilTime = refilTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ItemRandom> getItems() {
		return items;
	}
	public void setItems(List<ItemRandom> items) {
		this.items = items;
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
