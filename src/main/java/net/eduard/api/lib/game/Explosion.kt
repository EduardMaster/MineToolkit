package net.eduard.api.lib.game

import org.bukkit.Location
import org.bukkit.entity.Entity

/**
 * Representa uma explosão sendo feita, com uma Força em Raio, se quebra blocos, se causa fogo
 * @version 2.0
 * @since EduardAPI 1.0
 * @author Eduard
 */
class Explosion(
        var power: Float = 3f,
        var isBreakBlocks: Boolean = false,
        var isMakeFire: Boolean = false

) {




    fun create(entity: Entity): Explosion {
        create(entity.location)
        return this
    }

    fun create(location: Location): Explosion {
        location.world.createExplosion(location.x, location.y, location.z, power, isMakeFire, isBreakBlocks)
        return this
    }
}