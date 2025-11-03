package me.sjun.dev.mirae.listener.player;

import me.sjun.dev.mirae.MiraeX;
import me.sjun.dev.mirae.account.MXAccount;
import me.sjun.dev.mirae.listener.MXListener;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Player account registrant.
 */
public final class PlayerAccountCreator implements MXListener.Lowest<PlayerJoinEvent> {
    @Override
    public void handle(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (MiraeX.getInstance().getAccountLedger().getAccount(player.getUniqueId()).isEmpty()) {
            MXAccount account = MiraeX.getInstance().getAccountLedger().createAccount(player);
            account.setName(player.getName());
        };
    }
}
