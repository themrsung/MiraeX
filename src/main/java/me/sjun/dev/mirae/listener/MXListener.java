package me.sjun.dev.mirae.listener;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * MiraeX Listener.
 *
 * @param <E> The event
 */
public interface MXListener<E extends Event> extends Listener {
    /**
     * Handles the event.
     *
     * @param event The event to handle
     */
    @EventHandler
    void handle(@NotNull E event);

    /**
     * Lowest priority handler.
     *
     * @param <E> The event
     */
    interface Lowest<E extends Event> extends MXListener<E> {
        @Override
        @EventHandler(priority = EventPriority.LOWEST)
        void handle(@NotNull E event);
    }

    /**
     * Monitor priority handler.
     *
     * @param <E> The event
     */
    interface Monitor<E extends Event> extends MXListener<E> {
        @Override
        @EventHandler(priority = EventPriority.MONITOR)
        void handle(@NotNull E event);
    }
}
