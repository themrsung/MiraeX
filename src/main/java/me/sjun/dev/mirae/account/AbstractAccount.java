package me.sjun.dev.mirae.account;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import me.sjun.dev.mirae.gson.GsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Locally modifiable account.
 */
public final class AbstractAccount implements MXAccount {
    /**
     * Creates a new local account.
     *
     * @param uniqueId The unique identifier
     * @param name     The name
     */
    public AbstractAccount(@NotNull UUID uniqueId, @NotNull String name) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.balance = 0;
    }


    private final @NotNull UUID uniqueId;
    private @NotNull String name;
    double balance;

    /**
     * Returns the JSON serializer.
     *
     * @return THe JSON serializer
     */
    static @NotNull Serializer serializer() {
        return Serializer.serializer;
    }

    /**
     * JSON serialization.
     */
    static final class Serializer implements GsonSerializer<AbstractAccount> {
        private static final Serializer serializer = new Serializer();

        private Serializer() {
        }

        @Override
        public JsonElement serialize(AbstractAccount abstractAccount, Type type, JsonSerializationContext context) {
            return null;
        }

        @Override
        public AbstractAccount deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return null;
        }
    }
}
