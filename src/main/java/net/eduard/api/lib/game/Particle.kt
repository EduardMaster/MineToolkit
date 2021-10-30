package net.eduard.api.lib.game

import net.eduard.api.lib.abstraction.Minecraft
import net.eduard.api.lib.modules.Mine
import org.bukkit.Location
import org.bukkit.entity.Player

class Particle(
    var particle: ParticleType=ParticleType.HEART,
    var amount: Int=1,
    var speed: Float=0f,
    var xRandom: Float=0f,
    var yRandom: Float=0f,
    var zRandom: Float=0f
) {

    constructor() : this(ParticleType.HEART);
    constructor(particle: ParticleType) : this(particle , 1,0f,0f,0f,0f)
    constructor(particle: ParticleType, amount: Int) : this(particle , amount,0f,0f,0f,0f)
    constructor(particle: ParticleType, amount: Int,speed: Float) : this(particle , amount,speed,0f,0f,0f)



    fun create(player: Player, local: Location): Particle {
        Minecraft.instance.sendParticle(player, particle.particleName, local, amount, xRandom, yRandom, zRandom, speed)
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