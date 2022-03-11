package net.eduard.api.server

import org.bukkit.Location
import org.bukkit.entity.Player

interface PlantSystem : PluginSystem {
    fun transferPlantsInPlotToOtherPlayer(plotLocation : Location, owner: Player, receiver: Player) : MutableList<Location>
}