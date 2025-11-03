package me.sjun.dev.mirae.event.account;

import me.sjun.dev.mirae.account.MXAccount;
import me.sjun.dev.mirae.event.MXEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An account-related event.
 */
public abstract class AccountEvent extends MXEvent {
    /**
     * Creates a new event.
     *
     * @param account The account
     */
    public AccountEvent(@NotNull MXAccount account) {
        this.account = account;
    }

    protected final @NotNull MXAccount account;

    /**
     * Returns the account.
     *
     * @return The account
     */
    public @NotNull MXAccount getAccount() {
        return account;
    }
}
