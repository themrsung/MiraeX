package me.sjun.dev.mirae.account;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public sealed interface AccountLedger extends Serializable permits ConcurrentAccountLedger {
    /**
     * Returns a list of all accounts.
     *
     * @return The list of all accounts
     */
    @NotNull List<MXAccount> getAccounts();

    /**
     * Returns the account associated with the unique identifier.
     *
     * @param uniqueId The unique identifier
     * @return The optional account
     */
    @NotNull Optional<MXAccount> getAccount(@NotNull UUID uniqueId);

    /**
     * Creates a new account.
     *
     * @param uniqueId The unique identifier
     * @param name     The name
     * @return The created account
     * @throws IllegalArgumentException When the unique identifier is already taken
     */
    @NotNull MXAccount createAccount(@NotNull UUID uniqueId, @NotNull String name) throws IllegalArgumentException;

    /**
     * Creates a new account.
     *
     * @param player The player
     * @return The created account
     * @throws IllegalArgumentException When the unique identifier is already taken
     */
    @NotNull MXAccount createAccount(@NotNull OfflinePlayer player) throws IllegalArgumentException;

    /**
     * Removes the account.
     *
     * @param uniqueId The unique identifier
     * @return {@code true} if it was successfully removed
     */
    boolean removeAccount(@Nullable UUID uniqueId);

    /**
     * Adds balance to the account.
     *
     * @param account The account
     * @param amount  The amount
     * @param message The message
     */
    void addBalance(@NotNull MXAccount account, double amount, @NotNull String message);

    /**
     * Subtracts balance from the account.
     *
     * @param account The account
     * @param amount  The amount
     * @param message The message
     */
    void subtractBalance(@NotNull MXAccount account, double amount, @NotNull String message);

    /**
     * Transfers balance.
     *
     * @param sender    The sender
     * @param recipient The recipient
     * @param amount    The amount to send
     * @param message   The message
     * @return {@code true} if it was successful
     */
    boolean transferBalance(@NotNull MXAccount sender, @NotNull MXAccount recipient, double amount, @NotNull String message);

    /**
     * Forcefully transfers balance.
     *
     * @param sender    The sender
     * @param recipient The recipient
     * @param amount    The amount
     * @param message   The message
     */
    void forceTransferBalance(@NotNull MXAccount sender, @NotNull MXAccount recipient, double amount, @NotNull String message);
}
