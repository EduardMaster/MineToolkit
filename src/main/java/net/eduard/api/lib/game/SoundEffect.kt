package net.eduard.api.lib.game

import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_8_R3.CraftSound.getSound
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.lang.Error
import java.lang.Exception

/**
 * Representa um Som sendo Tocado com um Volume e um Tom
 * @author Eduard
 * @version 2.0
 * @since 1.0
 */
class SoundEffect(
    var sound: Sound,
    var volume: Float,
    var pitch: Float
) {
    constructor() : this(getSoundByName("LEVEL_UP"))
    constructor(sound: Sound) : this(sound, 2f, 1f)
    constructor(sound: Sound, volume: Float) : this(sound, volume, 1f)

    fun create(location: Location): SoundEffect {
        location.world.playSound(location, sound, volume, pitch)
        return this
    }


    fun create(entity: Entity): SoundEffect {
        if (entity is Player) {
            val player = entity
            player.playSound(player.location, sound, volume, pitch)
            return this
        }

        return create(entity.location)
    }

    companion object {
        fun getSoundByName(name: String): Sound {
            return try {
                Sound.valueOf(name.toUpperCase())
            } catch (ex: Error) {
                Sound.values()[0]
            } catch (ex: Exception) {
                Sound.values()[0]
            }
        }

        @JvmStatic
        fun create(sound: String): SoundEffect {
            return SoundEffect(getSoundByName(sound))
        }
    }

}