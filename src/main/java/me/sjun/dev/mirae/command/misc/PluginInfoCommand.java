package me.sjun.dev.mirae.command.misc;

import me.sjun.dev.mirae.command.MXCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Plugin info command.
 */
public final class PluginInfoCommand extends MXCommand {
    /**
     * Constructor.
     */
    public PluginInfoCommand() {
        super("miraex", "MiraeX info command.", "/miraex", List.of("mirae", "mx"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) {
        sender.sendMessage(Component.text("MiraeX").color(TextColor.fromHexString("#B80404")));
        sender.sendMessage(Component.text("- 제작: 서준").clickEvent(ClickEvent.openUrl("https://sjun.me/")));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return List.of();
    }
}
