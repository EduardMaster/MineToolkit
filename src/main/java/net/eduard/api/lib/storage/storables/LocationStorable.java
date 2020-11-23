package net.eduard.api.lib.storage.storables;

import com.google.gson.*;
import net.eduard.api.lib.storage.StorageAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.eduard.api.lib.storage.Storable;

import java.lang.reflect.Type;
import java.util.Map;

public class LocationStorable implements Storable<Location>, JsonSerializer<Location>, JsonDeserializer<Location> {

	public Location newInstance() {
		return new Location(Bukkit.getWorlds().get(0), 1, 1, 1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		return (Location) StorageAPI.restore(Location.class,jsonDeserializationContext.deserialize(jsonElement, Map.class)); // connection pool
	}



	@Override
	public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
		return jsonSerializationContext.serialize(StorageAPI.store(Location.class,location));
	}
}
