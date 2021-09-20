package net.eduard.api.server.minigame

import net.eduard.api.lib.abstraction.Blocks
import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.modules.Mine
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.io.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * Schematic do WorldEdit Compacto
 *
 * @author Eduard
 */
class MinigameSchematic(var name: String = "Mapinha") {
    class BlockInfo() {
        var id: Short = 1
        var data: Byte = 0

    }

    companion object {
        lateinit var MAPS_FOLDER: File

        private val cache = mutableMapOf<Player, MinigameSchematic>()
        private val schematics = mutableMapOf<String, MinigameSchematic>()

        fun isEditing(player: Player) = cache.containsKey(player)

        fun exists(name: String) = schematics.containsKey(name)
        fun loadToCache(player: Player, name: String) {
            cache[player] = schematics[name]!!
        }
        fun unloadAll(){
            cache.clear()
            schematics.clear()
        }

        fun loadAll(folder: File) {
            folder.mkdirs()
            if (folder.listFiles() == null)
                return

            for (subfile in folder.listFiles()!!) {
                if (!subfile.isDirectory) {
                    val schema = load(subfile)

                    schematics[schema.name] = schema
                }
            }
        }

        fun saveAll(folder: File) {
            folder.mkdirs()
            for ((name, schematic) in schematics) {
                schematic.save(
                    File(
                        MAPS_FOLDER,
                        "$name.map"
                    )
                )
            }

        }

        fun hasSchematic(name: String) = name in schematics
        fun getSchematic(name: String) = schematics[name]!!

        fun getSchematic(player: Player): MinigameSchematic {
            var gameSchematic = cache[player]
            if (gameSchematic == null) {
                gameSchematic = MinigameSchematic()
                cache[player] = gameSchematic
            }
            return gameSchematic
        }


        fun getIndex(x: Int, y: Int, z: Int, width: Int, length: Int): Int {
            return y * width * length + z * width + x
        }

        fun load(subfile: File): MinigameSchematic {
            val name = subfile.name.replace(".map", "")
            return MinigameSchematic(name).reload(subfile)
        }

        fun log(message: String) {
            Bukkit.getConsoleSender().sendMessage(
                "§b[MinigameSchematic] §f"
                        + message
            )
        }
    }

    fun register() {
        schematics[name] = this
    }

    fun unregister() {
        schematics.remove(name)
    }


    @Transient
    var start = 0L

    @Transient
    var end = 0L
    val past get() = end - start


    var relative = Vector(0, 100, 0)
    var low = Vector(-10, 100, -10)
    var high = Vector(10, 100, 10)


    fun inside(location: Location): Boolean {
        val x = location.blockX
        val y = location.blockY
        val z = location.blockZ
        return (low.blockX < x && high.blockX > x &&
                low.blockY < y && high.blockY > y
                && low.blockZ < z && high.blockZ > z)
    }

    @Transient
    var count = 0
    var width: Short = 0
    var height: Short = 0
    var length: Short = 0

    @Transient
    var blocks: Array<BlockInfo?> = arrayOf()


    fun copy(world: World) {

        copy(relative.toLocation(world), low.toLocation(world), high.toLocation(world))
    }

    fun getAllChests(relative: Location): MutableList<Chest> {
        val worldUsed = relative.world
        val startX = low.blockX + (relative.blockX - this.relative.blockX)
        val startY = low.blockY + (relative.blockY - this.relative.blockY)
        val startZ = low.blockZ + (relative.blockZ - this.relative.blockZ)
        val list = mutableListOf<Chest>()
        for (x in 0 until width) {
            for (y in 0 until height) {
                for (z in 0 until length) {
                    val block = worldUsed.getBlockAt(
                        startX + x,
                        startY + y,
                        startZ + z
                    )
                    if (block.state is Chest) {
                        list.add(block.state as Chest)
                    }
                }
            }
        }
        return list
    }

    fun copy(relativeLocation: Location) {
        val world = relativeLocation.world
        copy(relativeLocation, low.toLocation(world), high.toLocation(world))
    }

    fun copy(): MinigameSchematic {
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
        width = (highLoc.blockX - lowLoc.blockX).toShort()
        height = (highLoc.blockY - lowLoc.blockY).toShort()
        length = (highLoc.blockZ - lowLoc.blockZ).toShort()
        val size = width * height * length
        blocks = Array(size) { BlockInfo() }
        val worldUsed = relativeLocation.world


        for (x in 0 until width) {
            for (y in 0 until height) {
                for (z in 0 until length) {
                    count++
                    val index = getIndex(x, y, z, width.toInt(), length.toInt())
                    val block = worldUsed.getBlockAt(
                        lowLoc.blockX + x,
                        lowLoc.blockY + y,
                        lowLoc.blockZ + z
                    )

                    val id = block.typeId
                    if (id == 0) {
                        blocks[index] = null
                        continue
                    }
                    val blockInfo = blocks[index] ?: BlockInfo()
                    blockInfo.id = id.toShort()
                    blockInfo.data = block.data
                    blocks[index] = blockInfo

                }
            }
        }
        end = System.currentTimeMillis()
        log("Copiando blocos do schematic $name tempo levado ${past}ms")
    }

