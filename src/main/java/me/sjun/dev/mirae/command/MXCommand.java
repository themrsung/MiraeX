package me.sjun.dev.mirae.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * MiraeX command.
 */
public abstract class MXCommand extends Command {
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
