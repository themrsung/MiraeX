package me.sjun.dev.mirae.event.account;

import me.sjun.dev.mirae.account.MXAccount;
import org.jetbrains.annotations.NotNull;

/**
 * Balance transfer event.
 */
public class BalanceTransferEvent extends AccountToAccountEvent {
    /**
     * Creates a new event.
     * @param sender The sender
     * @param recipient The recipient
     * @param amount The amount
     * @param message The message
     */
    public BalanceTransferEvent(
            @NotNull MXAccount sender,
            @NotNull MXAccount recipient,
            double amount,
            @NotNull String message
    ) {
        super(sender, recipient);
        this.amount = amount;
        this.message = message;
    }

    protected final double amount;
    protected final @NotNull String message;

    /**
     * Returns the amount.
     * @return The amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the message.
     * @return The message
     */
    public @NotNull String getMessage() {
        return message;
    }
}
