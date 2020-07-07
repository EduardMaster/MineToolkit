package net.eduard.api.lib.game

import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

class VisualEffect(var type: Effect? = Effect.FLAME, var data: Int = 0) {


    fun create(entity: Entity, radius: Int = 10): VisualEffect {
        return create(entity.location, radius)

    }
    fun create(loc: Location, radius: Int = 10): VisualEffect {
        loc.world.playEffect(loc, type, data, radius)
        return this
    }

    fun create(p: Player): VisualEffect {
        p.playEffect(p.location, type, data)
        return this
    }

}