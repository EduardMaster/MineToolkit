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
    init {
        save<World>(50) { name }
        load {
            Bukkit.getWorld(it)
        }
        save<Location>(150) {
            "${world.name};$x;$y;$z;$yaw;$pitch"
        }
        load {
            val split = it.split(";")
            Location(
                Bukkit.getWorld(split[0]), split[1].toDouble(),
                split[2].toDouble(), split[3].toDouble(), split[4].toFloat()
                , split[5].toFloat()
            )
        }
        save<Vector>(75) { "$x;$y;$z" }
        load {
            val (x, y, z) = it.split(";")
                .map { n -> n.toDouble() }
            Vector(
                x, y, z
            )
        }
        save<FakePlayer>(100) {
            "$name;$uniqueId"
        }
        load {
            val split = it.split(";")
            FakePlayer(split[0], UUID.fromString(split[1]))
        }
        save<Enchantment>(100) {
            "$id"
        }
        load {
            Enchantment.getById(Extra.toInt(it))
        }

    }
}





