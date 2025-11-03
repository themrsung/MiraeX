package me.sjun.dev.mirae.task;

import org.jetbrains.annotations.NotNull;

/**
 * Task.
 */
@FunctionalInterface
public interface Task extends Runnable {
    /**
     * Executes the task.
     */
    @Override
    void run();

    /**
     * Returns the interval in ticks.
     *
     * @return Interval in ticks
     */
    default long getInterval() {
        return 20;
    }

    /**
     * Returns the delay in ticks.
     *
     * @return Delay in ticks
     */
    default long getDelay() {
        return 20;
    }

    /**
     * Returns the task type.
     *
     * @return The task type
     */
    default @NotNull Type getType() {
        return Type.INSTANT;
    }

    /**
     * Task type.
     */
    enum Type {
        INSTANT,
        REPEATING,
        DELAYED;
    }
}
