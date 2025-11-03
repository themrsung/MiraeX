package me.sjun.dev.mirae.config;

import com.google.gson.*;
import me.sjun.dev.mirae.gson.GsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Configuration file.
 */
public final class MXConfig implements Serializable {
    /// Static

    /**
     * The save path.
     */
    private static final @NotNull String SAVE_PATH = "plugins/MiraeX/";

    /**
     * Returns the save path.
     * @return The save path
     */
    public static @NotNull String getSavePath() {
        return SAVE_PATH;
    }

    /// Modifiable

    /**
     * Initializes a new config.
     */
    public MXConfig() {
        this.serverName = "미래 온라인";
        this.currencyName = "크레딧";
        this.currencySymbol = "Ç";
    }

    private @NotNull String serverName;
    private @NotNull String currencyName;
    private @NotNull String currencySymbol;

    /**
     * Returns the server name.
     *
     * @return The server name
     */
    public @NotNull String getServerName() {
        return serverName;
    }

    /**
     * Sets the server name.
     *
     * @param serverName The server name
     */
    public void setServerName(@NotNull String serverName) {
        this.serverName = serverName;
    }

    /**
     * Returns the currency name.
     *
     * @return The currency name
     */
    public @NotNull String getCurrencyName() {
        return currencyName;
    }

    /**
     * Sets the currency name.
     *
     * @param currencyName The currency name
     */
    public void setCurrencyName(@NotNull String currencyName) {
        this.currencyName = currencyName;
    }

    /**
     * Returns the currency symbol.
     *
     * @return The currency symbol
     */
    public @NotNull String getCurrencySymbol() {
        return currencySymbol;
    }

    /**
     * Sets the currency symbol.
     *
     * @param currencySymbol The currency symbol
     */
    public void setCurrencySymbol(@NotNull String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    /**
     * Returns the JSON serializer.
     *
     * @return The JSON serializer
     */
    public static @NotNull Serializer serializer() {
        return Serializer.serializer;
    }

    /**
     * JSON serialization.
     */
    public static final class Serializer implements GsonSerializer<MXConfig> {
        private static final Serializer serializer = new Serializer();

        private Serializer() {
        }

        @Override
        public JsonElement serialize(MXConfig config, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("serverName", config.serverName);
            object.addProperty("currencyName", config.currencyName);
            object.addProperty("currencySymbol", config.currencySymbol);
            return object;
        }

        @Override
        public MXConfig deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (element == null || element.isJsonNull()) {
                throw new JsonParseException("Cannot deserialize null config.");
            }

            if (!element.isJsonObject()) {
                throw new JsonParseException("Expected JsonObject for MXConfig.");
            }

            JsonObject object = element.getAsJsonObject();

            if (!object.has("serverName") || object.get("serverName").isJsonNull()) {
                throw new JsonParseException("Required parameter serverName missing.");
            }

            if (!object.has("currencyName") || object.get("currencyName").isJsonNull()) {
                throw new JsonParseException("Required parameter currencyName missing.");
            }

            if (!object.has("currencySymbol") || object.get("currencySymbol").isJsonNull()) {
                throw new JsonParseException("Required parameter currencySymbol missing.");
            }

            MXConfig config = new MXConfig();
            config.setServerName(object.get("serverName").getAsString());
            config.setCurrencyName(object.get("currencyName").getAsString());
            config.setCurrencySymbol(object.get("currencySymbol").getAsString());

            return config;
        }
    }
}
