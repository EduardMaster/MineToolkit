package net.eduard.api.lib.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.modules.Copyable;
import net.eduard.api.lib.storage.Storable;

public class Chunk implements Storable, Copyable {
	private String world;
	private int x, z;

	public org.bukkit.Chunk getChunk() {
		return Bukkit.getWorld(world).getChunkAt(x, z);
	}
	@Override
	public boolean saveInline() {
		return true;
	}

	public Chunk copy() {
		return copy(this);
	}

	public Chunk newChunk(int x, int z) {
		return new Chunk(world, x, z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		result = prime * result + x;
		result = prime * result + z;
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
		Chunk other = (Chunk) obj;
		if (world == null) {
			if (other.world != null)
				return false;
		} else if (!world.equals(other.world))
			return false;
		if (x != other.x)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	public Chunk() {
	}
	public Chunk(Location location) {
		this(location.getChunk());
	}
	public Chunk(org.bukkit.Chunk chunk) {
		this.world = chunk.getWorld().getName();
		this.x = chunk.getX();
		this.z = chunk.getZ();
	}

	public Chunk(String world, int x, int z) {
		super();
		this.world = world;
		this.x = x;
		this.z = z;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	@Override
	public Object restore(Object object) {
		if (object instanceof String) {
			String string = (String) object;
			String[] split = string.split(";");
			return new Chunk(split[0], Mine.toInt(split[1]), Mine.toInt(split[2]));

		}
		return null;
	}

	@Override
	public Object store(Object object) {
		if (object instanceof Chunk) {
			Chunk chunk = (Chunk) object;
			return chunk.getWorld() + ";" + chunk.getX() + ";" + chunk.getZ();

		}
		return null;
	}

}
