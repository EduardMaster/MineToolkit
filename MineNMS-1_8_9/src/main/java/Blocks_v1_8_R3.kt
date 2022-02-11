package net.eduard.api.lib.abstraction

import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld

/**
 *
 */
class Blocks_v1_8_R3(
    x: Int, y: Int, z: Int, blockChunk: Chunk?,
    world: World
) : CraftBlock((blockChunk as CraftChunk?), x, y, z),
    Blocks {
    val position = BlockPosition(x, y, z)
    val worldServer: WorldServer = (world as CraftWorld).handle
    var chunkCache: net.minecraft.server.v1_8_R3.Chunk? =
        (blockChunk as CraftChunk?)?.handle ?: worldServer.chunkProviderServer.originalGetChunkAt(x shr 4, z shr 4)

    override fun getWorld(): World {
        return worldServer.world
    }


    override fun getChunk(): Chunk {
        return worldServer.getChunkAt(x shr 4, z shr 4).bukkitChunk
    }

    override fun getType(): Material {
        return Material.getMaterial(typeId)
    }

    override fun getTypeId(): Int {
        val section = section ?: return 0
        val type = section.getType(x and 0xF, y and 0xF, z and 0xF)
        val block = type.block
        return net.minecraft.server.v1_8_R3.Block.getId(block)
    }

    override fun getData(): Byte {
        val section = section ?: return 0
        val type = section.getType(x and 0xF, y and 0xF, z and 0xF)
        val block = type.block
        return block.toLegacyData(type).toByte()
    }

    override fun setType(material: Material) {
        setTypeIdAndData(material.id, 0, false)
    }

    override fun setTypeAndData(material: Material, data: Int): Boolean {
        return setTypeIdAndData(material.id, data.toByte(), false)
    }

    override fun setTypeIdAndData(type: Int, data: Byte, applyPhysics: Boolean): Boolean {
        val section = section ?: return false
        val xSec = x and 0xF
        val ySec = y and 0xF
        val zSec = z and 0xF
        val combined: Int = type + (data.toInt() shl 12)
        val blockData = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combined)
        if (blockData === section.getType(xSec, ySec, zSec)) {
            //println("Travou aqui type $type  / $data")
            return false
        }
        section.setType(xSec, ySec, zSec, blockData)
        //worldServer.x(position)
        // altera a luz emitida do bloco (eles ficam igual a glowstone)
        //section.b(x, y, z, 0)
        //altera a luz do ceu aparentemente setar 15 n ta ajudando
        //section.a(xSec, (y shr 0xF) and 0xF, zSec, 15)

        if (applyPhysics) {
            // worldServer.notifyAndUpdatePhysics(position)
        } else {
            //println("notificando mudanca")

        }
        // Mine.broadcast("Modificando bloco")
        worldServer.x(position)
        Bukkit.getScheduler().runTask(Bukkit.getPluginManager().plugins.first(), this::sendPacket)
        // sendPacket()
        // Bukkit.getScheduler().runTask(Bukkit.getPluginManager().plugins.first(), this::sendPacket)
        //super.setTypeIdAndData(type, data, applyPhysics)
        return true
    }

    override fun sendPacket() {
        worldServer.notify(position)
    }

    override fun fixLightning() {
        //método responsavel por concertar as luzes porem da mais lag
        worldServer.x(position)
        //worldServer.getLightLevel(position)
        sendPacket()
    }


    override fun getRelative(modX: Int, modY: Int, modZ: Int): Block {
        return Blocks_v1_8_R3(x + modX, y + modY, z + modZ, chunkCache?.bukkitChunk, worldServer.world)
    }


    private val section: ChunkSection?
        get() {
            val x = x
            val y = y
            val z = z
            try {
                val chunk = chunkCache ?: return null
                // val chunk = worldServer.chunkProviderServer.originalGetChunkAt(x shr 4, z shr 4)
                val yBitShifted = (y shr 4) and 0xF
                if (yBitShifted >= 16) {
                    return null;
                }
                var chunkSection = chunk.sections[yBitShifted]
                if (chunkSection == null) {
                    chunkSection = ChunkSection(y shr 4 shl 4, !worldServer.worldProvider.o())
                    chunk.sections[y shr 4] = chunkSection
                    //chunkSection = chunk.sections[y shr 4]
                }
                return chunkSection
            } catch (ex: Exception) {
                return null;
            }
        }
}