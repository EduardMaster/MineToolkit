package net.eduard.api.lib.database

import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.FakePlayer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.enchantments.Enchantment
import org.bukkit.util.Vector
import java.util.*

object BukkitTypes {
   fun register() {
        customType<World> {
            saveMethod = {
                name
            }
            reloadMethod = {
                Bukkit.getWorld(this)
            }
        }
        customType<Location> {
            saveMethod = {
                "${world.name};$x;$y;$z;$yaw;$pitch"
            }
            reloadMethod = {
                val split = split(";")
                Location(
                    Bukkit.getWorld(split[0]), split[1].toDouble(),
                    split[2].toDouble(), split[3].toDouble(), split[4].toFloat()
                    , split[5].toFloat()
                )
            }
        }
        customType<Vector> {
            saveMethod = {
                "$x;$y;$z"
            }
            reloadMethod = {
                val (x, y, z) = split(";")
                    .map { n -> n.toDouble() }
                Vector(
                    x, y, z
                )
            }
        }
        customType<FakePlayer> {
            saveMethod = {
                "$name;$uniqueId"
            }
            reloadMethod = {
                val split = split(";")
                FakePlayer(split[0], UUID.fromString(split[1]))
            }
            sqlSize=100
        }
        customType<Enchantment> {
            saveMethod = {
                "$id"
            }
            reloadMethod = {
                Enchantment.getById(Extra.toInt(this))
            }
            sqlSize=100
        }
    }
}





