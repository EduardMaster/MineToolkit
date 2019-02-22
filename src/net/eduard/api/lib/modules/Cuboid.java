package net.eduard.api.lib.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;
/**
 * 
 * Sistema de geração de Cuboid
 * @author Internet
 */
public class Cuboid implements Cloneable, ConfigurationSerializable, Iterable<Block> {

	protected String worldName;
	protected final Vector minimumPoint, maximumPoint;

	public Cuboid(Cuboid cuboid) {
		this(cuboid.worldName, cuboid.minimumPoint.getX(), cuboid.minimumPoint.getY(), cuboid.minimumPoint.getZ(),
				cuboid.maximumPoint.getX(), cuboid.maximumPoint.getY(), cuboid.maximumPoint.getZ());
	}

	public Cuboid(Location loc) {
		this(loc, loc);
		
	}


	public Cuboid(Location loc1, Location loc2) {
		if (loc1 != null && loc2 != null) {
			if (loc1.getWorld() != null && loc2.getWorld() != null) {
				if (!loc1.getWorld().getUID().equals(loc2.getWorld().getUID()))
					throw new IllegalStateException("The 2 locations of the cuboid must be in the same world!");
			} else {
				throw new NullPointerException("One/both of the worlds is/are null!");
			}
			this.worldName = loc1.getWorld().getName();

			double xPos1 = Math.min(loc1.getX(), loc2.getX());
			double yPos1 = Math.min(loc1.getY(), loc2.getY());
			double zPos1 = Math.min(loc1.getZ(), loc2.getZ());
			double xPos2 = Math.max(loc1.getX(), loc2.getX());
			double yPos2 = Math.max(loc1.getY(), loc2.getY());
			double zPos2 = Math.max(loc1.getZ(), loc2.getZ());
			this.minimumPoint = new Vector(xPos1, yPos1, zPos1);
			this.maximumPoint = new Vector(xPos2, yPos2, zPos2);
		} else {
			throw new NullPointerException("One/both of the locations is/are null!");
		}
	}

	public Cuboid(String worldName, double x1, double y1, double z1, double x2, double y2, double z2) {
		if (worldName == null || Bukkit.getServer().getWorld(worldName) == null)
			throw new NullPointerException("One/both of the worlds is/are null!");
		this.worldName = worldName;

		double xPos1 = Math.min(x1, x2);
		double xPos2 = Math.max(x1, x2);
		double yPos1 = Math.min(y1, y2);
		double yPos2 = Math.max(y1, y2);
		double zPos1 = Math.min(z1, z2);
		double zPos2 = Math.max(z1, z2);
		this.minimumPoint = new Vector(xPos1, yPos1, zPos1);
		this.maximumPoint = new Vector(xPos2, yPos2, zPos2);
	}

	public boolean containsLocation(Location location) {
		return location != null && location.getWorld().getName().equals(this.worldName)
				&& location.toVector().isInAABB(this.minimumPoint, this.maximumPoint);
	}

	public boolean containsVector(Vector vector) {
		return vector != null && vector.isInAABB(this.minimumPoint, this.maximumPoint);
	}

	public List<Block> getBlocks() {
		List<Block> blockList = new ArrayList<>();
		World world = this.getWorld();
		if (world != null) {
			for (int x = this.minimumPoint.getBlockX(); x <= this.maximumPoint.getBlockX(); x++) {
				for (int y = this.minimumPoint.getBlockY(); y <= this.maximumPoint.getBlockY()
						&& y <= world.getMaxHeight(); y++) {
					for (int z = this.minimumPoint.getBlockZ(); z <= this.maximumPoint.getBlockZ(); z++) {
						blockList.add(world.getBlockAt(x, y, z));
					}
				}
			}
		}
		return blockList;
	}

	public Location getLowerLocation() {
		return this.minimumPoint.toLocation(this.getWorld());
	}

	public double getLowerX() {
		return this.minimumPoint.getX();
	}

	public double getLowerY() {
		return this.minimumPoint.getY();
	}

	public double getLowerZ() {
		return this.minimumPoint.getZ();
	}

	public Location getUpperLocation() {
		return this.maximumPoint.toLocation(this.getWorld());
	}

	public double getUpperX() {
		return this.maximumPoint.getX();
	}

	public double getUpperY() {
		return this.maximumPoint.getY();
	}

	public double getUpperZ() {
		return this.maximumPoint.getZ();
	}

	public double getVolume() {
		return (this.getUpperX() - this.getLowerX() + 1) * (this.getUpperY() - this.getLowerY() + 1)
				* (this.getUpperZ() - this.getLowerZ() + 1);
	}

	public World getWorld() {
		World world = Bukkit.getServer().getWorld(this.worldName);
		if (world == null)
			throw new NullPointerException("World '" + this.worldName + "' is not loaded.");
		return world;
	}

	public void setWorld(World world) {
		if (world != null)
			this.worldName = world.getName();
		else
			throw new NullPointerException("The world cannot be null.");
	}

	@Override
	public Cuboid clone() {
		return new Cuboid(this);
	}

	@Override
	public ListIterator<Block> iterator() {
		return this.getBlocks().listIterator();
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> serializedCuboid = new HashMap<>();
		serializedCuboid.put("worldName", this.worldName);
		serializedCuboid.put("x1", this.minimumPoint.getX());
		serializedCuboid.put("x2", this.maximumPoint.getX());
		serializedCuboid.put("y1", this.minimumPoint.getY());
		serializedCuboid.put("y2", this.maximumPoint.getY());
		serializedCuboid.put("z1", this.minimumPoint.getZ());
		serializedCuboid.put("z2", this.maximumPoint.getZ());
		return serializedCuboid;
	}

	public static Cuboid deserialize(Map<String, Object> serializedCuboid) {
		try {
			String worldName = (String) serializedCuboid.get("worldName");

			double xPos1 = (Double) serializedCuboid.get("x1");
			double xPos2 = (Double) serializedCuboid.get("x2");
			double yPos1 = (Double) serializedCuboid.get("y1");
			double yPos2 = (Double) serializedCuboid.get("y2");
			double zPos1 = (Double) serializedCuboid.get("z1");
			double zPos2 = (Double) serializedCuboid.get("z2");

			return new Cuboid(worldName, xPos1, yPos1, zPos1, xPos2, yPos2, zPos2);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}