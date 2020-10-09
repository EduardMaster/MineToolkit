package net.eduard.api.lib.abstraction

import net.eduard.api.lib.modules.MineReflect
import org.bukkit.Location
import org.bukkit.Material
import java.lang.Exception

interface Block {

    fun setType(material: Material)
    fun setTypeAndData(material: Material, data: Int)
    fun getType(): Material
    fun getX(): Int
    fun getY(): Int
    fun getZ(): Int

    companion object {

        private lateinit var blockClass: Class<*>
        fun get(location: Location): Block? {
            return try {

                if (!Companion::blockClass.isInitialized) {
                    blockClass = Class.forName(
                        "net.eduard.api.lib.abstraction.Block_"
                                + MineReflect.getVersion()
                    )
                }


                blockClass.getDeclaredConstructor(Location::class.java)
                    .newInstance(location) as Block


            } catch (e: Exception) {
                null
            }
        }
    }
}