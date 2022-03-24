package net.eduard.api.lib.abstraction

interface EntityRule {
    fun canRun(): Boolean
    fun run()
    fun finished(): Boolean
    fun unregister()
    fun priority() : Int


}