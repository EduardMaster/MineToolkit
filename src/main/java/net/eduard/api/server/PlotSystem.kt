package net.eduard.api.server

import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

interface PlotSystem : PluginSystem {
    fun isSamePlot(location: Location, locationTarget: Location) : Boolean
    fun getPlot(location : Location) : PlotData?
    fun hasPlot(location : Location) : Boolean
    fun getPlots(player : Player) : Collection<PlotData>

    interface PlotData{
        val x : Int
        val z : Int
        val owner : OfflinePlayer
        val owners : Collection<OfflinePlayer>
        val members : Collection<OfflinePlayer>
        fun transfer(newOwner : OfflinePlayer) : Boolean
    }
}