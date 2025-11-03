package me.sjun.dev.mirae.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Master CIS Gson.
 */
public final class MXGson {
    /**
     * Returns the GSON instance.
     *
     * @return The GSON instance
     */
    public static Gson gson() {
        return gson;
    }

    /**
     * Returns the pretty GSON instance.
     *
     * @return The pretty GSON instance
     */
    public static Gson prettyGson() {
        return prettyGson;
    }

    /**
     * Returns the Javalin mapper.
     *
     * @return The Javalin mapper
     */
    public static JsonMapper javalinMapper() {
        return javalinMapper;
    }

    /**
     * Serializes using pretty JSON instance.
     *
     * @param object The object to serialize
     * @return The pretty JSON
     */
    public static String serialize(Object object) {
        return prettyGson.toJson(object);
    }

    /**
     * Deserializes the JSON.
     *
     * @param json The JSON
     * @param type The type
     * @param <T>  The type
     * @return The deserialized object
     */
    public static <T> T deserialize(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    private static final GsonBuilder builder = new GsonBuilder();

    static {
        builder.registerTypeAdapter(LocalDate.class, LocalDateSerializer.serializer())
                .registerTypeAdapter(LocalDateTime.class, LocalDateTimeSerializer.serializer());
    }

    private static final Gson gson = builder.create();
    private static final Gson prettyGson = builder.setPrettyPrinting().create();

    private static final JsonMapper javalinMapper = new JsonMapper() {
        @NotNull
        @Override
        public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
            return gson.fromJson(json, targetType);
        }

        @NotNull
        @Override
        public String toJsonString(@NotNull Object obj, @NotNull Type type) {
            return prettyGson.toJson(obj, type);
        }
    };
}
