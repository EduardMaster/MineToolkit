package net.eduard.api.lib.storage.bukkit_storables;

import org.bukkit.Bukkit;
import org.bukkit.World;

import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.Storable.*;

@StorageAttributes(inline = true)
public class WorldStorable implements Storable<World> {


    public World restore(String str) {

        return Bukkit.getWorld(str);


    }


    public String store(World world) {

        return world.getName();

    }

}
