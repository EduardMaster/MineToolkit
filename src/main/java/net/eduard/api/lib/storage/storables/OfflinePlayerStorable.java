package net.eduard.api.lib.storage.storables;

import java.lang.reflect.Type;
import java.util.UUID;

import com.google.gson.*;
import net.eduard.api.lib.storage.annotations.StorageAttributes;
import org.bukkit.OfflinePlayer;

import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.storage.Storable;

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
        return new FakePlayer("Player");
    }

    public String store(OfflinePlayer player) {
        return player.getName() + ";" + player.getUniqueId();
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
