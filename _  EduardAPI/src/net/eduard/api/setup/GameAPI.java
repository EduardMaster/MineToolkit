package net.eduard.api.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;


/**
 * API de controle de Entidades, Localizações e muito mais
 * @author Eduard
 *
 */

public final class GameAPI {
	/**
	 * Pega o Ip do Jogador atual
	 * @param player Jogador
	 * @return Ip do Jogador
	 */
	public static String getIp(Player player) {
		return player.getAddress().getAddress().getHostAddress();
	}
	public static List<LivingEntity> getNearbyEntities(LivingEntity player,
			double x, double y, double z, EntityType... types) {
		List<LivingEntity> list = new ArrayList<>();
		for (Entity item : player.getNearbyEntities(x, y, z)) {
			if (item instanceof LivingEntity) {
				LivingEntity livingEntity = (LivingEntity) item;
				if (types != null) {
					for (EntityType entitie : types) {
						if (livingEntity.getType().equals(entitie)) {
							if (!list.contains(livingEntity))
								list.add(livingEntity);
						}
					}
				} else
					list.add(livingEntity);
			}
		}
		return list;

	}
	  @SuppressWarnings("unchecked")
	public static <T extends Player> T getTarget(Player entity, Iterable<T> entities)
	  {
	    if (entity == null)
	      return null;
	    Player target = null;
//	    double threshold = 1.0D;
	    for (Player other : entities) {
	      Vector n = other.getLocation().toVector().subtract(entity.getLocation().toVector());
	      if ((entity.getLocation().getDirection().normalize().crossProduct(n).lengthSquared() < 1.0D) && 
	        (n.normalize().dot(entity.getLocation().getDirection().normalize()) >= 0.0D)) {
	        if ((target == null) || 
	          (target.getLocation().distanceSquared(entity.getLocation()) > other.getLocation()
	          .distanceSquared(entity.getLocation()))) {
	          target = other;
	        }
	      }
	    }
	    return (T) target;
	  }

	public static List<LivingEntity> getNearbyEntities(LivingEntity entity,
			double radio, EntityType... entities) {

		return getNearbyEntities(entity, radio, radio, radio, entities);

	}
	public static List<Player> getPlayerAtRange(Location location,
			double range) {

		List<Player> players = new ArrayList<>();
		for (Player p : location.getWorld().getPlayers()) {
			if (!location.getWorld().equals(p.getWorld()))continue;
			if (p.getLocation().distance(location) <= range) {
				players.add(p);
			}
		}
		return players;
	}
	public static boolean hasLightOn(Entity entity) {
		return hasLightOn(entity.getLocation());
	}
	public static boolean hasLightOn(Location location) {
		return hasLightOn(location.getBlock());
	}
	public static boolean hasLightOn(Block block) {
		return block.getLightLevel() > 10;
	}
	public static Player getRandomPlayer(){
		return getRandomPlayer(getPlayers());
	}
	public static Player getRandomPlayer(List<Player> list){
		return list.get(ExtraAPI.getRandomInt(1, list.size())-1);
	}
	
	public static void setDirection(Entity entity, Entity target) {
		entity.teleport(entity.getLocation()
				.setDirection(target.getLocation().getDirection()));
	}
	
	public static List<Player> getPlayers() {
		return RexAPI.getPlayers();
	}
	
	


	public static void hide(Player player) {
		for (Player target :getPlayers()) { 
			if (target != player) {
				target.hidePlayer(player);
			}
		}
	}
	public static boolean isOnGround(Entity entity) {
		return entity.getLocation().getBlock().getRelative(BlockFace.DOWN)
				.getType() != Material.AIR;
	}



	public static void makeInvunerable(Player player) {
		player.setNoDamageTicks(ObjectAPI.DAY_IN_SECONDS * 20);

	}

	public static void makeInvunerable(Player player, int seconds) {
		player.setNoDamageTicks(seconds * 20);

	}

	public static void makeVulnerable(Player player) {

		player.setNoDamageTicks(0);
	}

	public static void moveTo(Entity entity, Location target, double gravity) {
		Location location = entity.getLocation().clone();
		double distance = target.distance(location);
		double x = -(gravity - ((target.getX() - location.getX()) / distance));
		double y = -(gravity - ((target.getY() - location.getY()) / distance));
		double z = -(gravity - ((target.getZ() - location.getZ()) / distance));
		Vector vector = new Vector(x, y, z);
		entity.setVelocity(vector);
	}

