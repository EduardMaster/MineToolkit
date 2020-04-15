package net.eduard.api.lib.game;

import java.util.Map;

import net.eduard.api.lib.modules.MineReflect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;

public class Particle implements Storable {

	private Location location;

	private int amount;

	private ParticleType particle;

	private float speed;

	private float xRandom;

	private float yRandom;

	private float zRandom;
public Particle() {
}
	public Particle(ParticleType type, Location location, int amount) {
		setLocation(location);
		setAmount(amount);
		setParticle(type);
	}

	public Particle(ParticleType type, Location location, int amount, float random, float speed) {

		this(type, location, amount);
		setxRandom(random);
		setyRandom(random);
		setzRandom(random);
		setSpeed(speed);
	}

	public Particle(ParticleType type, Location location, int amount, float xRandom, float yRandom, int zRandom) {

		this(type, location, amount);
		setxRandom(xRandom);
		setyRandom(yRandom);
		setzRandom(zRandom);
	}

	public Particle(ParticleType type, Location location, int amount, float xRandom, float yRandom, int zRandom,
			float speed) {

		this(type, location, amount, xRandom, yRandom, zRandom);
		setSpeed(speed);
	}

	public Particle create() {
		try {
			MineReflect.sendPackets(getPacket());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this;
	}

	private Object getPacket() throws Exception {

		return Extra.getNew(MineReflect.classPacketPlayOutWorldParticles, particle.getParticleName(), (float) location.getX(),
				(float) location.getY(), (float) location.getZ(), xRandom, yRandom, zRandom, speed, amount);
	}

	public Particle create(Player p) {

		try {
			MineReflect.sendPacket(getPacket(), p);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this;
	}

	public int getAmount() {

		return amount;
	}

	public Location getLocation() {

		return location;
	}

	public ParticleType getParticle() {

		return particle;
	}

	public float getSpeed() {

		return speed;
	}

	public float getxRandom() {

		return xRandom;
	}

	public float getyRandom() {

		return yRandom;
	}

	public float getzRandom() {

		return zRandom;
	}

	public void setAmount(int amount) {

		this.amount = amount;
	}

	public void setLocation(Location location) {

		this.location = location;
	}

	public void setParticle(ParticleType particle) {

		this.particle = particle;
	}

	public void setSpeed(float speed) {

		this.speed = speed;
	}

	public void setxRandom(float xRandom) {

		this.xRandom = xRandom;
	}

	public void setyRandom(float yRandom) {

		this.yRandom = yRandom;
	}

	public void setzRandom(float zRandom) {

		this.zRandom = zRandom;
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
