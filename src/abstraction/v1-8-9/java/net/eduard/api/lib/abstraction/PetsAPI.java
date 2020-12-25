package net.eduard.api.lib.abstraction;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.UUID;

public class PetsAPI {

	 private static Field gsa;
	    private static Field goalSelector;
	    private static Field targetSelector;
	    static {
	        try {
	            gsa = PathfinderGoalSelector.class.getDeclaredField("b");
	            gsa.setAccessible(true);
	            goalSelector = EntityInsentient.class.getDeclaredField("goalSelector");
	            goalSelector.setAccessible(true);
	            targetSelector = EntityInsentient.class.getDeclaredField("targetSelector");
	            targetSelector.setAccessible(true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    @SuppressWarnings("deprecation")
	    public static void makePet(LivingEntity e, UUID toFollow) {
	        try {
	            Object nms_entity = ((CraftLivingEntity) e).getHandle();
	            if (nms_entity instanceof EntityInsentient) {
	                PathfinderGoalSelector goal = (PathfinderGoalSelector) goalSelector.get(nms_entity);
	                PathfinderGoalSelector target = (PathfinderGoalSelector) targetSelector.get(nms_entity);
	                gsa.set(goal, new UnsafeList<Object>());
	                gsa.set(target, new UnsafeList<Object>());
	                goal.a(0, new PathfinderGoalFloat((EntityInsentient) nms_entity));
	                goal.a(1, new PathfinderGoalWalktoTile((EntityInsentient) nms_entity, toFollow));
	            } else {
	                throw new IllegalArgumentException(e.getType().getName() + " is not an instance of an EntityInsentient.");
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	    public static class PathfinderGoalWalktoTile extends PathfinderGoal {
	        private final EntityInsentient entity;
	        private PathEntity path;
	        private final UUID uuid;

	        public PathfinderGoalWalktoTile(EntityInsentient entitycreature, UUID playerUUID) {
	            this.entity = entitycreature;
	            this.uuid = playerUUID;

	        }
	        @Override
	        public boolean a() {
	        	Player alvo = Bukkit.getPlayer(uuid);
	            if (Bukkit.getPlayer(uuid) == null) {
	                return path != null;
	            }
	            Location targetLocation = alvo.getLocation();
				//boolean flag = this.entity.getNavigation().m();
	            this.path = this.entity.getNavigation().a(targetLocation.getX() + 1, targetLocation.getY(), targetLocation.getZ() + 1);

	            if (this.path != null) {
	                this.c();
	            }
	            return this.path != null;
	        }
	        @Override
	        public void c() {
	        	this.entity.getNavigation().a(this.path, 1D);
	        }
	    }		
}
