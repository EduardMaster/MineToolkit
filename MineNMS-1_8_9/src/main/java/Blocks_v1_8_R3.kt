package net.eduard.api.lib.abstraction

import net.minecraft.server.v1_8_R3.*
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld

/**
 *
 */
class Blocks_v1_8_R3(
    x: Int, y: Int, z: Int,
    val worldServer: WorldServer
) : CraftBlock(null, x, y, z),
    Blocks {

    constructor(location: Location) : this(location.block) {}
    constructor(block: Block) :
            this(block.x, block.y, block.z, (block.world as CraftWorld).handle) {
    }

    override fun getWorld(): World {
        return worldServer.world
    }

    override fun getChunk(): Chunk {
        return worldServer.getChunkAt(x shr 4, z shr 4).bukkitChunk
    }

    override fun getTypeId(): Int {
        val section = section
        val type = section!!.getType(x and 0xF, y and 0xF, z and 0xF)
        val block = type.block
        return net.minecraft.server.v1_8_R3.Block.getId(block)
    }

    override fun getData(): Byte {
        val section = section
        val type = section!!.getType(x and 0xF, y and 0xF, z and 0xF)
        val block = type.block
        return block.toLegacyData(type).toByte()
    }

    override fun setTypeIdAndData(type: Int, data: Byte, applyPhysics: Boolean): Boolean {
        val section = section
        val x = x and 0xF
        val y = y and 0xF
        val z = z and 0xF
        val position = BlockPosition(getX(), getY(), getZ())
        val combined: Int = type + (data.toInt() shl 12)
        val blockData = net.minecraft.server.v1_8_R3.Block
            .getByCombinedId(combined)
        if (blockData === section!!.getType(x, y, z)) {
            return false
        }
        //section!!.b(x,y,z,15)
        //section.a(x,y,z,15)
        section!!.setType(x, y, z, blockData)
        //método responsavel por concertar as luzes porem da mais lag
        worldServer.x(position)
      //  worldServer.getLightLevel(position)



        if (applyPhysics) {
            worldServer.notify(position)
        }
        //super.setTypeIdAndData(type, data, applyPhysics)
        return true
    }

    override fun setTypeAndData(material: Material, data: Int) {
        setTypeIdAndData(material.id, data.toByte(), true)
    }

    override fun getRelative(modX: Int, modY: Int, modZ: Int): Block {
        return Blocks_v1_8_R3(x + modX, y + modY, z + modZ, worldServer)
    }

    private val section: ChunkSection?
        get() {
            val chunk = worldServer.chunkProviderServer
                .originalGetChunkAt(x shr 4, z shr 4)
            var chunkSection = chunk.sections[y shr 4]
            if (chunkSection == null) {
                chunk.sections[y shr 4] = ChunkSection(y shr 4 shl 4, !worldServer.worldProvider.o())
                chunkSection = chunk.sections[y shr 4]
            }
            return chunkSection
        }
}