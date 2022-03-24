package net.eduard.api.lib.abstraction

import net.minecraft.server.v1_8_R3.PathfinderGoal
import org.bukkit.entity.Creature

open class EntityRule_v1_8_R3(
val creature : Creature,
var entityRule : EntityRule?) : PathfinderGoal() {
    override fun a(): Boolean {
        return entityRule?.canRun() ?: false
    }

    override fun c() {
        entityRule?.run()
    }

    override fun d() {
        entityRule?.unregister()
    }

    override fun i(): Boolean {
        return true
    }

    override fun b(): Boolean {
        return entityRule?.finished() ?: true
    }

    fun priority(): Int {
        return j()
    }

    fun setPriority(newPriority: Int) {
        a(newPriority)
    }
}