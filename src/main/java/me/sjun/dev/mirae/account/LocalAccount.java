package me.sjun.dev.mirae.account;

import com.google.gson.*;
import me.sjun.dev.mirae.gson.GsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Local account.
 */
public final class LocalAccount extends AbstractAccount {
    /**
     * Creates a new local account.
     *
     * @param uniqueId The unique identifier
     * @param name     The name
     */
    public LocalAccount(@NotNull UUID uniqueId, @NotNull String name) {
        super(uniqueId, name);
    }

    @Override
    public @NotNull AccountType getType() {
        return AccountType.LOCAL;
    }

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
    static final class Serializer implements GsonSerializer<LocalAccount> {
        private static final Serializer serializer = new Serializer();

        private Serializer() {
        }

        @Override
        public JsonElement serialize(LocalAccount localAccount, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.add("type", context.serialize(localAccount.getType()));
            object.add("uniqueId", context.serialize(localAccount.getUniqueId()));
            object.addProperty("name", localAccount.getName());
            object.addProperty("balance", localAccount.getBalance());
            return object;
        }

        @Override
        public LocalAccount deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (element == null || element.isJsonNull()) {
                throw new JsonParseException("Cannot deserialize null account.");
            }

            JsonObject object = element.getAsJsonObject();

            if (!object.has("type") || object.get("type").isJsonNull()) {
                throw new JsonParseException("Required parameter type missing.");
            }

            AccountType accountType = context.deserialize(object.get("type"), AccountType.class);
            if (accountType != AccountType.LOCAL) {
                throw new JsonParseException("Invalid account type for LocalAccount: " + accountType);
            }

            if (!object.has("uniqueId") || object.get("uniqueId").isJsonNull()) {
                throw new JsonParseException("Required parameter uniqueId missing.");
            }

            if (!object.has("name") || object.get("name").isJsonNull()) {
                throw new JsonParseException("Required parameter name missing.");
            }

            if (!object.has("balance") || object.get("balance").isJsonNull()) {
                throw new JsonParseException("Required parameter balance missing.");
            }

            UUID uniqueId = context.deserialize(object.get("uniqueId"), UUID.class);
            String name = object.get("name").getAsString();

            LocalAccount account = new LocalAccount(uniqueId, name);
            account.balance = object.get("balance").getAsDouble();

            return account;
        }
    }
}
