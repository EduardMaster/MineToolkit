package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import net.eduard.api.server.kit.KitAbility;


public class Urgal extends KitAbility{

	public Urgal() {
		setIcon(Material.POTION,8261, "�fGanhe po��es que te d�o muita for�a");
		add(new Potion(PotionType.STRENGTH,1).toItemStack(2));
	}
}
