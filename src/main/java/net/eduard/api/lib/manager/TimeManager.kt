package net.eduard.api.lib.manager

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

import net.eduard.api.lib.modules.BukkitTimeHandler
import net.eduard.api.lib.modules.Mine

/**
 * Controlador de Tempo, classe que controla e ajuda na criação de
 * temporarizador (Timer)<br></br>
 * , de atrasador (Delayer) que são Tarefas de Tempo ([BukkitTask])
 *
 * @author Eduard
 */
open class TimeManager(var time: Long = 20) : EventsManager(), Runnable,
    BukkitTimeHandler {


    /**
     * Tempo anterior para fazer uma checagem
     */

    var startedTime: Long = 0


    @Transient
    lateinit var task: BukkitTask

    val isRunning: Boolean
        get() = existsTask() &&
                Bukkit.getScheduler().isCurrentlyRunning(task!!.taskId)


    /**
     * Metodo principal do Efeito a cada Tempo
     */
    override fun run() {}


    override fun getPluginConnected(): Plugin {
        return plugin
    }

    /**
     * Cria um Delay com um Plugin
     *
     *
     * @return Delay
     */
    fun syncDelay(): BukkitTask {
        task = newTask(time,false,false,this)
        startedTime = System.currentTimeMillis()
        return task
    }

    /**
     * Cria um Timer com um Plugin
     *
     *
     * @return Timer
     */
    fun syncTimer(): BukkitTask {
        task = newTask(time,true,false,this)
        startedTime = System.currentTimeMillis()
        return task
    }

    /**
     * Cria um Timer desincronizado com um Plugin
     *
     *
     * @return Timer
     */
    fun asyncTimer(): BukkitTask {
        task = newTask(time,true,true,this)
        startedTime = System.currentTimeMillis()
        return task
    }

    /**
     * Cria um Delay com um Plugin e um Efeito rodavel
     *
     *
     * @return Delay
     */
    fun asyncDelay(): BukkitTask {
        task = newTask(time,false,true,this)
        startedTime = System.currentTimeMillis()
        return task
    }

    /**
     *
     * @return Se ligou um Timer ou Delay
     */
    fun existsTask(): Boolean {
        return this::task.isInitialized
    }

    /**
     * Desliga o Timer/Delay criado
     */
    fun stopTask() {
        if (isRunning) {
            task.cancel()
        }
    }


}
