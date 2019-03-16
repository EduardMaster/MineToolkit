package net.eduard.api.lib.old;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Representa um efeito de pulo
 * 
 * @version 1.0
 * @see 0.9
 * @author Eduard
 * @deprecated Versão Atual {@link net.eduard.api.lib.game.Jump}<br>
 * 
 *
 */
public class Jump {
	private double force;
	private double high;
	private Jump secondEffect;
	private Sounds sound;
	private int ticksDelay;
	private Vector vector;
	private boolean withHigh;

	public Jump(boolean withHigh, double high, double force, Vector vector, Sounds sound, Jump secondEffect,
			int delay) {
		setWithHigh(withHigh);
		setHigh(high);
		setForce(force);
		setVector(vector);
		setSound(sound);
		setSecondEffect(secondEffect);
		setTicksDelay(delay);
	}

	public void create(final Entity entity) {
		Vector newVector = null;
		if (getVector() != null) {
			newVector = getVector();
		} else {
			newVector = entity.getLocation().getDirection();
		}
		if (isWithHigh()) {
			newVector.setY(getHigh());
		}
		newVector.multiply(getForce());
		entity.setVelocity(newVector);
		if (getSound() != null) {
			if ((entity instanceof Player)) {
				Player player = (Player) entity;
				getSound().create(player);
			} else {
				getSound().create(entity.getLocation());
			}
		}
		if (getSecondEffect() != null) {
			new EventSetup(getTicksDelay(), 1) {
				public void run() {
					Jump.this.getSecondEffect().create(entity);
				}
			};
		}
	}

	public double getForce() {
		return this.force;
	}

	public double getHigh() {
		return this.high;
	}

	public Jump getSecondEffect() {
		return this.secondEffect;
	}

	public Sounds getSound() {
		return this.sound;
	}

	public int getTicksDelay() {
		return this.ticksDelay;
	}

	public Vector getVector() {
		return this.vector;
	}

	public boolean isWithHigh() {
		return this.withHigh;
	}

	public void setForce(double force) {
		this.force = force;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public void setSecondEffect(Jump secondEffect) {
		this.secondEffect = secondEffect;
	}

	public void setSound(Sounds sound) {
		this.sound = sound;
	}

	public void setTicksDelay(int ticksDelay) {
		this.ticksDelay = ticksDelay;
	}

	public void setVector(Vector vector) {
		this.vector = vector;
	}

	public void setWithHigh(boolean withHigh) {
		this.withHigh = withHigh;
	}
}
