package net.eduard.api.lib.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.click.PlayerEffect;
import net.eduard.api.lib.game.Effects;
import net.eduard.api.lib.game.Explosion;
import net.eduard.api.lib.game.Jump;
import net.eduard.api.lib.game.Sounds;
import net.eduard.api.lib.modules.Copyable;

public class EffectManager extends TimeManager implements PlayerEffect, Copyable {

	private transient PlayerEffect effect;
	private String permission;
	private String message;
	private List<String> commands = new ArrayList<>();
	private Location teleport;
	private List<ItemStack> items = new ArrayList<>();
	private Effects display;
	private List<PotionEffect> potions = new ArrayList<>();
	private Sounds sound;
	private Jump jump;
	private Explosion explosion;
	private boolean closeInventory;
	private boolean clearInventory;

	public EffectManager() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void effect(Player p) {
		if (effect != null)
			effect.effect(p);
		if (!p.hasPermission(permission))
			return;
		for (String cmd : commands) {
			Mine.makeCommand(cmd);
		}
		if (sound != null)
			sound.create(p);
		if (message != null) {
			p.sendMessage(message);
		}
		if (closeInventory)
			p.closeInventory();
		if (clearInventory) {
			Mine.clearInventory(p);
		}
		if (teleport != null)
			p.teleport(teleport);
		if (jump != null)
			jump.create(p);

		if (display != null) {
			display.create(p);
		}
		for (ItemStack item : items) {
			p.getInventory().addItem(item);
		}
		for (PotionEffect pot : potions) {
			pot.apply(p);
		}

	}


	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public PlayerEffect getEffect() {
		return effect;
	}

	public void setEffect(PlayerEffect effect) {
		this.effect = effect;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Location getTeleport() {
		return teleport;
	}

	public void setTeleport(Location teleport) {
		this.teleport = teleport;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Effects getDisplay() {
		return display;
	}

	public void setDisplay(Effects display) {
		this.display = display;
	}

	public List<PotionEffect> getPotions() {
		return potions;
	}

	public void setPotions(List<PotionEffect> potions) {
		this.potions = potions;
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public void setItems(List<ItemStack> items) {
		this.items = items;
	}

	public Sounds getSound() {
		return sound;
	}

	public void setSound(Sounds sound) {
		this.sound = sound;
	}

	public Jump getJump() {
		return jump;
	}

	public void setJump(Jump jump) {
		this.jump = jump;
	}

	public Explosion getExplosion() {
		return explosion;
	}

	public void setExplosion(Explosion explosion) {
		this.explosion = explosion;
	}

	public boolean isCloseInventory() {
		return closeInventory;
	}

	public void setCloseInventory(boolean closeInventory) {
		this.closeInventory = closeInventory;
	}

	public boolean isClearInventory() {
		return clearInventory;
	}

	public void setClearInventory(boolean clearInventory) {
		this.clearInventory = clearInventory;
	}

}
