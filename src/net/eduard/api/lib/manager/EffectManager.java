package net.eduard.api.lib.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.click.PlayerEffect;
import net.eduard.api.lib.game.VisualEffect;
import net.eduard.api.lib.game.Explosion;
import net.eduard.api.lib.game.Jump;
import net.eduard.api.lib.game.SoundEffect;
import net.eduard.api.lib.modules.Copyable;

public class EffectManager extends TimeManager implements PlayerEffect, Copyable {

	private transient PlayerEffect effect;
	private String REQUIRE_PERMISSION;
	private String SEND_MESSAGE;
	private List<String> MAKE_COMMANDS = new ArrayList<>();
	private Location TELEPORT_TO;
	private List<ItemStack> GIVE_ITEMS = new ArrayList<>();
	private VisualEffect SHOW_VISUAL_EFFECT;
	private List<PotionEffect> APPLY_EFFECTS = new ArrayList<>();
	private SoundEffect PLAY_SOUND;
	private Jump JUMP_TO;
	private Explosion MAKE_EXPLOSION;
	private boolean CLOSE_INVENTORY;
	private boolean CLEAR_INVENTORY;

	public EffectManager() {
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void effect(Player p) {
		if (effect != null)
			effect.effect(p);
		if (!p.hasPermission(REQUIRE_PERMISSION))
			return;
		for (String cmd : MAKE_COMMANDS) {
			Mine.makeCommand(cmd);
		}
		if (PLAY_SOUND != null)
			PLAY_SOUND.create(p);
		if (SEND_MESSAGE != null) {
			p.sendMessage(SEND_MESSAGE);
		}
		if (CLOSE_INVENTORY)
			p.closeInventory();
		if (CLEAR_INVENTORY) {
			Mine.clearInventory(p);
		}
		if (TELEPORT_TO != null)
			p.teleport(TELEPORT_TO);
		if (JUMP_TO != null)
			JUMP_TO.create(p);

		if (SHOW_VISUAL_EFFECT != null) {
			SHOW_VISUAL_EFFECT.create(p);
		}
		for (ItemStack item : GIVE_ITEMS) {
			p.getInventory().addItem(item);
		}
		for (PotionEffect pot : APPLY_EFFECTS) {
			pot.apply(p);
		}

	}


	public List<String> getCommands() {
		return MAKE_COMMANDS;
	}

	public void setCommands(List<String> commands) {
		this.MAKE_COMMANDS = commands;
	}

	public PlayerEffect getEffect() {
		return effect;
	}

	public void setEffect(PlayerEffect effect) {
		this.effect = effect;
	}

	public String getPermission() {
		return REQUIRE_PERMISSION;
	}

	public void setPermission(String permission) {
		this.REQUIRE_PERMISSION = permission;
	}

	public Location getTeleport() {
		return TELEPORT_TO;
	}

	public void setTeleport(Location teleport) {
		this.TELEPORT_TO = teleport;
	}

	public String getMessage() {
		return SEND_MESSAGE;
	}

	public void setMessage(String message) {
		this.SEND_MESSAGE = message;
	}

	public VisualEffect getDisplay() {
		return SHOW_VISUAL_EFFECT;
	}

	public void setDisplay(VisualEffect display) {
		this.SHOW_VISUAL_EFFECT = display;
	}

	public List<PotionEffect> getPotions() {
		return APPLY_EFFECTS;
	}

	public void setPotions(List<PotionEffect> potions) {
		this.APPLY_EFFECTS = potions;
	}

	public List<ItemStack> getItems() {
		return GIVE_ITEMS;
	}

	public void setItems(List<ItemStack> items) {
		this.GIVE_ITEMS = items;
	}

	public SoundEffect getSound() {
		return PLAY_SOUND;
	}

	public void setSound(SoundEffect sound) {
		this.PLAY_SOUND = sound;
	}

	public Jump getJump() {
		return JUMP_TO;
	}

	public void setJump(Jump jump) {
		this.JUMP_TO = jump;
	}

	public Explosion getExplosion() {
		return MAKE_EXPLOSION;
	}

	public void setExplosion(Explosion explosion) {
		this.MAKE_EXPLOSION = explosion;
	}

	public boolean isCloseInventory() {
		return CLOSE_INVENTORY;
	}

	public void setCloseInventory(boolean closeInventory) {
		this.CLOSE_INVENTORY = closeInventory;
	}

	public boolean isClearInventory() {
		return CLEAR_INVENTORY;
	}

	public void setClearInventory(boolean clearInventory) {
		this.CLEAR_INVENTORY = clearInventory;
	}

}
