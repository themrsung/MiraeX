package me.sjun.dev.mirae;

import com.google.gson.JsonParseException;
import me.sjun.dev.mirae.account.AccountLedger;
import me.sjun.dev.mirae.account.ConcurrentAccountLedger;
import me.sjun.dev.mirae.command.CommandRegistrant;
import me.sjun.dev.mirae.command.misc.PluginIntroCommand;
import me.sjun.dev.mirae.config.MXConfig;
import me.sjun.dev.mirae.gson.MXGson;
import me.sjun.dev.mirae.listener.EventRegistrant;
import me.sjun.dev.mirae.listener.player.PlayerAccountCreator;
import me.sjun.dev.mirae.task.TaskRegistrant;
import me.sjun.dev.mirae.task.io.AutosaveTask;
import me.sjun.dev.mirae.vault.VaultAdapter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.logging.Level;

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

        loadMiraeConfig();

        getLogger().info("Registering listeners...");
        eventRegistrant = EventRegistrant.start()
                .queue(new PlayerAccountCreator())
                .register(this);

        getLogger().info("Registering commands...");
        commandRegistrant = CommandRegistrant.start()
                .queue(new PluginIntroCommand())
                .register(this);

        getLogger().info("Registering synchronous tasks...");
        taskRegistrant = TaskRegistrant.start()
                .queue(new AutosaveTask())
                .register(this);

        getLogger().info("Hooking into Vault...");
        getServer().getServicesManager().register(Economy.class, new VaultAdapter(accountLedger), this, ServicePriority.Normal);
        getLogger().info("Account ledger registered to Vault economy API.");

        instance = this;
        getLogger().info("MiraeX loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling MiraeX...");
        saveMiraeConfig();
        instance = null;
        getLogger().info("MiraeX disabled!");
    }

    private void loadMiraeConfig() {
        Path directory = Path.of(MXConfig.getSavePath());
        Path configPath = directory.resolve("config.json");

        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to create MiraeX config directory.", e);
        }

        if (Files.exists(configPath)) {
            try (Reader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
                config = MXGson.gson().fromJson(reader, MXConfig.class);
            } catch (IOException | JsonParseException e) {
                getLogger().log(Level.SEVERE, "Failed to load MiraeX config. Using defaults.", e);
                config = new MXConfig();
            }
            return;
        }

        config = new MXConfig();
        saveMiraeConfig();
    }

    public void save() throws IOException {
        Path directory = Path.of(MXConfig.getSavePath());
        Path accountsPath = directory.resolve("accounts.json");

        Files.createDirectories(directory);

        try (Writer writer = Files.newBufferedWriter(accountsPath, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            MXGson.prettyGson().toJson(accountLedger, writer);
        }
    }

    public void load() throws IOException {
        Path directory = Path.of(MXConfig.getSavePath());
        Path accountsPath = directory.resolve("accounts.json");

        if (!Files.exists(accountsPath)) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(accountsPath, StandardCharsets.UTF_8)) {
            AccountLedger loadedLedger = MXGson.gson().fromJson(reader, AccountLedger.class);
            if (loadedLedger instanceof ConcurrentAccountLedger concurrentLoaded && accountLedger instanceof ConcurrentAccountLedger concurrentExisting) {
                concurrentExisting.replaceAccounts(concurrentLoaded.getAccounts());
            } else if (loadedLedger != null) {
                getLogger().log(Level.WARNING, "Unsupported account ledger implementation: " + loadedLedger.getClass().getName());
            }
        } catch (JsonParseException e) {
            getLogger().log(Level.SEVERE, "Failed to load account ledger.", e);
        }
    }

    private void saveMiraeConfig() {
        if (config == null) {
            return;
        }

        Path directory = Path.of(MXConfig.getSavePath());
        Path configPath = directory.resolve("config.json");

        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to create MiraeX config directory.", e);
            return;
        }

        try (Writer writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            MXGson.prettyGson().toJson(config, writer);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to save MiraeX config.", e);
        }
    }

    private MiraeX() {
        accountLedger = new ConcurrentAccountLedger();
    }

    private static MiraeX instance;
    private final @NotNull AccountLedger accountLedger;

    private MXConfig config;

    /**
     * Returns the event registrant.
     *
     * @return The event registrant
     */
    public @NotNull EventRegistrant getEventRegistrant() {
        return eventRegistrant;
    }

    /**
     * Returns the command registrant.
     *
     * @return The command registrant
     */
    public @NotNull CommandRegistrant getCommandRegistrant() {
        return commandRegistrant;
    }

    /**
     * Returns the task registrant.
     *
     * @return The task registrant
     */
    public @NotNull TaskRegistrant getTaskRegistrant() {
        return taskRegistrant;
    }

    private EventRegistrant eventRegistrant;
    private CommandRegistrant commandRegistrant;
    private TaskRegistrant taskRegistrant;
}
