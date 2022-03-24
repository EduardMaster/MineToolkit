package net.eduard.api.lib.abstraction

import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature
import org.bukkit.entity.Creature
import java.util.concurrent.TimeUnit
import kotlin.math.pow

class PathFinderFollowLocation(val creature: Creature, val targetLocation: Location, val speed: Double) :
    EntityRule_v1_8_R3() {
    val nmsCreature get() = (creature as CraftCreature).handle
    var lastFollow = System.currentTimeMillis()
    val followDelay = TimeUnit.SECONDS.toMillis(5)
    val canFollow
        get() = System.currentTimeMillis() > (lastFollow + followDelay)
                && nmsCreature.goalTarget == null
    var toFollowLocation: Location = targetLocation
    val minDistanceSquared get() = 2.0.pow(2.0)

    override fun run() {
        lastFollow = System.currentTimeMillis();
        if (targetLocation.distanceSquared(creature.location) > 20.0.pow(2.0)) {
            updateToFollowLocation()
        }
        nmsCreature.navigation.a(toFollowLocation.x, toFollowLocation.y, toFollowLocation.z, speed)
    }

    fun updateToFollowLocation() {
        val diference = creature.location.subtract(targetLocation)
        toFollowLocation = creature.location.add(diference.toVector().normalize().multiply(-19))
    }

    override fun canRun(): Boolean {
        return canFollow
    }

    override fun finished(): Boolean {
        return !nmsCreature.navigation.m() && (creature.target != null || creature.location.distanceSquared(
            targetLocation
        ) < minDistanceSquared)
    }

    override fun unregister() {

    }

}