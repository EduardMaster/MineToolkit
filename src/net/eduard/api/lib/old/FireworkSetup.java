package net.eduard.api.lib.old;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * Sistema de criar efeitos de fogos de artifícios
 * 
 * @version 1.0
 * @since 0.7
 * @deprecated Versão nova {@link net.eduard.api.lib.old.Firework} 2.0
 * @author Eduard
 *
 */
public class FireworkSetup {
	private Firework firework;
	private FireworkMeta meta;
	private List<FireworkEffect> effects;

	public FireworkSetup(Location location) {
		this.firework = ((Firework) location.getWorld().spawn(location, Firework.class));

		this.meta = this.firework.getFireworkMeta();
		FireworkMeta meta = this.firework.getFireworkMeta();
		this.effects = meta.getEffects();
	}

	public List<FireworkEffect> getEffects() {
		return this.effects;
	}

	public Firework getFirework() {
		return this.firework;
	}

	public void setPower(int power) {
		this.meta.setPower(power);
		this.firework.setFireworkMeta(this.meta);
	}

	public void create(FireworkEffect.Type type, Color firstColor, Color lastColor) {
		create(type, true, true, firstColor, lastColor);
	}

	public void create(FireworkEffect.Type type, boolean flicker, boolean trail, Color firstColor, Color lastColor) {
		create(FireworkEffect.builder().with(type).flicker(flicker).trail(trail).withColor(firstColor)
				.withFade(lastColor).build());
	}

	public void create(FireworkEffect effect) {
		this.meta.addEffect(effect);
		this.firework.setFireworkMeta(this.meta);
	}
}
