package net.eduard.api.lib.storage.storables;

import java.lang.reflect.Type;
import java.sql.Timestamp;

import com.google.gson.*;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.annotations.StorageAttributes;

@StorageAttributes(inline = true)
public class TimeStampStorable implements Storable<Timestamp>  , JsonDeserializer<Timestamp>, JsonSerializer<Timestamp> {

    public String store(Timestamp timestamp) {

        return "" + timestamp.getTime();

    }


    public Timestamp restore(Object string) {
        return new Timestamp(Extra.toLong(string));


    }
    @Override
    public Timestamp deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        return new Timestamp(jsonElement.getAsLong());
    }

    @Override
    public JsonElement serialize(Timestamp timestamp, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(timestamp.getTime());
    }

}
