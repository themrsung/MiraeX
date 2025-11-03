package me.sjun.dev.mirae.gson;

import com.google.gson.*;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public interface GsonSerializer<T> extends JsonSerializer<T>, JsonDeserializer<T> {
    @Override
    JsonElement serialize(T t, Type type, JsonSerializationContext context);

    @Override
    T deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException;

    static <X> JsonArray serializeArray(X[] values, JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        Arrays.stream(values).map(context::serialize).forEach(array::add);
        return array;
    }

    static <X> JsonArray serializeCollection(Collection<X> values, JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        values.stream().map(context::serialize).forEach(array::add);
        return array;
    }

    static <X> JsonArray serializeNestedArray(X[][] values, JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        Arrays.stream(values).map(x -> serializeArray(x, context)).forEach(array::add);
        return array;
    }

    static <K, V> JsonArray serializeMap(Map<K, V> map, JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        map.forEach((k, v) -> {
            JsonObject entry = new JsonObject();
            entry.add("key", context.serialize(k));
            entry.add("value", context.serialize(v));
            array.add(entry);
        });
        return array;
    }

    @SuppressWarnings("unchecked")
    static <X> X[] deserializeArray(JsonArray values, JsonDeserializationContext context, Class<X> type) {
        X[] array = (X[]) Array.newInstance(type, values.size());

        for (int i = 0; i < array.length; i++) {
            array[i] = context.deserialize(values.get(i), type);
        }

        return array;
    }

    static <K, V> Map<K, V> deserializeMap(JsonElement json, JsonDeserializationContext context, Class<K> keyType, Class<V> valueType) {
        JsonArray array = json.getAsJsonArray();
        Map<K, V> map = new LinkedHashMap<>();
        array.forEach(entry -> {
            if (!(entry instanceof JsonObject o)) return;
            if (!o.has("key") || !o.has("value")) return;

            K key = context.deserialize(o.get("key"), keyType);
            V value = context.deserialize(o.get("value"), valueType);

            map.put(key, value);
        });

        return Map.copyOf(map);
    }
}
