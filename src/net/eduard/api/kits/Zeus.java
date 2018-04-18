package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.game.Ability;


public class Zeus extends Ability{
	
	public Zeus() {
		setIcon(Material.BOW, "§fGanhe uma espada Melhor");
		add(Material.WOOD_SWORD);
		add(Material.BOW);
		getItems().add(new ItemStack(Material.ARROW,64));
	}
	@EventHandler
	public void event(EntityDamageEvent e){
		if (e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if (hasKit(p)){
				if (e.getCause() == DamageCause.LIGHTNING){
					e.setCancelled(true);
				}
			}
			
		}
	}
	@EventHandler
	public void event(ProjectileHitEvent e){
		if (e.getEntity() instanceof Arrow){
			Arrow arrow = (Arrow) e.getEntity();
			if (arrow.getShooter() instanceof Player){
				Player p = (Player) arrow.getShooter();
				if (hasKit(p)){
					if (cooldown(p)){
						Mine.strike(arrow.getLocation());
					}
				}
				
			}
			
		}
	}

}
