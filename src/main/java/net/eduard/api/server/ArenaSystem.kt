package net.eduard.api.server

import org.bukkit.entity.Player

interface ArenaSystem : PluginSystem {

    fun isInArena(player : Player) : Boolean
}