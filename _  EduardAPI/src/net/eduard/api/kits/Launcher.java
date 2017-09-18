package net.eduard.api.kits;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import net.eduard.api.game.Ability;
import net.eduard.api.game.Jump;
import net.eduard.api.game.LaunchPad;
import net.eduard.api.game.Sounds;

public class Launcher extends Ability {

	private LaunchPad pad= new LaunchPad(-1,19, new Jump(Sounds.create(Sound.BAT_TAKEOFF), new Vector(0, 2.5, 0)));
	

	public Launcher() {
		setIcon(Material.SPONGE, "§fGanhe 20 esponjas especiais");
		add(new ItemStack(Material.SPONGE, 20));
	};
	@Override
	public void register(Plugin plugin) {
		pad.register(plugin);
		 super.register(plugin);
	}
	@Override
	public Object restore(Map<String, Object> map) {
		pad.register(getPlugin());
		return null;
	}

}
