package me.sjun.dev.mirae.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class MXEvent extends Event {
    /**
     * Creates a new event.
     */
    public MXEvent() {
        super();
    }

    /**
     * Creates a new event.
     *
     * @param async Whether the event is asynchronous
     */
    public MXEvent(boolean async) {
        super(async);
    }

    /// Boilerplate

    private static final @NotNull HandlerList handlers = new HandlerList();

    /**
     * Boilerplate getter method.
     *
     * @return The handler list
     */
    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
