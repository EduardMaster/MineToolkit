package net.eduard.api.lib.world;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.util.Vector;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.modules.Copyable;
import net.eduard.api.lib.storage.Storable;

/**
 * Schematic do WorldEdit Compacto
 * 
 * @author Eduard
 *
 */
public class Schematic implements Storable,Copyable {

	private Vector relative, low, high;
	private transient int count;
	private short width;
	private short height;
	private short length;
	private transient List<Chest> chests = new ArrayList<>();
	private transient byte[] blocksId;
	private transient byte[] blocksData;


	public short getWidth() {
		return width;
	}

	public void setWidth(short width) {
		this.width = width;
	}

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public short getLength() {
		return length;
	}

	public void setLength(short length) {
		this.length = length;
	}

	public static int getIndex(int x, int y, int z, int width, int length) {
		return y * width * length + z * width + x;
	}

	public Schematic() {
	}


	public boolean hasFirstLocation() {
		return high != null;
	}

	public boolean hasSecondLocation() {
		return low != null;
	}
	public void copy(World world) {
		copy(relative.toLocation(world), low.toLocation(world), high.toLocation(world));
	}

	public void copy(Location relativeLocation) {
		World world = relativeLocation.getWorld();
		copy(relativeLocation, low.toLocation(world), high.toLocation(world));
	}

	public Schematic copy() {
		return copy(this);
	}

	@SuppressWarnings("deprecation")
	public void copy(Location relativeLocation, Location firstLocation, Location secondLocation) {
		setCount(0);
		Location highLoc = Mine.getHighLocation(firstLocation, secondLocation);
		Location lowLoc = Mine.getLowLocation(firstLocation, secondLocation);
		setHigh(highLoc.toVector());
		setLow(lowLoc.toVector());
		setRelative(relativeLocation.toVector());
		chests.clear();

		width = (short) (highLoc.getBlockX() - lowLoc.getBlockX());
		height = (short) (highLoc.getBlockY() - lowLoc.getBlockY());
		length = (short) (highLoc.getBlockZ() - lowLoc.getBlockZ());
		int size = width * height * length;
		this.blocksId = new byte[size];
		this.blocksData = new byte[size];
		World worldUsed = relativeLocation.getWorld();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					count++;
					int index = getIndex(x, y, z, width, length);
					Block block = worldUsed.getBlockAt(lowLoc.getBlockX() + x, lowLoc.getBlockY() + y,
							lowLoc.getBlockZ() + z);
					int id = block.getTypeId();
					if (block.getState() instanceof Chest) {
						Chest chest = (Chest) block.getState();
						chests.add(chest);
						
					}
					blocksId[index] = (byte) id;
					blocksData[index] = block.getData();
				}
			}
		}
	}

	public byte[] getBlocksId() {
		return blocksId;
	}

	public void setBlocksId(byte[] blocksId) {
		this.blocksId = blocksId;
	}

	public byte[] getBlocksData() {
		return blocksData;
	}

	public void setBlocksData(byte[] blocksData) {
		this.blocksData = blocksData;
	}

	public void paste(Location newRelative) {
		paste(newRelative, false);
	}

	@SuppressWarnings("deprecation")
	public void paste(Location newRelative, boolean minusLag) {
		World worldUsed = newRelative.getWorld();
		this.chests.clear();
		int difX = newRelative.getBlockX() - relative.getBlockX();
		int difY = newRelative.getBlockY() - relative.getBlockY();
		int difZ = newRelative.getBlockZ() - relative.getBlockZ();
		setCount(0);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					count++;
					int index = getIndex(x, y, z, width, length);
					Block block = worldUsed.getBlockAt(difX + low.getBlockX() + x, difY + low.getBlockY() + y,
							difZ + low.getBlockZ() + z);
					
					byte typeId = blocksId[index];
					byte typeData = blocksData[index];
					if (typeId<0) {
						typeId=0;
					}
					if (typeData<0) {
						typeData=0;
					}
					if (minusLag) {
						if (typeId == 0) {
							continue;
						}
					}
					if (block != null) {
						if (block.getTypeId() != typeId || block.getData() != typeData)
						{
//							Mine.console("erro "+typeId+ "   "+typeData);
							block.setTypeIdAndData(typeId, typeData, false);
						}else {
						}
					}else {
					}
					if (block.getState() instanceof Chest) {
						Chest chest = (Chest) block.getState();
						chests.add(chest);
						
					}
				}
			}
		}

	}

	public void setType(byte id, byte data) {
		for (int i = 0; i < blocksId.length; i++) {
			blocksId[i] = id;
			blocksData[i] = data;
		}
	}

	public void save(File file) {
		try {
			file.getParentFile().mkdirs();
			FileOutputStream s = new FileOutputStream(file);
			DataOutputStream d = new DataOutputStream(new GZIPOutputStream(s));
			
			d.writeShort(width);
			d.writeShort(height);
			d.writeShort(length);
			d.writeInt(blocksId.length);
			d.write(blocksId);
			d.writeInt(blocksId.length);
			d.write(blocksData);
			d.writeUTF(Mine.saveVector(low));
			d.writeUTF(Mine.saveVector(high));
			d.writeUTF(Mine.saveVector(relative));
			d.flush();
			d.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Schematic reload(File file) {
		try {
			FileInputStream s = new FileInputStream(file);
			DataInputStream d = new DataInputStream(new GZIPInputStream(s));
			// int len = d.readShort();
			// byte[] bytes = new byte[len];
			// d.readFully(bytes);
			
			this.width = d.readShort();
			this.height = d.readShort();
			this.length = d.readShort();
			int size = d.readInt();
			// System.out.println(width+" "+height+" "+length+" "+world+" "+size);
			this.blocksId = new byte[size];
			d.readFully(blocksId);

			//
			size = d.readInt();
			this.blocksData = new byte[size];
			d.readFully(blocksData);

			low = Mine.toVector(d.readUTF());
			high = Mine.toVector(d.readUTF());
			relative = Mine.toVector(d.readUTF());

			d.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;

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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static Schematic load(File subfile) {
		return new Schematic().reload(subfile);
	}

	public Vector getRelative() {
		return relative;
	}

	public void setRelative(Vector relative) {
		this.relative = relative;
	}

	public Vector getLow() {
		return low;
	}

	public void setLow(Vector low) {
		this.low = low;
	}

	public Vector getHigh() {
		return high;
	}

	public void setHigh(Vector high) {
		this.high = high;
	}

	public List<Chest> getChests() {
		return chests;
	}

	public void setChests(List<Chest> chests) {
		this.chests = chests;
	}


}
