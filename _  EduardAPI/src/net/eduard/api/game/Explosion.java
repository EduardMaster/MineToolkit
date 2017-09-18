package net.eduard.api.game;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import net.eduard.api.setup.StorageAPI.Storable;

public class Explosion implements Storable{

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
