package me.sjun.dev.mirae.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Event registrant.
 */
public final class TaskRegistrant {
    /**
     * Creates a new instance.
     *
     * @return A new instance
     */
    public static @NotNull TaskRegistrant start() {
        return new TaskRegistrant();
    }

    /**
     * Private constructor.
     */
    private TaskRegistrant() {
        taskList = new ArrayList<>();
    }

    private final List<Task> taskList;

    /**
     * Returns the list of listeners.
     *
     * @return The list of listeners
     */
    public List<Task> getListeners() {
        return List.copyOf(taskList);
    }

    /**
     * Queues an event listener.
     *
     * @param listener The listener
     * @return {@code this}
     */
    public @NotNull TaskRegistrant queue(@NotNull Task listener) {
        taskList.add(listener);
        return this;
    }

    /**
     * Registers all the queued events.
     *
     * @param plugin The plugin instance
     * @return {@code this}
     */
    public @NotNull TaskRegistrant register(@NotNull JavaPlugin plugin) {
        taskList.forEach(task -> {
            switch (task.getType()) {
                case INSTANT -> Bukkit.getScheduler().runTask(plugin, task);
                case DELAYED -> Bukkit.getScheduler().runTaskLater(plugin, task, task.getDelay());
                case REPEATING -> Bukkit.getScheduler().runTaskTimer(plugin, task, task.getDelay(), task.getInterval());
            }
        });

        plugin.getLogger().info("Registered " + taskList.size() + " tasks to synchronous scheduler.");
        return this;
    }
}
