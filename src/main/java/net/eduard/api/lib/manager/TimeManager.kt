package net.eduard.api.lib.manager

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

import net.eduard.api.lib.modules.BukkitTimeHandler
import net.eduard.api.lib.modules.Mine

/**
 * Classe que Herda EventsManager, como se fosse um BukkitRunnable que
 * pode aceitar Eventos Também, utiliza a classe BukkitTimeHandler para
 * interagir com o BukkitScheduler escrevendo apenas Aliases
 *
 * @author Eduard
 */
open class TimeManager(var time: Long) : EventsManager(), Runnable,
    BukkitTimeHandler {

    constructor() : this(20)

    /**
     * Tempo do Inicio do Delay/Timer
     */

    var startedTime: Long = 0


    /**
     * BukkitTask criado apartir das aliases internas
     */
    @Transient
    lateinit var task: BukkitTask

    /**
     * Verifica se este Timer/Delay esta em execução
     */
    val isRunning: Boolean
        get() = existsTask() &&
                Bukkit.getScheduler().isCurrentlyRunning(task.taskId)


    /**
     * Se for Timer executa o run() repetidamente a cada X tempo,
     * se for delay executa o run() apenas uma vez depois de X Tempo
     */
    override fun run() {}

    /**
     * Retorna um Plugin que Interagiu com esta classe primeiro,
     * geralmente retorna EduardAPIMain
     */
    override fun getPluginConnected(): Plugin {
        return plugin
    }

    /**
     * Cria um Sync Delay
     *
     *
     * @return Delay
     */
    fun syncDelay(): BukkitTask {
        task = newTask(time, false, false, this)
        startedTime = System.currentTimeMillis()
        return task
    }

    /**
     * Cria um Sync Timer
     *
     *
     * @return Timer
     */
    fun syncTimer(): BukkitTask {
        task = newTask(time, true, false, this)
        startedTime = System.currentTimeMillis()
        return task
    }

    /**
     * Cria um Async Timer
     *
     *
     * @return Timer
     */
    fun asyncTimer(): BukkitTask {
        task = newTask(time, true, true, this)
        startedTime = System.currentTimeMillis()
        return task
    }

    /**
     * Cria um Async Delay
     *
     *
     * @return Delay
     */
    fun asyncDelay(): BukkitTask {
        task = newTask(time, false, true, this)
        startedTime = System.currentTimeMillis()
        return task
    }

    /**
     *
     * @return Se ligou um Timer/Delay, porem pode já estar finalizado
     */
    fun existsTask(): Boolean {
        return this::task.isInitialized
    }

    /**
     * Desliga o Timer/Delay criado
     */
    fun stopTask() {
        if (existsTask())
            task.cancel()

    }


}
