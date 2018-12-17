package net.eduard.api.lib.manager;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.lib.modules.Copyable.NotCopyable;
import net.eduard.api.lib.storage.Storable;

/**
 * Controlador de Eventos ({@link Listener})
 * 
 * @author Eduard
 *
 */
public class EventsManager implements Listener, Storable {
	/**
	 * Se o Listener esta registrado
	 */
	private transient boolean registred;
	/**
	 * Plugin
	 */
	@NotCopyable
	private transient Plugin plugin;

	/**
	 * Construtor base deixando Plugin automatico
	 */
	public EventsManager() {
		setPlugin(defaultPlugin());
	}

	public Plugin defaultPlugin() {
		return JavaPlugin.getProvidingPlugin(getClass());
	}

	/**
	 * Construtor pedindo um Plugin
	 * 
	 * @param plugin
	 *            Plugin
	 */
	public EventsManager(Plugin plugin) {
		register(plugin);
	}

	/**
	 * Registra o Listener para o Plugin
	 * 
	 * @param plugin
	 *            Plugin
	 */
	public void register(Plugin plugin) {
		unregisterListener();
		this.registred = true;
		setPlugin(plugin);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	public Plugin getPlugin() {
		return plugin;
	}
	
	public Object restore(Map<String, Object> map) {
		return null;
	}

	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub

	}

	/**
	 * Desregistra o Listener
	 */
	public void unregisterListener() {
		if (registred) {
			HandlerList.unregisterAll(this);
			this.registred = false;
		}
	}

	/**
	 * @return Se a Listener esta registrado
	 */
	public boolean isRegistered() {
		return registred;
	}

	/**
	 * 
	 * @return Plugin
	 */
	public Plugin getPluginInstance() {
		return plugin;
	}

	/**
	 * Seta o Plugin
	 * 
	 * @param plugin
	 *            Plugin
	 */
	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

}

