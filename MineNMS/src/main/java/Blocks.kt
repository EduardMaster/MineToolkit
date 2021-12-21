package net.eduard.api.lib.abstraction

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World

interface Blocks {

    fun setType(material: Material)
    fun setTypeAndData(material: Material, data: Int) : Boolean
    fun getType(): Material
    fun getX(): Int
    fun getY(): Int
    fun getZ(): Int
    fun fixLightning()
    fun sendPacket()

    companion object {

        private lateinit var blockClass: Class<*>
        fun get(location: Location): Blocks? {
            return try {
                if (!Companion::blockClass.isInitialized) {
                    blockClass = Class.forName(
                        "net.eduard.api.lib.abstraction.Blocks_"
                                + Minecraft.getVersion())
                }
                blockClass.getDeclaredConstructor(Location::class.java)
                    .newInstance(location) as Blocks

            } catch (e: Exception) {
                null
            }
        }
        fun get(x : Int, y : Int , z: Int, world : World): Blocks? {
            return try {
                if (!Companion::blockClass.isInitialized) {
                    blockClass = Class.forName(
                        "net.eduard.api.lib.abstraction.Blocks_"
                                + Minecraft.getVersion())
                }
                blockClass.getDeclaredConstructor(Int::class.java, Int::class.java, Int::class.java, World::class.java)
                    .newInstance(x,y,z,world) as Blocks

            } catch (e: Exception) {
                null
            }
        }
    }
}