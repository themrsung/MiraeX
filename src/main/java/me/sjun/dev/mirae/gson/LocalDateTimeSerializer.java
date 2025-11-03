package me.sjun.dev.mirae.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * LDT serialization.
 */
public final class LocalDateTimeSerializer implements GsonSerializer<LocalDateTime> {
    /**
     * Returns the JSON serializer.
     *
     * @return The JSON serializer
     */
    public static LocalDateTimeSerializer serializer() {
        return serializer;
    }

    private static final LocalDateTimeSerializer serializer = new LocalDateTimeSerializer();

    private LocalDateTimeSerializer() {
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

    @Override
    public JsonElement serialize(LocalDateTime dateTime, Type type, JsonSerializationContext context) {
        if (dateTime == null) return JsonNull.INSTANCE;
        return new JsonPrimitive(formatter.format(ZonedDateTime.of(dateTime, ZoneId.systemDefault())));
    }

    @Override
    public LocalDateTime deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!(element instanceof JsonPrimitive p)) return null;
        String s = p.getAsString();
        try {
            return LocalDateTime.from(ZonedDateTime.parse(s, formatter));
        } catch (DateTimeParseException e) {
            throw new JsonParseException(e);
        }
    }
}
