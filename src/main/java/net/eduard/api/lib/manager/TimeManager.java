package net.eduard.api.lib.manager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import net.eduard.api.lib.modules.BukkitTimeHandler;


/**
 * Gerenciador de Timers e Tasks
 *
 * @author Eduard
 */

public class TimeManager extends EventsManager implements Runnable, BukkitTimeHandler {
     transient long taskDuration = 20;
     transient long taskStart;
     transient BukkitTask taskUsed;
    public boolean existsTask() {
        return taskUsed != null;
    }
    /**
     * Desliga o Timer/Delay criado
     */
    public void stopTask() {
        if (existsTask()) {
            taskUsed.cancel();
            Bukkit.getScheduler().cancelTask(taskUsed.getTaskId());
            taskUsed = null;
        }
    }
    public void run() {
    }
    public Plugin getPluginConnected() {
        return getPlugin();
    }
    public boolean isRunning() {
        return existsTask() && Bukkit.getScheduler().isCurrentlyRunning(taskUsed.getTaskId());
    }

    public TimeManager() {
        this(1);
    }
    public TimeManager(long ticks) {
        this.taskDuration = ticks;
    }
    public TimeManager(int seconds) {
        this.taskDuration = 20L * seconds;
    }

    /**
     * Cria um Sync Delay
     *
     * @return BukkitTask
     */
    public BukkitTask syncDelay() {
        taskUsed = newTask(taskDuration, false, false, this);
        taskStart = System.currentTimeMillis();
        return taskUsed;
    }

    /**
     * Cria um Sync Timer
     *
     * @return Timer
     */
    public BukkitTask syncTimer() {
        taskUsed = newTask(taskDuration, true, false, this);
        taskStart = System.currentTimeMillis();
        return taskUsed;
    }

    /**
     * Cria um Async Timer
     *
     * @return Timer
     */
    public BukkitTask asyncTimer() {
        taskUsed = newTask(taskDuration, true, true, this);
        taskStart = System.currentTimeMillis();
        return taskUsed;
    }

    /**
     * Cria um Async Delay
     *
     * @return Delay
     */
    public BukkitTask asyncDelay() {
        taskUsed = newTask(taskDuration, false, true, this);
        taskStart = System.currentTimeMillis();
        return taskUsed;
    }
}
