package me.sjun.dev.mirae.listener.player;

import me.sjun.dev.mirae.listener.MXListener;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Player account registrant.
 */
public final class PlayerAccountRegistrant implements MXListener.Lowest<PlayerJoinEvent> {
    @Override
    public void handle(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();

    }
}
