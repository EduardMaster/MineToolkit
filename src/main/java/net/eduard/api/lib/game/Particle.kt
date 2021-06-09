package net.eduard.api.lib.game

import net.eduard.api.lib.abstraction.Minecraft
import net.eduard.api.lib.modules.Mine
import org.bukkit.Location
import org.bukkit.entity.Player

class Particle(

        var particle: ParticleType = ParticleType.HEART,
        var amount: Int = 1,
        var speed: Float = 0.25f,
        var xRandom: Float = 0f,
        var yRandom: Float = 0f,
        var zRandom: Float = 0f
) {


    fun create(player: Player, local: Location): Particle {

        Minecraft.instance.sendParticle(player, particle.particleName, local, amount,xRandom,yRandom,zRandom,speed)
        return this
    }

    fun create(local: Location): Particle {
        for (player in Mine.getPlayers()) {
            Minecraft.instance.sendParticle(
                player,
                particle.particleName,
                local,
                amount,
                xRandom,
                yRandom,
                zRandom,
                speed
            )
        }
        return this
    }

}