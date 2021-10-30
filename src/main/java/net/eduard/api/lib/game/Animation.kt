package net.eduard.api.lib.game

import org.bukkit.entity.ArmorStand

/**
 * Sistema de animar [ArmorStand]
 *
 * @author Eduard
 * @version 1.0
 * @since 2.5
 */
class Animation(val stand: ArmorStand) {

    private val one_degree = Math.PI / 180


    fun moveHead(x: Double, y: Double, z: Double) {
        val headAngule = stand.headPose.add(x, y, z)
        stand.headPose = headAngule
    }

    fun moveHeadFront(degrees: Int) {
        moveHead(one_degree * degrees, 0.0, 0.0)
    }

    fun moveHeadBack(degrees: Int) {
        moveHead(-one_degree * degrees, 0.0, 0.0)
    }

    fun moveHeadUp(degrees: Int) {
        moveHead(0.0, 0.0, one_degree * degrees)
    }

    fun moveHeadDown(degrees: Int) {
        moveHead(0.0, 0.0, -one_degree * degrees)
    }

    fun moveHeadLeft(degrees: Int) {
        moveHead(0.0, one_degree * degrees, 0.0)
    }

    fun moveHeadRight(degrees: Int) {
        moveHead(0.0, -one_degree * degrees, 0.0)
    }

}