package net.eduard.api.server.ranks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.eduard.api.lib.storage.StorageAPI.Storable;

public class Rank implements Storable {

	private String name;
	private String prefix;
	private String suffix;
	private String nextRank;
	private String previousRank;
	private String headName;
	private double price;
	private int position;
	private List<String> permissions = new ArrayList<>();

	public Rank() {
	}
	

	

	public Rank(String name, int position) {
		this.name = name;
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPreviousRank() {
		return previousRank;
	}

	public void setPreviousRank(String previousRank) {
		this.previousRank = previousRank;
	}

	public String getNextRank() {
		return nextRank;
	}

	public void setNextRank(String nextRank) {
		this.nextRank = nextRank;
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

	public String getHeadName() {
		return headName;
	}

	public void setHeadName(String headName) {
		this.headName = headName;
	}

}