	public static void moveTo(Entity entity, Location target, double staticX,
			double staticY, double staticZ, double addX, double addY,
			double addZ) {
		Location location = entity.getLocation();
		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			location = livingEntity.getEyeLocation();
		}
		entity.setVelocity(getVelocity(location, target, staticX, staticY,
				staticZ, addX, addY, addZ));
	}
	public static boolean isFlying(Entity entity) {
		return entity.getLocation().getBlock().getRelative(BlockFace.DOWN, 2)
				.getType() == Material.AIR;
	}
	
	public static boolean isInvulnerable(Player player) {
		return player.getNoDamageTicks() > 1;
	}
	
	public static LightningStrike strike(LivingEntity living, int maxDistance) {
		return strike(getTargetLoc(living, maxDistance));
	}

	public static LightningStrike strike(Location location) {
		return location.getWorld().strikeLightning(location);
	}

	public static void teleport(Entity entity, Location target) {
		entity.teleport(
				target.setDirection(entity.getLocation().getDirection()));
	}
	public static void removeEffects(Player player) {
		player.setFireTicks(0);
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
	}
	public static void resetLevel(Player player) {
		player.setLevel(0);
		player.setExp(0);
		player.setTotalExperience(0);
	}
	

	public static void setDirection(Entity entity, Location target) {
		Location location = entity.getLocation().clone();
		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			location = livingEntity.getEyeLocation().clone();

		}
		entity.teleport(entity.getLocation()
				.setDirection(getDiretion(location, target)));

	}
	public static Vector getDiretion(Location location, Location target) {
		double distance = target.distance(location);
		double x = ((target.getX() - location.getX()) / distance);
		double y = ((target.getY() - location.getY()) / distance);
		double z = ((target.getZ() - location.getZ()) / distance);
		return new Vector(x, y, z);
	}
	

	public static void changeTabName(Player player, String displayName) {
		player.setPlayerListName(ExtraAPI.toText(displayName));
	}
	
	
	public static Vector getVelocity(Location entity, Location target,
			double staticX, double staticY, double staticZ, double addX,
			double addY, double addZ) {
		double distance = target.distance(entity);
		double x = (staticX + (addX * distance))
				* ((target.getX() - entity.getX()) / distance);
		double y = (staticY + (addY * distance))
				* ((target.getY() - entity.getY()) / distance);
		double z = (staticZ + (addZ * distance))
				* ((target.getZ() - entity.getZ()) / distance);
		return new Vector(x, y, z);

	}




	public static void resetScoreboard(Player player) {
		player.setScoreboard(ExtraAPI.getMainScoreboard());
	}
	// Parei aqui

	public static void refreshAll(Player player) {
		ItemAPI.clearInventory(player);
		removeEffects(player);
		refreshLife(player);
		refreshFood(player);
		makeVulnerable(player);
		resetLevel(player);
	}

	public static void refreshFood(Player player) {
		player.setFoodLevel(20);
		player.setSaturation(20);
		player.setExhaustion(0);
	}

	public static void refreshLife(Player p) {
		p.setHealth(p.getMaxHealth());
	}



	public static void setSpawn(Entity entity) {

		entity.getWorld().setSpawnLocation((int) entity.getLocation().getX(),
				(int) entity.getLocation().getY(),
				(int) entity.getLocation().getZ());
	}
	public static void show(Player player) {
		for (Player target : getPlayers()) {
			if (target != player) {
				target.showPlayer(player);
			}
		}
	}
	public static void teleport(LivingEntity entity, int range) {
		teleport(entity, getTargetLoc(entity, range));
	}

	public static void teleportToSpawn(Entity entity) {

		entity.teleport(entity.getWorld().getSpawnLocation()
				.setDirection(entity.getLocation().getDirection()));
	}
	public static boolean isFalling(Entity entity) {
		return entity.getVelocity().getY() < ObjectAPI.WALKING_VELOCITY;
	}
	public static List<Player> getOnlinePlayers() {
		return getPlayers();
	}
	public static Location getTargetLoc(LivingEntity entity, int distance) {
		@SuppressWarnings("deprecation")
		Block block = entity.getTargetBlock((HashSet<Byte>)null, distance);
		return block.getLocation();
	}
	
	public static Player getNearestPlayer(Player player) {
		double dis = 0.0D;
		Player target = null;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (dis == 0.0D) {
				dis = p.getLocation().distance(player.getLocation());
				target = p;
			} else {
				double newdis = p.getLocation().distance(player.getLocation());
				if (newdis < dis) {
					dis = newdis;
					target = p;
				}
			}
		}

		return target;
	}
	public static void fixDrops(List<ItemStack> drops) {
		HashMap<ItemStack, ItemStack> itens = new HashMap<>();
		for (ItemStack drop : drops) {
			Material type = drop.getType();
			if (type == Material.AIR | type == null) {
				continue;
			}
			boolean find = false;
			for (Entry<ItemStack, ItemStack> entry : itens.entrySet()) {
				if (drop.isSimilar(entry.getKey())) {
					ItemStack item = entry.getKey();
					item.setAmount(item.getAmount() + drop.getAmount());
					find = true;
					break;
				}
			}
			if (!find) {
				itens.put(drop, drop);
			}
			
		}
		drops.clear();
		drops.addAll(itens.values());
	}
}
