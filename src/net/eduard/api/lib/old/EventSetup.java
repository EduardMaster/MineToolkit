package net.eduard.api.lib.old;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.eduard.api.lib.manager.TimeManager;

/**
 * 
 * Sistema de controlar o tempo no Bukkit parecido com {@link BukkitRunnable}
 * Versão anterior {@link TimeSetup}
 * 
 * @version 2.0
 * @since EduardAPI 0.7
 * @author Eduard
 * @deprecated Versão atual {@link TimeManager}
 */
public abstract class EventSetup implements Runnable {
	private int id = 0;

	private Runnable run;

	private int ticks = 20;

	private int time = 1;

	private int times = 1;

	public EventSetup(int ticks, int times) {
		setTimes(times);
		setTicks(ticks);
		restart();
	}

	public boolean exist() {
		return this.id > 0;
	}

	public int getId() {
		return this.id;
	}

	public Runnable getRun() {
		return this.run;
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

	public EventSetup restart() {
		stop();
		setTime(getTimes());
		setId(new BukkitRunnable() {
			public void run() {
				EventSetup.this.run();
				if (EventSetup.this.getRun() != null) {
					EventSetup.this.getRun().run();
				}
				if (EventSetup.this.getTime() == 1) {
					EventSetup.this.stop();
					return;
				}
				EventSetup.this.setTime(EventSetup.this.getTime() - 1);
			}

		}.runTaskTimer(JavaPlugin.getProvidingPlugin(getClass()), getTicks(), getTicks()).getTaskId());
		return this;
	}

	private EventSetup setId(int id) {
		this.id = id;
		return this;
	}

	public EventSetup setRun(Runnable run) {
		this.run = run;
		return this;
	}

	public EventSetup setTicks(int ticks) {
		ticks = ticks < 1 ? 1 : ticks;
		this.ticks = ticks;
		return this;
	}

	public EventSetup setTime(int time) {
		time = time < 1 ? 1 : time;
		this.time = time;
		return this;
	}

	public EventSetup setTimes(int times) {
		times = times < 1 ? 1 : times;
		this.times = times;
		return this;
	}

	public EventSetup stop() {
		if (exist()) {
			Bukkit.getScheduler().cancelTask(this.id);
			this.id = 0;
		}
		return this;
	}
}
