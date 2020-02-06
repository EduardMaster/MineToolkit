package net.eduard.api.lib.game;

import net.eduard.api.lib.modules.Extra;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.modules.Copyable;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAttributes;
/**
 * Representa uma Chunk do Minecraft para ser Configuravel
 * @author Eduard
 *
 */
@StorageAttributes(inline=true)
public class Chunks implements Storable, Copyable {
	private String world;
	private int x, z;

	public org.bukkit.Chunk getChunk() {
		return Bukkit.getWorld(world).getChunkAt(x, z);
	}

	public Chunks copy() {
		return copy(this);
	}

	public Chunks newChunk(int x, int z) {
		return new Chunks(world, x, z);
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
		Chunks other = (Chunks) obj;
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

	public Chunks() {
	}
	public Chunks(Location location) {
		this(location.getChunk());
	}
	public Chunks(org.bukkit.Chunk chunk) {
		this.world = chunk.getWorld().getName();
		this.x = chunk.getX();
		this.z = chunk.getZ();
	}

	public Chunks(String world, int x, int z) {
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
			return new Chunks(split[0], Extra.toInt(split[1]), Extra.toInt(split[2]));

		}
		return null;
	}

	@Override
	public Object store(Object object) {
		if (object instanceof Chunks) {
			Chunks chunk = (Chunks) object;
			return chunk.getWorld() + ";" + chunk.getX() + ";" + chunk.getZ();

		}
		return null;
	}

}
