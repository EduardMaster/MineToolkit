package net.eduard.api.server.vips;

import java.util.ArrayList;
import java.util.List;

import net.eduard.api.lib.storage.Storable;

public class Vip implements Storable {

	private String name;
	private double price;
	private List<String> commands = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
