package net.eduard.api.lib.storage.storables;

import net.eduard.api.lib.storage.annotations.StorageAttributes;
import org.bukkit.Bukkit;
import org.bukkit.World;

import net.eduard.api.lib.storage.Storable;

@StorageAttributes(inline = true)
public class WorldStorable implements Storable<World> {
    public World restore(String str) {
        World mundo = Bukkit.getWorld(str);
        if (mundo == null) {
            mundo = Bukkit.getWorlds().get(0);
        }
        return mundo;
    }

    public String store(World world) {
        return world.getName();
    }

}
