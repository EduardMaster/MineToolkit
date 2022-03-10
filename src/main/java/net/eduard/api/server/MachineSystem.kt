package net.eduard.api.server

import org.bukkit.entity.Player

interface MachineSystem : PluginSystem{

    fun unstallMachinesOfAtPlot(player : Player)
}