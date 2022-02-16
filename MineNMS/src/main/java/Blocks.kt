package net.eduard.api.lib.abstraction

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import java.lang.reflect.Constructor

interface Blocks {
    fun setType(material: Material)
    fun setTypeAndData(material: Material, data: Int): Boolean
    fun getType(): Material
    fun getX(): Int
    fun getY(): Int
    fun getZ(): Int
    fun sendPacket()

    companion object {

        private lateinit var blockClass: Class<*>
        private lateinit var blockCoordsConstrutor: Constructor<*>
        fun get(location: Location): Blocks? {
              return get(location.blockX, location.blockY,location.blockZ ,location.world , null)
        }
        fun get(block : Block): Blocks? {
            return get(block.x, block.y, block.z , block.world , block.chunk)
        }
        fun get(x: Int, y: Int, z: Int, world: World, chunk:  Chunk?): Blocks? {
            return try {
                if (!Companion::blockClass.isInitialized) {
                    blockClass = Class.forName("net.eduard.api.lib.abstraction.Blocks_" + Minecraft.getVersion())
                }
                if (!Companion::blockCoordsConstrutor.isInitialized)
                {
                    blockCoordsConstrutor = blockClass.getDeclaredConstructor(
                        Int::class.java,
                        Int::class.java,
                        Int::class.java,
                        Chunk::class.java,
                        World::class.java
                    )
                }
                blockCoordsConstrutor.newInstance(x, y, z,chunk, world) as Blocks
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}