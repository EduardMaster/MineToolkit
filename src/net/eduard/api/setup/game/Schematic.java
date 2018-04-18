package net.eduard.api.setup.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.StorageAPI.Storable;

/**
 * Schematic do WorldEdit Compacto
 * 
 * @author Eduard-PC
 *
 */
public class Schematic implements Storable {

	private String world;
	private short width;
	private short height;
	private short length;
	private byte[] blocksId;
	private byte[] blocksData;

	public static int getIndex(int x, int y, int z, int width, int length) {
		return y * width * length + z * width + x;
	}

	public Schematic() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	public void copy(Location firstLocation, Location secondLocation) {
		Location highLoc = Mine.getHighLocation(firstLocation, secondLocation);
		Location lowLoc = Mine.getLowLocation(firstLocation, secondLocation);
		this.world = highLoc.getWorld().getName();
		width = (short) (highLoc.getBlockX() - lowLoc.getBlockX());
		height = (short) (highLoc.getBlockY() - lowLoc.getBlockY());
		length = (short) (highLoc.getBlockZ() - lowLoc.getBlockZ());
		int size = width * height * length;
		this.blocksId = new byte[size];
		this.blocksData = new byte[size];
		World worldUsed = Bukkit.getWorld(world);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					int index = getIndex(x, y, z, width, length);
					Block block = worldUsed.getBlockAt(lowLoc.getBlockX() + x, lowLoc.getBlockY() + y,
							lowLoc.getBlockZ() + z);
					int id = block.getTypeId();
					blocksId[index] = (byte) id;
					blocksData[index] = block.getData();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void paste(Location lowLoc) {
		World worldUsed = Bukkit.getWorld(world);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					int index = getIndex(x, y, z, width, length);
					Block block = worldUsed.getBlockAt(lowLoc.getBlockX() + x, lowLoc.getBlockY() + y,
							lowLoc.getBlockZ() + z);
					byte typeId = blocksId[index];
					byte typeData = blocksData[index];
					if (block.getTypeId() != typeId && block.getData() != typeData) {
						block.setTypeIdAndData(block.getTypeId(), block.getData(), false);
					} else if (block.getData() != block.getData()) {
						block.setData(typeData, false);
					} else if (block.getTypeId() != block.getTypeId()) {
						block.setTypeId(typeId, false);
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
			FileOutputStream s = new FileOutputStream(file);
			DataOutputStream d = new DataOutputStream(new GZIPOutputStream(s));
			d.writeUTF(world);
			d.writeShort(width);
			d.writeShort(height);
			d.writeShort(length);
			d.writeInt(blocksId.length);
			d.write(blocksId);
			d.writeInt(blocksId.length);
			d.write(blocksData);
			d.flush();
			d.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reload(File file) {
		try {
			FileInputStream s = new FileInputStream(file);
			DataInputStream d = new DataInputStream(new GZIPInputStream(s));
			// int len = d.readShort();
			// byte[] bytes = new byte[len];
			// d.readFully(bytes);
			this.world = d.readUTF();
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
			d.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

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
