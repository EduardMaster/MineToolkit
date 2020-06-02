package net.eduard.api.lib.game;


import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Representa uma explosão sendo feita, Força, se quebra blocos, se causa fogo
 * @version 2.0
 * @since EduardAPI 1.0
 * @author Eduard
 *
 */
public class Explosion {

	private float power;
	private boolean breakBlocks;
	private boolean makeFire;
	public Explosion() {
		
	}
	public Explosion(float power, boolean breakBlocks, boolean makeFire) {
		super();
		this.power = power;
		this.breakBlocks = breakBlocks;
		this.makeFire = makeFire;
	}
	public float getPower() {
		return power;
	}
	public void setPower(float power) {
		this.power = power;
	}
	public boolean isBreakBlocks() {
		return breakBlocks;
	}
	public void setBreakBlocks(boolean breakBlocks) {
		this.breakBlocks = breakBlocks;
	}
	public boolean isMakeFire() {
		return makeFire;
	}
	public void setMakeFire(boolean makeFire) {
		this.makeFire = makeFire;
	}

	public Explosion create(Entity entity) {
		create(entity.getLocation());
		return this;
	}

	public Explosion create(Location location) {
		location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), power, makeFire, breakBlocks);
		return this;
	}

	
	
}
