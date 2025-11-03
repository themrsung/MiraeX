package me.sjun.dev.mirae.account;

import com.google.gson.*;
import me.sjun.dev.mirae.event.account.BalanceModifyEvent;
import me.sjun.dev.mirae.event.account.BalanceTransferEvent;
import me.sjun.dev.mirae.gson.GsonSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Concurrent account ledger.
 */
public final class ConcurrentAccountLedger implements AccountLedger {
    /**
     * Creates a new account ledger.
     */
    public ConcurrentAccountLedger() {
        this.accountMap = new HashMap<>();
    }

    private final Map<@NotNull UUID, @NotNull MXAccount> accountMap;

    @Override
    public @NotNull List<MXAccount> getAccounts() {
        return List.copyOf(accountMap.values());
    }

    @Override
    public @NotNull Optional<MXAccount> getAccount(@NotNull UUID uniqueId) {
        return Optional.ofNullable(accountMap.get(uniqueId));
    }

    @Override
    public @NotNull MXAccount createAccount(@NotNull UUID uniqueId, @NotNull String name) throws IllegalArgumentException {
        if (accountMap.containsKey(uniqueId)) {
            throw new IllegalArgumentException("UUID is already taken.");
        }

        MXAccount account = new LocalAccount(uniqueId, name);
        accountMap.put(uniqueId, account);
        return account;
    }

    @Override
    public @NotNull MXAccount createAccount(@NotNull OfflinePlayer player) throws IllegalArgumentException {
        return createAccount(player.getUniqueId(), Objects.requireNonNullElse(player.getName(), "null"));
    }

    @Override
    public boolean removeAccount(@Nullable UUID uniqueId) {
        return Objects.nonNull(accountMap.remove(uniqueId));
    }

    @Override
    public void addBalance(@NotNull MXAccount account, double amount, @NotNull String message) {
        AbstractAccount abs = (AbstractAccount) account;
        abs.balance += amount;
        Bukkit.getPluginManager().callEvent(new BalanceModifyEvent(account, amount, message));
    }

    @Override
    public void subtractBalance(@NotNull MXAccount account, double amount, @NotNull String message) {
        AbstractAccount abs = (AbstractAccount) account;
        abs.balance -= amount;
        Bukkit.getPluginManager().callEvent(new BalanceModifyEvent(account, amount, message));
    }

    @Override
    public boolean transferBalance(@NotNull MXAccount sender, @NotNull MXAccount recipient, double amount, @NotNull String message) {
        return false;
    }

    @Override
    public void forceTransferBalance(@NotNull MXAccount sender, @NotNull MXAccount recipient, double amount, @NotNull String message) {
        AbstractAccount absSender = (AbstractAccount) sender;
        AbstractAccount absRecipient = (AbstractAccount) recipient;

        absSender.balance -= amount;
        absRecipient.balance += amount;

        Bukkit.getPluginManager().callEvent(new BalanceTransferEvent(sender, recipient, amount, message));
    }

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
    static final class Serializer implements GsonSerializer<ConcurrentAccountLedger> {
        private static final Serializer serializer = new Serializer();

        private Serializer() {
        }

        @Override
        public JsonElement serialize(ConcurrentAccountLedger ledger, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.add("accounts", GsonSerializer.serializeCollection(ledger.getAccounts(), context));
            return object;
        }

        @Override
        public ConcurrentAccountLedger deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = element.getAsJsonObject();
            ConcurrentAccountLedger ledger = new ConcurrentAccountLedger();
            Arrays.stream(GsonSerializer.deserializeArray(object.getAsJsonArray("accounts"), context, MXAccount.class))
                    .forEach(account -> ledger.accountMap.put(account.getUniqueId(), account));

            return ledger;
        }
    }
}
