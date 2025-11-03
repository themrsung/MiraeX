package me.sjun.dev.mirae.command;

import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Command registrant.
 */
public final class CommandRegistrant {
    /**
     * Creates a new instance.
     *
     * @return A new instance
     */
    public static @NotNull CommandRegistrant start() {
        return new CommandRegistrant();
    }

    /**
     * Private constructor.
     */
    private CommandRegistrant() {
        commandList = new ArrayList<>();
    }

    private final List<Command> commandList;

    /**
     * Returns the list of commands.
     *
     * @return The list of commands
     */
    public List<Command> getCommands() {
        return List.copyOf(commandList);
    }

    /**
     * Queues an event command.
     *
     * @param command The command
     * @return {@code this}
     */
    public @NotNull CommandRegistrant queue(@NotNull Command command) {
        commandList.add(command);
        return this;
    }

    /**
     * Registers all the queued events.
     *
     * @param plugin The plugin instance
     * @return {@code this}
     */
    public @NotNull CommandRegistrant register(@NotNull JavaPlugin plugin) {
        commandList.forEach(command -> plugin.getServer().getCommandMap().register(plugin.getName().toLowerCase(), command));
        plugin.getLogger().info("Registered " + commandList.size() + " commands to command map.");
        return this;
    }
}
