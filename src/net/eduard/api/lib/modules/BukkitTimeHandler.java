package net.eduard.api.lib.modules;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public interface BukkitTimeHandler {

	public Plugin getPlugin();


	public default BukkitTask asyncTimer(Runnable runnable, long ticks, long startTicks) {
		return newTimer(runnable, ticks, startTicks, true);
	}
	public default BukkitTask syncTimer(Runnable runnable, long ticks, long startTicks) {
		return newTimer(runnable, ticks, startTicks, false);
	}

	public default BukkitTask syncDelay(Runnable runnable, long ticks) {
		return newDelay(runnable, ticks, false);
	}

	public default BukkitTask asyncDelay(Runnable runnable, long ticks) {
		return newDelay(runnable, ticks, true);
	}

	public default BukkitTask newTimer(Runnable runnable, long ticks, long startTicks, boolean async) {
		if (async)
			return Bukkit.getScheduler().runTaskTimerAsynchronously(getPlugin(), runnable, startTicks, ticks);
		return Bukkit.getScheduler().runTaskTimer(getPlugin(), runnable, startTicks, ticks);
	}
	public default BukkitTask newDelay(Runnable runnable, long ticks, boolean async) {
		if (async)
			return Bukkit.getScheduler().runTaskLaterAsynchronously(getPlugin(), runnable, ticks);
		return Bukkit.getScheduler().runTaskLater(getPlugin(), runnable, ticks);
	}

	public default void cancelAllTasks() {
		Bukkit.getScheduler().cancelTasks(getPlugin());
	}

}
