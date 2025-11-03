package me.sjun.dev.mirae.account;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
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
     * Returns the name.
     *
     * @return The name
     */
    @NotNull String getName();

    /**
     * Returns the balance.
     *
     * @return The balance
     */
    double getBalance();
}
