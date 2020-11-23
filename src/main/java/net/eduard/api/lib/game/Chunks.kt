package net.eduard.api.lib.game

import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.storage.annotations.StorageAttributes
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location

/**
 * Representa uma Chunk Fake
 *
 * @author Eduard
 */
@StorageAttributes(inline = true)
class Chunks() {
    var world: String = "world"
    var x = 0
    var z = 0
    val chunk: Chunk
        get() = Bukkit.getWorld(world).getChunkAt(x, z)

    constructor(location: Location) : this(){
        x = location.chunk.x
        z = location.chunk.z
    }

    fun copy(): Chunks {
        return Copyable.copyObject(this)
    }

    fun newChunk(x: Int, z: Int): Chunks {
        return Chunks().also {
            it.world = world
            it.x = x
            it.z = z
        }
    }

}