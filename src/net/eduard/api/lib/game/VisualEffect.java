package net.eduard.api.lib.game;

import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.eduard.api.lib.storage.Storable;

public class VisualEffect implements Storable {

	private int data;

	private Effect type;

	public VisualEffect() {
	}

	public VisualEffect(Effect type) {
		this(type, 0);
	}

	public VisualEffect(Effect type, int data) {
		this.data = data;
		this.type = type;
	}

	
	public int getData() {

		return data;
	}

	
	public Effect getType() {

		return type;
	}


	public VisualEffect setData(int data) {

		this.data = data;
		return this;
	}

	public VisualEffect setType(Effect type) {

		this.type = type;
		return this;
	}

	public VisualEffect create(Entity entity, int radius) {
		create(entity.getLocation(), radius);
		return this;
	}

	public VisualEffect create(Location loc) {
		return create(loc, 0);
	}

	
	public VisualEffect create(Location loc, int radius) {
		loc.getWorld().playEffect(loc, type, data, radius);
		return this;
	}

	

	@SuppressWarnings("deprecation")
	public VisualEffect create(Player p) {

		p.playEffect(p.getLocation(), type, data);
		return this;
	}

	@Override
	public String toString() {
		return "Effects [data=" + data + ", type=" + type + "]";
	}
	public static VisualEffect newEffects(String toString) {
		String[] split = toString.substring(toString.indexOf("["),toString.lastIndexOf("]")).split(", ");
		int data = Integer.valueOf(split[0].split("=")[1]);
		Effect effect = Effect.valueOf(split[1].split("=")[1]);
		return new VisualEffect(effect, data);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + data;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VisualEffect other = (VisualEffect) obj;
		if (data != other.data)
			return false;
		if (type != other.type)
			return false;
		return true;
	}


}
