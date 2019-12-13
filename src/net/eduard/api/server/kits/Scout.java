package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import net.eduard.api.server.kit.KitAbility;


public class Scout extends KitAbility{
	public Scout() {
		setIcon(Material.POTION,8261, "�fGanhe po��es que te d�o muita for�a");
		add(new Potion(PotionType.SPEED,1).toItemStack(3));
	}
}
