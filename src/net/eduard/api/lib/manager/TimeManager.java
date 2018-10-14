package net.eduard.api.lib.manager;

import java.util.Map;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import net.eduard.api.lib.Mine;

/**
 * Controlador de Tempo, classe que controla e ajuda na cria��o de
 * temporarizador (Timer)<br>
 * , de atrasador (Delayer) que s�o Tarefa de Tempo (Task ou BukkitTask)
 * 
 * @author Eduard-PC
 *
 */
public class TimeManager extends EventsManager implements Runnable {

	/**
	 * Construtor base automatico usando o Plugin da Mine;
	 */
	public TimeManager() {
		setPlugin(defaultPlugin());
	}

	/**
	 * Construtor pedindo um Plugin
	 * 
	 * @param plugin
	 *            Plugin
	 */
	public TimeManager(JavaPlugin plugin) {
		setPlugin(plugin);
	}

	/**
	 * Tempo em ticks para o Delay ou Timer
	 */
	private long time = 20;

	/**
	 * Tempo anterior para fazer a compara��o
	 */
	private long startTime;

	private transient BukkitTask task;

	/**
	 * Metodo principal do Efeito a cada Tempo
	 */
	@Override
	public void run() {
	}

	/**
	 * Cria um Delay com um Plugin
	 * 
	 * @param plugin
	 *            Plugin
	 * @return Delay
	 */
	public BukkitTask delay(Plugin plugin) {
		setTask(Mine.delay(plugin, time, this));
		setStartTime(Mine.getNow());
		return task;
	}

	/**
	 * Cria um Timer com um Plugin
	 * 
	 * @param plugin
	 *            Plugin
	 * @return Timer
	 */
	public BukkitTask timer(Plugin plugin) {
		setTask(Mine.timer(plugin, time, this));
		setStartTime(Mine.getNow());
		return task;
	}
	/**
	 * Cria um Timer desincronizado com um Plugin
	 * 
	 * @param plugin
	 *            Plugin
	 * @return Timer
	 */
	public BukkitTask timers(Plugin plugin) {
		setTask(Mine.timers(plugin, time, this));
		setStartTime(Mine.getNow());
		return task;
	}

	/**
	 * Cria um Delay com um Plugin e um Efeito rodavel
	 * 
	 * @param plugin
	 *            Plugin
	 * @param run
	 *            Efeito rodavel
	 * @return Delay
	 */
	public BukkitTask delay(long ticks, Runnable run) {
		setTask(Mine.delay(getPlugin(), ticks, run));
		setStartTime(Mine.getNow());
		return task;
	}

	/**
	 * Cria um Delay com um Plugin e um Efeito rodavel
	 * 
	 * @param plugin
	 *            Plugin
	 * @param run
	 *            Efeito rodavel
	 * @return Delay
	 */
	public BukkitTask delays(long ticks, Runnable run) {
		setTask(Mine.delays(getPlugin(), ticks, run));
		setStartTime(Mine.getNow());
		return task;
	}
	/**
	 * Cria um Timer com um Plugin e um Efeito rodavel
	 * 
	 * @param plugin
	 *            Plugin
	 * @param run
	 *            Efeito rodavel
	 * @return Timer
	 */
	public BukkitTask timer(long ticks, Runnable run) {
		setTask(Mine.timer(getPlugin(), ticks, run));
		setStartTime(Mine.getNow());
		return task;
	}

	/**
	 * Cria um Timer com um Plugin e um Efeito rodavel
	 * 
	 * @param plugin
	 *            Plugin
	 * @param run
	 *            Efeito rodavel
	 * @return Timer
	 */
	public BukkitTask timers(long ticks, Runnable run) {
		setTask(Mine.timers(getPlugin(), ticks, run));
		setStartTime(Mine.getNow());
		return task;
	}
	/**
	 * 
	 * @return Tempo em ticks
	 */
	public long getTime() {
		return time;
	}

	/**
	 * 
	 * @return Se ligou um Timer ou Delay
	 */
	public boolean existsTask() {
		return task != null;
	}

	/**
	 * Desliga o Timer/Delay criado
	 */
	public void stopTask() {
		if (existsTask()) {
			task.cancel();
			task = null;
		}
	}

	/**
	 * Seta o Tempo
	 * 
	 * @param time
	 *            Tempo em ticks
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Define o Tempo
	 * 
	 * @param time
	 *            Tempo em segundos
	 */
	public void setTime(int time) {
		setTime(time * 20L);
	}

	/**
	 * 
	 * @return O tempo do inicio
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * Define o Tempo de inicio
	 * 
	 * @param startTime
	 *            Tempo em ticks
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public BukkitTask getTask() {
		return task;
	}

	/**
	 * Define
	 * 
	 * @param task
	 */
	public void setTask(BukkitTask task) {
		this.task = task;
	}

	@Override
	public Object restore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub

	}

	

}
