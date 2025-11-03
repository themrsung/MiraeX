package me.sjun.dev.mirae;

import me.sjun.dev.mirae.account.AccountLedger;
import me.sjun.dev.mirae.account.ConcurrentAccountLedger;
import me.sjun.dev.mirae.command.CommandRegistrant;
import me.sjun.dev.mirae.command.misc.PluginIntroCommand;
import me.sjun.dev.mirae.config.MXConfig;
import me.sjun.dev.mirae.listener.EventRegistrant;
import me.sjun.dev.mirae.listener.player.PlayerAccountCreator;
import me.sjun.dev.mirae.vault.VaultAdapter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class MiraeX extends JavaPlugin {
    /**
     * Returns whether the plugin is loaded.
     *
     * @return {@code true} if loaded
     */
    public static boolean isLoaded() {
        return Objects.nonNull(instance);
    }

    /**
     * Returns the instance.
     *
     * @return The instance
     */
    public static @NotNull MiraeX getInstance() {
        return instance;
    }

    /**
     * Returns the account ledger.
     *
     * @return The account ledger
     */
    public @NotNull AccountLedger getAccountLedger() {
        return accountLedger;
    }

    /**
     * Returns the Mirae config.
     *
     * @return The Mirae config
     */
    public @NotNull MXConfig getMiraeConfig() {
        return config;
    }

    @Override
    public void onEnable() {
        getLogger().info("Starting MiraeX...");

        getLogger().info("Registering listeners...");
        eventRegistrant = EventRegistrant.start()
                .queue(new PlayerAccountCreator())
                .register(this);

        getLogger().info("Registering commands...");
        commandRegistrant = CommandRegistrant.start()
                .queue(new PluginIntroCommand())
                .register(this);

        getLogger().info("Hooking into Vault...");
        getServer().getServicesManager().register(Economy.class, new VaultAdapter(accountLedger), this, ServicePriority.Normal);
        getLogger().info("Account ledger registered to Vault economy API.");

        instance = this;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private MiraeX() {
        accountLedger = new ConcurrentAccountLedger();
    }

    private static MiraeX instance;
    private final @NotNull AccountLedger accountLedger;

    private MXConfig config;

    private EventRegistrant eventRegistrant;
    private CommandRegistrant commandRegistrant;
}
