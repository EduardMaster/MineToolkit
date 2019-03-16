package net.eduard.api.lib.old;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.eduard.api.lib.manager.TimeManager;

/**
 * 
 * Sistema de controlar o tempo no Bukkit parecido com {@link BukkitRunnable}
 * Versão nova {@link EventSetup}
 * 
 * @version 1.0
 * @since EduardAPI 0.7
 * @author Eduard
 * @deprecated Versão atual {@link TimeManager}
 */
public abstract class TimeSetup implements EventManager {
	private int id = 0;

	private int times = 1;

	private int time = 1;

	private int ticks = 20;

	public TimeSetup() {
	}

	public TimeSetup(int ticks, int times) {
		setTicks(ticks);
		setTimes(times);
	}

	public boolean exist() {
		return this.id > 0;
	}

	private int getId() {
		return this.id;
	}

	public int getTicks() {
		return this.ticks;
	}

	public int getTime() {
		return this.time;
	}

	public int getTimes() {
		return this.times;
	}

	private void setId(int id) {
		this.id = id;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public void start() {
		if (exist()) {
			stop();
		}
		setTime(getTimes());
		setId(new BukkitRunnable() {
			public void run() {
				TimeSetup.this.event();
				if (TimeSetup.this.getTime() == 1) {
					TimeSetup.this.stop();
					return;
				}
				TimeSetup.this.setTime(TimeSetup.this.getTime() - 1);
			}

		}.runTaskTimer(JavaPlugin.getProvidingPlugin(getClass()), getTicks(), getTicks()).getTaskId());
	}

	public void stop() {
		if (exist()) {
			Bukkit.getScheduler().cancelTask(getId());
			setId(0);
		}
	}
}
