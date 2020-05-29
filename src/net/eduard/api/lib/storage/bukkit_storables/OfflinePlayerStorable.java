package net.eduard.api.lib.storage.bukkit_storables;

import java.lang.reflect.Type;
import java.util.UUID;

import com.google.gson.*;
import org.bukkit.OfflinePlayer;

import net.eduard.api.lib.game.FakePlayer;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.Storable.*;

@StorageAttributes(inline = true)
public class OfflinePlayerStorable implements Storable<OfflinePlayer>, JsonSerializer<OfflinePlayer>, JsonDeserializer<OfflinePlayer> {



    @Override
    public OfflinePlayer newInstance() {
        return new FakePlayer();
    }

    @Override
    public OfflinePlayer restore(String id) {

        if (id.contains(";")) {
            String[] split = id.split(";");
            if (split[1].equals("null")) {
                return new FakePlayer(split[0]);
            } else
                return new FakePlayer(split[0], UUID.fromString(split[1]));

        }

        return null;
    }


    public String store(OfflinePlayer p) {
        return p.getName() + ";" + p.getUniqueId();
    }

    @Override
    public OfflinePlayer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return this.restore(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(OfflinePlayer offlinePlayer, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(this.store(offlinePlayer));
    }
}
