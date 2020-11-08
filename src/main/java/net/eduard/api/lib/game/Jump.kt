package net.eduard.api.lib.game

import org.bukkit.entity.Entity
import org.bukkit.util.Vector



class Jump {
    var isWithHigh = true
    var isHighFirst = false
    var force = 2.0
    var high = 0.5
    var sound: SoundEffect? = null
    var isUseVector = false
    var vector: Vector? = null

    constructor() {}
    constructor(sound: SoundEffect, vector: Vector) {
        this.sound = sound
        isUseVector = true
        this.vector = vector
    }

    constructor(highFirst: Boolean, force: Double, high: Double,
                sound: SoundEffect) {
        isHighFirst = highFirst
        this.force = force
        this.high = high
        this.sound = sound
    }

    constructor(withHigh: Boolean, highFirst: Boolean, force: Double, high: Double,
                sound: SoundEffect, useVector: Boolean, vector: Vector?) {
        isWithHigh = withHigh
        isHighFirst = highFirst
        this.force = force
        this.high = high
        this.sound = sound
        isUseVector = useVector
        this.vector = vector
    }

    fun create(entity: Entity) {
        var newVector: Vector? = null
        if (isUseVector && vector != null) {
            newVector = vector
        } else {
            newVector = entity.location.direction
            if (isHighFirst) {
                if (isWithHigh) {
                    newVector.y = high
                }
                newVector.multiply(force)
            } else {
                newVector.multiply(force)
                if (isWithHigh) {
                    newVector.y = high
                }
            }
        }
        entity.velocity = newVector
        if (sound != null) {
            sound!!.create(entity)
        }
    }

}