package net.eduard.api.server.kits;

import net.eduard.api.server.kit.KitAbility;
import org.bukkit.Material;

public class Barbarian extends KitAbility {
    public Barbarian(String name) {
       setIcon(Material.IRON_AXE, "§fNão leve e nem cause NockBack");
       add(Material.WOOD_SWORD);
   }

}
