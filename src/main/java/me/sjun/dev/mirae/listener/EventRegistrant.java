package me.sjun.dev.mirae.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Event registrant.
 */
public final class EventRegistrant {
    /**
     * Creates a new instance.
     *
     * @return A new instance
     */
    public static @NotNull EventRegistrant start() {
        return new EventRegistrant();
    }

    /**
     * Private constructor.
     */
    private EventRegistrant() {
        listenerList = new ArrayList<>();
    }

    private final List<Listener> listenerList;

    /**
     * Queues an event listener.
     *
     * @param listener The listener
     * @return {@code this}
     */
    public @NotNull EventRegistrant queue(@NotNull Listener listener) {
        listenerList.add(listener);
        return this;
    }

    /**
     * Registers all the queued events.
     *
     * @param plugin The plugin instance
     */
    public void register(@NotNull JavaPlugin plugin) {
        listenerList.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, plugin));
        plugin.getLogger().info("Registered " + listenerList.size() + " listeners to plugin manager.");
    }
}
