package net.eduard.api.tutorial.eventos;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

import net.eduard.api.manager.TimeManager;

public class BloquearExplosaoCreeper extends TimeManager{
	
	
	@EventHandler
	public void quandoUmaEntidadeTentarExplodir(EntityExplodeEvent e) {

		Entity entidade = e.getEntity();
		if (entidade instanceof Creeper) {
			e.setCancelled(true);
		}
	}
}
