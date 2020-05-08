package net.eduard.api.lib.modules;


import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

/**
 * Classe para criação de tarefas de tempos em tempos, ou com atraso
 *
 * @author Eduard
 */
public interface BukkitTimeHandler {

    /**
     * @return Retorna a instancia do plugin
     */
    Plugin getPluginConnected();

    default BukkitTask newTask(long time, boolean repeat, boolean async, Runnable task) {
        if (repeat) {
            if (async) {
                return getScheduler().runTaskTimerAsynchronously(getPluginConnected(), task, time, time);
            } return getScheduler().runTaskTimer(getPluginConnected(), task, time, time);
        }else{
            if (async){
                return getScheduler().runTaskLaterAsynchronously(getPluginConnected(), task, time);
            }else{
                return getScheduler().runTaskLater(getPluginConnected(), task, time);
            }
        }
    }

    default BukkitTask asyncTimer(Runnable runnable, long ticks, long startTicks) {

        return newTimer(runnable, ticks, startTicks, true);
    }

    default BukkitTask syncTimer(Runnable runnable, long ticks, long startTicks) {
        return newTimer(runnable, ticks, startTicks, false);
    }

    default BukkitTask syncDelay(Runnable runnable, long ticks) {

        return newDelay(runnable, ticks, false);
    }

    default BukkitTask asyncDelay(Runnable runnable, long ticks) {
        return newDelay(runnable, ticks, true);
    }

    default BukkitTask asyncTask(Runnable runnable) {
        return newTask(runnable, true);
    }

    default BukkitTask syncTask(Runnable runnable) {
        return newTask(runnable, false);
    }

    default BukkitTask newTimer(Runnable runnable, long ticks, long startTicks, boolean async) {

        if (async)
            return getScheduler().runTaskTimerAsynchronously(getPluginConnected(), runnable, startTicks, ticks);
        return getScheduler().runTaskTimer(getPluginConnected(), runnable, startTicks, ticks);
    }

    default BukkitTask newTask(Runnable runnable, boolean async) {
        if (async) {
            return getScheduler().runTaskAsynchronously(getPluginConnected(), runnable);
        } else {
            return getScheduler().runTask(getPluginConnected(), runnable);
        }
    }


    default BukkitTask newDelay(Runnable runnable, long ticks, boolean async) {
        if (async)
            return getScheduler().runTaskLaterAsynchronously(getPluginConnected(), runnable, ticks);
        return getScheduler().runTaskLater(getPluginConnected(), runnable, ticks);
    }

    default BukkitScheduler getScheduler() {
        return Bukkit.getScheduler();
    }

    /**
     * Cancela todos Repetidores e Atrasos criado nesta classe
     */
    default void cancelAllTasks() {
        getScheduler().cancelTasks(getPluginConnected());
    }

}
