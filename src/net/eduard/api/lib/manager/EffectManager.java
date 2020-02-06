package net.eduard.api.lib.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.click.PlayerEffect;
import net.eduard.api.lib.game.VisualEffect;
import net.eduard.api.lib.game.Explosion;
import net.eduard.api.lib.game.Jump;
import net.eduard.api.lib.game.SoundEffect;
import net.eduard.api.lib.modules.Copyable;

public class EffectManager extends TimeManager implements PlayerEffect, Copyable {

	private transient PlayerEffect effect;
	public String REQUIRE_PERMISSION;
	public String SEND_MESSAGE;
	public List<String> MAKE_PLAYER_COMMANDS = new ArrayList<>();
	public List<String> MAKE_CONSOLE_COMMANDS = new ArrayList<>();
	public Location TELEPORT_TO;
	public List<ItemStack> GIVE_ITEMS = new ArrayList<>();
	public VisualEffect SHOW_VISUAL_EFFECT;
	public List<PotionEffect> APPLY_EFFECTS = new ArrayList<>();
	public SoundEffect PLAY_SOUND;
	public Jump JUMP_TO;
	public Explosion MAKE_EXPLOSION;
	public boolean CLOSE_INVENTORY;
	public boolean CLEAR_INVENTORY;

	public PlayerEffect getEffect() {
		return effect;
	}

	public void setEffect(PlayerEffect effect) {
		this.effect = effect;
	}
	public void setDisplay(VisualEffect effect) {
		SHOW_VISUAL_EFFECT = effect;
	}
	public VisualEffect getDisplay() {
		return SHOW_VISUAL_EFFECT;
	}
	public List<ItemStack> getItems() {
		return GIVE_ITEMS;
	}
	public List<PotionEffect> getPotions() {
		return APPLY_EFFECTS;
	}

	public String getMessage() {
		return SEND_MESSAGE;
	}

	public void setJump(Jump jump) {
		JUMP_TO = jump;
	}

	public Jump getJump() {
		return JUMP_TO;
	}

	public Explosion getExplosion() {
		return MAKE_EXPLOSION;
	}

	public List<String> getPlayerCommands() {
		return MAKE_PLAYER_COMMANDS;
	}
	public SoundEffect getSound() {
		return PLAY_SOUND;
	}

	public List<String> getConsoleCommands() {
		return MAKE_CONSOLE_COMMANDS;
	}

	public void setSound(SoundEffect effect) {
		PLAY_SOUND = effect;
	}

	public void setExplosion(Explosion explosion) {
		MAKE_EXPLOSION = explosion;
	}

	public void setMessage(String msg) {
		SEND_MESSAGE = msg;
	}
	public EffectManager() {

		// TODO Auto-generated constructor stub
	}

	@Override
	public void effect(Player p) {
		if (effect != null)
			effect.effect(p);
		if (REQUIRE_PERMISSION != null)
			if (!p.hasPermission(REQUIRE_PERMISSION))
				return;
		for (String cmd : MAKE_CONSOLE_COMMANDS) {
			Mine.makeCommand(cmd.replace("$player", p.getName()));
		}
		for (String cmd : MAKE_PLAYER_COMMANDS) {
			p.performCommand(cmd.replace("$player", p.getName()).replaceFirst("/", ""));
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

}
