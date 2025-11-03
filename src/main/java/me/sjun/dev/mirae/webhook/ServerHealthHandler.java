package me.sjun.dev.mirae.webhook;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import me.sjun.dev.mirae.MiraeX;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class ServerHealthHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) throws Exception {
        if (MiraeX.isLoaded()) {
            context.status(200).json(Map.of("status", "ok"));
            return;
        } else {
            context.status(503).json(Map.of("status", "unknown"));
        }
    }
}
