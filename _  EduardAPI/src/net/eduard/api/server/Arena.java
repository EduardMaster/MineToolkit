package net.eduard.api.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.eduard.api.setup.StorageAPI.Copyable;
import net.eduard.api.setup.StorageAPI.Storable;
/**
 * Simples Esquema de Blocos denominado Arena
 * @author Eduard
 *
 */
public class Arena implements Storable ,Copyable {

	private List<ArenaBlock> blocks = new ArrayList<>();
	private Location highPosition;
	private Location secondPosition;
	private Location playerPosition;
	private Location firstPosition;
	private Location lowPosition;
	public Arena() {

	}
	public Arena(Location firstPosition, Location secondPosition) {
		this(firstPosition, secondPosition, null);
	}

	public Arena(Location firstPosition, Location secondPosition,
			Location playerPosition) {
		super();
		this.firstPosition = firstPosition;
		this.secondPosition = secondPosition;
		this.playerPosition = playerPosition;
		setupHighLow();
	}
	public Arena copy() {
		return copy(this);
	}
	public Arena setupHighLow() {
		int x1 = (int) Math.max(firstPosition.getX(), secondPosition.getX());
		int y1 = (int) Math.max(firstPosition.getY(), secondPosition.getY());
		int z1 = (int) Math.max(firstPosition.getZ(), secondPosition.getZ());
		highPosition = new Location(playerPosition.getWorld(), x1, y1, z1);
		int x2 = (int) Math.min(firstPosition.getX(), secondPosition.getX());
		int y2 = (int) Math.min(firstPosition.getY(), secondPosition.getY());
		int z2 = (int) Math.min(firstPosition.getZ(), secondPosition.getZ());
		lowPosition = new Location(playerPosition.getWorld(), x2, y2, z2);
		return this;
	}
	public Arena copy(boolean copyAir) {
		blocks.clear();
		setupHighLow();
		int x1 = lowPosition.getBlockX();
		int y1 = lowPosition.getBlockY();
		int z1 = lowPosition.getBlockZ();
		int x2 = highPosition.getBlockX();
		int y2 = highPosition.getBlockY();
		int z2 = highPosition.getBlockZ();
		for (int x = x1; x < x2; x++) {
			for (int y = y1; y < y2; y++) {
				for (int z = z1; z < z2; z++) {
					Block block = new Location(playerPosition.getWorld(),
							x, y, z).getBlock();
					if (!copyAir && block.getType() == Material.AIR)
						continue;
					blocks.add(new ArenaBlock(block));
				}
			}
		}
		return this;
	}
	public Arena move(Location playerPosition) {
		int difX = this.playerPosition.getBlockX() - playerPosition.getBlockX();
		int difY = this.playerPosition.getBlockY() - playerPosition.getBlockY();
		int difZ = this.playerPosition.getBlockZ() - playerPosition.getBlockZ();
		for (ArenaBlock block : this.blocks) {
			int x = block.getX() - difX;
			int y = block.getY() - difY;
			int z = block.getZ() - difZ;
			block.setY(y);
			block.setZ(z);
			block.setX(x);
		}
		this.highPosition.setX(highPosition.getX() - difX);
		this.highPosition.setY(highPosition.getY() - difY);
		this.highPosition.setZ(highPosition.getZ() - difZ);
		this.lowPosition.setX(lowPosition.getX() - difX);
		this.lowPosition.setY(lowPosition.getY() - difY);
		this.lowPosition.setZ(lowPosition.getZ() - difZ);
		this.firstPosition.setX(firstPosition.getX() - difX);
		this.firstPosition.setY(firstPosition.getY() - difY);
		this.firstPosition.setZ(firstPosition.getZ() - difZ);
		this.secondPosition.setX(secondPosition.getX() - difX);
		this.secondPosition.setY(secondPosition.getY() - difY);
		this.secondPosition.setZ(secondPosition.getZ() - difZ);
		this.playerPosition = playerPosition;
		return this;
	}
	public Arena paste(boolean applyPhisics) {
		for (ArenaBlock block : blocks) {
			block.update(applyPhisics);
		}
		return this;
	}
	public Arena paste(Location playerPosition) {
		return copy(this).move(playerPosition).paste(false);
	}

	public List<ArenaBlock> getBlocks() {
		return blocks;
	}
	public void setBlocks(List<ArenaBlock> blocks) {
		this.blocks = blocks;
	}
	public Location getFirstPosition() {
		return secondPosition;
	}

	public void setFirstPosition(Location firstPosition) {
		this.secondPosition = firstPosition;
	}

	public Location getSecondPosition() {
		return secondPosition;
	}

	public void setSecondPosition(Location secondPosition) {
		this.secondPosition = secondPosition;
	}

	public Location getPlayerPosition() {
		return playerPosition;
	}

	public void setPlayerPosition(Location playerPosition) {
		this.playerPosition = playerPosition;
	}

	public Location getHighPosition() {
		return secondPosition;
	}

	public void setHighPosition(Location highPosition) {
		this.secondPosition = highPosition;
	}

	public Location getLowPosition() {
		return secondPosition;
	}

	public void setLowPosition(Location lowPosition) {
		this.secondPosition = lowPosition;
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
