package net.eduard.api.server.kits;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftSnowball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_7_R4.EntityFishingHook;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntitySnowball;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PlayerConnection;

public class GrapplerHook extends EntityFishingHook {
	private Snowball sb;
	private EntitySnowball controller;
	public int a;
	public EntityHuman owner;
	public Entity hooked;
	public boolean lastControllerDead;
	public boolean isHooked;
	public static Vector moveTo(Location entity, Location target, double defaultX, double defaultY, double defaultZ,
			double addX, double addY, double addZ) {
		double distance = target.distance(entity);
		double x = (defaultX + (addX * distance)) * ((target.getX() - entity.getX()) / distance);
		double y = (defaultY + (addY * distance)) * ((target.getY() - entity.getY()) / distance);
		double z = (defaultZ + (addZ * distance)) * ((target.getZ() - entity.getZ()) / distance);
		return new Vector(x, y, z);

	}
	public GrapplerHook(Player p,double force) {
		super(((CraftPlayer) p).getHandle().getWorld(), ((CraftPlayer) p).getHandle());
		owner = ((CraftPlayer) p).getHandle();
		Location location = p.getEyeLocation();
		sb = (p.launchProjectile(Snowball.class));
		controller = ((CraftSnowball) sb).getHandle();
		this.sb.setVelocity(location.getDirection().multiply(force));
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { controller.getId() });

		for (Player player : Bukkit.getOnlinePlayers()) {
			try {
				try {
					PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
					connection.sendPacket(packet);
				} catch (Exception ex) {
				}
			} catch (Exception ex) {
			}
		}
		((CraftWorld) location.getWorld()).getHandle().addEntity(this);

	}

	public void spawn(Location location) {

	}

	@Override
	protected void c() {
	}

	@Override
	public void h() {
		lastControllerDead = controller.dead;
		for (Entity entity : controller.world.getWorld().getEntities()) {
			if ((!(entity instanceof org.bukkit.entity.Firework))
					&& (entity.getEntityId() != getBukkitEntity().getEntityId())
					&& (entity.getEntityId() != owner.getBukkitEntity().getEntityId())
					&& (entity.getEntityId() != controller.getBukkitEntity().getEntityId())
					&& ((entity.getLocation().distance(controller.getBukkitEntity().getLocation()) < 2.0D)
							|| (((entity instanceof Player)) && (((Player) entity).getEyeLocation()
									.distance(controller.getBukkitEntity().getLocation()) < 2.0D)))) {
				controller.die();
				hooked = entity;
				isHooked = true;
				locX = entity.getLocation().getX();
				locY = entity.getLocation().getY();
				locZ = entity.getLocation().getZ();
				motX = 0.0D;
				motY = 0.04D;
				motZ = 0.0D;
			}
		}
		try {
			locX = hooked.getLocation().getX();
			locY = hooked.getLocation().getY();
			locZ = hooked.getLocation().getZ();
			motX = 0.0D;
			motY = 0.04D;
			motZ = 0.0D;
			isHooked = true;
		} catch (Exception e) {
			if (controller.dead) {
				isHooked = true;
			}
			locX = controller.locX;
			locY = controller.locY;
			locZ = controller.locZ;
		}
	}

	@Override
	public void die() {
	}

	public void remove() {
		super.die();
	}

	public boolean isHooked() {
		return isHooked;
	}

	public void setHookedEntity(Entity damaged) {
		hooked = damaged;
	}
}
