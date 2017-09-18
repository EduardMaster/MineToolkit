package net.eduard.api.tutorial.sistemas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.eduard.api.setup.ExtraAPI;

@SuppressWarnings("deprecation")
public class EstacarMobs3 implements Listener{

	private static HashMap<LivingEntity, Integer> stacks = new HashMap<>();
	@EventHandler
	public void event(CreatureSpawnEvent e) {
		if (e.getSpawnReason() == SpawnReason.CUSTOM)return;
		if (e.getSpawnReason() == SpawnReason.SPAWNER) {
			List<LivingEntity> inutils = new ArrayList<>();
			for (Entity entity : e.getEntity().getNearbyEntities(10, 10, 10)) {
				if (entity instanceof LivingEntity) {
					LivingEntity livingEntity = (LivingEntity) entity;
					if (livingEntity.getType() == e.getEntityType()) {
						inutils.add(livingEntity);
					}
				}
			}
			
			inutils.add(e.getEntity());
			if (inutils.size()>0) {
				int stack = 1;
				LivingEntity inutil = inutils.get(0);
				int x= 1;
				for (LivingEntity 	entity:inutils) {
					if (stacks.containsKey(entity)) {
						stack+=stacks.get(entity);
						if (x==1) {
							x++;
							continue;
						}
						entity.remove();
					}
				}
				stack(inutil, stack);
				if (inutils.size()>1) {
					e.setCancelled(true);
				}
				
			}
			
		} else {
			e.setCancelled(true);
		}

	}
	@EventHandler
	public void event(EntityDeathEvent e ) {
		if (stacks.containsKey(e.getEntity())) {
			new ArrayList<>();
			Integer stack = stacks.get(e.getEntity());
			stacks.remove(e.getEntity());
			stack--;
			if (stack == 0)return;
			LivingEntity entity = (LivingEntity) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), e.getEntityType());
			stack(entity, stack);
		}
	}
	@EventHandler
	public void event(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getState() instanceof CreatureSpawner ) {
				
				CreatureSpawner creatureSpawner = (CreatureSpawner) e.getClickedBlock().getState();
				creatureSpawner.setCreatureType(CreatureType.ZOMBIE);
//				creatureSpawner.setRawData((byte) CreatureType.ZOMBIE.getTypeId());
				creatureSpawner.update(true, true);
			}
		}
	}
	public void stack(LivingEntity entity,int stack) {
		entity.setCustomName("§6"+ExtraAPI.toTitle(entity.getType().name())+" §ex"+stack);
		entity.setCustomNameVisible(true);
		stacks.put(entity, stack);
	}
}
