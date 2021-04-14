package net.eduard.api.lib.storage.storables;

import net.eduard.api.lib.storage.annotations.StorageAttributes;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;

@StorageAttributes(inline = true)
public class ChunkStorable implements Storable<Chunk> {

    public Chunk restore(String string) {
        String[] split = string.split(";");
        return Bukkit.getWorld(split[0]).getChunkAt(Extra.toInt(split[1]), Extra.toInt(split[2]));
    }

    public String store(Chunk chunk) {
        return chunk.getWorld().getName() + ";" + chunk.getX() + ";" + chunk.getZ();
    }

}
