package net.eduard.api.lib.game

import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

/**
 * Representa um Som sendo Tocado com um Volume e um Tom
 * @author Eduard
 * @version 2.0
 * @since 1.0
 */
class SoundEffect (var sound: Sound = Sound.values()[0], var volume: Float = 2f, var pitch: Float = 1f) {
    fun create(location: Location): SoundEffect {
        location.world.playSound(location, sound, volume, pitch)
        return this
    }


    fun create(entity: Entity): SoundEffect {
        if (entity is Player) {
            val p = entity
            p.playSound(p.location, sound, volume, pitch)
            return this
        }
        return create(entity.location)
    }

    companion object {
        @JvmStatic
		fun create(sound: String): SoundEffect {
            return try {
                SoundEffect(Sound.valueOf(sound))
            } catch (e: Exception) {
                SoundEffect(Sound.values()[0])
            }
        }
    }

}