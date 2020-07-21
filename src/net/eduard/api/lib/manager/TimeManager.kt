package net.eduard.api.lib.manager

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.modules.BukkitTimeHandler
import org.bukkit.plugin.java.JavaPlugin

/**
 * Controlador de Tempo, classe que controla e ajuda na criação de
 * temporarizador (Timer)<br></br>
 * , de atrasador (Delayer) que são Tarefas de Tempo ([BukkitTask])
 *
 * @author Eduard
 */
open class TimeManager(var time: Long = 20) : EventsManager(), Runnable, BukkitTimeHandler {


    /**
     * Tempo anterior para fazer uma checagem
     */

    var startTime: Long = 0

    /**
     * Define
     *
     * @param task
     */
    @Transient
    var task: BukkitTask? = null

    val isRunning: Boolean
        get() = existsTask() && Bukkit.getScheduler().isCurrentlyRunning(task!!.taskId)


    /**
     * Metodo principal do Efeito a cada Tempo
     */
    override fun run() {}


    override fun getPluginConnected(): Plugin {
        return pluginInstance
    }

    /**
     * Cria um Delay com um Plugin
     *
     *
     * @return Delay
     */
    fun syncDelay(): BukkitTask? {
        task = syncDelay(this, this.time)
        startTime = Mine.getNow()
        return task
    }

    /**
     * Cria um Timer com um Plugin
     *
     *
     * @return Timer
     */
    fun syncTimer(): BukkitTask? {
        task = syncTimer(this, this.time, this.time)
        startTime = Mine.getNow()
        return task
    }

    /**
     * Cria um Timer desincronizado com um Plugin
     *
     *
     * @return Timer
     */
    fun asyncTimer(): BukkitTask? {
        task = asyncTimer(this, this.time, this.time)
        startTime = Mine.getNow()
        return task
    }

    /**
     * Cria um Delay com um Plugin e um Efeito rodavel
     *
     *
     * @return Delay
     */
    fun asyncDelay(): BukkitTask? {
        task = asyncDelay(this, this.time)
        startTime = Mine.getNow()
        return task
    }

    /**
     *
     * @return Se ligou um Timer ou Delay
     */
    fun existsTask(): Boolean {
        return task != null
    }

    /**
     * Desliga o Timer/Delay criado
     */
    fun stopTask() {
        if (existsTask()) {
            task!!.cancel()
            task = null
        }
    }


}
