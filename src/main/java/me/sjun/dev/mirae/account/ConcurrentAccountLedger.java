package me.sjun.dev.mirae.account;

import me.sjun.dev.mirae.event.account.BalanceModifyEvent;
import me.sjun.dev.mirae.event.account.BalanceTransferEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        MXAccount account = new AbstractAccount(uniqueId, name);
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
}
