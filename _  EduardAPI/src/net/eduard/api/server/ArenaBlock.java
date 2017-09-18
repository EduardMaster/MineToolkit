package net.eduard.api.server;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import net.eduard.api.setup.StorageAPI.Storable;

public class ArenaBlock implements Storable{

	private String world;
	private int x, y, z;
	private int id, data;
	public ArenaBlock(Block block) {
		setBlock(block);
	}public ArenaBlock() {
		// TODO Auto-generated constructor stub
	}
	public World getRealWorld() {
		return Bukkit.getWorld(world);
	}
	public Block getBlock() {
		return getRealWorld().getBlockAt(x, y, z);
	}
	@SuppressWarnings("deprecation")
	public void update(boolean applyPhisics) {
		getBlock().setTypeIdAndData(id, (byte) data, applyPhisics);
	}
	public void setBlock(Location location) {
		setBlock(location.getBlock());
	}
	@SuppressWarnings("deprecation")
	public void setBlock(Block block) {
		this.world = block.getWorld().getName();
		this.x = block.getX();
		this.z = block.getZ();
		this.y = block.getZ();
		this.id = block.getTypeId();
		this.data = block.getData();
	}
	public Location getLocation() {
		return new Location(getRealWorld(), x, y, z);
	}
	
	public BlockState getState() {
		return getBlock().getState();
	}

	public String getWorld() {
		return world;
	}
	public void setWorld(String world) {
		this.world = world;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getData() {
		return data;
	}
	public void setData(int data) {
		this.data = data;
	}
	
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
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
