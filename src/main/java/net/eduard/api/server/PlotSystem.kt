package net.eduard.api.server

import org.bukkit.Location
import java.util.*

interface PlotSystem : PluginSystem {
    fun isSamePlot(location: Location, locationTarget: Location) : Boolean
    fun getPlot(location : Location) : PlotData?
    interface PlotData{
        val x : Int
        val z : Int
        val owner : UUID
    }
}