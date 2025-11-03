package me.sjun.dev.mirae.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * LDT serialization.
 */
public final class LocalDateSerializer implements GsonSerializer<LocalDate> {
    /**
     * Returns the JSON serializer.
     *
     * @return The JSON serializer
     */
    public static LocalDateSerializer serializer() {
        return serializer;
    }

    private static final LocalDateSerializer serializer = new LocalDateSerializer();

    private LocalDateSerializer() {
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public JsonElement serialize(LocalDate dateTime, Type type, JsonSerializationContext context) {
        if (dateTime == null) return JsonNull.INSTANCE;
        return new JsonPrimitive(formatter.format(dateTime));
    }

    @Override
    public LocalDate deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!(element instanceof JsonPrimitive p)) return null;
        String s = p.getAsString();
        try {
            return LocalDate.parse(s, formatter);
        } catch (DateTimeParseException e) {
            throw new JsonParseException(e);
        }
    }
}