    fun paste(newRelative: Location, minusLag: Boolean = false) {
        start = System.currentTimeMillis()
        val worldUsed = newRelative.world

        val difX = newRelative.blockX - relative.blockX
        val difY = newRelative.blockY - relative.blockY
        val difZ = newRelative.blockZ - relative.blockZ
        log("Colando com Diferencial de: X: $difX, Y: $difY, Z: $difZ")

        count = 0
        for (x in 0 until width) {
            for (y in 0 until height) {
                for (z in 0 until length) {
                    count++
                    val index = getIndex(
                        x, y, z,
                        width.toInt(),
                        length.toInt()
                    )
                    val block = worldUsed.getBlockAt(
                        difX + low.blockX + x, difY + low.blockY + y,
                        difZ + low.blockZ + z
                    )

                    val blockInfo = blocks[index]
                    var typeId: Short
                    var typeData: Byte
                    if (blockInfo == null) {
                        typeId = 0
                        typeData = 0
                    } else {
                        typeId = blockInfo.id
                        typeData = blockInfo.data
                    }


                    if (typeId < 0) {
                        typeId = 0
                    }
                    if (typeData < 0) {
                        typeData = 0
                    }
                    if (block == null) continue

                    if (minusLag && (block.typeId == typeId.toInt()
                                && block.data == typeData)) {
                        continue
                    }
                    val blockOf = Blocks.get(block.location) ?: continue
                    blockOf.setTypeAndData(
                        Material.getMaterial(typeId.toInt()), typeData.toInt()
                    )

                }
            }
        }

        end = System.currentTimeMillis()
        log("Colando blocos do schematic $name tempo levado ${past}ms")
    }

    fun setType(id: Short, data: Byte) {
        if (id == 0.toShort()) {
            for (i in blocks.indices) {
                blocks[i] = null
            }
        } else {
            for (i in blocks.indices) {
                val block = blocks[i] ?: BlockInfo()
                block.id = id
                block.data = data
                blocks[i] = block
            }
        }

    }

    fun save(stream: OutputStream) {
        start = System.currentTimeMillis()
        try {
            val dataWriter = DataOutputStream(GZIPOutputStream(stream))
            dataWriter.writeShort(width.toInt())
            dataWriter.writeShort(height.toInt())
            dataWriter.writeShort(length.toInt())
            val byteArrayWriter = ByteArrayOutputStream(blocks.size * 3)
            val byteArrayDataWriter = DataOutputStream(byteArrayWriter)
            var blocksWrited = 0
            for (block in blocks) {
                if (block == null) {
                    byteArrayDataWriter.writeShort(0)
                    byteArrayDataWriter.writeByte(0)
                } else {
                    byteArrayDataWriter.writeShort(block.id.toInt())
                    byteArrayDataWriter.writeByte(block.data.toInt())
                }
                blocksWrited++

            }

            //log("Escreveu $blocksWrited no arquivo")
            //log("Escreveu ${byteArrayWriter.size()} bytes no arquivo")
            dataWriter.writeInt(byteArrayWriter.size())
            dataWriter.write(byteArrayWriter.toByteArray())
            byteArrayDataWriter.close()
            dataWriter.writeUTF(Mine.serializeVector(low))
            dataWriter.writeUTF(Mine.serializeVector(high))
            dataWriter.writeUTF(Mine.serializeVector(relative))
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

    fun reload(stream: InputStream): MinigameSchematic {
        try {
            val dataReader = DataInputStream(GZIPInputStream(stream))
            width = dataReader.readShort()
            height = dataReader.readShort()
            length = dataReader.readShort()
            val size = dataReader.readInt() / 3
            // log("Size of blocks $size")
            blocks = Array(size) { BlockInfo() }
            val array = ByteArray(size * 3)
            dataReader.readFully(array)
            val arrayReader = ByteArrayInputStream(array)
            val arrayDataReader = DataInputStream(arrayReader)
            for (i in 0 until size) {
                var block = blocks[i]
                val id = arrayDataReader.readShort()
                val data = arrayDataReader.readByte()
                if (id == 0.toShort()) {
                    blocks[i] = null
                } else {
                    block = block ?: BlockInfo()
                    block.id = id
                    block.data = data
                    blocks[i] = block
                }
            }
            low = Mine.deserializeVector(dataReader.readUTF())
            high = Mine.deserializeVector(dataReader.readUTF())
            relative = Mine.deserializeVector(dataReader.readUTF())
            dataReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        log("Recuperando Schematic $name do HD tempo levado ${past}ms")
        return this
    }

    fun reload(file: File): MinigameSchematic {
        val fileReader = FileInputStream(file)
        reload(fileReader)
        return this
    }


}