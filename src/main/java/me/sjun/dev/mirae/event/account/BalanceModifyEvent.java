package me.sjun.dev.mirae.event.account;

import me.sjun.dev.mirae.account.MXAccount;
import org.jetbrains.annotations.NotNull;

/**
 * Balance modify event.
 */
public class BalanceModifyEvent extends AccountEvent {
    /**
     * Creates a new event.
     *
     * @param account The account
     * @param amount  The amount
     * @param message The message
     */
    public BalanceModifyEvent(@NotNull MXAccount account, double amount, @NotNull String message) {
        super(account);

        this.amount = amount;
        this.message = message;
    }

    protected final double amount;
    protected final @NotNull String message;

    /**
     * Returns the amount.
     *
     * @return The amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the message.
     *
     * @return The message
     */
    public @NotNull String getMessage() {
        return message;
    }
}
