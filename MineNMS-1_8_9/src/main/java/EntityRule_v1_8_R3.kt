package net.eduard.api.lib.abstraction

import net.minecraft.server.v1_8_R3.PathfinderGoal

abstract class EntityRule_v1_8_R3 : PathfinderGoal(), EntityRule {
    override fun a(): Boolean {
        return canRun()
    }

    override fun c() {

        run()
    }

    override fun d() {
       unregister()
    }

    override fun i(): Boolean {
        return true
    }

    override fun b(): Boolean {
        return finished()
    }

    override fun priority(): Int {
        return j()
    }

    override fun setPriority(newPriority: Int) {
        a(newPriority)
    }
}