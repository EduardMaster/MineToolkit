package net.eduard.api.lib.modules;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

/**
 * Um ChunkGenerator sem definir nenhum tipo de Bloco ou seja completamente vazio<br>
 * Usado no WorldCreator na função ...generator(new EmptyWorldGenerator())
 * 
 * @author Eduard
 *
 */
public class EmptyWorldGenerator extends ChunkGenerator {

	@Deprecated
	public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ,
			BiomeGrid biomeGrid) {
		return new byte[world.getMaxHeight() / 16][];
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		return new Location(world, 100, 100, 100);
	}

	public void setBlock(byte[][] result, int x, int y, int z, byte blockID) {
		if (result[(y >> 4)] == null) {
			result[(y >> 4)] = new byte[4096];
		}
		result[(y >> 4)][((y & 0xF) << 8 | z << 4 | x)] = blockID;
	}

	@SuppressWarnings("deprecation")
	public byte getId(Material material) {
		return (byte) material.getId();
	}

	public byte getId(Material material, short data) {
		return 0;
	}

	public void setLayer(byte[][] result, int level, Material material) {
		int x, z;
		for (x = 0; x < 16; x++) {
			for (z = 0; z < 16; z++) {
				setBlock(result, x, level, z, getId(material));
			}
		}
	}

	public void setCorner(byte[][] result, int level, Material material) {
		int x, z;
		for (x = 0; x < 16; x++) {
			setBlock(result, x, level, 0, getId(material));
		}
		for (z = 0; z < 16; z++) {
			setBlock(result, 0, level, z, getId(material));
		}
	}

	public void setLayer(byte[][] result, int minLevel, int maxLevel, Material material) {
		int y;
		for (y = minLevel; y <= maxLevel; y++) {
			setLayer(result, y, material);
		}
	}
}

