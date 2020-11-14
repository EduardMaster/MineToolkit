package net.eduard.api.server.minigame

import net.eduard.api.lib.abstraction.Block
import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.modules.Mine
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Chest
import org.bukkit.util.Vector
import java.io.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * Schematic do WorldEdit Compacto
 *
 * @author Eduard
 */
class GameSchematic(var name : String = "Mapinha") {
    companion object {
        fun getIndex(x: Int, y: Int, z: Int, width: Int, length: Int): Int {
            return y * width * length + z * width + x
        }

        fun load(subfile: File): GameSchematic {
            return GameSchematic().reload(subfile)
        }
        fun log(message : String){
            Bukkit.getConsoleSender().sendMessage("§e[GameSchematic] §f"
            + message)
        }
    }



    @Transient
    var start = 0L

    @Transient
    var end = 0L
    val past get() = end - start



    var relative = Vector(0, 100, 0)
    var low = Vector(-10, 100, -10)
    var high = Vector(10, 100, 10)


    fun inside(location: Location) : Boolean {
        val x = location.blockX
        val y = location.blockY
        val z = location.blockZ
        return (low.blockX < x && high.blockX  > x &&
                low.blockY < y && high.blockY > y
                && low.blockZ < z && high.blockZ > z)
    }

    @Transient
    var count = 0
    var width: Short = 0
    var height: Short = 0
    var length: Short = 0

    @Transient
    var chests = mutableListOf<Chest>()

    @Transient
    lateinit var blocksId: ByteArray

    @Transient
    lateinit var blocksData: ByteArray


    fun copy(world: World) {
        copy(relative.toLocation(world), low.toLocation(world), high.toLocation(world))
    }

    fun copy(relativeLocation: Location) {
        val world = relativeLocation.world
        copy(relativeLocation, low.toLocation(world), high.toLocation(world))
    }

    fun copy(): GameSchematic {
        return Copyable.copyObject(this)
    }

    fun copy(
        relativeLocation: Location,
        firstLocation: Location,
        secondLocation: Location
    ) {
        start = System.currentTimeMillis()
        count = 0
        val highLoc =
            Mine.getHighLocation(firstLocation, secondLocation)
        val lowLoc = Mine.getLowLocation(firstLocation, secondLocation)
        high = highLoc.toVector()
        low = lowLoc.toVector()
        relative = relativeLocation.toVector()
        chests.clear()
        width = (highLoc.blockX - lowLoc.blockX).toShort()
        height = (highLoc.blockY - lowLoc.blockY).toShort()
        length = (highLoc.blockZ - lowLoc.blockZ).toShort()
        val size = width * height * length
        blocksId = ByteArray(size)
        blocksData = ByteArray(size)

        val worldUsed = relativeLocation.world
        for (x in 0 until width) {
            for (y in 0 until height) {
                for (z in 0 until length) {
                    count++
                    val index = getIndex( x, y, z, width.toInt(), length.toInt())
                    val block = worldUsed.getBlockAt(lowLoc.blockX + x,
                        lowLoc.blockY + y,
                        lowLoc.blockZ + z
                    )
                    val id = block.typeId
                    if (block.state is Chest) {
                        val chest = block.state as Chest
                        chests.add(chest)
                    }
                    blocksId[index] = id.toByte()
                    blocksData[index] = block.data
                }
            }
        }
        end = System.currentTimeMillis()
        log("Copiando blocos do schematic $name tempo levado ${past}ms")
    }

    fun paste(newRelative: Location, minusLag: Boolean = false) {
        start = System.currentTimeMillis()
        val worldUsed = newRelative.world
        chests.clear()
        val difX = newRelative.blockX - relative.blockX
        val difY = newRelative.blockY - relative.blockY
        val difZ = newRelative.blockZ - relative.blockZ
        count = 0
        for (x in 0 until width) {
            for (y in 0 until height) {
                for (z in 0 until length) {
                    count++
                    val index = getIndex(
                        x,y,z,
                        width.toInt(),
                        length.toInt()
                    )
                    val block = worldUsed.getBlockAt(
                        difX + low.blockX + x, difY + low.blockY + y,
                        difZ + low.blockZ + z
                    )
                    var typeId = blocksId[index]
                    var typeData = blocksData[index]
                    if (typeId < 0) {
                        typeId = 0
                    }
                    if (typeData < 0) {
                        typeData = 0
                    }
                    if (minusLag) {
                        if (typeId.toInt() == 0) {
                            continue
                        }
                    }
                    if (block != null) {
                        if (block.typeId != typeId.toInt() || block.data != typeData) {
                            val bloco = Block.get(block.location) ?: continue
                            bloco.setTypeAndData (
                                Material.getMaterial(typeId.toInt())
                                , typeData.toInt()
                            )
                        }
                        if (block.state is Chest) {
                            val chest = block.state as Chest
                            chests.add(chest)
                        }
                    }
                }
            }
        }
        end = System.currentTimeMillis()
        log("Colando blocos do schematic $name tempo levado ${past}ms")
    }

    fun setType(id: Byte, data: Byte) {
        for (i in blocksId.indices) {
            blocksId[i] = id
            blocksData[i] = data
        }
    }

    fun save(stream: OutputStream) {
        start = System.currentTimeMillis()
        try {
            val dataWriter = DataOutputStream(GZIPOutputStream(stream))
            dataWriter.writeShort(width.toInt())
            dataWriter.writeShort(height.toInt())
            dataWriter.writeShort(length.toInt())
            dataWriter.writeInt(blocksId.size)
            dataWriter.write(blocksId)
            dataWriter.writeInt(blocksId.size)
            dataWriter.write(blocksData)
            dataWriter.writeUTF(Mine.saveVector(low))
            dataWriter.writeUTF(Mine.saveVector(high))
            dataWriter.writeUTF(Mine.saveVector(relative))
            dataWriter.flush()
            dataWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        end = System.currentTimeMillis()
        log("Armazenando Schematic $name no HD tempo levado ${past}ms")
    }

    fun save(file: File) {
        file.parentFile.mkdirs()
        val stream = FileOutputStream(file)
        save(stream)
    }

    fun reload(stream: InputStream): GameSchematic {
        try {
            val dataReader = DataInputStream(GZIPInputStream(stream))
            width = dataReader.readShort()
            height = dataReader.readShort()
            length = dataReader.readShort()
            var size = dataReader.readInt()
            blocksId = ByteArray(size)
            dataReader.readFully(blocksId)
            size = dataReader.readInt()
            blocksData = ByteArray(size)
            dataReader.readFully(blocksData)
            low = Mine.toVector(dataReader.readUTF())
            high = Mine.toVector(dataReader.readUTF())
            relative = Mine.toVector(dataReader.readUTF())
            dataReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        log("Recuperando Schematic $name do HD tempo levado ${past}ms")
        return this
    }

    fun reload(file: File): GameSchematic {
        val fileReader = FileInputStream(file)
        reload(fileReader)
        return this
    }



}