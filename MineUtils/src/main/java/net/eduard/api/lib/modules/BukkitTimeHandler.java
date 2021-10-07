package net.eduard.api.lib.modules;


import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

/**
 * Timer = Repetidor de Ação a cada X Tempo
 * Delay = Atrasa a execução de Ação
 * Classe cheia de Aliases para as Funções do BukkitScheduler, para
 * facilitar a criação de BukkitTask
 *
 * @author Eduard
 */
@SuppressWarnings("unused")
public interface BukkitTimeHandler {

    /**
     * @return Retorna a instancia do plugin
     */
    Plugin getPluginConnected();



    default BukkitTask asyncTimer( long startTicks,long ticks ,Runnable runnable) {

        return newTimer(startTicks, ticks, true,runnable);
    }

    default BukkitTask syncTimer(long startTicks, long ticks, Runnable runnable) {
        return newTimer(startTicks, ticks, false,runnable);
    }

    default BukkitTask syncDelay( long ticks,Runnable runnable) {

        return newDelay( ticks, false,runnable);
    }

    default BukkitTask asyncDelay( long ticks,Runnable runnable)
    {
        return newDelay( ticks, true,runnable);
    }

    default BukkitTask asyncTask(Runnable runnable) {
        return newTask( true,runnable);
    }

    default BukkitTask syncTask(Runnable runnable) {
        return newTask( false,runnable);
    }

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

    default BukkitTask newTimer(long startTicks, long ticks , boolean async,Runnable runnable) {

        if (async)
            return getScheduler().runTaskTimerAsynchronously(getPluginConnected(), runnable, startTicks, ticks);
        return getScheduler().runTaskTimer(getPluginConnected(), runnable, startTicks, ticks);
    }

    default BukkitTask newTask(boolean async,Runnable runnable) {
        if (async) {
            return getScheduler().runTaskAsynchronously(getPluginConnected(), runnable);
        } else {
            return getScheduler().runTask(getPluginConnected(), runnable);
        }
    }


    default BukkitTask newDelay(long ticks, boolean async,Runnable runnable) {
        if (async)
            return getScheduler().runTaskLaterAsynchronously(getPluginConnected(), runnable, ticks);
        return getScheduler().runTaskLater(getPluginConnected(), runnable, ticks);
    }

    default BukkitScheduler getScheduler() {
        return Bukkit.getScheduler();
    }

    /**
     * Cancela todas BukkitTask feitas por este Plugin
     */
    default void cancelAllTasks() {
        getScheduler().cancelTasks(getPluginConnected());
    }

}
