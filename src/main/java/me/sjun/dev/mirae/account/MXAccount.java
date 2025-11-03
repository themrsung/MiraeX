package me.sjun.dev.mirae.account;

import com.google.gson.*;
import me.sjun.dev.mirae.gson.GsonSerializer;
import me.sjun.dev.mirae.storage.ItemStorage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

/**
 * MiraeX account.
 */
public sealed interface MXAccount extends Serializable permits AbstractAccount {
    /**
     * Returns the unique identifier.
     *
     * @return The unique identifier
     */
    @NotNull UUID getUniqueId();

    /**
     * Returns the offline player.
     *
     * @return The offline player
     */
    @NotNull OfflinePlayer getOfflinePlayer();

    /**
     * Returns the player.
     *
     * @return The player
     */
    @NotNull Optional<Player> getPlayer();

    /**
     * Returns the name.
     *
     * @return The name
     */
    @NotNull String getName();

    /**
     * Sets the name.
     *
     * @param name The name
     */
    void setName(@NotNull String name);

    /**
     * Updates the name from Bukkit servers.
     */
    void updateName();

    /**
     * Returns the balance.
     *
     * @return The balance
     */
    double getBalance();

    /**
     * Returns the item storage.
     *
     * @return The item storage
     */
    @NotNull ItemStorage getItemStorage();

    /**
     * Returns the account type.
     *
     * @return The account type
     */
    @NotNull AccountType getType();

    /**
     * Returns the JSON serializer.
     *
     * @return The JSON serializer
     */
    static @NotNull Serializer serializer() {
        return Serializer.serializer;
    }

    /**
     * JSON serialization.
     */
    final class Serializer implements GsonSerializer<MXAccount> {
        private static final Serializer serializer = new Serializer();

        private Serializer() {
        }

        @Override
        public JsonElement serialize(MXAccount account, Type type, JsonSerializationContext context) {
            return switch (account) {
                case LocalAccount local -> LocalAccount.serializer().serialize(local, type, context);
            };
        }

        @Override
        public MXAccount deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = element.getAsJsonObject();
            if (!object.has("type") || object.get("type").isJsonNull()) {
                throw new JsonParseException("Required parameter type missing.");
            }

            AccountType accountType = context.deserialize(object.get("type"), AccountType.class);

            if (accountType == null) {
                throw new JsonParseException("Unable to deserialize account type.");
            }

            return switch (accountType) {
                case LOCAL -> LocalAccount.serializer().deserialize(element, type, context);
            };
        }
    }
}
