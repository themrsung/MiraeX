package me.sjun.dev.mirae;

import me.sjun.dev.mirae.account.AccountLedger;
import me.sjun.dev.mirae.account.ConcurrentAccountLedger;
import me.sjun.dev.mirae.command.CommandRegistrant;
import me.sjun.dev.mirae.command.misc.PluginIntroCommand;
import me.sjun.dev.mirae.listener.EventRegistrant;
import me.sjun.dev.mirae.listener.player.PlayerAccountCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerInteractEvent;
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

    @Override
    public void onEnable() {
        getLogger().info("Starting MiraeX...");

        getLogger().info("Registering listeners...");
        eventRegistrant = EventRegistrant.start()
                .queue(new PlayerAccountCreator())
                .queue((PlayerInteractEvent e) -> {
                    Bukkit.broadcast(Component.text(e.getPlayer().getName()));
                })
                .register(this);

        getLogger().info("Registering commands...");
        commandRegistrant = CommandRegistrant.start()
                .queue(new PluginIntroCommand())
                .register(this);

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

    private EventRegistrant eventRegistrant;
    private CommandRegistrant commandRegistrant;
}
