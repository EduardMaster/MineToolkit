package net.eduard.api.server.minigame

import net.eduard.api.lib.abstraction.Blocks
import net.eduard.api.lib.manager.TimeManager
import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.modules.Mine
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Chest
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk
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
class MinigameSchematic(var name: String) {

    class BlockInfo() {
        var id: Short = 1
        var data: Byte = 0
    }

    /*
    class SchematicLightFixer(val blocks: List<Blocks>) : TimeManager(1L) {
        var ticks = 0
        val it = blocks.iterator();

        init {
            log("Iniciando atualizacao de luzes desta colagem " + blocks.size)
        }

        override fun run() {
            var current = 0
            val start = System.currentTimeMillis()
            while (it.hasNext() && current < 10000) {
                val block = it.next();
                block.fixLightning()
                block.sendPacket()
                current++
            }
            val end = System.currentTimeMillis()
            val past = end - start
            //log("Luzes Atualizando [$ticks] Tempo levado §f(${past}ms)")
            ticks++
            if (!it.hasNext()) {
                log("Luzes da colagem arrumadas §f(${ticks}ticks)")
                stopTask()
                /*
                for (block in blocks){
                    block.sendPacket()
                }

                 */
            }
        }

    }
     */

    class SchematicPasting(
        val schematic: MinigameSchematic,
        val locationBase: Vector,
        val maxBlockReadPerTick: Int,
        val world: World
    ) : TimeManager(1L) {
        var needUpdateLight: MutableList<Blocks> = ArrayList(schematic.size)
        var currentStage = 0
        var currentX = 0
        var currentY = 0
        var currentZ = 0
        var tick = 1

        fun nextBlock() {
            currentStage++
            currentX++
            if (currentX >= schematic.width) {
                currentZ++
                currentX = 0
                if (currentZ >= schematic.length) {
                    currentY++
                    currentZ = 0
                }
            }
        }

        fun fixLightning() {
            val lightStart = System.currentTimeMillis();
            log("Iniciando concerto de luzes")
            var chunksUpdate = 0
            val finalLoc = locationBase.clone()
                .add(Vector(schematic.width.toInt(), schematic.height.toInt(), schematic.length.toInt()))
            for (x in (locationBase.blockX shr 4) until (finalLoc.blockX shr 4)) {
                for (z in (locationBase.blockZ shr 4) until (finalLoc.blockZ shr 4)) {
                    val chunk = world.getChunkAt(x, z) as CraftChunk;
                    val chunkNms = chunk.handle
                   chunkNms.initLighting()
                    chunksUpdate++
                }
            }
            val lightEnd = System.currentTimeMillis();
            val lightPast = lightEnd - lightStart
            log("Finalizando concerto de luzes Chunks: $chunksUpdate §f${lightPast}ms ")

        }

        fun hasNextBlock() = currentY < schematic.height && currentX < schematic.width && currentZ < schematic.length

        var actionWhenFinish: Runnable? = null
        var startMoment = System.currentTimeMillis();

        init {
            syncTimer()
            log("Iniciando colagem, quantidade de blocos: §f" + schematic.size)

        }


        fun finished(): Boolean {
            return !hasNextBlock()
        }

        var blocksChanged = 0
        var finished = false
        var start = System.currentTimeMillis()
        var end = System.currentTimeMillis()
        val past get() = end - start
        var blocksRead = 0

        override fun run() {
            try {
                if (finished) {
                    return
                }
                start = System.currentTimeMillis();

                while (hasNextBlock()) {
                    val index = getIndex(currentX, currentY, currentZ, schematic.width, schematic.length)
                    if (blocksRead > maxBlockReadPerTick) {
                        break;
                    }
                    if (finished()) {
                        break;
                    }
                    val blockInfo = schematic.blocks[index]
                    val typeID = blockInfo?.id?.toInt() ?: 0;
                    val typeData = blockInfo?.data?.toInt() ?: 0
                    val blockOf = Blocks.get(
                        locationBase.x.toInt() + currentX,
                        locationBase.y.toInt() + currentY,
                        locationBase.z.toInt() + currentZ,
                        world, null
                    ) ?: continue
                    if ((blockOf.getType().id == typeID)) {
                        blocksRead++
                        nextBlock()
                        continue
                    }
                    val hasChange = blockOf.setTypeAndData(Material.getMaterial(typeID), typeData)
                    if (hasChange) {
                        needUpdateLight.add(blockOf)
                    }
                    nextBlock()
                    blocksChanged++
                    blocksRead++
                }
                blocksRead = 0
                end = System.currentTimeMillis();
                tick++
                log("Colagem Parte [$tick] Blocos lidos: ${currentStage} / Alterados: $blocksChanged §f(${past}ms)")

                if (finished()) {
                    val pasteDuration = System.currentTimeMillis() - startMoment
                    log("Colagem finalizados em $tick ticks, blocos modificados: $blocksChanged §f(${pasteDuration}ms)")
                    stopTask()
                    finished = true
                    actionWhenFinish?.run()
                    fixLightning()
                    //SchematicLightFixer(needUpdateLight).syncTimer()
                }


            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }


    companion object {
        var MAPS_FOLDER: File = File("schematics/")
        private val cache = mutableMapOf<Player, MinigameSchematic>()
        private val schematics = mutableMapOf<String, MinigameSchematic>()

        fun isEditing(player: Player) = cache.containsKey(player)
        fun exists(name: String) = schematics.containsKey(name)
        fun loadToCache(player: Player, name: String) {
            cache[player] = schematics[name]!!
        }

        fun unloadAll() {
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
                    if (schema.isBugged()) {
                        log("O Schematic ${schema.name} esta bugado e nao foi descarregado")
                    } else {
                        schematics[schema.name] = schema
                    }
                }
            }
        }

        fun saveAll(folder: File) {
            folder.mkdirs()
            for ((name, schematic) in schematics) {
                schematic.save(File(MAPS_FOLDER, "$name.map"))
            }
        }

        fun hasSchematic(name: String) = name in schematics
        fun getSchematic(name: String) = schematics[name]!!
        fun getSchematics() = schematics.values
        fun getSchematic(player: Player): MinigameSchematic {
            var gameSchematic = cache[player]
            if (gameSchematic == null) {
                gameSchematic = newSchematic(player)
            }
            return gameSchematic
        }

        fun newSchematic(player: Player): MinigameSchematic {
            val gameSchematic = MinigameSchematic()
            cache[player] = gameSchematic
            return gameSchematic
        }


        fun getIndex(x: Int, y: Int, z: Int, width: Short, length: Short): Int {
            return y * width * length + z * width + x
        }

        fun load(subfile: File): MinigameSchematic {
            val name = subfile.name.replace(".map", "")
            return MinigameSchematic(name).reload(subfile)
        }

        fun log(message: String) {
            Mine.console("§b[MinigameSchematic] §3$message")
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

    val size get() = width * length * height

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

    fun emptyBlockArray() {
        blocks = Array(width * height * length) { null }
    }


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
                    val block = worldUsed.getBlockAt(startX + x, startY + y, startZ + z)
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

    fun prepare(world: World, relativeVec: Vector, firstVec: Vector, secondVec: Vector) {
        prepare(relativeVec.toLocation(world), firstVec.toLocation(world), secondVec.toLocation(world))
    }

    fun prepare(relativeLocation: Location, firstLocation: Location, secondLocation: Location) {
        val highLoc = Mine.getHighLocation(firstLocation, secondLocation)
        val lowLoc = Mine.getLowLocation(firstLocation, secondLocation)
        high = highLoc.toVector()
        low = lowLoc.toVector()
        relative = relativeLocation.toVector()
        width = (highLoc.blockX - lowLoc.blockX).toShort()
        height = (highLoc.blockY - lowLoc.blockY).toShort()
        length = (highLoc.blockZ - lowLoc.blockZ).toShort()
        emptyBlockArray()
    }


    fun copy(
        relativeLocation: Location,
        firstLocation: Location,
        secondLocation: Location
    ) {
        start = System.currentTimeMillis()
        log("Iniciando copiamento de blocos do $name")
        count = 0


        emptyBlockArray()
        val worldUsed = relativeLocation.world
        prepare(relativeLocation, firstLocation, secondLocation)
        val lowX = low.blockX
        val lowY = low.blockY
        val lowZ = low.blockZ
        for (y in 0 until height) {
            for (z in 0 until length) {
                for (x in 0 until width) {
                    count++
                    val index = getIndex(x, y, z, width, length)
                    val block = worldUsed.getBlockAt(lowX + x, lowY + y, lowZ + z)

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
        log("Copiamento de blocos do $name finalizado §f(${past}ms)")
    }

    fun paste(newRelative: Location): SchematicPasting {
        return paste(newRelative, true)
    }

    fun paste(newRelative: Location, minusLag: Boolean): SchematicPasting {
        start = System.currentTimeMillis()
        val worldUsed = newRelative.world
        val difX = (newRelative.blockX - relative.blockX)
        val difY = (newRelative.blockY - relative.blockY)
        val difZ = (newRelative.blockZ - relative.blockZ)
        log("Escaneando mapa $name com Diferencial de §fX: $difX, Y: $difY, Z: $difZ")
        count = 0
        val pasteBase = Vector(low.blockX + difX  , low.blockY + difY , low.blockZ + difZ )
        end = System.currentTimeMillis()
        log("Iniciando Colagem do $name §f(${past}ms)")
        val pasting = SchematicPasting(this, pasteBase, 100000, worldUsed)

        return pasting;
    }

    fun setType(id: Short, data: Byte) {
        start = System.currentTimeMillis()
        val zeroShort = 0.toShort();
        if (id == zeroShort) {
            for (blockIndex in blocks.indices) {
                blocks[blockIndex] = null
            }
        } else {
            for (blockIndex in blocks.indices) {
                val block = blocks[blockIndex] ?: BlockInfo()
                block.id = id
                block.data = data
                blocks[blockIndex] = block
            }
        }
        end = System.currentTimeMillis()
        log("Blocos modificados para $id:$data §f(${past}ms)")
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
        log("Armazenando Schematic $name no HD tempo levado §f(${past}ms)")
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
            val zeroShort = 0.toShort();
            for (blockIndex in 0 until size) {
                var block = blocks[blockIndex]
                val id = arrayDataReader.readShort()
                val data = arrayDataReader.readByte()
                if (id == zeroShort) {
                    blocks[blockIndex] = null
                } else {
                    block = block ?: BlockInfo()
                    block.id = id
                    block.data = data
                    blocks[blockIndex] = block
                }
            }
            low = Mine.deserializeVector(dataReader.readUTF())
            high = Mine.deserializeVector(dataReader.readUTF())
            relative = Mine.deserializeVector(dataReader.readUTF())
            dataReader.close()
            stream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        log("Recuperando Schematic $name do HD tempo levado §f(${past}ms)")

        return this
    }

    fun isBugged() = width == 0.toShort()
            || length == 0.toShort()
            || height == 0.toShort()

    fun reload(file: File): MinigameSchematic {
        val fileReader = FileInputStream(file)
        reload(fileReader)
        return this
    }

    constructor() : this("Mapinha")

}