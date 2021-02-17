package net.eduard.api.lib.game;

import net.eduard.api.lib.modules.EmptyWorldGenerator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

/**
 * API de criação de Mundo Plano
 * 
 * @author Eduard
 * @version 1.0
 *
 */
public class FlatWorldGenerator extends EmptyWorldGenerator {

	@Override
	public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ,
			ChunkGenerator.BiomeGrid biomeGrid) {
		byte[][] result = new byte[world.getMaxHeight() / 16][];
		setLayer(result, 0, Material.BEDROCK);
		setLayer(result, 1, 3, Material.DIRT);
		setLayer(result, 4, Material.GRASS);
		setCorner(result, 8, Material.DIAMOND_BLOCK);

		return result;
	}

}


