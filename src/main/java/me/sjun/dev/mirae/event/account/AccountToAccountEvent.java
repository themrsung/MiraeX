package me.sjun.dev.mirae.event.account;

import me.sjun.dev.mirae.account.MXAccount;
import org.jetbrains.annotations.NotNull;

/**
 * An account-related event.
 */
public abstract class AccountToAccountEvent extends AccountEvent {
    /**
     * Creates a new event.
     *
     * @param actor  The acting account
     * @param target The target account
     */
    public AccountToAccountEvent(@NotNull MXAccount actor, @NotNull MXAccount target) {
        super(actor);
        this.target = target;
    }

    protected final @NotNull MXAccount target;

    /**
     * Returns the target account.
     *
     * @return The target account
     */
    public @NotNull MXAccount getTarget() {
        return target;
    }
}
