package net.eduard.api.lib.abstraction

import net.minecraft.server.v1_8_R3.*
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature
import org.bukkit.entity.Creature

class PathFinderFollowLocation(val creature: Creature, val targetLocation: Location, val speed : Double, val minDistance : Double = 4.0) : PathfinderGoal() {
    val nmsCreature get() = (creature as CraftCreature).handle
    var lastFollow = System.currentTimeMillis()
    val followDelay = 1000L
    var lastPath : PathEntity? = null
    val canFollow get() = System.currentTimeMillis() > (lastFollow + followDelay)
    lateinit var toFollowLocation: Location
    val minDistanceSquared get() = minDistance * minDistance

    fun debug(msg: String) {
      //  Mine.console("[EntityID: " + nmsCreature.id + "] " + msg)
    }
    fun run() {
        //debug("Movendo")
        lastFollow = System.currentTimeMillis();
        if (targetLocation.distanceSquared(creature.location) > 20) {
            updateToFollowLocation()
        }
        nmsCreature.navigation.a(toFollowLocation.x, toFollowLocation.y, toFollowLocation.z, speed)
    }
    init {
        updateToFollowLocation()
    }

    fun updateToFollowLocation() {
        val diference = creature.location.subtract(targetLocation)
        toFollowLocation = creature.location.add(diference.toVector().normalize().multiply(-19))

       // debug("§cNew TargetLocation: $toFollowLocation ")
       // debug("§bTargetLocation Real: $targetLocation ")
    }


    override fun a(): Boolean {
        return canStart()
    }

    override fun c() {
        run()
    }

    override fun d() {

    }

    override fun i(): Boolean {
        return true
    }


    override fun b(): Boolean {
        return needStop();
    }


    fun canStart(): Boolean {
        return canFollow && !needStop();
    }

    fun needStop(): Boolean {
        //Mine.console("Precisa parar")
        return creature.location.distanceSquared(targetLocation) < minDistanceSquared;
    }

    fun stop() {
        //debug("Parando de Mover")
    }




    /*
    class PathfinderGoalMoveTowardsTarget(private val a: EntityCreature, private val f: Double, private val g: Float) :
        PathfinderGoal() {
        private var b: EntityLiving? = null
        private var c = 0.0
        private var d = 0.0
        private var e = 0.0
        override fun a(): Boolean {
            b = this.a.goalTarget
            return if (b == null) {
                false
            } else if (b!!.h(this.a) > (g * g).toDouble()) {
                false
            } else {
                val var1 = RandomPositionGenerator.a(
                    this.a, 16, 7, Vec3D(
                        b!!.locX, b!!.locY, b!!.locZ
                    )
                )
                if (var1 == null) {
                    false
                } else {
                    c = var1.a
                    d = var1.b
                    e = var1.c
                    true
                }
            }
        }

        override fun b(): Boolean {
            return !this.a.navigation.m() && b!!.isAlive
             && b!!.h(this.a) < (g * g).toDouble()
        }

        override fun d() {
            b = null
        }

        override fun c() {
            this.a.navigation.a(c, d, e, f)
        }

        init {
            this.a(1)
        }
    }

     */

}