package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.game.Ability;
import net.eduard.api.lib.storage.game.Sounds;

public class Berserker extends Ability {
	public ItemStack soup = Mine.newItem(Material.BROWN_MUSHROOM, "§6Sopa");
	public Berserker() {
		setIcon(Material.MUSHROOM_SOUP, "§fAo eliminar um Inimigo vai ganhar sopas");
		message("§6Modo berseker ativado");
		sound(Sounds.create("AMBIENCE_THUNDER"));
		getPotions().add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 20*30));
		getPotions().add(new PotionEffect(PotionEffectType.SPEED, 0, 20*30));
	}
	@EventHandler
	public void event(EntityDeathEvent e) {
		if (e.getEntity().getKiller() instanceof Player) {
			Player p = e.getEntity().getKiller();
			if (p == null) {
				return;
			}
			if (hasKit(p)) {
				effect(p);
//				sendMessage(p);
//				givePotions(p);
//				makeSound(p);
			}
		}
	}
}
