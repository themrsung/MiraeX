package me.sjun.dev.mirae.task.io;

import me.sjun.dev.mirae.MiraeX;
import me.sjun.dev.mirae.task.Task;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Autosave task.
 */
public class AutosaveTask implements Task {
    @Override
    public void run() {
        try {
            MiraeX.getInstance().save();
        } catch (IOException e) {
            MiraeX.getInstance().getLogger().severe("Autosave failed: " + e.getMessage());
        }
    }

    @Override
    public long getInterval() {
        return 20 * 60 * 5;
    }

    @Override
    public long getDelay() {
        return 20 * 60 * 5;
    }

    @Override
    public @NotNull Type getType() {
        return Type.REPEATING;
    }
}
