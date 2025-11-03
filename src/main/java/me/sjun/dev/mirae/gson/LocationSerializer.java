package me.sjun.dev.mirae.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Location Serializer.
 */
public final class LocationSerializer implements GsonSerializer<Location> {
    /**
     * Returns the serializer instance.
     *
     * @return The serializer instance
     */
    public static @NotNull LocationSerializer serializer() {
        return serializer;
    }

    private static final LocationSerializer serializer = new LocationSerializer();

    private LocationSerializer() {
    }

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
        return context.serialize(location.serialize());
    }

    @Override
    public Location deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return Location.deserialize(context.deserialize(element, Map.class));
    }
}
