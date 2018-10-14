package net.eduard.api.lib.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.click.PlayerEffect;
import net.eduard.api.lib.game.Effects;
import net.eduard.api.lib.game.Explosion;
import net.eduard.api.lib.game.Jump;
import net.eduard.api.lib.game.Sounds;

public class EffectManager extends TimeManager implements PlayerEffect {
	private List<PotionEffect> potions = new ArrayList<>();
	private List<ItemStack> items = new ArrayList<>();
	private PlayerEffect effect;
	private String permission;
	private Location teleport;
	private String command;
	private Effects display;
	private Sounds sound;
	private Explosion explosion;
	private Jump jump;
	private String message;
	private List<String> commands = new ArrayList<>();
	private boolean closeInventory;
	private boolean clearInventory;
	private boolean clearHotBar;
	private boolean clearArmours;
	private boolean clearItems;

	public EffectManager() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public EffectManager clone() {
		try {
			return (EffectManager) super.clone();
		} catch (Exception e) {
			return null;
		}
	}

	public EffectManager makeCommand(Player p) {
		if (command != null) {
			p.chat("/" + command);
		}
		return this;
	}

	public EffectManager sendMessage(Player p) {
		if (message != null) {
			p.sendMessage(message);
		}
		return this;
	}

	public EffectManager displayEffect(Player p) {
		if (display != null) {
			display.create(p);
		}
		return this;

	}

	public EffectManager makeCustomEffect(Player p) {
		if (effect != null) {
			effect.effect(p);
		}
		return this;
	}

	public EffectManager makeExplosion(Entity entity) {
		if (explosion != null) {
			explosion.create(entity);
		}
		return this;
	}

	public EffectManager makeSound(Entity entity) {
		if (sound != null) {
			sound.create(entity);
		}
		return this;
	}

	public EffectManager teleport(Entity entity) {
		if (teleport != null) {
			entity.teleport(teleport);
		}
		return this;
	}

	public EffectManager jump(Entity entity) {
		if (jump != null) {
			jump.create(entity);
		}
		return this;

	}

	public boolean hasPermission(Player p) {
		if (permission != null)
			if (!p.hasPermission(permission))
				return false;
		return true;
	}

	public EffectManager closeInventory(Player p) {
		if (closeInventory) {
			p.closeInventory();
		}
		return this;
	}

	public EffectManager clearInventory(Player p) {
		if (clearInventory) {
			Mine.clearInventory(p);
		}
		return this;
	}

	public EffectManager clearItems(LivingEntity livingEntity) {
		if (clearItems) {
			Mine.clearItens(livingEntity);
		}
		return this;
	}

	public EffectManager clearArmours(LivingEntity livingEntity) {
		if (clearArmours) {
			Mine.clearArmours(livingEntity);
		}
		return this;

	}

	public EffectManager clearHotBar(Player p) {

		if (clearHotBar) {
			Mine.clearHotBar(p);
		}
		return this;
	}

	public EffectManager giveItems(Player p) {
		Mine.give(items, p.getInventory());
		return this;
	}

	public EffectManager givePotions(LivingEntity entity) {
		entity.addPotionEffects(potions);
		return this;
	}

	@Override
	public void effect(Player p) {
		makeCustomEffect(p);
		if (!hasPermission(p))
			return;
		makeCommand(p);
		makeSound(p);
		sendMessage(p);
		closeInventory(p);
		clearInventory(p);
		clearHotBar(p);
		clearItems(p);
		clearArmours(p);
		teleport(p);

		jump(p);
		givePotions(p);
		giveItems(p);

		displayEffect(p);

	}

	public String getPermission() {
		return permission;
	}

	public EffectManager permission(String permission) {
		this.permission = permission;
		return this;
	}

	public List<ItemStack> getItems() {
		return items;

	}

	public EffectManager items(List<ItemStack> items) {
		this.items = items;
		return this;
	}

	public EffectManager items(ItemStack... items) {
		return items(Arrays.asList(items));
	}

	public Location getTeleport() {
		return teleport;
	}

	public EffectManager teleport(Location location) {
		this.teleport = location;
		return this;
	}

	public String getCommand() {
		return command;
	}

	public EffectManager command(String command) {
		this.command = command;
		return this;
	}

	public boolean isCloseInventory() {
		return closeInventory;
	}

	public EffectManager closeInventory(boolean closeInventory) {
		this.closeInventory = closeInventory;
		return this;
	}

	public boolean isClearInventory() {
		return clearInventory;
	}

	public EffectManager clearInventory(boolean clearInventory) {
		this.clearInventory = clearInventory;
		return this;
	}

	public boolean isClearHotBar() {
		return clearHotBar;
	}

	public EffectManager clearHotBar(boolean clearHotBar) {
		this.clearHotBar = clearHotBar;
		return this;
	}

	public boolean isClearArmours() {
		return clearArmours;
	}

	public EffectManager clearArmours(boolean clearArmours) {
		this.clearArmours = clearArmours;
		return this;
	}

	public boolean isClearItems() {
		return clearItems;
	}

	public EffectManager clearItems(boolean clearItems) {
		this.clearItems = clearItems;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public EffectManager message(String message) {
		this.message = message;
		return this;
	}

	public List<PotionEffect> getPotions() {
		return potions;
	}

	public EffectManager potions(List<PotionEffect> potions) {
		this.potions = potions;
		return this;
	}

	public PlayerEffect getEffect() {
		return effect;
	}

	public EffectManager newEffect(PlayerEffect effect) {
		this.effect = effect;
		return this;
	}

	public Sounds getSound() {
		return sound;
	}

	public EffectManager sound(Sounds sound) {
		this.sound = sound;
		return this;
	}

	public Jump getJump() {
		return jump;
	}

	public EffectManager jump(Jump jump) {
		this.jump = jump;
		return this;
	}

	public EffectManager potions(PotionEffect... potions) {
		this.potions.addAll(Arrays.asList(potions));
		return this;
	}

	public Explosion getExplosion() {
		return explosion;
	}

	public EffectManager explosion(Explosion explosion) {
		this.explosion = explosion;
		return this;
	}

	public Effects getDisplay() {
		return display;
	}

	public EffectManager display(Effects display) {
		this.display = display;
		return this;
	}

	public List<String> getCommands() {
		return commands;
	}

	public void commands(List<String> commands) {
		this.commands = commands;
	}

}
