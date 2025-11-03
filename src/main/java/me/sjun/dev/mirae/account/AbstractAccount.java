package me.sjun.dev.mirae.account;

import me.sjun.dev.mirae.storage.ItemStorage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * Locally modifiable account.
 */
public abstract sealed class AbstractAccount implements MXAccount permits LocalAccount {
    /**
     * Creates a new local account.
     *
     * @param uniqueId The unique identifier
     * @param name     The name
     */
    public AbstractAccount(@NotNull UUID uniqueId, @NotNull String name) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.balance = 0;
        this.itemStorage = new ItemStorage();
    }


    private final @NotNull UUID uniqueId;
    private @NotNull String name;
    double balance;
    private final @NotNull ItemStorage itemStorage;

    @Override
    public @NotNull UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public @NotNull OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uniqueId);
    }

    @Override
    public @NotNull Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(uniqueId));
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public void updateName() {
        getPlayer().map(Player::getName).ifPresent(this::setName);
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public @NotNull ItemStorage getItemStorage() {
        return itemStorage;
    }
}
