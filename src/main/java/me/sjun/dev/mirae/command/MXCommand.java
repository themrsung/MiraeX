package me.sjun.dev.mirae.command;

import me.sjun.dev.mirae.MiraeX;
import me.sjun.dev.mirae.account.MXAccount;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * MiraeX command.
 */
public abstract class MXCommand extends Command {
    protected final TextColor RED = TextColor.fromHexString("#ff0000");
    protected final TextColor GREEN = TextColor.fromHexString("#00ff00");
    protected final TextColor BLUE = TextColor.fromHexString("#2f7dff");
    protected final TextColor YELLOW = TextColor.fromHexString("#ffff00");

    protected final Sound SUCCESS_SOUND = Sound.sound()
            .type(Key.key("minecraft:entity.experience_orb.pickup"))
            .source(Sound.Source.UI)
            .pitch(1)
            .volume(1)
            .build();

    protected final Sound FAILURE_SOUND = Sound.sound()
            .type(Key.key("minecraft:ui.button.click"))
            .source(Sound.Source.UI)
            .pitch(1)
            .volume(1)
            .build();
    ;

    protected final Component NO_PERMISSION = Component.text("권한이 부족합니다.").color(RED);

    /**
     * Success feedback and return true.
     *
     * @param sender  The command sender
     * @param message The message
     * @return {@code true}
     */
    protected boolean successFeedback(@NotNull CommandSender sender, @NotNull Component message) {
        sender.sendMessage(message);
        sender.playSound(SUCCESS_SOUND);
        return true;
    }

    /**
     * Success feedback and return true.
     *
     * @param sender  The command sender
     * @param message The message
     * @return {@code true}
     */
    protected boolean successFeedback(@NotNull CommandSender sender, @NotNull String message) {
        return successFeedback(sender, Component.text(message).color(GREEN));
    }

    /**
     * Failure feedback and return false.
     *
     * @param sender  The command sender
     * @param message The message
     * @return {@code false}
     */
    protected boolean failureFeedback(@NotNull CommandSender sender, @NotNull Component message) {
        sender.sendMessage(message);
        sender.playSound(FAILURE_SOUND);
        return false;
    }

    /**
     * Failure feedback and return false.
     *
     * @param sender  The command sender
     * @param message The message
     * @return {@code false}
     */
    protected boolean failureFeedback(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(Component.text(message).color(RED));
        sender.playSound(FAILURE_SOUND);
        return false;
    }

    /**
     * Finds account by name exact.
     *
     * @param query The query
     * @return The account if found
     */
    protected static @NotNull Optional<MXAccount> accountByNameExact(@Nullable String query) {
        return Optional.ofNullable(query)
                .flatMap(name -> MiraeX.getInstance().getAccountLedger().getAccounts()
                        .stream()
                        .filter(account -> account.getName().equalsIgnoreCase(name))
                        .findAny());
    }

    /**
     * Returns the list of accounts by name.
     *
     * @param query The name
     * @return The list of accounts
     */
    protected static @NotNull List<MXAccount> accountsByName(@Nullable String query) {
        if (query == null) return List.of();

        String normalizedQuery = query.toLowerCase();
        Map<String, MXAccount> accountMap = new TreeMap<>((a, b) -> {
            if (a.equalsIgnoreCase(normalizedQuery)) return -1;
            else if (b.equalsIgnoreCase(normalizedQuery)) return 1;
            else if (a.startsWith(normalizedQuery)) return -1;
            else if (b.startsWith(normalizedQuery)) return 1;
            else if (a.contains(normalizedQuery)) return -1;
            else if (b.contains(normalizedQuery)) return 1;

            return 0;
        });

        MiraeX.getInstance().getAccountLedger().getAccounts().stream()
                .filter(a -> a.getName().toLowerCase().contains(normalizedQuery))
                .forEach(a -> accountMap.put(a.getName(), a));

        return List.copyOf(accountMap.values());
    }

    /**
     * Protected constructor.
     *
     * @param name         The name
     * @param description  The description
     * @param usageMessage The usage
     * @param aliases      The list of aliases
     */
    protected MXCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args);

    @Override
    public abstract @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException;
}
